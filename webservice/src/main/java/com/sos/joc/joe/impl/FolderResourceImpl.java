package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
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
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());
            body.setPath(normalizeFolder(body.getPath()));
            String path = (body.getPath() + "/").replaceAll("//+", "/");

            if (!folderPermissions.isPermittedForFolder(body.getPath())) {
                return accessDeniedResponse();
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);

            final int parentDepth = Paths.get(body.getPath()).getNameCount();
            //Map: grouped by DBItemJoeObject::operationIsDelete -> DBItemJoeObject::objectType -> new FolderItem(DBItemJoeObject::path, false) collection
            Map<Boolean, Map<String, Set<FolderItem>>> folderContent = dbLayer.getFolderContentRecursive(body.getJobschedulerId(), body.getPath())
                    .stream().filter(item -> {

                        return Paths.get(item.getPath()).getParent().getNameCount() == parentDepth; // not recursive

                    }).collect(Collectors.groupingBy(DBItemJoeObject::operationIsDelete, Collectors.groupingBy(DBItemJoeObject::getObjectType,
                            Collectors.mapping(item -> new FolderItem(Paths.get(item.getPath()).getFileName().toString(), false), Collectors
                                    .toSet()))));
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
            
            for (FolderItem folder: folderContentToAdd.get(JobSchedulerObjectType.FOLDER.value())) {
                if (!folderPermissions.isPermittedForFolder(path + folder.getName())) {
                    folderContentToDel.get(JobSchedulerObjectType.FOLDER.value()).add(folder);
                }
            }

            JOCHotFolder httpClient = new JOCHotFolder(this);
            JsonArray folder = httpClient.getFolder(path);

            for (JsonString jsonStr : folder.getValuesAs(JsonString.class)) {
                String s = jsonStr.getString();
                if (s.startsWith(".")) {
                    continue;
                }

                if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.FOLDER))) {
                    if (folderPermissions.isPermittedForFolder(path + s)) {
                        folderContentToAdd.get(JobSchedulerObjectType.FOLDER.value()).add(new FolderItem(s.replaceFirst("/$", ""), true));
                    }
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.JOB))) {
                    folderContentToAdd.get(JobSchedulerObjectType.JOB.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.JOBCHAIN))) {
                    folderContentToAdd.get(JobSchedulerObjectType.JOBCHAIN.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.ORDER))) {
                    folderContentToAdd.get(JobSchedulerObjectType.ORDER.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.PROCESSCLASS))) {
                    folderContentToAdd.get(JobSchedulerObjectType.PROCESSCLASS.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.SCHEDULE))) {
                    folderContentToAdd.get(JobSchedulerObjectType.SCHEDULE.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.LOCK))) {
                    folderContentToAdd.get(JobSchedulerObjectType.LOCK.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.MONITOR))) {
                    folderContentToAdd.get(JobSchedulerObjectType.MONITOR.value()).add(new FolderItem(s, true));
                } else if (s.endsWith(Helper.getFileExtension(JobSchedulerObjectType.NODEPARAMS))) { // maybe test if job chain exist
                    folderContentToAdd.get(JobSchedulerObjectType.NODEPARAMS.value()).add(new FolderItem(s, true));
                } else {
                    folderContentToAdd.get(JobSchedulerObjectType.OTHER.value()).add(new FolderItem(s, true));
                }
            }
            
            Folder entity = new Folder();
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
