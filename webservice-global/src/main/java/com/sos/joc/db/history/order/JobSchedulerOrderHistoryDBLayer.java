package com.sos.joc.db.history.order;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerOrderHistoryLogDBItemPostgres;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.model.order.OrderHistoryFilter;

public class JobSchedulerOrderHistoryDBLayer extends DBLayer {

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

    public Path writeGzipLogFile(OrderHistoryFilter orderHistoryFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException,
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
            return schedulerHistoryDBItem.writeGzipLogFile(getPrefix(orderHistoryFilter));
        }
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
                .getOrderId(), orderHistoryFilter.getHistoryId()).replace(',', '.').replaceAll("['\"|:*?<>/\\]", "");
    }

}