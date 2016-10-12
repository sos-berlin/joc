package com.sos.joc.db.inventory;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;


public class InventoryOperatingSystemsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryOperatingSystemsDBLayer.class);

    public InventoryOperatingSystemsDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryOperatingSystem getInventoryOperatingSystem(Long osId) throws Exception {
        try {
            String sql = String.format("from %s where id = :id", DBITEM_INVENTORY_OPERATING_SYSTEMS);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", osId);
            List<DBItemInventoryOperatingSystem> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

}