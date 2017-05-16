package com.sos.joc.db.inventory.os;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventoryOperatingSystemsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryOperatingSystemsDBLayer.class);

    public InventoryOperatingSystemsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemInventoryOperatingSystem getInventoryOperatingSystem(Long osId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_OPERATING_SYSTEMS);
            sql.append(" where id = :id");
            LOGGER.debug(sql.toString());
            Query<DBItemInventoryOperatingSystem> query = getSession().createQuery(sql.toString());
            query.setParameter("id", osId);
            return query.getSingleResult();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

}