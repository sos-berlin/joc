package com.sos.joc.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateOpenSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
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
        return log;
    }

    private String getLogFromDB() {
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
        } catch (JocConfigurationException | SOSHibernateOpenSessionException e) {
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
}
