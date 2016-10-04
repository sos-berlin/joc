package com.sos.joc.order.impl;


import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;
import com.sos.joc.order.resource.IOrderLogHtmlResource;

@Path("order")
public class OrderLogHtmlResourceImpl extends JOCResourceImpl implements IOrderLogHtmlResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getOrderLogHtml(String accessToken, String jobschedulerId, String orderId, String jobChain, String historyId) throws Exception {
        LOGGER.debug("init order/log/html");

        try {
            
            OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema = new OrderFilterWithHistoryIdSchema();
            if (historyId != null){
                orderFilterWithHistoryIdSchema.setHistoryId(Long.parseLong(historyId));
            }
            orderFilterWithHistoryIdSchema.setJobChain(jobChain);
            orderFilterWithHistoryIdSchema.setOrderId(orderId);
            orderFilterWithHistoryIdSchema.setJobschedulerId(jobschedulerId);
            orderFilterWithHistoryIdSchema.setMime(OrderFilterWithHistoryIdSchema.Mime.HTML);
           
            checkRequiredParameter("jobschedulerId", orderFilterWithHistoryIdSchema.getJobschedulerId());
            checkRequiredParameter("jobChain", orderFilterWithHistoryIdSchema.getJobChain());
            checkRequiredParameter("orderId", orderFilterWithHistoryIdSchema.getOrderId());
            checkRequiredParameter("historyId", orderFilterWithHistoryIdSchema.getHistoryId());

            JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getOrder().getView().isOrderLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent logContent = new LogContent(orderFilterWithHistoryIdSchema);

            String log = logContent.getOrderLog();
            return JOCDefaultResponse.responseStatus200(logContent.htmlPageWithColouredLogContent(log));

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}
