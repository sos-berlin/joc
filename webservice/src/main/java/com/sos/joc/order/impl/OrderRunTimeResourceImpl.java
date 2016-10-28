package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderRunTimeResource;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRunTimeResourceImpl.class);
    private static final String API_CALL = "./order/run_time";

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderFilter orderFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, orderFilter.getJobschedulerId(), getPermissons(accessToken).getOrder().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            RunTime200 runTimeAnswer = new RunTime200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("orderId", orderFilter.getOrderId()) && checkRequiredParameter("jobChain", orderFilter.getJobChain())) {
                runTimeAnswer = RunTime.set(jocXmlCommand, createOrderRunTimePostCommand(orderFilter), "//order/run_time");
            }
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createOrderRunTimePostCommand(OrderFilter orderFilterSchema) {
        JSCmdShowOrder showOrder = new JSCmdShowOrder(Globals.schedulerObjectFactory);
        showOrder.setJobChain(normalizePath(orderFilterSchema.getJobChain()));
        showOrder.setOrder(orderFilterSchema.getOrderId());
        showOrder.setWhat("run_time");
        return Globals.schedulerObjectFactory.toXMLString(showOrder);
    }

}
