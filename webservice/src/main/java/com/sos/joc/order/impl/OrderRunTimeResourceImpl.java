package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.order.OrderFilterSchema;
import com.sos.joc.order.resource.IOrderRunTimeResource;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderFilterSchema orderFilterSchema) throws Exception {
        LOGGER.debug("init order/run_time");
        JOCDefaultResponse jocDefaultResponse = init(orderFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            Runtime200Schema runTimeAnswer = new Runtime200Schema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("orderId", orderFilterSchema.getOrderId())
                    && checkRequiredParameter("jobChain", orderFilterSchema.getJobChain())) {
                runTimeAnswer = RunTime.set(jocXmlCommand, createOrderRunTimePostCommand(orderFilterSchema), "//order/run_time");
            }
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String createOrderRunTimePostCommand(OrderFilterSchema orderFilterSchema) {
        JSCmdShowOrder showOrder = new JSCmdShowOrder(Globals.schedulerObjectFactory);
        showOrder.setJobChain(normalizePath(orderFilterSchema.getJobChain()));
        showOrder.setOrder(orderFilterSchema.getOrderId());
        showOrder.setWhat("run_time");
        return Globals.schedulerObjectFactory.toXMLString(showOrder);
    }

}
