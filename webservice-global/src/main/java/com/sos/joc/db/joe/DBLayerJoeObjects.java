package com.sos.joc.db.joe;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.JoeTree;
import com.sos.joc.model.tree.Tree;

public class DBLayerJoeObjects {

    private final SOSHibernateSession sosHibernateSession;
    private static final String FOLDERS_BY_PATH = FoldersByPath.class.getName();

    public DBLayerJoeObjects(SOSHibernateSession session) {
        this.sosHibernateSession = session;
    }

    public DBItemJoeObject getJoeObjectDbItem(final Long id) throws SOSHibernateException {
        return sosHibernateSession.get(DBItemJoeObject.class, id);
    }

    public FilterJoeObjects resetFilter() {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setAccount("");
        filter.setPath("");
        filter.setSchedulerId("");
        return filter;
    }

    private String getWhere(FilterJoeObjects filter) {
        List<String> conditions = new ArrayList<String>();

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().isEmpty()) {
            conditions.add("schedulerId = :schedulerId");
        }

        if (filter.getPathWithChildren()) {
            conditions.add("(path = :path or folder = :folder)");
        } else {

            if (filter.getPath() != null && !filter.getPath().isEmpty()) {
                if (filter.isRecursive()) {
                    conditions.add("(path like :path or path = :pathabsolut)");
                } else if (filter.isJobChainWithOrders()) {
                    conditions.add("(path like :orderPath or path = :path)");
                } else {
                    conditions.add("path = :path");
                }
            }

            if (filter.getFolder() != null && !filter.getFolder().isEmpty()) {
                conditions.add("folder = :folder");
            }
        }

        if (filter.getObjectType() != null) {
            conditions.add("objectType = :objectType");
        }

        if (filter.getObjectTypes() != null && !filter.getObjectTypes().isEmpty()) {
            if (filter.getObjectTypes().size() == 1) {
                conditions.add("objectType = :objectTypes");
            } else {
                conditions.add("objectType in (:objectTypes)");
            }
        }

        if (filter.getAccount() != null && !filter.getAccount().isEmpty()) {
            conditions.add("account = :account");
        }

        if (filter.getOperation() != null && !filter.getOperation().isEmpty()) {
            conditions.add("operation = :operation");
        }

        String where = conditions.stream().collect(Collectors.joining(" and "));
        if (!where.isEmpty()) {
            where = " where " + where;
        }

        return where;
    }

    private <T> Query<T> bindParameters(FilterJoeObjects filter, Query<T> query) {

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().isEmpty()) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (filter.getAccount() != null && !filter.getAccount().isEmpty()) {
            query.setParameter("account", filter.getAccount());
        }
        if (filter.getObjectType() != null) {
            query.setParameter("objectType", filter.getObjectType().value());
        }
        if (filter.getObjectTypes() != null && !filter.getObjectTypes().isEmpty()) {
            if (filter.getObjectTypes().size() == 1) {
                query.setParameter("objectTypes", filter.getObjectTypes().get(0));
            } else {
                query.setParameterList("objectTypes", filter.getObjectTypes());
            }
        }
        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            query.setParameter("path", filter.getPath());
        }
        if (filter.isRecursive()) {
            query.setParameter("pathabsolut", filter.getPathAbsolut());
        }
        if (filter.isJobChainWithOrders()) {
            query.setParameter("orderPath", filter.getOrderPath());
        }
        if (filter.getFolder() != null && !filter.getFolder().isEmpty()) {
            query.setParameter("folder", filter.getFolder());
        }
        if (filter.getOperation() != null && !filter.getOperation().isEmpty()) {
            query.setParameter("operation", filter.getOperation());
        }
        return query;
    }

    public List<DBItemJoeObject> getJoeObjectList(FilterJoeObjects filter, final int limit) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            String q = "from " + DBLayer.DBITEM_JOE_OBJECT + getWhere(filter) + filter.getOrderCriteria() + filter.getSortMode();

            Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
            query = bindParameters(filter, query);

            if (limit > 0) {
                query.setMaxResults(limit);
            }
            return sosHibernateSession.getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemJoeObject> getRecursiveJoeObjectList(FilterJoeObjects filter) throws DBConnectionRefusedException, DBInvalidDataException {
        filter.setRecursive();
        return getJoeObjectList(filter, 0);
    }

    public DBItemJoeObject getJoeObject(FilterJoeObjects filter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String q = "from " + DBLayer.DBITEM_JOE_OBJECT + getWhere(filter);

            Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
            query = bindParameters(filter, query);

            return sosHibernateSession.getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Integer updateFolderCommand(FilterJoeObjects filter, String command) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String q = "update " + DBLayer.DBITEM_JOE_OBJECT + " set operation = '" + command + "' " + getWhere(filter);

            Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
            query = bindParameters(filter, query);

            return sosHibernateSession.executeUpdate(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }

    }

    @SuppressWarnings("unchecked")
    public <T extends Tree> List<T> getFoldersByFolder(final String schedulerId, final String folderName, Collection<String> objectTypes,
            boolean forJoe) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(FOLDERS_BY_PATH);
            if (forJoe) {
                sql.append("(path, objectType, operation) from ").append(DBLayer.DBITEM_JOE_OBJECT);
            } else {
                sql.append("(joe) from ").append(DBLayer.DBITEM_JOE_OBJECT).append(" joe");
            }
            sql.append(" where schedulerId = :schedulerId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( path = :folderName or path like :likeFolderName )");
            }
            if (objectTypes.size() == 1) {
                sql.append(" and objectType = :objectType");
            } else {
                sql.append(" and objectType in (:objectType)");
            }
            Query<T> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (objectTypes.size() == 1) {
                query.setParameter("objectType", objectTypes.iterator().next());
            } else {
                query.setParameterList("objectType", objectTypes);
            }
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            List<T> result = sosHibernateSession.getResultList(query);
            if (result != null && !result.isEmpty()) {
                if (forJoe) {
                    return getFoldersByFolder(schedulerId, result.stream().map(T::getPath).collect(Collectors.toSet()));
                } else {
                    List<T> treeList = new ArrayList<T>();
                    Map<String, List<T>> groupedResult = result.stream().collect(Collectors.groupingBy(T::getPath));
                    for (String key : groupedResult.keySet()) {
                        JoeTree joeTree = new JoeTree();
                        joeTree.setPath(key);
                        for (T entry : groupedResult.get(key)) {
                            JoeTree joe = (JoeTree) entry;
                            if ("FOLDER".equals(joe.getName())) {
                                joeTree.setDeleted(joe.getDeleted());
                            } else {
                                joeTree.getAgentClusters().addAll(joe.getAgentClusters());
                                joeTree.getJobChains().addAll(joe.getJobChains());
                                joeTree.getJobs().addAll(joe.getJobs());
                                joeTree.getLocks().addAll(joe.getLocks());
                                joeTree.getMonitors().addAll(joe.getMonitors());
                                joeTree.getOrders().addAll(joe.getOrders());
                                joeTree.getProcessClasses().addAll(joe.getProcessClasses());
                                joeTree.getSchedules().addAll(joe.getSchedules());
                                joeTree.getNodeParams().addAll(joe.getNodeParams());
                            }
                        }
                        treeList.add((T) joeTree);
                    }
                    return treeList;
                }
            }
            return new ArrayList<T>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    private <T extends Tree> List<T> getFoldersByFolder(String schedulerId, Set<String> folders) throws DBConnectionRefusedException,
            DBInvalidDataException {
        if (folders == null || folders.isEmpty()) {
            return new ArrayList<T>();
        }
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(FOLDERS_BY_PATH).append("(path, objectType, operation) from ").append(DBLayer.DBITEM_JOE_OBJECT);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and objectType = :objectType");
            sql.append(" and path in (:folders)");
            Query<T> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("objectType", "FOLDER");
            query.setParameterList("folders", folders);
            List<T> result = sosHibernateSession.getResultList(query);
            if (result != null) {
                return result;
            }
            return new ArrayList<T>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Boolean folderExists(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBLayer.DBITEM_JOE_OBJECT);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path like :likePath");
            sql.append(" and operation = :operation");
            Query<Long> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("likePath", (path + "/").replaceAll("//+", "/") + "%");
            query.setParameter("operation", "store");
            return sosHibernateSession.getSingleResult(query) > 0;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemJoeObject getJoeObject(String jobSchedulerId, String path, JobSchedulerObjectType type) throws DBConnectionRefusedException, DBInvalidDataException {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setSchedulerId(jobSchedulerId);
        filter.setPath(path);
        filter.setObjectType(type);
        return getJoeObject(filter);
    }

    public void update(DBItemJoeObject item) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            sosHibernateSession.update(item);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void save(DBItemJoeObject item) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            item.setModified(Date.from(Instant.now()));
            sosHibernateSession.save(item);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void delete(DBItemJoeObject joeObject) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (joeObject != null && joeObject.getId() != null) {
                sosHibernateSession.delete(joeObject);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public int delete(FilterJoeObjects filter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String q = "delete from " + DBLayer.DBITEM_JOE_OBJECT + getWhere(filter);
            Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
            bindParameters(filter, query);
            return sosHibernateSession.executeUpdate(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemJoeObject> getOrdersAndNodeParams(DBItemJoeObject jobChain) throws DBConnectionRefusedException, DBInvalidDataException {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setJobChainWithOrders(jobChain.getPath());
        filter.setSchedulerId(jobChain.getSchedulerId());
        filter.setObjectTypes(JobSchedulerObjectType.ORDER.value(), JobSchedulerObjectType.NODEPARAMS.value());
        return getJoeObjectList(filter, 0);
    }
    
    public List<DBItemJoeObject> getNodeParams(DBItemJoeObject order) throws DBConnectionRefusedException, DBInvalidDataException {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setPath(order.getPath());
        filter.setSchedulerId(order.getSchedulerId());
        filter.setObjectType(JobSchedulerObjectType.NODEPARAMS);
        return getJoeObjectList(filter, 0);
    }

}