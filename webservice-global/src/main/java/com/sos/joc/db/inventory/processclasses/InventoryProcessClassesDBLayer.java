package com.sos.joc.db.inventory.processclasses;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventoryProcessClassesDBLayer extends DBLayer {

    public InventoryProcessClassesDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public List<DBItemInventoryProcessClass> getProcessClasses(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryProcessClass> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryProcessClass> getProcessClassesByFolders(String folderName, Long instanceId, boolean recursive)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select ipc from ");
                sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where ipc.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and ipc.instanceId = :instanceId");
            }
            Query<DBItemInventoryProcessClass> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (recursive) {
                query.setParameter("folderName", folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryProcessClass getProcessClass(String processClassPath, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and name = :processClassPath");
            Query<DBItemInventoryProcessClass> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("processClassPath", processClassPath);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Date getProcessClassConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder("select files.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" files, ");
            sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" pc ");
            sql.append(" where files.id = pc.fileId");
            sql.append(" and pc.id = :id");
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