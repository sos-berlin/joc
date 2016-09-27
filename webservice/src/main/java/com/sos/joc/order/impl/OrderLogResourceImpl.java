package com.sos.joc.order.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200Schema;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;
import com.sos.joc.order.resource.IOrderLogResource;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema) throws Exception {
        LOGGER.debug("init OrderHistory");

        try {
            JOCDefaultResponse jocDefaultResponse = init(orderFilterWithHistoryIdSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200Schema entity = new LogContent200Schema();

            entity.setDeliveryDate(new Date());
            LogContentSchema logContentSchema = new LogContentSchema();
            logContentSchema.setHtml("<html><body>myLog</body></html>");
            logContentSchema.setPlain("myLog");
            entity.setLog(logContentSchema);
            entity.setSurveyDate(new Date());

            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
