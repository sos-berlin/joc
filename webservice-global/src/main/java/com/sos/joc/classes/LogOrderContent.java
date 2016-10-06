package com.sos.joc.classes;

import org.w3c.dom.Element;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

public class LogOrderContent extends LogContent {
    private static final String XPATH_ORDER_LOG = "//spooler/answer/order/log";
    private static final String WHAT_LOG = "log";
    private OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema;

    public LogOrderContent(OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema, DBItemInventoryInstance dbItemInventoryInstance) {
        super(dbItemInventoryInstance);
        this.orderFilterWithHistoryIdSchema = orderFilterWithHistoryIdSchema;

    }

     public String getLog() throws Exception {
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(orderFilterWithHistoryIdSchema.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateConnection);
        String log = jobSchedulerOrderHistoryDBLayer.getLogAsString(orderFilterWithHistoryIdSchema.getHistoryId());
        if (log==null){
            log = getOrderLogFromXmlCommand();
        }
        sosHibernateConnection.rollback();
        return log;
    }
    
    private String getOrderLogFromXmlCommand() throws Exception{
        
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

        JSCmdShowOrder jsCmdShowOrder = Globals.schedulerObjectFactory.createShowOrder();
        jsCmdShowOrder.setWhat(WHAT_LOG);
        jsCmdShowOrder.setJobChain(orderFilterWithHistoryIdSchema.getJobChain());
        jsCmdShowOrder.setOrder(orderFilterWithHistoryIdSchema.getOrderId());

        String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowOrder);

        jocXmlCommand.excutePost(xml);
        Element logElement = jocXmlCommand.executeXPath(WHAT_LOG, XPATH_ORDER_LOG);
        return logElement.getFirstChild().getNodeValue() ;
}

    
    

}
