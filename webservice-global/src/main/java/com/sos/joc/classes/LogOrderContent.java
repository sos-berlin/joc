package com.sos.joc.classes;

import com.sos.hibernate.classes.SOSHibernateConnection;
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
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(orderHistoryFilter.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        try {
            JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateConnection);
            String log = jobSchedulerOrderHistoryDBLayer.getLogAsString(orderHistoryFilter.getHistoryId());
            if (log == null) {
                log = getOrderLogFromXmlCommand();
            }
            return log;
        } catch (Exception e) {
            throw e;
        } finally {
            sosHibernateConnection.rollback();
        }
    }

    private String getOrderLogFromXmlCommand() throws Exception {

        String xml = String.format("<show_order order=\"%1$s\" job_chain=\"%2$s\" what=\"log\" />", orderHistoryFilter.getOrderId(),
                orderHistoryFilter.getJobChain());
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_ORDER_LOG, null);
    }
}
