package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.db.inventory.locks.InventoryLocksDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IFolderResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.lock.Lock;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.other.Folder;
import com.sos.joc.model.joe.other.FolderItem;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.schedule.Schedule;

@Path("joe")
public class FolderResourceImpl extends JOCResourceImpl implements IFolderResource {

    private static final String API_CALL = "./joe/read/folder";
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderResourceImpl.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

            checkRequiredParameter("path", body.getPath());
            body.setPath(normalizeFolder(body.getPath()));
            String path = (body.getPath() + "/").replaceAll("//+", "/");

            if (!folderPermissions.isPermittedForFolder(body.getPath())) {
                return accessDeniedResponse();
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);
            InventoryJobsDBLayer jobDbLayer = null;
            InventoryJobChainsDBLayer jobChainDbLayer = null;
            InventoryOrdersDBLayer orderDbLayer = null;
            InventoryProcessClassesDBLayer processClassDbLayer = null;
            InventoryLocksDBLayer lockDbLayer = null;
            InventorySchedulesDBLayer scheduleDbLayer = null;
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);

            final int parentDepth = Paths.get(body.getPath()).getNameCount();
            List<DBItemJoeObject> dbItems = dbLayer.getRecursiveJoeObjectList(filterJoeObjects);
            if (dbItems == null) {
                dbItems = new ArrayList<DBItemJoeObject>();
            }
            // Map: grouped by DBItemJoeObject::objectType
            // -> new FolderItem("filename of DBItemJoeObject::path", false) collection
            // filter for non-recursive level
            Map<String, Set<FolderItem>> folderContent = dbItems.stream().filter(item -> Paths.get(item.getPath()).getParent()
                    .getNameCount() == parentDepth).collect(Collectors.groupingBy(
                            DBItemJoeObject::getObjectType, Collectors.mapping(item -> {
                                FolderItem folderItem = new FolderItem();
                                folderItem.setName(Paths.get(item.getPath()).getFileName().toString());
                                folderItem.setDeployed(false);
                                folderItem.setDeleted(item.operationIsDelete());
                                try {
                                    switch (item.getObjectType()) {
                                    case "JOB":
                                        Job job = Globals.objectMapper.readValue(item.getConfiguration(), Job.class);
                                        folderItem.setTitle(job.getTitle());
                                        folderItem.setProcessClass(job.getProcessClass());
                                        folderItem.setIsOrderJob(job.getIsOrderJob() != null && "true,1,yes".contains(job.getIsOrderJob()));
                                        break;
                                    case "JOBCHAIN":
                                        JobChain jobChain = Globals.objectMapper.readValue(item.getConfiguration(), JobChain.class);
                                        folderItem.setTitle(jobChain.getTitle());
                                        folderItem.setProcessClass(jobChain.getProcessClass());
                                        break;
                                    case "ORDER":
                                        Order order = Globals.objectMapper.readValue(item.getConfiguration(), Order.class);
                                        folderItem.setTitle(order.getTitle());
                                        folderItem.setInitialState(order.getInitialState());
                                        folderItem.setEndState(order.getEndState());
                                        folderItem.setPriority(order.getPriority());
                                        break;
                                    case "AGENTCLUSTER":
                                    case "PROCESSCLASS":
                                        ProcessClass processClass = Globals.objectMapper.readValue(item.getConfiguration(), ProcessClass.class);
                                        folderItem.setMaxProcesses(processClass.getMaxProcesses());
                                        break;
                                    case "LOCK":
                                        Lock lock = Globals.objectMapper.readValue(item.getConfiguration(), Lock.class);
                                        folderItem.setMaxNonExclusive(lock.getMaxNonExclusive());
                                        break;
                                    case "SCHEDULE":
                                        Schedule schedule = Globals.objectMapper.readValue(item.getConfiguration(), Schedule.class);
                                        folderItem.setTitle(schedule.getTitle());
                                        folderItem.setSubstitute(schedule.getSubstitute());
                                        folderItem.setValidFrom(schedule.getValidFrom());
                                        folderItem.setValidTo(schedule.getValidTo());
                                        break;
                                    }
                                } catch (Exception e) {
                                    LOGGER.warn("", e);
                                }
                                return folderItem;
                            }, Collectors.toSet())));

            List<JobSchedulerObjectType> objectTypes = Arrays.asList(JobSchedulerObjectType.JOB, JobSchedulerObjectType.JOBCHAIN,
                    JobSchedulerObjectType.ORDER, JobSchedulerObjectType.AGENTCLUSTER, JobSchedulerObjectType.PROCESSCLASS,
                    JobSchedulerObjectType.SCHEDULE, JobSchedulerObjectType.LOCK, JobSchedulerObjectType.MONITOR);

            for (JobSchedulerObjectType objectType : objectTypes) {
                folderContent.putIfAbsent(objectType.value(), new HashSet<FolderItem>());
            }

            // doesn't know external files such as holidays and others
            InventoryFilesDBLayer dbInventoryFilesLayer = new InventoryFilesDBLayer(connection);
            List<DBItemInventoryFile> inventoryFiles = dbInventoryFilesLayer.getFiles(dbItemInventoryInstance.getId(), body.getPath());
            if (inventoryFiles != null) {
                for (DBItemInventoryFile inventoryFile : inventoryFiles) {
                    String objectType = inventoryFile.getFileType().replaceAll("_", "").toUpperCase();
                    if ("/".equals(body.getPath()) && "PROCESSCLASS".equals(objectType) && "(default)".equals(inventoryFile.getFileBaseName())) {
                        continue;
                    }
                    if (!Helper.CLASS_MAPPING.containsKey(objectType)) {
                        continue;
                    }
                    JobSchedulerObjectType objType = JobSchedulerObjectType.fromValue(objectType);
                    FolderItem folderItem = new FolderItem();
                    folderItem.setName(Helper.getPathWithoutExtension(inventoryFile.getFileBaseName(), objType));
                    folderItem.setDeployed(true);
                    String filePath = Helper.getPathWithoutExtension(inventoryFile.getFileName(), objType);
                    try {
                        switch (objType) {
                        case JOB:
                            if (jobDbLayer == null) {
                                jobDbLayer = new InventoryJobsDBLayer(connection);
                            }
                            DBItemInventoryJob job = jobDbLayer.getInventoryJobByName(filePath, inventoryFile.getInstanceId());
                            folderItem.setTitle(job.getTitle());
                            folderItem.setProcessClass(".".equals(job.getProcessClassName()) ? null : job.getProcessClassName());
                            folderItem.setIsOrderJob(job.getIsOrderJob());
                            break;
                        case JOBCHAIN:
                            if (jobChainDbLayer == null) {
                                jobChainDbLayer = new InventoryJobChainsDBLayer(connection);
                            }
                            DBItemInventoryJobChain jobChain = jobChainDbLayer.getJobChainByPath(filePath, inventoryFile.getInstanceId());
                            folderItem.setTitle(jobChain.getTitle());
                            folderItem.setProcessClass(".".equals(jobChain.getProcessClassName()) ? null : jobChain.getProcessClassName());
                            break;
                        case ORDER:
                            if (orderDbLayer == null) {
                                orderDbLayer = new InventoryOrdersDBLayer(connection);
                            }
                            DBItemInventoryOrder order = orderDbLayer.getInventoryOrderByName(inventoryFile.getInstanceId(), filePath);
                            folderItem.setTitle(order.getTitle());
                            folderItem.setInitialState(order.getInitialState());
                            folderItem.setEndState(order.getEndState());
                            folderItem.setPriority(order.getPriority() == null ? null : order.getPriority() + "");
                            break;
                        case AGENTCLUSTER:
                        case PROCESSCLASS:
                            if (processClassDbLayer == null) {
                                processClassDbLayer = new InventoryProcessClassesDBLayer(connection);
                            }
                            DBItemInventoryProcessClass processClass = processClassDbLayer.getProcessClass(filePath, inventoryFile.getInstanceId());
                            folderItem.setMaxProcesses(processClass.getMaxProcesses());
                            break;
                        case LOCK:
                            if (lockDbLayer == null) {
                                lockDbLayer = new InventoryLocksDBLayer(connection);
                            }
                            DBItemInventoryLock lock = lockDbLayer.getLock(filePath, inventoryFile.getInstanceId());
                            folderItem.setMaxNonExclusive(lock.getMaxNonExclusive());
                            break;
                        case SCHEDULE:
                            if (scheduleDbLayer == null) {
                                scheduleDbLayer = new InventorySchedulesDBLayer(connection);
                            }
                            DBItemInventorySchedule schedule = scheduleDbLayer.getSchedule(filePath, inventoryFile.getInstanceId());
                            folderItem.setTitle(schedule.getTitle());
                            if (!".".equals(schedule.getSubstituteName())) {
                                folderItem.setSubstitute(schedule.getSubstituteName());
                                if (schedule.getSubstituteValidFrom() != null) {
                                    folderItem.setValidFrom(formatter.format(ZonedDateTime.ofInstant(schedule.getSubstituteValidFrom().toInstant(),
                                            ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(dbItemInventoryInstance.getTimeZone()))));
                                }
                                if (schedule.getSubstituteValidTo() != null) {
                                    folderItem.setValidTo(formatter.format(ZonedDateTime.ofInstant(schedule.getSubstituteValidTo().toInstant(), ZoneId
                                            .systemDefault()).withZoneSameInstant(ZoneId.of(dbItemInventoryInstance.getTimeZone()))));
                                }
                            }
                            break;
                        default:
                            break;
                        }
                    } catch (Exception e) {
                        LOGGER.warn("", e);
                    }
                    folderContent.get(objectType).add(folderItem);
                }
            }

            Folder entity = new Folder();

            for (JobSchedulerObjectType objectType : objectTypes) {
                if (!folderContent.get(objectType.value()).isEmpty()) {
                    switch (objectType) {
                    case JOB:
                        entity.setJobs(folderContent.get(objectType.value()));
                        break;
                    case JOBCHAIN:
                        entity.setJobChains(folderContent.get(objectType.value()));
                        break;
                    case ORDER:
                        entity.setOrders(folderContent.get(objectType.value()));
                        break;
                    case AGENTCLUSTER:
                        entity.setAgentClusters(folderContent.get(objectType.value()));
                        break;
                    case PROCESSCLASS:
                        entity.setProcessClasses(folderContent.get(objectType.value()));
                        break;
                    case SCHEDULE:
                        entity.setSchedules(folderContent.get(objectType.value()));
                        break;
                    case LOCK:
                        entity.setLocks(folderContent.get(objectType.value()));
                        break;
                    case MONITOR:
                        entity.setMonitors(folderContent.get(objectType.value()));
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
