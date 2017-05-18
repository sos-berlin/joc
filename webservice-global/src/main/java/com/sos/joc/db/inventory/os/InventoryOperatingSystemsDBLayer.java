package com.sos.joc.db.inventory.os;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventoryOperatingSystemsDBLayer extends DBLayer {

    public InventoryOperatingSystemsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemInventoryOperatingSystem getInventoryOperatingSystem(Long osId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_OPERATING_SYSTEMS);
            sql.append(" where id = :id");
            Query<DBItemInventoryOperatingSystem> query = getSession().createQuery(sql.toString());
            query.setParameter("id", osId);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}