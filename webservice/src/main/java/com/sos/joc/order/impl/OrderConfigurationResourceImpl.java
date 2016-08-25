package com.sos.joc.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.Order200PSchema;
import com.sos.joc.order.post.OrderBody;
import com.sos.joc.order.post.OrderConfigurationBody;
import com.sos.joc.order.resource.IOrderConfigurationResource;
import com.sos.joc.response.JOCDefaultResponse;

@Path("order")
public class OrderConfigurationResourceImpl extends JOCResourceImpl implements IOrderConfigurationResource {
    private static final Logger LOGGER = Logger.getLogger(OrderConfigurationResourceImpl.class);
 
    @Override
    public JOCDefaultResponse postOrderConfiguration(String accessToken, OrderConfigurationBody orderBody) throws Exception {
        LOGGER.debug("init OrderConfiguration");
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
 
            ConfigurationSchema entity = new ConfigurationSchema();

            entity.setDeliveryDate(new Date());
            Configuration configuration = new Configuration();
            configuration.setConfigurationDate(new Date());
            Content content = new Content();
            content.setHtml("<html></html>");
            content.setXml("myXml");
            configuration.setContent(content);
            configuration.setPath("myPath");
            configuration.setSurveyDate(new Date());
            configuration.setType(Configuration.Type.order);
            entity.setConfiguration(configuration);
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
