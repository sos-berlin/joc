package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.History;
import com.sos.joc.model.order.HistorySchema;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.State;
import com.sos.joc.orders.resource.IOrdersResourceHistory;

@Path("orders")
public class OrdersResourceHistoryImpl extends JOCResourceImpl implements IOrdersResourceHistory {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceHistoryImpl.class);

    @Override
    public JOCDefaultResponse postOrdersHistory(String accessToken, OrdersFilterSchema orderFilterSchema) throws Exception {
        LOGGER.debug("init Orders");
        try {
            JOCDefaultResponse jocDefaultResponse = init(orderFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // Reading orders from the database tale inventory history tables
            // filtered by
            orderFilterSchema.getDateFrom(); // is ISO 8601 or something like 6h
                                             // 1w 65y
            orderFilterSchema.getDateTo(); // same as dateFrom
            orderFilterSchema.getTimeZone();
            orderFilterSchema.getRegex();
            orderFilterSchema.getOrders(); // list of wanted orders.

            // TODO JOC Cockpit Webservice (Data coming from db Inventory
            // History Tables)

            HistorySchema entity = new HistorySchema();
            List<History> listHistory = new ArrayList<History>();

            History history = new History();
            history.setEndTime(new Date());
            history.setHistoryId(-1);
            history.setJobChain("myJobChain");
            history.setNode("myNode");
            history.setOrderId("myOrderId");
            history.setPath("myPath");
            history.setStartTime(new Date());
            State state = new State();
            state.setSeverity(0);
            state.setText(State.Text.INCOMPLETE);
            history.setState(state);
            listHistory.add(history);

            entity.setDeliveryDate(new Date());
            entity.setHistory(listHistory);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
