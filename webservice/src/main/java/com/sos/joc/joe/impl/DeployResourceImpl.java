package com.sos.joc.joe.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

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
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeployResource;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.JSObjectEdit;

@Path("joe")
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    private static final String API_CALL = "./joe/deploy";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployResourceImpl.class);

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final Filter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            checkRequiredParameter("objectType", body.getObjectType());

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDeploy();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            if (!Helper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
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
            boolean fileRead = false;
            boolean fileIsXML = false;
            try {
                fileContent = jocHotFolder.getFile(path + Helper.getFileExtension(body.getObjectType()));
                fileRead = true;
                fileIsXML = true;
            } catch (JocException e) {
                LOGGER.warn(e.getMessage());
            }

            if (fileRead) {
                if (dbItemJoeObject == null) {
                    jsObjectEdit.setConfigurationDate(jocHotFolder.getLastModifiedDate());
                    jsObjectEdit.set_message("Using version in live folder. No draft version found in database");
                } else {
                    if (jocHotFolder.getLastModifiedDate().after(dbItemJoeObject.getModified())) {
                        jsObjectEdit.setConfigurationDate(jocHotFolder.getLastModifiedDate());
                        jsObjectEdit.set_message("Version in live folder is newer then draft version in database");
                    } else {
                        jsObjectEdit.setConfigurationDate(dbItemJoeObject.getModified());
                        fileContent = dbItemJoeObject.getConfiguration().getBytes();
                        fileIsXML = false;
                        jsObjectEdit.set_message("Draft version in database is newer then the version in the live folder");
                    }
                }
            } else {
                if (dbItemJoeObject != null) {
                    jsObjectEdit.setConfigurationDate(dbItemJoeObject.getModified());
                    fileContent = dbItemJoeObject.getConfiguration().getBytes();
                    jsObjectEdit.set_message("Using Draft version in databas. No configuration found in the live folder");
                    fileIsXML = false;
                } else {
                    fileContent = null;
                }
            }

            if (fileContent != null) {
                if (fileIsXML) {
                    jsObjectEdit.setConfiguration((IJSObject) Globals.xmlMapper.readValue(fileContent, Helper.CLASS_MAPPING.get(body.getObjectType()
                            .value())));
                } else {
                    jsObjectEdit.setConfiguration((IJSObject) Globals.objectMapper.readValue(fileContent, Helper.CLASS_MAPPING.get(body
                            .getObjectType().value())));
                }
            }
            jsObjectEdit.setDeployed(dbItemJoeObject == null);
            jsObjectEdit.setJobschedulerId(body.getJobschedulerId());
            jsObjectEdit.setPath(path);
            jsObjectEdit.setObjectType(body.getObjectType());
            jsObjectEdit.setDeliveryDate(Date.from(Instant.now()));

            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(jsObjectEdit);

            StreamingOutput streamOut = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    try {
                        output.write(bytes);
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            return JOCDefaultResponse.responseStatus200(streamOut, MediaType.APPLICATION_JSON);

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
