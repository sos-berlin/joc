package com.sos.joc.db.joe;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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

    public SortedSet<Tree> setLockedBy(String schedulerId, SortedSet<Tree> folders, Comparator<Tree> byPath) throws DBConnectionRefusedException, DBInvalidDataException {
        if (folders == null) {
            return null;
        }
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(LOCKS_BY_FOLDER).append("(folder, account) from ").append(DBLayer.DBITEM_JOE_LOCK);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and isLocked = :isLocked");
            sql.append(" and folder in (:folders)");
            Query<Tree> query = sosHibernateSession.createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("isLocked", true);
            query.setParameter("folders", folders.stream().map(Tree::getPath).collect(Collectors.toSet()));
            final List<Tree> result = sosHibernateSession.getResultList(query);
            if (result == null) {
               return folders;
            }
            Supplier<TreeSet<Tree>> supplier = () -> new TreeSet<Tree>(byPath);
            return folders.stream().map(i -> {
                int index = result.indexOf(i);
                if (index > -1) {
                    i.setLockedBy(result.get(index).getLockedBy());
                }
                return i;
            }).collect(Collectors.toCollection(supplier));
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}