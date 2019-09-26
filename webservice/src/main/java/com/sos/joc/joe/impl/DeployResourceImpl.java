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
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeployResource;
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

            checkRequiredParameter("path", body.getPath());
            if (!Helper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }

            boolean isDirectory = body.getPath().endsWith("/");

            String path = normalizePath(body.getPath());
            if (isDirectory) {
                if (!folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }
            } else {
                if (!folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);
 
            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getRecursiveJoeObjectList(filterJoeObjects, 0);
            for (DBItemJoeObject joeObject: listOfJoeObjects) {
                //deploy(joeObject);
            }
 
            return JOCDefaultResponse.responseStatusJSOk(null);

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
