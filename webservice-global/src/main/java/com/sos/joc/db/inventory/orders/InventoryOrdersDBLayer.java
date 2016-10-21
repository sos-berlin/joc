package com.sos.joc.db.inventory.orders;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryOrdersDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryOrdersDBLayer.class);

    public InventoryOrdersDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryOrder getInventoryOrderByOrderId(String jobChainName, String orderId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where jobChainName = :jobChainName");
            sql.append(" and orderId = :orderId");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
            query.setParameter("orderId", orderId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryOrder> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
   
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryOrder> getInventoryOrders(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    public Date getOrderConfigurationDate(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select ifile.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" ifile, ");
            sql.append(DBITEM_INVENTORY_ORDERS).append(" io ");
            sql.append(" where ifile.id = io.fileId");
            sql.append(" and io.id = :id");
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
 
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByOrders(String jobChainName, String orderId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ").append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where jobChainName = :jobChainName");
            sql.append(" and instanceId = :instanceId");
            if (orderId != null) {
                sql.append("and orderId = :orderId");
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
            query.setParameter("instanceId", instanceId);
            if (orderId != null) {
                query.setParameter("orderId", orderId);
            }
            List<DBItemInventoryOrder> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByFolders(String folderName, boolean recursive, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_ORDERS);
            if(recursive) {
                sql.append(" where name like :folderName ");
            } else {
                sql.append(" where name = :folderName ");
            }
            sql.append(" and instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            if(recursive) {
                query.setParameter("folderName", "%" + folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryOrder> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
}