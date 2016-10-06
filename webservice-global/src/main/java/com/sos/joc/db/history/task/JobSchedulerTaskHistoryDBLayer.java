package com.sos.joc.db.history.task;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;

/** @author Uwe Risse */
public class JobSchedulerTaskHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerTaskHistoryDBLayer.class);

    public JobSchedulerTaskHistoryDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    public String getLogAsString(Long taskHistoryId) throws Exception {
        String log = null;
        try {
            SchedulerTaskHistoryDBItem schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getConnection().get(SchedulerTaskHistoryDBItem.class,taskHistoryId);
            if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                log = schedulerHistoryDBItem.getLogAsString();
            }
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        return log;
    }

}