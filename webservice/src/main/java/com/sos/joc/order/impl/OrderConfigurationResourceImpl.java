package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.order.OrderConfigurationFilter;
import com.sos.joc.order.resource.IOrderConfigurationResource;
import com.sos.scheduler.model.commands.JSCmdShowOrder;

@Path("order")
public class OrderConfigurationResourceImpl extends JOCResourceImpl implements IOrderConfigurationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConfigurationResourceImpl.class);
    private static final String API_CALL = "./order/configuration";

    @Override
    public JOCDefaultResponse postOrderConfiguration(String accessToken, OrderConfigurationFilter orderBody) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, orderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("orderId", orderBody.getOrderId()) && checkRequiredParameter("jobChain", orderBody.getJobChain())) {
                boolean responseInHtml = orderBody.getMime() == ConfigurationMime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createOrderConfigurationPostCommand(orderBody),
                        "/spooler/answer/order", "order", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderBody));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createOrderConfigurationPostCommand(OrderConfigurationFilter body) {
        JSCmdShowOrder showOrder = new JSCmdShowOrder(Globals.schedulerObjectFactory);
        showOrder.setJobChain((normalizePath(body.getJobChain())));
        showOrder.setOrder(body.getOrderId());
        showOrder.setWhat("source");
        return showOrder.toXMLString();
    }

}
