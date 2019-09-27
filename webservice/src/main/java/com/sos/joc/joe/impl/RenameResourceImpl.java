package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocObjectAlreadyExistException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IRenameResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;

@Path("joe")
public class RenameResourceImpl extends JOCResourceImpl implements IRenameResource {

    private static final String API_CALL = "./joe/delete";

    @Override
    public JOCDefaultResponse rename(final String accessToken, final Filter body) {
        SOSHibernateSession connection = null;
        try {
            checkRequiredParameter("objectType", body.getObjectType());
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken); 
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());
            checkRequiredParameter("oldPath", body.getOldPath());
            String path = normalizePath(body.getPath());
            String oldPath = normalizePath(body.getOldPath());
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;

            if (isDirectory) {
                if (!this.folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }

            } else {
                if (!this.folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);
            
            if (item != null) {
                throw new JocObjectAlreadyExistException(path);
            } else if (isDirectory) {
                //TODO count(*) statement
                FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
                filterJoeObjects.setSchedulerId(body.getJobschedulerId());
                filterJoeObjects.setPath(body.getPath());

                
                List<DBItemJoeObject> children = dbLayer.getRecursiveJoeObjectList(filterJoeObjects, 0);
                if (children != null && children.size() > 0) {
                    throw new JocObjectAlreadyExistException(path);
                }
            }
            
            filter.setPath(oldPath);
            item = dbLayer.getJoeObject(filter);
            
            if (item != null) {
                item.setOperation("rename");
                item.setAccount(getAccount());
                item.setPath(path);
                if (!isDirectory) {
                    dbLayer.update(item);
                } else {
                    item.setConfiguration(null);
                    dbLayer.update(item);
                    //TODO executeUpdate
                    int oldPathLength = oldPath.length();
                    filter.setObjectType(null);
                    List<DBItemJoeObject> children = dbLayer.getRecursiveJoeObjectList(filter, 0);
                    for (DBItemJoeObject child : children) {
                        child.setOperation("rename");
                        child.setAccount(getAccount());
                        child.setPath(path + child.getPath().substring(oldPathLength));
                        dbLayer.update(child);
                    }
                }

            } else {
                throw new JobSchedulerObjectNotExistException(oldPath);
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
