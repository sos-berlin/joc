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
    private String jobSchedulerId;

    public InventoryOrdersDBLayer(SOSHibernateConnection conn, String jobSchedulerID) {
        super(conn);
        this.jobSchedulerId = jobSchedulerID;
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryOrder getInventoryOrderByOrderId(String jobChainName, String orderId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select order from ");
            sql.append(DBITEM_INVENTORY_ORDERS +" as order, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where order.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "'");
            sql.append(" and order.jobChainName = :jobChainName");
            sql.append(" and order.orderId = :orderId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
            query.setParameter("orderId", orderId);
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
    public List<DBItemInventoryOrder> getInventoryOrders() throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select io from ");
            sql.append(DBITEM_INVENTORY_ORDERS).append(" io, ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii ");
            sql.append("where io.instanceId = ii.id ");
            sql.append("and ii.schedulerId = '" + this.jobSchedulerId + "'");
            Query query = getConnection().createQuery(sql.toString());
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
    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByOrders(String jobChainName, String orderId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ").append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where jobChainName = :jobChainName ");
            if (orderId != null) {
                sql.append("and orderId = :orderId");
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
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
    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByFolders(String folderName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ").append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where name like :folderName ");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("folderName", "%" + folderName + "%");
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