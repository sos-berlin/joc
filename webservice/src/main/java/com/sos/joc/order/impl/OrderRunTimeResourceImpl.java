package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.order.post.OrderRunTimeBody;
import com.sos.joc.order.resource.IOrderRunTimeResource;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {
    private static final Logger LOGGER = Logger.getLogger(OrderRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderRunTimeBody orderRunTimeBody) throws Exception {
        LOGGER.debug("init order/run_time");
        JOCDefaultResponse jocDefaultResponse = init(orderRunTimeBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            Runtime200Schema runTimeAnswer = new Runtime200Schema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("orderId", orderRunTimeBody.getOrderId())
                    && jocXmlCommand.checkRequiredParameter("jobChain", orderRunTimeBody.getJobChain())) {
                runTimeAnswer = RunTime.set(jocXmlCommand, createOrderRunTimePostCommand(orderRunTimeBody), "//order/run_time");
            }
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String createOrderRunTimePostCommand(OrderRunTimeBody body) {
        JSCmdShowOrder showOrder = new JSCmdShowOrder(Globals.schedulerObjectFactory);
        showOrder.setJobChain(normalizePath(body.getJobChain()));
        showOrder.setOrder(body.getOrderId());
        showOrder.setWhat("run_time");
        return Globals.schedulerObjectFactory.toXMLString(showOrder);
    }

}
