package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.order.resource.IOrderLogResource;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {

    private static final String API_CALL = "./order/log";

    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {

        try {
            initLogging(API_CALL, orderHistoryFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, orderHistoryFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getOrder()
                    .getView().isOrderLog(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
            checkRequiredParameter("jobChain", normalizePath(orderHistoryFilter.getJobChain()));
            checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
            checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());

            LogContent200 entity = new LogContent200();
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);
            // TODO surveyDate from database
            entity.setSurveyDate(Date.from(Instant.now()));

            LogContent logContentSchema = new LogContent();
            String log = logOrderContent.getLog();

            if (orderHistoryFilter.getMime() != null && orderHistoryFilter.getMime() == LogMime.HTML) {
                logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(log));
            } else {
                logContentSchema.setPlain(log);
            }
            entity.setLog(logContentSchema);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
