package com.sos.joc.joe.impl;

import java.nio.file.Paths;
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
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocObjectAlreadyExistException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.IRenameResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.lock.LockInfo;

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
            boolean permission2 = JOEHelper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

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
            String folder = null;
            String oldFolder = null;
            
            if (isDirectory) { //TODO for later versions
                throw new JobSchedulerBadRequestException("Renaming of folders are not supported");
            }
            
            if (isDirectory) {
                body.setPath(normalizeFolder(body.getPath()));
                body.setOldPath(normalizeFolder(body.getOldPath()));
                if (body.getPath().equals(body.getOldPath())) {
                    return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
                }
                folder = body.getPath();
                oldFolder = body.getOldPath();

            } else {
                body.setPath(normalizePath(body.getPath()));
                body.setOldPath(normalizePath(body.getOldPath()));
                if (body.getPath().equals(body.getOldPath())) {
                    return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
                }
                folder = getParent(body.getPath());
                oldFolder = getParent(body.getOldPath());
            }
            if (!this.folderPermissions.isPermittedForFolder(folder)) {
                return accessDeniedResponse();
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbJoeLayer = new DBLayerJoeObjects(connection);
            InventoryFilesDBLayer dbInventoryFilesLayer = new InventoryFilesDBLayer(connection);
            //TODO lock for old path??
            LockResourceImpl.unForcelock(new DBLayerJoeLocks(connection), body.getJobschedulerId(), folder, getAccount());
            
            FilterJoeObjects oldPathFilter = new FilterJoeObjects();
            oldPathFilter.setConstraint(body);
            oldPathFilter.setPath(body.getOldPath());
            DBItemJoeObject oldItem = dbJoeLayer.getJoeObject(oldPathFilter);
            boolean oldItemExists = true;
            if (oldItem == null) {
                oldItemExists = false;
                oldItem = new DBItemJoeObject();
                if (!isDirectory) {
                    String extension = JOEHelper.getFileExtension(body.getObjectType());
                    try {
                        JOCHotFolder jocHotFolder = new JOCHotFolder(this);
                        oldItem.setConfiguration(Globals.objectMapper.writeValueAsString(Globals.xmlMapper.readValue(jocHotFolder.getFile(body
                                .getOldPath() + extension), JOEHelper.CLASS_MAPPING.get(body.getObjectType().value()))));
                    } catch (Exception e) {
                        oldItem.setConfiguration(null);
                    }
                } else {
                    oldItem.setConfiguration(null);
                }
                oldItem.setAccount(getAccount());
                oldItem.setAuditLogId(null);
                oldItem.setFolder(Paths.get(body.getOldPath()).getParent().toString().replace('\\', '/'));
                oldItem.setPath(body.getOldPath());
                oldItem.setId(null);
                oldItem.setCreated(Date.from(Instant.now()));
                oldItem.setModified(oldItem.getCreated());
                oldItem.setObjectType(body.getObjectType().value());
                oldItem.setOperation("store");
                oldItem.setSchedulerId(body.getJobschedulerId());
            }
            
            FilterJoeObjects newPathFilter = new FilterJoeObjects();
            newPathFilter.setConstraint(body);
            DBItemJoeObject newItem = dbJoeLayer.getJoeObject(newPathFilter);
            boolean newItemExistsWithDeleteOperation = newItem != null && newItem.operationIsDelete();
            if (isDirectory) {
                if ((newItem != null && !newItem.operationIsDelete()) || (newItem == null && dbInventoryFilesLayer.folderExists(
                        dbItemInventoryInstance.getId(), body.getPath()))) {
                    throw new JocObjectAlreadyExistException(body.getPath());
                }
            } else {
                if ((newItem != null && !newItem.operationIsDelete()) || (newItem == null && dbInventoryFilesLayer.fileExists(dbItemInventoryInstance
                        .getId(), body.getPath() + JOEHelper.getFileExtension(body.getObjectType())))) {
                    throw new JocObjectAlreadyExistException(body.getPath());
                }
            }
            
            if (newItemExistsWithDeleteOperation) {
                dbJoeLayer.update(updateDBItemfromOld(newItem, oldItem));
            } else {
                dbJoeLayer.save(setNewDBItemfromOld(oldItem, body.getPath()));
            }
            
            if (isDirectory) {
                oldPathFilter.setObjectType(null);
                int oldPathLength = body.getOldPath().length();
                List<DBItemJoeObject> children = dbJoeLayer.getRecursiveJoeObjectList(oldPathFilter);
                if (dbInventoryFilesLayer.folderExists(dbItemInventoryInstance.getId(), body.getOldPath())) {
                    oldItem.setOperation("delete");
                    oldItem.setAccount(getAccount());
                    oldItem.setModified(Date.from(Instant.now()));
                    if (oldItemExists) {
                        dbJoeLayer.update(oldItem);
                    } else {
                        dbJoeLayer.save(oldItem); 
                    }
                    for (DBItemJoeObject child : children) {
                        boolean childExistsInInventory = false;
                        if ("FOLDER".equals(child.getObjectType())) {
                            childExistsInInventory = dbInventoryFilesLayer.folderExists(dbItemInventoryInstance.getId(), child.getPath());
                        } else {
                            childExistsInInventory = dbInventoryFilesLayer.fileExists(dbItemInventoryInstance.getId(), child.getPath() + JOEHelper.getFileExtension(body.getObjectType()));
                        }
                        if (childExistsInInventory) {
                            DBItemJoeObject newDbItem = setNewDBItemfromOld(child, body.getPath() + child.getPath().substring(oldPathLength));
                            dbJoeLayer.save(newDbItem);
                            child.setOperation("delete");
                            child.setAccount(getAccount());
                            child.setModified(Date.from(Instant.now()));
                            dbJoeLayer.update(child);
                        } else {
                            child.setPath(body.getPath() + child.getPath().substring(oldPathLength));
                            child.setFolder(getParent(child.getPath()));
                            dbJoeLayer.update(child);
                        }
                        
                    }
                } else {
                    if (oldItemExists) {
                        dbJoeLayer.delete(oldItem);
                    }
                    for (DBItemJoeObject child : children) {
                        child.setPath(body.getPath() + child.getPath().substring(oldPathLength));
                        child.setFolder(getParent(child.getPath()));
                        dbJoeLayer.update(child);
                    }
                }
            } else {
                if (dbInventoryFilesLayer.fileExists(dbItemInventoryInstance.getId(), body.getOldPath() + JOEHelper.getFileExtension(body
                        .getObjectType()))) {
                    oldItem.setOperation("delete");
                    oldItem.setAccount(getAccount());
                    oldItem.setModified(Date.from(Instant.now()));
                    if (oldItemExists) {
                        dbJoeLayer.update(oldItem);
                    } else {
                        dbJoeLayer.save(oldItem);
                    }
                } else {
                    if (oldItemExists) {
                        dbJoeLayer.delete(oldItem);
                    }
                }
            }
            
//            if (body.getObjectType() == JobSchedulerObjectType.JOBCHAIN || body.getObjectType() == JobSchedulerObjectType.ORDER) {
//                // rename nodeparams too if exists
//                oldPathFilter.setObjectType(JobSchedulerObjectType.NODEPARAMS);
//                oldItem = dbJoeLayer.getJoeObject(oldPathFilter);
//                String liveContent = null;
//                oldItemExists = oldItem != null;
//                boolean oldItemExistsLive = false;
//                
//                try {
//                    String extension = JOEHelper.getFileExtension(oldPathFilter.getObjectType());
//                    JOCHotFolder jocHotFolder = new JOCHotFolder(this);
//                    oldItemExistsLive = true;
//                    liveContent = new String(jocHotFolder.getFile(oldPathFilter.getPath() + extension));
//                } catch (Exception e) {
//                }
//                
//                if (!oldItemExists && oldItemExistsLive) {
//                    oldItem = new DBItemJoeObject();
//                    oldItem.setConfiguration(liveContent);
//                    oldItem.setAccount(getAccount());
//                    oldItem.setAuditLogId(null);
//                    oldItem.setFolder(Paths.get(oldPathFilter.getPath()).getParent().toString().replace('\\', '/'));
//                    oldItem.setPath(oldPathFilter.getPath());
//                    oldItem.setId(null);
//                    oldItem.setCreated(Date.from(Instant.now()));
//                    oldItem.setModified(oldItem.getCreated());
//                    oldItem.setObjectType(oldPathFilter.getObjectType().value());
//                    oldItem.setOperation("store");
//                    oldItem.setSchedulerId(oldPathFilter.getSchedulerId());
//                }
//                newPathFilter.setObjectType(JobSchedulerObjectType.NODEPARAMS);
//                newItem = dbJoeLayer.getJoeObject(newPathFilter);
//                
//                if (newItem != null) {
//                    dbJoeLayer.update(updateDBItemfromOld(newItem, oldItem));
//                } else {
//                    dbJoeLayer.save(setNewDBItemfromOld(oldItem, newPathFilter.getPath()));
//                }
//                
//                if (oldItemExistsLive) {
//                    oldItem.setOperation("delete");
//                    oldItem.setAccount(getAccount());
//                    oldItem.setModified(Date.from(Instant.now()));
//                    if (oldItemExists) {
//                        dbJoeLayer.update(oldItem);
//                    } else {
//                        dbJoeLayer.save(oldItem);
//                    }
//                } else {
//                    if (oldItemExists) {
//                        dbJoeLayer.delete(oldItem);
//                    }
//                }
//            }
            
            try {
                CustomEvent evt = JOEHelper.getJoeUpdatedEvent(folder);
                SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
                if (!folder.equals(oldFolder)) {
                    evt = JOEHelper.getJoeUpdatedEvent(oldFolder);
                    SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
                }
            } catch (Exception e) {
                //
            }
            
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JoeFolderAlreadyLockedException e) {
            //e.addErrorMetaInfo(getJocError());
            LockInfo entity = new LockInfo();
            entity.setIsLocked(true);
            entity.setLockedSince(e.getLockedSince());
            entity.setLockedBy(e.getLockedBy());
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus434(entity);
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
        if ("/".equals(newPath)) {
            newItem.setFolder(".");
        } else {
            newItem.setFolder(getParent(newPath));
        }
        newItem.setAuditLogId(oldItem.getAuditLogId());
        newItem.setSchedulerId(oldItem.getSchedulerId());
        return newItem;
    }
    
    private DBItemJoeObject updateDBItemfromOld(DBItemJoeObject updatedItem, DBItemJoeObject oldItem) {
        updatedItem.setAccount(getAccount());
        updatedItem.setConfiguration(oldItem.getConfiguration());
        updatedItem.setOperation("store");
        updatedItem.setAuditLogId(oldItem.getAuditLogId());
        return updatedItem;
    }

}
