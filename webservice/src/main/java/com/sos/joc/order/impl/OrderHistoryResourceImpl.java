package com.sos.joc.order.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.order.History_;
import com.sos.joc.model.order.Step;
import com.sos.joc.model.order.StepHistorySchema;
import com.sos.joc.order.post.OrderBody;
import com.sos.joc.order.resource.IOrderHistoryResource;

@Path("order")
public class OrderHistoryResourceImpl extends JOCResourceImpl implements IOrderHistoryResource {
    private static final Logger LOGGER = Logger.getLogger(OrderHistoryResourceImpl.class);
 
    @Override
    public JOCDefaultResponse postOrderHistory(String accessToken, OrderBody orderBody) throws Exception {
        LOGGER.debug("init OrderHistory");
      
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
 
            StepHistorySchema entity = new StepHistorySchema();
            
            entity.setDeliveryDate(new Date());
            History_ history = new History_();
            history.setHistoryId(-1);
           
            ArrayList<Step> listOfSteps = new ArrayList<Step>();
            Step step1 = new Step();
            step1.setEndTime(new Date());
            ErrorSchema errorSchema1 = new ErrorSchema();
            errorSchema1.setCode("myCode1");
            errorSchema1.setMessage("myMessage1");
            step1.setError(errorSchema1);
            step1.setJob("myJob1");
            step1.setNode("myNode1");
            step1.setStartTime(new Date());
            step1.setStep(1);
            step1.setClusterMember(-1);
            step1.setExitCode(-1);
            step1.setTaskId(-1);
            listOfSteps.add(step1);

            Step step2 = new Step();
            step2.setEndTime(new Date());
            ErrorSchema errorSchema2 = new ErrorSchema();
            errorSchema2.setCode("myCode2");
            errorSchema2.setMessage("myMessage2");
            step2.setError(errorSchema2);
            step2.setJob("myJob2");
            step2.setNode("myNode2");
            step2.setStartTime(new Date());
            step2.setStep(1);
            step2.setClusterMember(-1);
            step2.setExitCode(-1);
            step2.setTaskId(-1);            listOfSteps.add(step2);

            history.setSteps(listOfSteps);
            entity.setHistory(history);
           
      
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
 
     }


}
