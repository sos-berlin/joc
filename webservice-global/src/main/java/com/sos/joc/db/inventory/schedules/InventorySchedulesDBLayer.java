package com.sos.joc.db.inventory.schedules;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;


public class InventorySchedulesDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventorySchedulesDBLayer.class);

    public InventorySchedulesDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventorySchedule> getSchedules(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventorySchedule> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventorySchedule getSchedule(String schedulePath, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where name = :name");
            sql.append(" and instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", schedulePath);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventorySchedule> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventorySchedule getSchedule(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
            sql.append(" where id = :id");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventorySchedule> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    public Date getScheduleConfigurationDate(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select files.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" files, ");
            sql.append(DBITEM_INVENTORY_SCHEDULES).append(" schedule ");
            sql.append(" where files.id = schedule.fileId");
            sql.append(" and schedule.id = :id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            Object result = query.uniqueResult();
            if (result != null) {
                return (Date)result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("rawtypes")
    public List getUsedIn(Long id, Long instanceId, String tableName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(tableName);
            sql.append(" where scheduleId = :id");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);                    
            List result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventorySchedule> getSchedulesByFolders(String folderName, Long instanceId, boolean recursive) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_SCHEDULES);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select is from ");
                sql.append(DBITEM_INVENTORY_SCHEDULES).append(" is, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where is.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and is.instanceId = :instanceId");
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (recursive) {
                query.setParameter("folderName", folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            List<DBItemInventorySchedule> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
}