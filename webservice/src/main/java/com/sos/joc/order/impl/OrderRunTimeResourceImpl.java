package com.sos.joc.order.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.common.RuntimeSchema;
import com.sos.joc.order.post.OrderRunTimeBody;
import com.sos.joc.order.resource.IOrderRunTimeResource;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {
    private static final Logger LOGGER = Logger.getLogger(OrderRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderRunTimeBody orderRunTimeBody) throws Exception {
        LOGGER.debug("init OrderRunTime");
        JOCDefaultResponse jocDefaultResponse = init(orderRunTimeBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            Runtime200Schema entity = new Runtime200Schema();

            entity.setDeliveryDate(new Date());

            RuntimeSchema runtimeSchema = new RuntimeSchema();
            runtimeSchema.setRunTime("myRuntime");
            runtimeSchema.setSurveyDate(new Date());
            entity.setRunTime(runtimeSchema);

            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
