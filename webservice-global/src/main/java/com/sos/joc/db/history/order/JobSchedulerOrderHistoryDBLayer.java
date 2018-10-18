package com.sos.joc.db.history.order;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryLogDBItemPostgres;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.model.order.OrderHistoryFilter;

public class JobSchedulerOrderHistoryDBLayer extends DBLayer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerOrderHistoryDBLayer.class);
    private String clusterMemberId = null;

    public JobSchedulerOrderHistoryDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public byte[] getLogAsByteArray(OrderHistoryFilter orderHistoryFilter) throws NumberFormatException, SOSHibernateException,
            DBMissingDataException, IOException {
        String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                orderHistoryFilter.getHistoryId());
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerOrderHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerOrderHistoryLogDBItemPostgres.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            return schedulerHistoryDBItem.getLogAsByteArray();
        } else {
            SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getSession().get(
                    SchedulerOrderHistoryDBItem.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            return schedulerHistoryDBItem.getLogAsByteArray();
        }
    }

    public Path writeLogFile(OrderHistoryFilter orderHistoryFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException,
            IOException {
        String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                orderHistoryFilter.getHistoryId());
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerOrderHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerOrderHistoryLogDBItemPostgres.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            return schedulerHistoryDBItem.writeLogFile(getPrefix(orderHistoryFilter));
        } else {
            SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getSession().get(
                    SchedulerOrderHistoryDBItem.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            return schedulerHistoryDBItem.writeLogFile(getPrefix(orderHistoryFilter));
        }
    }

    public Path writeGzipLogFile(OrderHistoryFilter orderHistoryFilter, boolean isDistributed) throws NumberFormatException, SOSHibernateException, DBMissingDataException,
            IOException {
        String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                orderHistoryFilter.getHistoryId());
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerOrderHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerOrderHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerOrderHistoryLogDBItemPostgres.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            if (isDistributed) {
                setClusterMemberId(orderHistoryFilter);
            }
            return schedulerHistoryDBItem.writeGzipLogFile(getPrefix(orderHistoryFilter));
        } else {
            SchedulerOrderHistoryDBItem schedulerHistoryDBItem = (SchedulerOrderHistoryDBItem) this.getSession().get(
                    SchedulerOrderHistoryDBItem.class, Long.parseLong(orderHistoryFilter.getHistoryId()));
            if (schedulerHistoryDBItem == null) {
                throw new DBMissingDataException(msg);
            }
            if (orderHistoryFilter.getJobschedulerId() != null && !orderHistoryFilter.getJobschedulerId().equals(schedulerHistoryDBItem
                    .getSchedulerId())) {
                throw new DBMissingDataException(msg);
            }
            if (isDistributed) {
                setClusterMemberId(orderHistoryFilter);
            }
            return schedulerHistoryDBItem.writeGzipLogFile(getPrefix(orderHistoryFilter));
        }
    }
    
    public void setClusterMemberId(OrderHistoryFilter orderHistoryFilter) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select occupyingClusterMemberId from ");
            sql.append(SchedulerOrderDBItem.class.getName());
            sql.append(" where spoolerId = :spoolerId");
            sql.append(" and jobChain = :jobChain");
            sql.append(" and id = :orderId");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("spoolerId", orderHistoryFilter.getJobschedulerId());
            query.setParameter("jobChain", orderHistoryFilter.getJobChain().replaceFirst("^/+", ""));
            query.setParameter("orderId", orderHistoryFilter.getOrderId());
            clusterMemberId = getSession().getSingleResult(query);
        } catch (Exception ex) {
            LOGGER.warn("",ex);
        }
    }

    public String getClusterMemberId() {
        return clusterMemberId;
    }

    public String getLogAsString(OrderHistoryFilter orderHistoryFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException,
            IOException {
        byte[] bytes = getLogAsByteArray(orderHistoryFilter);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }
    
    private String getPrefix(OrderHistoryFilter orderHistoryFilter) {
        return String.format("sos-%s.%s.%s.order.log-download-", Paths.get(orderHistoryFilter.getJobChain()).getFileName().toString(), orderHistoryFilter
                .getOrderId(), orderHistoryFilter.getHistoryId()).replace(',', '.').replaceAll("[/\\\\:;*?!&\"'<>|^]", "");
    }
}