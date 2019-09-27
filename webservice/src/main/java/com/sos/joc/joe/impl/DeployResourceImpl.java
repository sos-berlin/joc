package com.sos.joc.joe.impl;

import java.util.List;

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
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeployResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;

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

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());

            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;
            if (!isDirectory && !Helper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }

            String path = "";
            if (isDirectory) {
                if (!folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }
                path = normalizeFolder(body.getPath());
            } else {
                if (!folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
                path = normalizePath(body.getPath());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(sosHibernateSession);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);
            filterJoeObjects.setOrderCriteria("created");
            if (isDirectory) {
                filterJoeObjects.setRecursive();
            }

            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);
            JOCHotFolder jocHotFolder = new JOCHotFolder(this);

            for (DBItemJoeObject joeObject : listOfJoeObjects) {

                if ("FOLDER".equals(joeObject.getObjectType())) {

                    if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                        return accessDeniedResponse();
                    }

                    switch (joeObject.getOperation().toLowerCase()) {
                    case "store":
                        jocHotFolder.putFolder(normalizeFolder(joeObject.getPath()) + "/");
                        break;
                    case "delete":
                        jocHotFolder.delete(normalizeFolder(joeObject.getPath() + "/"));
                        break;

                    default:
                        break;
                    }
                } else {
                    if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                        return accessDeniedResponse();
                    }

                    if (!Helper.CLASS_MAPPING.containsKey(joeObject.getObjectType())) {
                        throw new JobSchedulerBadRequestException("unsupported objectType found in database: " + joeObject.getObjectType());
                    }

                    String extension = Helper.getFileExtension(JobSchedulerObjectType.fromValue(joeObject.getObjectType()));

                    switch (joeObject.getOperation().toLowerCase()) {
                    case "store":
                        final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(Globals.objectMapper.readValue(joeObject.getConfiguration(),
                                Helper.CLASS_MAPPING.get(joeObject.getObjectType())));
                        jocHotFolder.putFile(joeObject.getPath() + extension, bytes);
                        break;
                    case "delete":
                        jocHotFolder.delete(joeObject.getPath() + extension);
                        break;

                    default:
                        break;
                    }
                }
                dbLayerJoeObjects.delete(joeObject);
            }
            Globals.commit(sosHibernateSession);

            return JOCDefaultResponse.responseStatusJSOk(null);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback(sosHibernateSession);
            Globals.disconnect(sosHibernateSession);
        }
    }

}
