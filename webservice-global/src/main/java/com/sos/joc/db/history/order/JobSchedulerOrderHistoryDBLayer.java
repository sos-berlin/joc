package com.sos.joc.db.history.order;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

/** @author Uwe Risse */
public class JobSchedulerOrderHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerOrderHistoryDBLayer.class);

    public JobSchedulerOrderHistoryDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    public String getLogAsString(Long orderHistoryId) throws Exception {
        String log = null;
        try {
            SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getConnection().get(SchedulerOrderHistoryDBItem.class,orderHistoryId);
            if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                log = schedulerHistoryDBItem.getLogAsString();
            }
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        return log;
    }

}