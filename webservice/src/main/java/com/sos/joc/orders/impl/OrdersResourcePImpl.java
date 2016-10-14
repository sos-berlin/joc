package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourcePImpl.class);
    private String dateFromFilter = null;
    private String dateToFilter = null;
    private String timeZoneFilter = null;
    private String regex = null;
    private List<Order_> ordersFilter = null;
    private List<FoldersSchema> foldersFilter = null;
    private Boolean compact = null;

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilterSchema ordersFilterSchema) throws Exception {
        LOGGER.debug("init OrdersP");
        try {
            JOCDefaultResponse jocDefaultResponse = init(ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            /** 
             * FILTERS:
             * dateFrom
             * dateTo
             * timeZone
             * regex
             * orders
             * folders
             */
            OrdersPSchema entity = new OrdersPSchema();
            entity.setDeliveryDate(new Date());

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection, ordersFilterSchema.getJobschedulerId());
            // FILTERS
            dateFromFilter = ordersFilterSchema.getDateFrom();
            dateToFilter = ordersFilterSchema.getDateTo();
            timeZoneFilter = ordersFilterSchema.getTimeZone();
            
            regex = ordersFilterSchema.getRegex();
            ordersFilter = ordersFilterSchema.getOrders();
            foldersFilter = ordersFilterSchema.getFolders();
            compact = ordersFilterSchema.getCompact();
            
            List<DBItemInventoryOrder> ordersFromDB = new ArrayList<DBItemInventoryOrder>();
            if (ordersFilter != null && !ordersFilter.isEmpty()) {
                for (Order_ order_ : ordersFilter) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByOrders(order_.getJobChain(), order_.getOrderId());
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (foldersFilter != null && !foldersFilter.isEmpty()) {
                for (FoldersSchema foldersSchema : foldersFilter) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByFolders(foldersSchema.getFolder());
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (regex != null && !regex.isEmpty()) {
                List<DBItemInventoryOrder> unfilteredOrders = dbLayer.getInventoryOrders();
                for (DBItemInventoryOrder unfilteredOrder : unfilteredOrders) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredOrder.getName());
                    if (regExMatcher.find()) {
                        ordersFromDB.add(unfilteredOrder); 
                    }
                }
            } else {
                ordersFromDB = dbLayer.getInventoryOrders();
            }
            if(ordersFromDB != null && !ordersFromDB.isEmpty()) {
                entity.setOrders(fillOutputOrders(ordersFromDB, dbLayer));
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private List<Order> fillOutputOrders (List<DBItemInventoryOrder> ordersFromDB, InventoryOrdersDBLayer dbLayer) throws Exception {
        List<Order> listOfOutputOrders = new ArrayList<Order>();
        for (DBItemInventoryOrder inventoryOrder : ordersFromDB) {
            Order order = new Order();
            order.setSurveyDate(inventoryOrder.getModified());
            order.setPath(inventoryOrder.getName());
            order.setOrderId(inventoryOrder.getOrderId());
            order.setJobChain(inventoryOrder.getJobChainName());
            if (compact == null || !compact) {
                Date configDate = dbLayer.getOrderConfigurationDate(inventoryOrder.getId());
                if(configDate != null) {
                    order.setConfigurationDate(new Date());
                }
                order.setEndState(inventoryOrder.getEndState());
                order.setInitialState(inventoryOrder.getInitialState());
                order.setTitle(inventoryOrder.getTitle());
                order.setType(Order.Type.PERMANENT);
                order.setPriority(inventoryOrder.getPriority());
            }
            listOfOutputOrders.add(order);
        }
        return listOfOutputOrders;
    }
    
}