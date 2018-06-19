package com.sos.joc.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.order.OrderHistoryFilter;

public class LogOrderContent extends LogContent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogOrderContent.class);
    private static final String XPATH_ORDER_LOG = "/spooler/answer/order/log";
    private OrderHistoryFilter orderHistoryFilter;

    public LogOrderContent(OrderHistoryFilter orderHistoryFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.orderHistoryFilter = orderHistoryFilter;
    }

    public String getLog() throws Exception {

        String log = getLogFromDB();
        if (log == null) {
            log = getOrderLogFromXmlCommand();
        }
        if (log == null) {
            String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                    orderHistoryFilter.getHistoryId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return log;
    }

    public Path writeLogFile() throws Exception {

        Path path = writeLogFileFromDB();
        if (path == null) {
            path = writeOrderLogFileFromXmlCommand();
        }
        if (path == null) {
            String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                    orderHistoryFilter.getHistoryId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return path;
    }
    
    public Path writeGzipLogFile() throws Exception {

        Path path = writeGzipLogFileFromDB();
        if (path == null) {
            path = writeGzipOrderLogFileFromXmlCommand();
        }
        if (path == null) {
            String msg = String.format("Order log of %s,%s with id %s is missing", orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(),
                    orderHistoryFilter.getHistoryId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return path;
    }

    private String getLogFromDB() throws DBMissingDataException, IOException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(orderHistoryFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getOrderLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateSession);
                return jobSchedulerOrderHistoryDBLayer.getLogAsString(orderHistoryFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } catch (JocConfigurationException | SOSHibernateException | NumberFormatException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private Path writeLogFileFromDB() throws DBMissingDataException, IOException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(orderHistoryFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getOrderLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateSession);
                return jobSchedulerOrderHistoryDBLayer.writeLogFile(orderHistoryFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } catch (JocConfigurationException | SOSHibernateException | NumberFormatException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private Path writeGzipLogFileFromDB() throws DBMissingDataException, IOException {
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
        } catch (JocConfigurationException | SOSHibernateException | NumberFormatException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private String getOrderLogFromXmlCommand() throws Exception {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        String xml = jocXmlCommand.getShowOrderCommand(orderHistoryFilter.getJobChain(), orderHistoryFilter.getOrderId(), "log");
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_ORDER_LOG, null);
    }

    private Path writeOrderLogFileFromXmlCommand() throws Exception {
        String orderLog = getOrderLogFromXmlCommand();
        if (orderLog == null) {
            return null;
        }
        Path path = null;
        try {
            path = Files.createTempFile("sos-download-", null);
            Files.write(path, orderLog.getBytes());
            return path;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(path);
            } catch (Exception e1) {
            }
            throw e;
        }
    }
    
    private Path writeGzipOrderLogFileFromXmlCommand() throws Exception {
        String orderLog = getOrderLogFromXmlCommand();
        if (orderLog == null) {
            return null;
        }
        Path path = null;
        GZIPOutputStream gzip = null;
        try {
            path = Files.createTempFile("sos-download-", null);
            gzip = new GZIPOutputStream(new FileOutputStream(path.toFile()));
            gzip.write(orderLog.getBytes());
            return path;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(path);
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            try {
                if (gzip != null) {
                    gzip.close();
                }
            } catch (Exception e1) {
            }
        }
    }
}
