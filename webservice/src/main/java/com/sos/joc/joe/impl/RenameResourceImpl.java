package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
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
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;
            
            if (isDirectory) {
                body.setPath(normalizeFolder(body.getPath()));
                body.setOldPath(normalizeFolder(body.getOldPath()));
                if (!this.folderPermissions.isPermittedForFolder(body.getPath())) {
                    return accessDeniedResponse();
                }

            } else {
                body.setPath(normalizePath(body.getPath()));
                body.setOldPath(normalizePath(body.getOldPath()));
                if (!this.folderPermissions.isPermittedForFolder(getParent(body.getPath()))) {
                    return accessDeniedResponse();
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbJoeLayer = new DBLayerJoeObjects(connection);
            InventoryFilesDBLayer dbInventoryFilesLayer = new InventoryFilesDBLayer(connection);
            
            FilterJoeObjects oldPathFilter = new FilterJoeObjects();
            oldPathFilter.setConstraint(body);
            oldPathFilter.setPath(body.getOldPath());
            DBItemJoeObject oldItem = dbJoeLayer.getJoeObject(oldPathFilter);
            if (oldItem == null) {
                throw new JobSchedulerObjectNotExistException(body.getOldPath());
            }
            
            FilterJoeObjects newPathFilter = new FilterJoeObjects();
            newPathFilter.setConstraint(body);
            DBItemJoeObject newItem = dbJoeLayer.getJoeObject(newPathFilter);
            if (isDirectory) {
                if (newItem != null || dbInventoryFilesLayer.folderExists(dbItemInventoryInstance.getId(), body.getPath()) || dbJoeLayer.folderExists(
                        body.getJobschedulerId(), body.getPath())) {
                    throw new JocObjectAlreadyExistException(body.getPath());
                }
            } else {
                if (newItem != null || dbInventoryFilesLayer.fileExists(dbItemInventoryInstance.getId(), body.getPath() + Helper.getFileExtension(body
                        .getObjectType()))) {
                    throw new JocObjectAlreadyExistException(body.getPath());
                }
            }
            
            newItem = setNewDBItemfromOld(oldItem, body.getPath());
            dbJoeLayer.save(newItem);
            
            oldItem.setOperation("delete");
            oldItem.setAccount(getAccount());
            oldItem.setModified(Date.from(Instant.now()));
            dbJoeLayer.update(oldItem);
            
            if (isDirectory) {
                oldPathFilter.setObjectType(null);
                int oldPathLength = body.getOldPath().length();
                List<DBItemJoeObject> children = dbJoeLayer.getRecursiveJoeObjectList(oldPathFilter);
                for (DBItemJoeObject child : children) {
                    if (child.operationIsDelete()) {
                       continue; 
                    }
                    DBItemJoeObject newDbItem = setNewDBItemfromOld(child, body.getPath() + child.getPath().substring(oldPathLength));
                    dbJoeLayer.save(newDbItem);
                    
                    child.setOperation("delete");
                    child.setAccount(getAccount());
                    child.setModified(Date.from(Instant.now()));
                    dbJoeLayer.update(child);
                }
            }
            
            try {
                CustomEvent evt = Helper.sendEvent(body.getPath(), body.getObjectType().value());
                SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
            } catch (Exception e) {
                //
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
    
    private DBItemJoeObject setNewDBItemfromOld(DBItemJoeObject oldItem, String newPath) {
        DBItemJoeObject newItem = new DBItemJoeObject();
        newItem.setId(null);
        newItem.setObjectType(oldItem.getObjectType());
        newItem.setAccount(getAccount());
        newItem.setCreated(oldItem.getCreated());
        newItem.setConfiguration(oldItem.getConfiguration());
        newItem.setOperation("store");
        newItem.setPath(newPath);
        newItem.setAuditLogId(oldItem.getAuditLogId());
        newItem.setSchedulerId(oldItem.getSchedulerId());
        return newItem;
    }

}
