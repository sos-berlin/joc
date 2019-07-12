package com.sos.joc.db.audit;

import java.util.Date;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemJocStartedOrders;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class StartedOrdersDBLayer extends DBLayer {

    public StartedOrdersDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public void save(DBItemJocStartedOrders startedOrdersDbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (startedOrdersDbItem != null) {
                startedOrdersDbItem.setCreated(new Date());
                getSession().save(startedOrdersDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
