package com.sos.joc.db.joe;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.joe.DBItemJoeLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.tree.Tree;

public class DBLayerJoeLocks {

    private final SOSHibernateSession sosHibernateSession;
    private static final String LOCKS_BY_FOLDER = LocksByFolder.class.getName();

    public DBLayerJoeLocks(SOSHibernateSession session) {
        this.sosHibernateSession = session;
    }
    
    public DBItemJoeLock getJoeLock(String schedulerId, String folder) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBLayer.DBITEM_JOE_LOCK);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and folder = :folder");
            Query<DBItemJoeLock> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("folder", folder);
            return sosHibernateSession.getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void update(DBItemJoeLock item) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            item.setModified(Date.from(Instant.now()));
            sosHibernateSession.update(item);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void save(DBItemJoeLock item) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            item.setId(null);
            item.setModified(Date.from(Instant.now()));
            item.setCreated(item.getModified());
            sosHibernateSession.save(item);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public void delete(DBItemJoeLock item) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (item != null) {
                sosHibernateSession.delete(item);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void delete(String schedulerId, String folder) throws DBInvalidDataException, DBConnectionRefusedException {
        delete(getJoeLock(schedulerId, folder));
    }

    public <T extends Tree> SortedSet<T> setLockedBy(String schedulerId, SortedSet<T> folders, Comparator<T> byPath)
            throws DBConnectionRefusedException, DBInvalidDataException {
        if (folders == null || folders.isEmpty()) {
            return folders;
        }
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(LOCKS_BY_FOLDER).append("(folder, account, modified) from ").append(DBLayer.DBITEM_JOE_LOCK);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and isLocked = :isLocked");
            sql.append(" and folder in (:folders)");
            Query<T> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("isLocked", true);
            query.setParameter("folders", folders.stream().map(T::getPath).collect(Collectors.toSet()));
            final List<T> result = sosHibernateSession.getResultList(query);
            if (result == null) {
                return folders;
            }
            Supplier<TreeSet<T>> supplier = () -> new TreeSet<T>(byPath);
            return folders.stream().map(i -> {
                int index = result.indexOf(i);
                if (index > -1) {
                    i.setLockedBy(result.get(index).getLockedBy());
                    i.setLockedSince(result.get(index).getLockedSince());
                }
                return i;
            }).collect(Collectors.toCollection(supplier));
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Map<String, DBItemJoeLock> getLocksFromOthers(String schedulerId, String account) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBLayer.DBITEM_JOE_LOCK);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and account != :account");
            sql.append(" and isLocked = :isLocked");
            Query<DBItemJoeLock> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("account", account);
            query.setParameter("isLocked", true);
            List<DBItemJoeLock> result = sosHibernateSession.getResultList(query);
            if (result != null) {
                return result.stream().collect(Collectors.toMap(DBItemJoeLock::getFolder, Function.identity()));
            }
            return new HashMap<String, DBItemJoeLock>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}