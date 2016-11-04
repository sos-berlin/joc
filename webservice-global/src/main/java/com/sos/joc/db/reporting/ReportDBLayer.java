package com.sos.joc.db.reporting;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemReportExecution;
import com.sos.jitl.reporting.db.DBItemReportTrigger;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.Duration;


public class ReportDBLayer extends DBLayer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportDBLayer.class);

    public ReportDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public Long getOrderEstimatedDuration(String orderId) throws Exception {
        // from Table REPORT_TRIGGERS
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_REPORT_TRIGGERS);
            sql.append(" where name = :orderId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("orderId", orderId);
            List<DBItemReportTrigger> result = query.list();
            if (result != null) {
                Long durationSum = 0L;
                int count = result.size();
                for (DBItemReportTrigger reportTrigger : result) {
                    Duration duration = new Duration();
                    duration.setStartTime(reportTrigger.getStartTime());
                    duration.setEndTime(reportTrigger.getEndTime());
                    duration.initDuration();
                    if(duration.getDurationInMillis() == 0) {
                        count--;
                    } else {
                        durationSum += duration.getDurationInMillis();
                    }
                }
                if (count != 0) {
                    return durationSum / count;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public Long getTaskEstimatedDuration(String jobName) throws Exception {
        // from Table REPORT_EXECUTIONS
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_REPORT_EXECUTIONS);
            sql.append(" where name = :jobName");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobName", jobName);
            List<DBItemReportExecution> result = query.list();
            if (result != null) {
                Long durationSum = 0L;
                Integer count = result.size();
                for (DBItemReportExecution reportExecution : result) {
                    Duration duration = new Duration();
                    duration.setStartTime(reportExecution.getStartTime());
                    duration.setEndTime(reportExecution.getEndTime());
                    duration.initDuration();
                    if(duration.getDurationInMillis() == 0) {
                        count--;
                    } else {
                        durationSum += duration.getDurationInMillis();
                    }
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