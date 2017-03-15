package com.sos.joc.classes;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.model.order.OrderHistoryFilter;

public class LogOrderContent extends LogContent {

    private static final String XPATH_ORDER_LOG = "/spooler/answer/order/log";
    private OrderHistoryFilter orderHistoryFilter;

    public LogOrderContent(OrderHistoryFilter orderHistoryFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.orderHistoryFilter = orderHistoryFilter;
    }

    public String getLog() throws Exception {
 
        SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(orderHistoryFilter.getJobschedulerId());
        SOSHibernateSession sosHibernateSession = sosHibernateFactory.openStatelessSession();

        Globals.beginTransaction(sosHibernateSession);
        try {
            JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateSession);
            String log = jobSchedulerOrderHistoryDBLayer.getLogAsString(orderHistoryFilter);
            if (log == null) {
                log = getOrderLogFromXmlCommand();
            }
            return log;
        } catch (Exception e) {
            sosHibernateSession.rollback();
            throw e;
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
