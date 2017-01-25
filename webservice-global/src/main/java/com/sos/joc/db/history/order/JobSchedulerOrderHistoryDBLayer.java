package com.sos.joc.db.history.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryLogDBItemPostgres;

/** @author Uwe Risse */
public class JobSchedulerOrderHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerOrderHistoryDBLayer.class);

    public JobSchedulerOrderHistoryDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    public String getLogAsString(String orderHistoryId) throws Exception {
        String log = null;
        try {
            if (this.getConnection().getFactory().dbmsIsPostgres()) {
                SchedulerOrderHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryLogDBItemPostgres) this.getConnection()
                        .get(SchedulerOrderHistoryLogDBItemPostgres.class, Long.parseLong(orderHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            } else {
                SchedulerOrderHistoryDBItem schedulerHistoryDBItem =
                        (SchedulerOrderHistoryDBItem) this.getConnection().get(SchedulerOrderHistoryDBItem.class, Long.parseLong(orderHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            }

        } catch (Exception e1) {
            LOGGER.error(e1.getMessage(), e1);
            return null;
        }
        return log;
    }

}