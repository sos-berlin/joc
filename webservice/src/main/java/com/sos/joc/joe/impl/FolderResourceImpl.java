package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IFolderResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.other.Folder;
import com.sos.joc.model.joe.other.FolderItem;

@Path("joe")
public class FolderResourceImpl extends JOCResourceImpl implements IFolderResource {

    private static final String API_CALL = "./joe/read/folder";

    @Override
    public JOCDefaultResponse readFolder(final String accessToken, final Filter body) {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isView();
            boolean permission2 = Helper.hasPermission(JobSchedulerObjectType.FOLDER, sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

//            This check is only necessary if JobScheduler API is used (online version)
//            if (versionIsOlderThan("1.13.1")) {
//                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
//            }

            checkRequiredParameter("path", body.getPath());
            body.setPath(normalizeFolder(body.getPath()));
            String path = (body.getPath() + "/").replaceAll("//+", "/");

            if (!folderPermissions.isPermittedForFolder(body.getPath())) {
                return accessDeniedResponse();
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);
            
            final int parentDepth = Paths.get(body.getPath()).getNameCount();
            List<DBItemJoeObject> dbItems = dbLayer.getRecursiveJoeObjectList(filterJoeObjects);
            if (dbItems == null) {
                dbItems = new ArrayList<DBItemJoeObject>();
            }
            // Map: grouped by DBItemJoeObject::operationIsDelete -> DBItemJoeObject::objectType 
            // -> new FolderItem("filename of DBItemJoeObject::path", false) collection
            // filter for non-recursive level
            Map<Boolean, Map<String, Set<FolderItem>>> folderContent = dbItems.stream().filter(item -> Paths.get(item.getPath()).getParent()
                    .getNameCount() == parentDepth).collect(Collectors.groupingBy(DBItemJoeObject::operationIsDelete, Collectors.groupingBy(
                            DBItemJoeObject::getObjectType, Collectors.mapping(item -> new FolderItem(Paths.get(item.getPath()).getFileName()
                                    .toString(), false), Collectors.toSet()))));
            folderContent.putIfAbsent(Boolean.FALSE, new HashMap<String, Set<FolderItem>>());
            folderContent.putIfAbsent(Boolean.TRUE, new HashMap<String, Set<FolderItem>>());
            
            List<JobSchedulerObjectType> objectTypes = Arrays.asList(JobSchedulerObjectType.FOLDER, JobSchedulerObjectType.JOB,
                    JobSchedulerObjectType.JOBCHAIN, JobSchedulerObjectType.ORDER, JobSchedulerObjectType.PROCESSCLASS,
                    JobSchedulerObjectType.SCHEDULE, JobSchedulerObjectType.LOCK, JobSchedulerObjectType.MONITOR, JobSchedulerObjectType.NODEPARAMS,
                    JobSchedulerObjectType.OTHER);

            Map<String, Set<FolderItem>> folderContentToAdd = folderContent.get(Boolean.FALSE);
            Map<String, Set<FolderItem>> folderContentToDel = folderContent.get(Boolean.TRUE);
            for (JobSchedulerObjectType objectType : objectTypes) {
                folderContentToAdd.putIfAbsent(objectType.value(), new HashSet<FolderItem>());
                folderContentToDel.putIfAbsent(objectType.value(), new HashSet<FolderItem>());
            }
            
            //Add folders of recursive objects because it could exist these objects but not the objectType==FOLDER in the non-recursive set
            Set<FolderItem> furtherFolders = dbItems.stream().filter(item -> Paths.get(item.getPath()).getParent().getNameCount() > parentDepth).map(
                    item -> new FolderItem(Paths.get(item.getPath()).getName(parentDepth).toString(), false)).collect(Collectors.toSet());
            folderContentToAdd.get(JobSchedulerObjectType.FOLDER.value()).addAll(furtherFolders);

            for (FolderItem folder : folderContentToAdd.get(JobSchedulerObjectType.FOLDER.value())) {
                if (!folderPermissions.isPermittedForFolder(path + folder.getName())) {
                    folderContentToDel.get(JobSchedulerObjectType.FOLDER.value()).add(folder);
                }
            }
            
            //offline version: doesn't know external files such as monitor, holidays and others
            InventoryFilesDBLayer dbInventoryFilesLayer = new InventoryFilesDBLayer(connection);
            List<DBItemInventoryFile> inventoryFiles = dbInventoryFilesLayer.getFiles(dbItemInventoryInstance.getId(), body.getPath());
            if (inventoryFiles != null) {
                for (DBItemInventoryFile inventoryFile : inventoryFiles) {
                    String objectType = inventoryFile.getFileType().replaceAll("_", "").toUpperCase();
                    if ("AGENTCLUSTER".equals(objectType)) {
                        objectType = "PROCESSCLASS";
                    }
                    if (!Helper.CLASS_MAPPING.containsKey(objectType)) {
                        continue;
                    }
                    folderContentToAdd.get(objectType).add(new FolderItem(Helper.getPathWithoutExtension(inventoryFile.getFileBaseName(),
                            JobSchedulerObjectType.fromValue(objectType)), true));
                }
            }
            //end of offline
            
            Folder entity = new Folder();
            
//            //online version: needs too much time -> connection timeout (default=2s), if not available
//            try {
//                JOCHotFolder httpClient = new JOCHotFolder(this);
//                JsonArray folder = httpClient.getFolder(path);
//
//                for (JsonString jsonStr : folder.getValuesAs(JsonString.class)) {
//                    String s = jsonStr.getString();
//                    if (s.startsWith(".")) {
//                        continue;
//                    }
//                    for (JobSchedulerObjectType objectType : objectTypes) {
//                        if (Helper.pathIsObjectOf(s, objectType)) {
//                            if (objectType == JobSchedulerObjectType.FOLDER && !folderPermissions.isPermittedForFolder(path + s)) {
//                                break;
//                            }
//                            folderContentToAdd.get(objectType.value()).add(new FolderItem(Helper.getPathWithoutExtension(s, objectType), true));
//                            break;
//                        }
//                    }
//                }
//            } catch (JobSchedulerObjectNotExistException e) {
//                entity.set_message(e.toString());
//            } catch (JocException e) {
//                LOGGER.warn(e.toString());
//            }
//            //end of online

            for (JobSchedulerObjectType objectType : objectTypes) {
                folderContentToAdd.get(objectType.value()).removeAll(folderContentToDel.get(objectType.value()));
                if (!folderContentToAdd.get(objectType.value()).isEmpty()) {
                    switch (objectType) {
                    case FOLDER:
                        entity.setFolders(folderContentToAdd.get(objectType.value()));
                        break;
                    case JOB:
                        entity.setJobs(folderContentToAdd.get(objectType.value()));
                        break;
                    case JOBCHAIN:
                        entity.setJobChains(folderContentToAdd.get(objectType.value()));
                        break;
                    case ORDER:
                        entity.setOrders(folderContentToAdd.get(objectType.value()));
                        break;
                    case PROCESSCLASS:
                        entity.setProcessClasses(folderContentToAdd.get(objectType.value()));
                        break;
                    case SCHEDULE:
                        entity.setSchedules(folderContentToAdd.get(objectType.value()));
                        break;
                    case LOCK:
                        entity.setLocks(folderContentToAdd.get(objectType.value()));
                        break;
                    case MONITOR:
                        entity.setMonitors(folderContentToAdd.get(objectType.value()));
                        break;
                    case NODEPARAMS:
                        entity.setNodeParams(folderContentToAdd.get(objectType.value()));
                        break;
                    case OTHER:
                        entity.setOthers(folderContentToAdd.get(objectType.value()));
                        break;
                    default:
                        break;
                    }
                }
            }

            entity.setDeliveryDate(Date.from(Instant.now()));
            entity.setPath(body.getPath());

            return JOCDefaultResponse.responseStatus200(entity);
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
