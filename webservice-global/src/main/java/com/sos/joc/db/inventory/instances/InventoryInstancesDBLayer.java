package com.sos.joc.db.inventory.instances;

import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryInstancesDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryInstancesDBLayer.class);

    public InventoryInstancesDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId) throws Exception {
        try {
            String sql = String.format("from %s where upper(schedulerId) = :schedulerId",DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
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

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryInstance> getInventoryInstances() throws Exception {
        try {
            String sql = "from " +DBITEM_INVENTORY_INSTANCES;
            Query query = getConnection().createQuery(sql);

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    public DBItemInventoryInstance getInventoryInstancesByKey(Long id) throws Exception {
        try {
            String sql = String.format("from %s where id = :id", DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql);
            query.setParameter("id", id);

            List<DBItemInventoryInstance> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
     
    }
    

}