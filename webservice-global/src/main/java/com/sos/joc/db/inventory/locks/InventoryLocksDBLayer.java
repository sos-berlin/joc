package com.sos.joc.db.inventory.locks;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventoryLocksDBLayer extends DBLayer {

    public InventoryLocksDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public List<DBItemInventoryLock> getLocks(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_LOCKS);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryLock> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryLock> result = getSession().getResultList(query);
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

    public List<DBItemInventoryLock> getLocksByFolders(String folderPath, Long instanceId, boolean recursive)
            throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_LOCKS);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select il from ");
                sql.append(DBITEM_INVENTORY_LOCKS).append(" il, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where il.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and il.instanceId = :instanceId");
            }
            Query<DBItemInventoryLock> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (recursive) {
                query.setParameter("folderName", folderPath + "%");
            } else {
                query.setParameter("folderName", folderPath);
            }
            List<DBItemInventoryLock> result = getSession().getResultList(query);
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

    public DBItemInventoryLock getLock(String lockPath, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_LOCKS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and name = :lockPath");
            Query<DBItemInventoryLock> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("lockPath", lockPath);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Date getLockConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder("select files.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" files, ");
            sql.append(DBITEM_INVENTORY_LOCKS).append(" locks ");
            sql.append(" where files.id = locks.fileId");
            sql.append(" and locks.id = :id");
            Query<Date> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}