package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrderHistoryItem;
import com.sos.joc.model.order.OrderHistoryState;
import com.sos.joc.model.order.OrderHistoryStateText;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.orders.resource.IOrdersResourceHistory;

@Path("orders")
public class OrdersResourceHistoryImpl extends JOCResourceImpl implements IOrdersResourceHistory {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceHistoryImpl.class);
    private static final String API_CALL = "./orders/history";

    @Override
    public JOCDefaultResponse postOrdersHistory(String accessToken, OrdersFilter ordersFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, ordersFilter.getJobschedulerId(), getPermissons(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // Reading orders from the database tale inventory history tables
            // filtered by
            ordersFilter.getDateFrom(); // is ISO 8601 or something like 6h
                                        // 1w 65y
            ordersFilter.getDateTo(); // same as dateFrom
            ordersFilter.getTimeZone();
            ordersFilter.getRegex();
            ordersFilter.getOrders(); // list of wanted orders.

            // TODO JOC Cockpit Webservice (Data coming from db Inventory
            // History Tables)

            List<OrderHistoryItem> listHistory = new ArrayList<OrderHistoryItem>();

            OrderHistoryItem history = new OrderHistoryItem();
            history.setEndTime(new Date());
            history.setHistoryId("-1");
            history.setJobChain("myJobChain");
            history.setNode("myNode");
            history.setOrderId("myOrderId");
            history.setPath("myPath");
            history.setStartTime(new Date());
            OrderHistoryState state = new OrderHistoryState();
            state.setSeverity(0);
            state.set_text(OrderHistoryStateText.INCOMPLETE);
            history.setState(state);
            listHistory.add(history);

            OrderHistory entity = new OrderHistory();
            entity.setDeliveryDate(new Date());
            entity.setHistory(listHistory);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, ordersFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, ordersFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }

}
