package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.order.resource.IOrderLogHtmlResource;

@Path("order")
public class OrderLogHtmlResourceImpl extends JOCResourceImpl implements IOrderLogHtmlResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogHtmlResourceImpl.class);
    private static final String API_CALL = "./order/log/html";

    @Override
    public JOCDefaultResponse getOrderLogHtml(String accessToken, String jobschedulerId, String orderId, String jobChain, String historyId)
            throws Exception {
        LOGGER.debug(API_CALL);
        OrderHistoryFilter orderHistoryFilter = new OrderHistoryFilter();

        try {
            orderHistoryFilter.setHistoryId(historyId);
            orderHistoryFilter.setJobChain(jobChain);
            orderHistoryFilter.setOrderId(orderId);
            orderHistoryFilter.setJobschedulerId(jobschedulerId);
            orderHistoryFilter.setMime(LogMime.HTML);

            checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
            checkRequiredParameter("jobChain", orderHistoryFilter.getJobChain());
            checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
            checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());

            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerId, getPermissons(accessToken).getOrder().getView().isOrderLog(),
                    true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);
            String log = logOrderContent.getLog();

            return JOCDefaultResponse.responseHtmlStatus200(logOrderContent.htmlPageWithColouredLogContent(log, "Order " + orderHistoryFilter
                    .getOrderId()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderHistoryFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e);

        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderHistoryFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e, err);
        }
    }

}
