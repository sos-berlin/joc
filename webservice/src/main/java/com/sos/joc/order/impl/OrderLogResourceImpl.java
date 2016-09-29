package com.sos.joc.order.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogContent;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200Schema;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema.Mime;
import com.sos.joc.order.resource.IOrderLogResource;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema) throws Exception {
        LOGGER.debug("init OrderHistory");

        try {
            checkRequiredParameter("jobschedulerId", orderFilterWithHistoryIdSchema.getJobschedulerId());
            checkRequiredParameter("jobChain", orderFilterWithHistoryIdSchema.getJobChain());
            checkRequiredParameter("orderId", orderFilterWithHistoryIdSchema.getOrderId());
            checkRequiredParameter("historyId", orderFilterWithHistoryIdSchema.getHistoryId());

            JOCDefaultResponse jocDefaultResponse = init(orderFilterWithHistoryIdSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200Schema entity = new LogContent200Schema();
            LogContent logContent = new LogContent(orderFilterWithHistoryIdSchema);

            entity.setDeliveryDate(new Date());
            LogContentSchema logContentSchema = new LogContentSchema();
            String log = logContent.getOrderLog();
             
            if (orderFilterWithHistoryIdSchema.getMime() != null && orderFilterWithHistoryIdSchema.getMime().toString().equals(Mime.HTML.toString())) {
                logContentSchema.setHtml(logContent.htmlContent(log));
            } else {
                if (orderFilterWithHistoryIdSchema.getMime() == null || orderFilterWithHistoryIdSchema.getMime().toString().equals(Mime.PLAIN.toString())) {
                    logContentSchema.setPlain(log);
                } else {
                    JocError jocError = new JocError();
                    jocError.setCode("JOC-100");
                    jocError.setMessage("Unknow mime type: " + orderFilterWithHistoryIdSchema.getMime());
                    throw new JocException(jocError);
                }
            }
            entity.setLog(logContentSchema);
            entity.setSurveyDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

 

}
