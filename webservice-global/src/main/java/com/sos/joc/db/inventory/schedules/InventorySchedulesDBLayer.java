package com.sos.joc.db.inventory.schedules;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.TemporalType;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventorySchedulesDBLayer extends DBLayer {

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
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemInventorySchedule> getSubstitutedBySchedules(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where substituteId = :id");
            sql.append(" order by name");
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public boolean hasActiveSubstitutedBySchedule(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where substituteId = :id");
            sql.append(" and substituteValidFrom <= :now");
            sql.append(" and substituteValidTo > :now");
            Query<Long> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("now", Date.from(Instant.now()), TemporalType.TIMESTAMP);
            Long result = getSession().getSingleResult(query);
            if (result == null) {
                return false;
            }
            return result > 0L;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public String hasOverlapedSubstitutedBySchedule(Long id, Long substituteId, Date validFrom, Date validTo) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select name from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where substituteId = :substituteId");
            sql.append(" and id <> :id");
            sql.append(" and ( substituteValidFrom <= :validFrom");
            sql.append(" and substituteValidTo > :validFrom");
            sql.append(" or substituteValidFrom < :validTo");
            sql.append(" and substituteValidTo >= :validTo )");
            
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("substituteId", substituteId);
            query.setParameter("id", id);
            query.setParameter("validFrom", validFrom, TemporalType.TIMESTAMP);
            query.setParameter("validTo", validTo, TemporalType.TIMESTAMP);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
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
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventorySchedule getSchedule(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where id = :id");
            Query<DBItemInventorySchedule> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
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
            Query<Date> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public <T> List<T> getUsedIn(Long id, Long instanceId, Class<T> clazz)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(clazz.getSimpleName());
            sql.append(" where scheduleId = :id");
            sql.append(" and instanceId = :instanceId");
            Query<T> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    
    public <T> List<String> getUsedIn(Set<String> schedulePaths, Long instanceId, Class<T> clazz)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            if (schedulePaths == null || schedulePaths.isEmpty()) {
                return null;
            }
            StringBuilder sql = new StringBuilder();
            sql.append("select jo.name from ").append(clazz.getSimpleName()).append(" jo, ");
            sql.append(DBITEM_INVENTORY_SCHEDULES).append(" s ");
            sql.append(" where jo.scheduleId != 0");
            sql.append(" and jo.instanceId = :instanceId");
            sql.append(" and (s.id = jo.scheduleId or s.substituteId = jo.scheduleId)");
            sql.append(" and s.name in (:schedulePaths)");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulePaths", schedulePaths);
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
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
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
}