package com.sos.joc.classes;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

public class LogOrderContent extends LogContent {
    private static final String XPATH_ORDER_LOG = "/spooler/answer/order/log";
    private static final String WHAT_LOG = "log";
    private OrderHistoryFilter orderHistoryFilter;

    public LogOrderContent(OrderHistoryFilter orderHistoryFilter, DBItemInventoryInstance dbItemInventoryInstance) {
        super(dbItemInventoryInstance);
        this.orderHistoryFilter = orderHistoryFilter;
    }

     public String getLog() throws Exception {
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(orderHistoryFilter.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        try {
            JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateConnection);
            String log = jobSchedulerOrderHistoryDBLayer.getLogAsString(orderHistoryFilter.getHistoryId());
            if (log==null){
                log = getOrderLogFromXmlCommand();
            }
            return log;
        } catch (Exception e) {
            throw e;
        } finally {
            sosHibernateConnection.rollback();
        }
    }
    
    private String getOrderLogFromXmlCommand() throws Exception{
        
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        JSCmdShowOrder jsCmdShowOrder = Globals.schedulerObjectFactory.createShowOrder();
        jsCmdShowOrder.setWhat(WHAT_LOG);
        jsCmdShowOrder.setJobChain(orderHistoryFilter.getJobChain());
        jsCmdShowOrder.setOrder(orderHistoryFilter.getOrderId());
        String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowOrder);
        jocXmlCommand.executePost(xml);
        jocXmlCommand.throwJobSchedulerError();
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_ORDER_LOG, null);
    }
}
