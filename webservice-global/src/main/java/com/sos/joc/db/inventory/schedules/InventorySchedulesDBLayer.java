package com.sos.joc.db.inventory.schedules;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventorySchedulesDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventorySchedulesDBLayer.class);

    public InventorySchedulesDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public List<DBItemInventorySchedule> getSchedules(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return query.getResultList();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public DBItemInventorySchedule getSchedule(String schedulePath, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where name = :name");
            sql.append(" and instanceId = :instanceId");
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("name", schedulePath);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventorySchedule> result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public DBItemInventorySchedule getSchedule(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where id = :id");
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventorySchedule> result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public Date getScheduleConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select files.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" files, ");
            sql.append(DBITEM_INVENTORY_SCHEDULES).append(" schedule ");
            sql.append(" where files.id = schedule.fileId");
            sql.append(" and schedule.id = :id");
            LOGGER.debug(sql.toString());
            Query<Date> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            Date result = query.getSingleResult();
            if (result != null) {
                return result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public <T> List<T> getUsedIn(Long id, Long instanceId, Class<T> clazz)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(clazz.getSimpleName());
            sql.append(" where scheduleId = :id");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query<T> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            return query.getResultList();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public List<DBItemInventorySchedule> getSchedulesByFolders(String folderName, Long instanceId, boolean recursive)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select isched from ");
                sql.append(DBITEM_INVENTORY_SCHEDULES).append(" isched, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where isched.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and isched.instanceId = :instanceId");
            }
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (recursive) {
                query.setParameter("folderName", folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            return query.getResultList();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
    
}