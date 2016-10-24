package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.order.History;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrdersStepHistory;
import com.sos.joc.model.order.OrdersStepHistoryItem;
import com.sos.joc.order.resource.IOrderHistoryResource;

@Path("order")
public class OrderHistoryResourceImpl extends JOCResourceImpl implements IOrderHistoryResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryResourceImpl.class);
    private static final String API_CALL = "./order/history";
    
    @Override
    public JOCDefaultResponse postOrderHistory(String accessToken, OrderFilter orderFilter) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            JOCDefaultResponse jocDefaultResponse = init(orderFilter.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO JOC Cockpit Webservice

            History history = new History();
            history.setHistoryId("-1");

            ArrayList<OrdersStepHistoryItem> listOfSteps = new ArrayList<OrdersStepHistoryItem>();
            OrdersStepHistoryItem step1 = new OrdersStepHistoryItem();
            step1.setEndTime(new Date());
            Err errorSchema1 = new Err();
            errorSchema1.setCode("myCode1");
            errorSchema1.setMessage("myMessage1");
            step1.setError(errorSchema1);
            step1.setJob("myJob1");
            step1.setNode("myNode1");
            step1.setStartTime(new Date());
            step1.setStep(1);
            step1.setClusterMember(-1);
            step1.setExitCode(-1);
            step1.setTaskId("-1");
            listOfSteps.add(step1);

            OrdersStepHistoryItem step2 = new OrdersStepHistoryItem();
            step2.setEndTime(new Date());
            Err errorSchema2 = new Err();
            errorSchema2.setCode("myCode2");
            errorSchema2.setMessage("myMessage2");
            step2.setError(errorSchema2);
            step2.setJob("myJob2");
            step2.setNode("myNode2");
            step2.setStartTime(new Date());
            step2.setStep(1);
            step2.setClusterMember(-1);
            step2.setExitCode(-1);
            step2.setTaskId("-1");
            listOfSteps.add(step2);

            history.setSteps(listOfSteps);
            
            OrdersStepHistory entity = new OrdersStepHistory();
            entity.setHistory(history);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }

}
