package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joe.common.XmlDeserializer;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IReadFileResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.EmptyConfiguration;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.common.JoeMessage;
import com.sos.joc.model.joe.common.JoeObjectStatus;
import com.sos.joc.model.joe.common.VersionStateText;
import com.sos.schema.JsonValidator;

 
@Path("joe")
public class ReadFileResourceImpl extends JOCResourceImpl implements IReadFileResource {

    private static final String API_CALL = "./joe/read/file";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFileResourceImpl.class);

    @Override
    public JOCDefaultResponse readFile(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, Filter.class);
            Filter body = Globals.objectMapper.readValue(filterBytes, Filter.class);
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isView();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("objectType", body.getObjectType());
            checkRequiredParameter("path", body.getPath());
            
            if (!JOEHelper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
                throw new JobSchedulerBadRequestException("Unsupported object type: " + body.getObjectType().value());
            }
            String path = normalizePath(body.getPath());
            if (!folderPermissions.isPermittedForFolder(getParent(path))) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            JSObjectEdit jsObjectEdit = new JSObjectEdit();
            jsObjectEdit.setObjectVersionStatus(new JoeObjectStatus());
            jsObjectEdit.getObjectVersionStatus().setMessage(new JoeMessage());

            byte[] fileContent = null;
            byte[] fileLiveContent = null;

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setConstraint(body);

            DBItemJoeObject dbItemJoeObject = null;
            dbItemJoeObject = dbLayerJoeObjects.getJoeObject(filterJoeObjects);

            Date lastModifiedDate = null;
            JOCHotFolder jocHotFolder = new JOCHotFolder(this);

            try {
                fileLiveContent = jocHotFolder.getFile(path + JOEHelper.getFileExtension(body.getObjectType()));
            } catch (JobSchedulerObjectNotExistException e) {
                LOGGER.info(e.toString());
            } catch (JocException e) {
                LOGGER.warn(e.toString());
            }

            if (dbItemJoeObject == null || dbItemJoeObject.getConfiguration() == null || (body.getForceLive() != null && body.getForceLive())) {
                fileContent = fileLiveContent;
                if (fileContent != null) {
                    jsObjectEdit.setConfiguration((IJSObject) XmlDeserializer.deserialize(fileContent, JOEHelper.CLASS_MAPPING.get(body.getObjectType()
                            .value())));
                    lastModifiedDate = jocHotFolder.getLastModifiedDate();
                }
            } else {
                fileContent = dbItemJoeObject.getConfiguration().getBytes();
                if (fileContent != null) {
                    jsObjectEdit.setConfiguration((IJSObject) Globals.objectMapper.readValue(dbItemJoeObject.getConfiguration(), JOEHelper.CLASS_MAPPING
                            .get(body.getObjectType().value())));
                    lastModifiedDate = dbItemJoeObject.getModified();
                }
            }

            if (fileContent != null) {
                jsObjectEdit.getObjectVersionStatus().setDeployed(dbItemJoeObject == null);
                jsObjectEdit.setConfigurationDate(lastModifiedDate);

                if (dbItemJoeObject == null) {
                    jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText(
                            "Using version in live folder. No draft version found in database");
                    jsObjectEdit.getObjectVersionStatus().getMessage().set_messageCode("JOE1000");
                    jsObjectEdit.getObjectVersionStatus().setVersionState(VersionStateText.DRAFT_NOT_EXIST);
                } else {
                    if (fileLiveContent == null) {
                        jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText("Using Draft version in database as no live version found");
                        jsObjectEdit.getObjectVersionStatus().getMessage().set_messageCode("JOE1001");
                        jsObjectEdit.getObjectVersionStatus().setVersionState(VersionStateText.LIVE_NOT_EXIST);
                    } else {
                        if (jocHotFolder.getLastModifiedDate().after(dbItemJoeObject.getModified())) {
                            jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText(
                                    "Using Version in live folder as is newer then draft version in database");
                            jsObjectEdit.getObjectVersionStatus().getMessage().set_messageCode("JOE1002");
                            jsObjectEdit.getObjectVersionStatus().setVersionState(VersionStateText.LIVE_IS_NEWER);
                        } else {
                            jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText(
                                    "Using Draft version in database as is newer then the version in the live folder");
                            jsObjectEdit.getObjectVersionStatus().getMessage().set_messageCode("JOE1003");
                            jsObjectEdit.getObjectVersionStatus().setVersionState(VersionStateText.DRAFT_IS_NEWER);
                        }
                    }
                }
            } else {
                jsObjectEdit.getObjectVersionStatus().setDeployed(false);
                jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText("No configuration found");
                jsObjectEdit.getObjectVersionStatus().getMessage().set_messageCode("JOE1004");
                jsObjectEdit.getObjectVersionStatus().setVersionState(VersionStateText.NO_CONFIGURATION_EXIST);
                jsObjectEdit.setConfiguration(new EmptyConfiguration());
            }
            jsObjectEdit.setJobschedulerId(body.getJobschedulerId());
            jsObjectEdit.setPath(path);
            jsObjectEdit.setObjectType(body.getObjectType());
            jsObjectEdit.setDeliveryDate(Date.from(Instant.now()));
            jsObjectEdit.setDocPath(getDocPath(sosHibernateSession, body.getJobschedulerId(), body.getObjectType(), path, dbItemJoeObject));
            jsObjectEdit.setIsJitlJob(jsObjectEdit.getDocPath() != null && jsObjectEdit.getDocPath().startsWith("/sos/jitl-jobs"));

            return JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(jsObjectEdit));

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private String getDocPath(SOSHibernateSession sosHibernateSession, String jobschedulerId, JobSchedulerObjectType type, String path,
            DBItemJoeObject dbItemJoeObject) {
        try {
            if (dbItemJoeObject != null && dbItemJoeObject.getDocPath() != null) {
                return dbItemJoeObject.getDocPath();
            }
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(sosHibernateSession);
            return dbLayer.getDocumentationPath(jobschedulerId, type, path);
        } catch (Exception e) {
            return null;
        }
    }

}
