package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResourceP;
import com.sos.joc.response.JOCDefaultResponse;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourcePImpl.class);

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersBody ordersBody) throws Exception {
 
        
        LOGGER.debug("init OrdersP");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());       

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }        
        
        try {

            OrdersPSchema entity = new OrdersPSchema();

            // TODO JOC Cockpit Webservice: Reading from database
            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            List<Order> listOrder = new ArrayList<Order>();
            Order order1 = new Order();
            Order order2 = new Order();

            order1.setSurveyDate(new Date());
            order1.setPath("myPath1");
            order1.setOrderId("myOrderId1");
            order1.setJobChain("myJobChain1");
            if (!ordersBody.getCompact()) {
                order1.setEstimatedDuration(-1);
                order1.setConfigurationDate(new Date());
                order1.setEndState("myEndState");
                order1.setInitialState("myInitialState");
                order1.setTitle("myTitle");
                order1.setType(Order.Type.permanent);
                order1.setPriority(-1);

                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);
                order1.setParams(parameters);

                listOrder.add(order1);

                order2.setSurveyDate(new Date());
                order2.setPath("myPath1");
                order2.setOrderId("myOrderId1");
                order2.setJobChain("myJobChain1");
                if (!ordersBody.getCompact()) {
                    order2.setEstimatedDuration(-1);
                    order2.setConfigurationDate(new Date());
                    order2.setEndState("myEndState");
                    order2.setInitialState("myInitialState");
                    order2.setTitle("myTitle");
                    order2.setType(Order.Type.permanent);
                    order2.setPriority(-1);

                    order2.setParams(parameters);

                    listOrder.add(order2);
                }

                entity.setOrders(listOrder);

            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }
    
    
}
