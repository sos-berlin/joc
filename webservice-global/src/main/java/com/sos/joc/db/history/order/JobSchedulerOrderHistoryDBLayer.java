package com.sos.joc.db.history.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryLogDBItemPostgres;
import com.sos.joc.model.order.OrderHistoryFilter;

/* @author Uwe Risse */
public class JobSchedulerOrderHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerOrderHistoryDBLayer.class);

    public JobSchedulerOrderHistoryDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public String getLogAsString(OrderHistoryFilter orderHistoryFilter) throws Exception {
        String log = null;
        try {
            if (this.getSession().getFactory().dbmsIsPostgres()) {
                SchedulerOrderHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryLogDBItemPostgres) this.getSession().get(
                        SchedulerOrderHistoryLogDBItemPostgres.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
                if (schedulerHistoryDBItem == null) {
                    return null;
                }
                if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                        .getSchedulerId())) {
                    return null;
                }
                if (schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            } else {
                SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getSession().get(
                        SchedulerOrderHistoryDBItem.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
                if (schedulerHistoryDBItem == null) {
                    return null;
                }
                if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                        .getSchedulerId())) {
                    return null;
                }
                if (schedulerHistoryDBItem.getLog() != null) {
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