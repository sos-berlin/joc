package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.order.resource.IOrderLogResource;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogResourceImpl.class);
    private static final String API_CALL = "./order/log";
    
    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
            checkRequiredParameter("jobChain", orderHistoryFilter.getJobChain());
            checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
            checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());

            JOCDefaultResponse jocDefaultResponse = init(orderHistoryFilter.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isOrderLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200 entity = new LogContent200();
            //TODO surveyDate from database
            entity.setSurveyDate(Date.from(Instant.now()));
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance);

            LogContent logContentSchema = new LogContent();
            String log = logOrderContent.getLog();
             
            if (orderHistoryFilter.getMime() != null && orderHistoryFilter.getMime() == LogMime.HTML) {
                logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(log));
            } else {
                if (orderHistoryFilter.getMime() == null || orderHistoryFilter.getMime() == LogMime.PLAIN) {
                    logContentSchema.setPlain(log);
                } else {
                    JocError jocError = new JocError();
                    jocError.setCode(WebserviceConstants.WRONG_MIME_TYPE);
                    jocError.setMessage("Unknow mime type: " + orderHistoryFilter.getMime());
                    throw new JocException(jocError);
                }
            }
            entity.setLog(logContentSchema);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderHistoryFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderHistoryFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }

 

}
