package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IReadFileResource;
import com.sos.joc.model.joe.common.EmptyConfiguration;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.common.JoeMessage;
import com.sos.joc.model.joe.common.JoeObjectStatus;
import com.sos.joc.model.joe.common.VersionStateText;

@Path("joe")
public class ReadFileResourceImpl extends JOCResourceImpl implements IReadFileResource {

    private static final String API_CALL = "./joe/read/file";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFileResourceImpl.class);

    @Override
    public JOCDefaultResponse readFile(final String accessToken, final Filter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            checkRequiredParameter("objectType", body.getObjectType());

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isView();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());
            if (!Helper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
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

            if (dbItemJoeObject == null || (body.getForceLive() != null && body.getForceLive())) {
                try {
                    fileLiveContent = jocHotFolder.getFile(path + Helper.getFileExtension(body.getObjectType()));
                } catch (JobSchedulerObjectNotExistException e) {
                    LOGGER.warn(e.toString()); // maybe for this case something special?
                } catch (JocException e) {
                    LOGGER.warn(e.toString());
                }
                fileContent = fileLiveContent;
                if (fileContent != null) {
                    jsObjectEdit.setConfiguration((IJSObject) Globals.xmlMapper.readValue(fileContent, Helper.CLASS_MAPPING.get(body.getObjectType()
                            .value())));
                    lastModifiedDate = jocHotFolder.getLastModifiedDate();
                }
            } else {
                fileContent = dbItemJoeObject.getConfiguration().getBytes();
                if (fileContent != null) {
                    jsObjectEdit.setConfiguration((IJSObject) Globals.objectMapper.readValue(dbItemJoeObject.getConfiguration(), Helper.CLASS_MAPPING
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
                        jsObjectEdit.getObjectVersionStatus().getMessage().setMessageText("No live version found");
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

}
