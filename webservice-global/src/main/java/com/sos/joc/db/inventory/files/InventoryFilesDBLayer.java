package com.sos.joc.db.inventory.files;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.db.inventory.locks.InventoryLocksDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.other.FolderItem;
import com.sos.joc.model.tree.JoeTree;
import com.sos.joc.model.tree.Tree;

public class InventoryFilesDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFilesDBLayer.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public InventoryFilesDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public List<DBItemInventoryFile> getFiles(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public boolean isEmptyFolder(Long instanceId, String fileDirectory) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileDirectory = :fileDirectory");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileDirectory", fileDirectory);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            return result == null || result.size() == 0;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryFile> getFiles(Long instanceId, String folder) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileDirectory = :fileDirectory");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileDirectory", folder);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public boolean fileExists(Long instanceId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileName = :path");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query) != null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public boolean folderExists(Long instanceId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and (fileDirectory = :path or fileDirectory like :likePath)");
            Query<Long> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("path", path);
            query.setParameter("likePath", (path + "/").replaceAll("//+", "/") + "%");
            return getSession().getSingleResult(query) > 0;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryFile> getFilesByFileType(Long instanceId, String fileType) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileType = :fileType");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileType", fileType);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getFolders(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<String> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Tree> Set<T> getFoldersByFolderAndType(Long instanceId, String folderName, Set<String> types)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and (fileDirectory = :folderName or fileDirectory like :likeFolderName)");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    sql.append(" and fileType = :fileType");
                } else {
                    sql.append(" and fileType in (:fileType)");
                }
            }
            sql.append(" group by fileDirectory");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    query.setParameter("fileType", types.iterator().next());
                } else {
                    query.setParameterList("fileType", types);
                }
            }
            List<String> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result.stream().map(s -> {
                    T tree = (T) new JoeTree();
                    tree.setPath(s);
                    return tree;
                }).collect(Collectors.toSet());
            }
            return new HashSet<T>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<JoeTree> getFoldersByFolderAndType(Long instanceId, String folderName, Set<String> types, List<JoeTree> joeResults, ZoneId zoneId)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and (fileDirectory = :folderName or fileDirectory like :likeFolderName)");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    sql.append(" and fileType = :fileType");
                } else {
                    sql.append(" and fileType in (:fileType)");
                }
            }
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    query.setParameter("fileType", types.iterator().next());
                } else {
                    query.setParameterList("fileType", types);
                }
            }
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                Map<String, List<DBItemInventoryFile>> groupedResult = result.stream().collect(Collectors.groupingBy(
                        DBItemInventoryFile::getFileDirectory));
                return joeObjectToFolderItemMapper(groupedResult, joeResults, zoneId);
            }
            return new ArrayList<JoeTree>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<JoeTree> joeObjectToFolderItemMapper(Map<String, List<DBItemInventoryFile>> groupedResult, List<JoeTree> joeResults, ZoneId zoneId) {
        InventoryJobsDBLayer jobDbLayer = null;
        InventoryJobChainsDBLayer jobChainDbLayer = null;
        InventoryOrdersDBLayer orderDbLayer = null;
        InventoryProcessClassesDBLayer processClassDbLayer = null;
        InventoryLocksDBLayer lockDbLayer = null;
        InventorySchedulesDBLayer scheduleDbLayer = null;
        List<JoeTree> treeList = new ArrayList<JoeTree>();
        for (String key : groupedResult.keySet()) {
            Set<FolderItem> nodeParams = new HashSet<FolderItem>();
            JoeTree joeTree = new JoeTree();
            joeTree.setPath(key);
            int index = joeResults.indexOf(joeTree);
            if (index > -1) {
                joeTree = joeResults.remove(index);
                nodeParams = joeTree.getNodeParams();
            }
            for (DBItemInventoryFile item : groupedResult.get(key)) {
                String objectType = item.getFileType().replaceAll("_", "").toUpperCase();
                if ("PROCESSCLASS".equals(objectType) && "(default)".equals(item.getFileBaseName())) {
                    continue;
                }
                if (!JOEHelper.CLASS_MAPPING.containsKey(objectType)) {
                    continue;
                }
                JobSchedulerObjectType objType = JobSchedulerObjectType.fromValue(objectType);
                FolderItem folderItem = new FolderItem();
                folderItem.setName(JOEHelper.getPathWithoutExtension(item.getFileBaseName(), objType));
                folderItem.setDeployed(true);
                String filePath = JOEHelper.getPathWithoutExtension(item.getFileName(), objType);
                try {
                    switch (objType) {
                    case JOB:
                        if (jobDbLayer == null) {
                            jobDbLayer = new InventoryJobsDBLayer(getSession());
                        }
                        DBItemInventoryJob job = jobDbLayer.getInventoryJobByName(filePath, item.getInstanceId());
                        folderItem.setTitle(job.getTitle());
                        folderItem.setProcessClass(".".equals(job.getProcessClassName()) ? null : job.getProcessClassName());
                        folderItem.setIsOrderJob(job.getIsOrderJob());
                        joeTree.getJobs().add(folderItem);
                        break;
                    case JOBCHAIN:
                        if (jobChainDbLayer == null) {
                            jobChainDbLayer = new InventoryJobChainsDBLayer(getSession());
                        }
                        DBItemInventoryJobChain jobChain = jobChainDbLayer.getJobChainByPath(filePath, item.getInstanceId());
                        folderItem.setTitle(jobChain.getTitle());
                        folderItem.setProcessClass(".".equals(jobChain.getProcessClassName()) ? null : jobChain.getProcessClassName());
                        if (nodeParams.contains(folderItem)) {
                            folderItem.setDeployed(false);
                        }
                        joeTree.getJobChains().add(folderItem);
                        break;
                    case ORDER:
                        if (orderDbLayer == null) {
                            orderDbLayer = new InventoryOrdersDBLayer(getSession());
                        }
                        DBItemInventoryOrder order = orderDbLayer.getInventoryOrderByName(item.getInstanceId(), filePath);
                        folderItem.setTitle(order.getTitle());
                        folderItem.setInitialState(order.getInitialState());
                        folderItem.setEndState(order.getEndState());
                        folderItem.setPriority(order.getPriority() == null ? null : order.getPriority() + "");
                        if (nodeParams.contains(folderItem)) {
                            folderItem.setDeployed(false);
                        }
                        joeTree.getOrders().add(folderItem);
                        break;
                    case AGENTCLUSTER:
                        if (processClassDbLayer == null) {
                            processClassDbLayer = new InventoryProcessClassesDBLayer(getSession());
                        }
                        DBItemInventoryProcessClass agentCluster = processClassDbLayer.getProcessClass(filePath, item.getInstanceId());
                        folderItem.setMaxProcesses(agentCluster.getMaxProcesses());
                        joeTree.getAgentClusters().add(folderItem);
                        break;
                    case PROCESSCLASS:
                        if (processClassDbLayer == null) {
                            processClassDbLayer = new InventoryProcessClassesDBLayer(getSession());
                        }
                        DBItemInventoryProcessClass processClass = processClassDbLayer.getProcessClass(filePath, item.getInstanceId());
                        folderItem.setMaxProcesses(processClass.getMaxProcesses());
                        joeTree.getProcessClasses().add(folderItem);
                        break;
                    case LOCK:
                        if (lockDbLayer == null) {
                            lockDbLayer = new InventoryLocksDBLayer(getSession());
                        }
                        DBItemInventoryLock lock = lockDbLayer.getLock(filePath, item.getInstanceId());
                        folderItem.setMaxNonExclusive(lock.getMaxNonExclusive());
                        joeTree.getLocks().add(folderItem);
                        break;
                    case SCHEDULE:
                        if (scheduleDbLayer == null) {
                            scheduleDbLayer = new InventorySchedulesDBLayer(getSession());
                        }
                        DBItemInventorySchedule schedule = scheduleDbLayer.getSchedule(filePath, item.getInstanceId());
                        folderItem.setTitle(schedule.getTitle());
                        if (!".".equals(schedule.getSubstituteName())) {
                            folderItem.setSubstitute(schedule.getSubstituteName());
                            if (schedule.getSubstituteValidFrom() != null) {
                                folderItem.setValidFrom(formatter.format(ZonedDateTime.ofInstant(schedule.getSubstituteValidFrom().toInstant(), ZoneId
                                        .systemDefault()).withZoneSameInstant(zoneId)));
                            }
                            if (schedule.getSubstituteValidTo() != null) {
                                folderItem.setValidTo(formatter.format(ZonedDateTime.ofInstant(schedule.getSubstituteValidTo().toInstant(), ZoneId
                                        .systemDefault()).withZoneSameInstant(zoneId)));
                            }
                        }
                        joeTree.getSchedules().add(folderItem);
                        break;
                    default:
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.warn("", e);
                }
            }
            if (joeTree.getAgentClusters() != null && joeTree.getAgentClusters().isEmpty()) {
                joeTree.setAgentClusters(null);
            }
            if (joeTree.getJobChains() != null && joeTree.getJobChains().isEmpty()) {
                joeTree.setJobChains(null);
            }
            if (joeTree.getJobs() != null && joeTree.getJobs().isEmpty()) {
                joeTree.setJobs(null);
            }
            if (joeTree.getLocks() != null && joeTree.getLocks().isEmpty()) {
                joeTree.setLocks(null);
            }
            if (joeTree.getMonitors() != null && joeTree.getMonitors().isEmpty()) {
                joeTree.setMonitors(null);
            }
            if (joeTree.getOrders() != null && joeTree.getOrders().isEmpty()) {
                joeTree.setOrders(null);
            }
            if (joeTree.getProcessClasses() != null && joeTree.getProcessClasses().isEmpty()) {
                joeTree.setProcessClasses(null);
            }
            if (joeTree.getSchedules() != null && joeTree.getSchedules().isEmpty()) {
                joeTree.setSchedules(null);
            }
            joeTree.setNodeParams(null);
            treeList.add(joeTree);
        }
        joeResults.addAll(treeList);
        return joeResults;
    }

}