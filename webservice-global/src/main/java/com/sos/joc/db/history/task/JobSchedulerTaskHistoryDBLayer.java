package com.sos.joc.db.history.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerTaskHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerTaskHistoryLogDBItemPostgres;

/** @author Uwe Risse */
public class JobSchedulerTaskHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerTaskHistoryDBLayer.class);

    public JobSchedulerTaskHistoryDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public String getLogAsString(String taskHistoryId) throws Exception {
        String log = null;
        try {
            if (this.getConnection().getFactory().dbmsIsPostgres()) {
                SchedulerTaskHistoryLogDBItemPostgres schedulerHistoryDBItem =
                        (SchedulerTaskHistoryLogDBItemPostgres) this.getConnection().get(SchedulerTaskHistoryLogDBItemPostgres.class,
                                Long.parseLong(taskHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            } else {
                SchedulerTaskHistoryDBItem schedulerHistoryDBItem =
                        (SchedulerTaskHistoryDBItem) this.getConnection().get(SchedulerTaskHistoryDBItem.class, Long.parseLong(taskHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            }
        } catch (Exception e1) {
            LOGGER.error(e1.getMessage(), e1);
            log = null;
        }
        return log;
    }

}