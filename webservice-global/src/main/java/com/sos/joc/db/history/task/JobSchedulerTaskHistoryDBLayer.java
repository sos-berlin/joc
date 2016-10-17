package com.sos.joc.db.history.task;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.OrderDuration;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItemPostgres;

/** @author Uwe Risse */
public class JobSchedulerTaskHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerTaskHistoryDBLayer.class);

    public JobSchedulerTaskHistoryDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    public String getLogAsString(String taskHistoryId) throws Exception {
        String log = null;
        try {
            if (this.getConnection().dbmsIsPostgres()) {
                SchedulerTaskHistoryDBItemPostgres schedulerHistoryDBItem = (SchedulerTaskHistoryDBItemPostgres) this.getConnection().get(SchedulerTaskHistoryDBItemPostgres.class,Long.parseLong(taskHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            } else {
                SchedulerTaskHistoryDBItem schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getConnection().get(SchedulerTaskHistoryDBItem.class,Long.parseLong(taskHistoryId));
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

    public Long getTaskEstimatedDuration(String jobName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select history from ");
            sql.append(SchedulerTaskHistoryDBItem.class.getSimpleName()).append(" history ");
            sql.append("where history.jobName = :jobName");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobName", jobName);
            List<SchedulerTaskHistoryDBItem> result = query.list();
            if (result != null) {
                Long durationSum = 0L;
                Integer count = result.size();
                for (SchedulerTaskHistoryDBItem taskHistory : result) {
                    OrderDuration duration = new OrderDuration();
                    duration.setStartTime(taskHistory.getStartTime());
                    duration.setEndTime(taskHistory.getEndTime());
                    duration.initDuration();
                    durationSum += duration.getDurationInMillis();
                }
                if(count != null && count != 0) {
                    return durationSum / count;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
        
}