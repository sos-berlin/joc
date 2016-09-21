package com.sos.joc.db.inventory.orders;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryOrdersDBLayer extends DBLayer {

    private static final Logger LOGGER = Logger.getLogger(InventoryOrdersDBLayer.class);
    private String jobSchedulerId;

    public InventoryOrdersDBLayer(SOSHibernateConnection conn, String jobSchedulerID) {
        super(conn);
        this.jobSchedulerId = jobSchedulerID;
    }
    
    public DBItemInventoryOrder getInventoryOrderByOrderId(String jobChainName, String orderId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select order from ");
            sql.append(DBITEM_INVENTORY_ORDERS +" as order, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where order.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "'");
            sql.append(" and  (name) = :name");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", jobChainName + "," + orderId);

            
            List<DBItemInventoryOrder> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
   
    public List<DBItemInventoryOrder> getInventoryOrders() throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select order from ");
            sql.append(DBITEM_INVENTORY_ORDERS +" as order, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where order.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "'");
            Query query = getConnection().createQuery(sql.toString());

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
 
   

  

}