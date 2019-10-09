package com.sos.joc.db.joe;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

public class DBLayerJoeObjects {

    private final SOSHibernateSession sosHibernateSession;
    private static final String FOLDERS_BY_PATH = FoldersByPath.class.getName();

    public DBLayerJoeObjects(SOSHibernateSession session) {
        this.sosHibernateSession = session;
    }

    public DBItemJoeObject getJoeObjectDbItem(final Long id) throws SOSHibernateException {
        return (DBItemJoeObject) sosHibernateSession.get(DBItemJoeObject.class, id);
    }

    public FilterJoeObjects resetFilter() {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setAccount("");
        filter.setPath("");
        filter.setSchedulerId("");
        return filter;
    }

    private String getWhere(FilterJoeObjects filter) {
        String where = "1=1";
        String and = " and ";

        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " schedulerId = :schedulerId";
            and = " and ";
        }

        if (filter.getPath() != null && !"".equals(filter.getPath())) {
            if (filter.isRecursive()) {
                where += and + " path like :path";
            } else {
                where += and + " path = :path";
            }
            and = " and ";
        }

        if (filter.getObjectType() != null) {
            where += and + " objectType = :objectType";
            and = " and ";
        }

        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            where += and + " account = :account";
            and = " and ";
        }

        if (!"".equals(where.trim())) {
            where = " where " + where;
        }
        return where;
    }

    private <T> Query<T> bindParameters(FilterJoeObjects filter, Query<T> query) {

        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            query.setParameter("account", filter.getAccount());
        }
        if (filter.getObjectType() != null ) {
            query.setParameter("objectType", filter.getObjectType().value());
        }
        if (filter.getPath() != null && !"".equals(filter.getPath())) {
            query.setParameter("path", filter.getPath());
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

    public List<DBItemJoeObject> getRecursiveJoeObjectList(FilterJoeObjects filter) throws DBConnectionRefusedException,
            DBInvalidDataException {
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

    public Integer deleteFolderContentRecursive(final String schedulerId, final String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from ").append(DBLayer.DBITEM_JOE_OBJECT);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path like :path");

            Query<Integer> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path + "/%");
            return sosHibernateSession.executeUpdate(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Set<String> getFoldersByFolder(final String schedulerId, final String folderName, Collection<String> objectTypes) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(FOLDERS_BY_PATH).append("(path, objectType) from ").append(DBLayer.DBITEM_JOE_OBJECT);
            sql.append(" where schedulerId = :schedulerId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( path = :folderName or path like :likeFolderName )");
            }
            if (objectTypes.size() == 1) {
                sql.append(" and objectType = :objectType");
            } else {
                sql.append(" and objectType in (:objectType)");
            }
            sql.append(" and operation != 'delete'");
            Query<FoldersByPath> query = sosHibernateSession.createQuery(sql.toString());
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
            List<FoldersByPath> paths = sosHibernateSession.getResultList(query);
            if (paths != null) {
                return paths.stream().map(FoldersByPath::getFolder).collect(Collectors.toSet());
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<String> getFoldersByFolderToDelete(final String schedulerId, final String folderName) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select path from ").append(DBLayer.DBITEM_JOE_OBJECT);
            sql.append(" where schedulerId = :schedulerId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( path = :folderName or path like :likeFolderName )");
            }
            sql.append(" and objectType = :objectType");
            sql.append(" and operation = 'delete'");
            Query<String> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("objectType", "FOLDER");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            return sosHibernateSession.getResultList(query);
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
            Query<Long> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("likePath", path + "/%");
            return sosHibernateSession.getSingleResult(query) > 0;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
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
            sosHibernateSession.delete(joeObject);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}