package com.sos.joc.db.inventory.instances;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryInstancesDBLayer extends DBLayer {

    private static final Logger LOGGER = Logger.getLogger(InventoryInstancesDBLayer.class);

    public InventoryInstancesDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }
    
    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ");
            sql.append(DBITEM_INVENTORY_INSTANCES);
            sql.append(" where upper(schedulerId) = :schedulerId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId.toUpperCase());

            List<DBItemInventoryInstance> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
   
    public List<DBItemInventoryInstance> getInventoryInstances() throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ");
            sql.append(DBITEM_INVENTORY_INSTANCES);
            Query query = getConnection().createQuery(sql.toString());

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
 
   

  

}