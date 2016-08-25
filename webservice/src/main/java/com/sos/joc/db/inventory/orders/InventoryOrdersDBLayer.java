package com.sos.joc.db.inventory.orders;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryOrdersDBLayer extends DBLayer {

    private static final Logger LOGGER = Logger.getLogger(InventoryOrdersDBLayer.class);

    public InventoryOrdersDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }
    
    public DBItemInventoryOrder getInventoryOrderByOrderId(String jobChain, String orderId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where (orderId) = :orderId");
            sql.append("  and  (job_chain_name) = :jobChain");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("orderId", orderId);
            query.setParameter("jobChain", jobChain);

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
            StringBuilder sql = new StringBuilder("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            Query query = getConnection().createQuery(sql.toString());

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
 
   

  

}