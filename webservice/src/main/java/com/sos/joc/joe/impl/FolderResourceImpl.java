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
import com.sos.joc.joe.resource.IFolderResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.other.Folder;
import com.sos.joc.model.joe.other.FolderItem;

@Path("joe")
public class FolderResourceImpl extends JOCResourceImpl implements IFolderResource {

    private static final String API_CALL = "./joe/read/folder";
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderResourceImpl.class);

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
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);
            
            final int parentDepth = Paths.get(body.getPath()).getNameCount();
            // Map: grouped by DBItemJoeObject::operationIsDelete -> DBItemJoeObject::objectType -> new FolderItem(DBItemJoeObject::path, false) collection
            Map<Boolean, Map<String, Set<FolderItem>>> folderContent = dbLayer.getRecursiveJoeObjectList(filterJoeObjects)
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

            for (FolderItem folder : folderContentToAdd.get(JobSchedulerObjectType.FOLDER.value())) {
                if (!folderPermissions.isPermittedForFolder(path + folder.getName())) {
                    folderContentToDel.get(JobSchedulerObjectType.FOLDER.value()).add(folder);
                }
            }

            try {
                JOCHotFolder httpClient = new JOCHotFolder(this);
                JsonArray folder = httpClient.getFolder(path);

                for (JsonString jsonStr : folder.getValuesAs(JsonString.class)) {
                    String s = jsonStr.getString();
                    if (s.startsWith(".")) {
                        continue;
                    }
                    for (JobSchedulerObjectType objectType : objectTypes) {
                        if (Helper.pathIsObjectOf(s, objectType)) {
                            if (objectType == JobSchedulerObjectType.FOLDER && !folderPermissions.isPermittedForFolder(path + s)) {
                                break;
                            }
                            folderContentToAdd.get(objectType.value()).add(new FolderItem(Helper.getPathWithoutExtension(s, objectType), true));
                            break;
                        }
                    }
                }
            } catch (JocException e) {
                LOGGER.warn(e.toString());
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
