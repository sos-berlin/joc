package com.sos.joc.order.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.LogContent200Schema;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.order.post.OrderBody;
import com.sos.joc.order.resource.IOrderLogResource;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {
    private static final Logger LOGGER = Logger.getLogger(OrderLogResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, OrderBody orderBody) throws Exception {
        LOGGER.debug("init OrderHistory");

        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            LogContent200Schema entity = new LogContent200Schema();

            entity.setDeliveryDate(new Date());
            LogContentSchema logContentSchema = new LogContentSchema();
            logContentSchema.setHtml("<html><body>myLog</body></html>");
            logContentSchema.setPlain("myLog");
            entity.setLog(logContentSchema);
            entity.setSurveyDate(new Date());

            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
