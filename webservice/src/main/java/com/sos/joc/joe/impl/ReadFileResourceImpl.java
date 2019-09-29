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
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.JSObjectEdit;

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
            if (body.getObjectType() == JobSchedulerObjectType.FOLDER) {
                throw new JobSchedulerBadRequestException("Unsupported object type: " + body.getObjectType().value());
            }
            String path = normalizePath(body.getPath());
            if (!folderPermissions.isPermittedForFolder(getParent(path))) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setConstraint(body);

            DBItemJoeObject dbItemJoeObject = dbLayerJoeObjects.getJoeObject(filterJoeObjects);

            JSObjectEdit jsObjectEdit = new JSObjectEdit();
            JOCHotFolder jocHotFolder = new JOCHotFolder(this);
            byte[] fileContent = null;
            try {
                fileContent = jocHotFolder.getFile(path + Helper.getFileExtension(body.getObjectType()));
            } catch (JobSchedulerObjectNotExistException e) {
                LOGGER.warn(e.toString()); //maybe for this case something special?
            } catch (JocException e) {
                LOGGER.warn(e.toString());
            }

            if (fileContent != null) {
                if (dbItemJoeObject == null || dbItemJoeObject.getConfiguration() == null) {
                    jsObjectEdit.setConfigurationDate(jocHotFolder.getLastModifiedDate());
                    jsObjectEdit.set_message("Using version in live folder. No draft version found in database");
                    jsObjectEdit.setConfiguration((IJSObject) Globals.xmlMapper.readValue(fileContent, Helper.CLASS_MAPPING.get(body.getObjectType()
                            .value())));
                } else {
                    if (jocHotFolder.getLastModifiedDate() != null && jocHotFolder.getLastModifiedDate().after(dbItemJoeObject.getModified())) {
                        jsObjectEdit.setConfigurationDate(jocHotFolder.getLastModifiedDate());
                        jsObjectEdit.set_message("Version in live folder is newer then draft version in database");
                        jsObjectEdit.setConfiguration((IJSObject) Globals.xmlMapper.readValue(fileContent, Helper.CLASS_MAPPING.get(body
                                .getObjectType().value())));
                    } else {
                        jsObjectEdit.setConfigurationDate(dbItemJoeObject.getModified());
                        jsObjectEdit.setConfiguration((IJSObject) Globals.objectMapper.readValue(dbItemJoeObject.getConfiguration(),
                                Helper.CLASS_MAPPING.get(body.getObjectType().value())));
                        jsObjectEdit.set_message("Draft version in database is newer then the version in the live folder");
                    }
                }
            } else {
                if (dbItemJoeObject != null && dbItemJoeObject.getConfiguration() != null) {
                    jsObjectEdit.setConfigurationDate(dbItemJoeObject.getModified());
                    jsObjectEdit.set_message("Using Draft version in database. No configuration found in the live folder");
                    jsObjectEdit.setConfiguration((IJSObject) Globals.objectMapper.readValue(dbItemJoeObject.getConfiguration(), Helper.CLASS_MAPPING
                            .get(body.getObjectType().value())));
                } else {
                    fileContent = null;
                }
            }
            jsObjectEdit.setDeployed(dbItemJoeObject == null);
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
