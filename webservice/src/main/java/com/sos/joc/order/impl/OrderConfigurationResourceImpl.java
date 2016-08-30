package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationEntity;
import com.sos.joc.order.post.OrderConfigurationBody;
import com.sos.joc.order.resource.IOrderConfigurationResource;

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
 
            // TODO JOC Cockpit Webservice
            ConfigurationEntity configurationEntity = new ConfigurationEntity();
            return JOCDefaultResponse.responseStatus200(configurationEntity.getEntity());
            
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
