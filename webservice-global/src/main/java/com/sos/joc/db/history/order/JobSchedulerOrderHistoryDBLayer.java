package com.sos.joc.db.history.order;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.OrderDuration;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItemPostgres;

/** @author Uwe Risse */
public class JobSchedulerOrderHistoryDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerOrderHistoryDBLayer.class);

    public JobSchedulerOrderHistoryDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    public String getLogAsString(String orderHistoryId) throws Exception {
        String log = null;
        try {
            if (this.getConnection().dbmsIsPostgres()) {
                SchedulerOrderHistoryDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryDBItemPostgres) this.getConnection().get(SchedulerOrderHistoryDBItemPostgres.class,Long.parseLong(orderHistoryId));
                if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
                    log = schedulerHistoryDBItem.getLogAsString();
                }
            } else {
                SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getConnection().get(SchedulerOrderHistoryDBItem.class,Long.parseLong(orderHistoryId));
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

    public Long getOrderEstimatedDuration(String orderId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select history from ");
            sql.append(SchedulerOrderHistoryDBItem.class.getSimpleName()).append(" history ");
            sql.append("where history.orderId = :orderId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("orderId", orderId);
            List<SchedulerOrderHistoryDBItem> result = query.list();
            if (result != null) {
                Long durationSum = 0L;
                Integer count = result.size();
                for (SchedulerOrderHistoryDBItem orderHistory : result) {
                    OrderDuration duration = new OrderDuration();
                    duration.setStartTime(orderHistory.getStartTime());
                    duration.setEndTime(orderHistory.getEndTime());
                    duration.initDuration();
                    durationSum += duration.getDurationInMillis();
                }
                if (count != null && count != 0) {
                    return durationSum / count;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
        
}