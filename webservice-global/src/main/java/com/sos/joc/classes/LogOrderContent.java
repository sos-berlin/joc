package com.sos.joc.classes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.order.OrderHistoryFilter;

public class LogOrderContent extends LogContent {

    private OrderHistoryFilter orderHistoryFilter;

    public LogOrderContent(OrderHistoryFilter orderHistoryFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.orderHistoryFilter = orderHistoryFilter;
    }

    public Path writeGzipLogFile() throws Exception {

        Path path = writeGzipLogFileFromDB();
        if (path == null) {
            try {
                path = writeGzipOrderLogFileFromXmlCommand();
            } catch (JobSchedulerNoResponseException e) {
                // occurs if log is deleted on disk but doesn't still exist in database
                // retry database
                path = writeGzipLogFileFromDB();
                if (path == null) {
                    throw e;
                }
            }
        }
        if (path == null) {
            String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                    orderHistoryFilter.getHistoryId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return path;
    }
    
    private Path writeGzipLogFileFromDB() throws DBMissingDataException, IOException, NumberFormatException, SOSHibernateException, JocConfigurationException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(orderHistoryFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getOrderLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateSession);
                return jobSchedulerOrderHistoryDBLayer.writeGzipLogFile(orderHistoryFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private Path writeGzipOrderLogFileFromXmlCommand() throws Exception {
        String logFilename = String.format("order.%s.%s.log", orderHistoryFilter.getJobChain().replaceFirst("^/+", "").replaceAll("/", ","),
                orderHistoryFilter.getOrderId().replaceAll("[/\\\\:]", "_"));
        return writeGzipTaskLogFileFromGet(logFilename, getPrefix());
    }
    
    private String getPrefix() {
        return String.format("sos-%s.%s.%s.order.log-download-", Paths.get(orderHistoryFilter.getJobChain()).getFileName().toString(),
                orderHistoryFilter.getOrderId(), orderHistoryFilter.getHistoryId()).replace(',', '.').replaceAll("[/\\\\:;*?!&\"'<>|^]", "");
    }
}
