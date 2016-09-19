package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.order.OrderConfigurationFilterSchema;
import com.sos.joc.order.resource.IOrderConfigurationResource;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

@Path("order")
public class OrderConfigurationResourceImpl extends JOCResourceImpl implements IOrderConfigurationResource {
    private static final Logger LOGGER = Logger.getLogger(OrderConfigurationResourceImpl.class);
 
    @Override
    public JOCDefaultResponse postOrderConfiguration(String accessToken, OrderConfigurationFilterSchema orderBody) throws Exception {
        LOGGER.debug("init order/configuration");
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("orderId", orderBody.getOrderId())
                    && checkRequiredParameter("jobChain", orderBody.getJobChain())) {
                boolean responseInHtml = orderBody.getMime() == OrderConfigurationFilterSchema.Mime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createOrderConfigurationPostCommand(orderBody), "/spooler/answer/order", "order", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String createOrderConfigurationPostCommand(OrderConfigurationFilterSchema body) {
        JSCmdShowOrder showOrder = new JSCmdShowOrder(Globals.schedulerObjectFactory);
        showOrder.setJobChain((normalizePath(body.getJobChain())));
        showOrder.setOrder(body.getOrderId());
        showOrder.setWhat("source");
        return Globals.schedulerObjectFactory.toXMLString(showOrder);
    }


}
