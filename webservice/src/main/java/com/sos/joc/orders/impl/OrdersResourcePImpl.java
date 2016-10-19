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
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourcePImpl.class);
    private String dateFromFilter = null;
    private String dateToFilter = null;
    private String timeZoneFilter = null;
    private String regex = null;
    private List<OrderPath> ordersFilter = null;
    private List<Folder> foldersFilter = null;
    private Boolean compact = null;

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilter ordersFilterSchema) throws Exception {
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
            OrdersP entity = new OrdersP();
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
                for (OrderPath order : ordersFilter) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByOrders(order.getJobChain(), order.getOrderId());
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (foldersFilter != null && !foldersFilter.isEmpty()) {
                for (Folder folder : foldersFilter) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByFolders(folder.getFolder(), folder.getRecursive());
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
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private List<OrderP> fillOutputOrders (List<DBItemInventoryOrder> ordersFromDB, InventoryOrdersDBLayer dbLayer) throws Exception {
        List<OrderP> listOfOutputOrders = new ArrayList<OrderP>();
        for (DBItemInventoryOrder inventoryOrder : ordersFromDB) {
            OrderP order = new OrderP();
            order.setSurveyDate(inventoryOrder.getModified());
            order.setPath(inventoryOrder.getName());
            order.setOrderId(inventoryOrder.getOrderId());
            order.setJobChain(inventoryOrder.getJobChainName());
            Integer estimatedDuration = getEstimatedDurationInSeconds(inventoryOrder);
            if(estimatedDuration != null) {
                order.setEstimatedDuration(estimatedDuration);
            } else {
                order.setEstimatedDuration(0);
            }
            if (compact == null || !compact) {
                Date configDate = dbLayer.getOrderConfigurationDate(inventoryOrder.getId());
                if(configDate != null) {
                    order.setConfigurationDate(new Date());
                }
                order.setEndState(inventoryOrder.getEndState());
                order.setInitialState(inventoryOrder.getInitialState());
                order.setTitle(inventoryOrder.getTitle());
                order.set_type(OrderType.PERMANENT);
                order.setPriority(inventoryOrder.getPriority());
            }
            listOfOutputOrders.add(order);
        }
        return listOfOutputOrders;
    }
    
    private Integer getEstimatedDurationInSeconds(DBItemInventoryOrder order) throws Exception {
        JobSchedulerOrderHistoryDBLayer dbLayer = new JobSchedulerOrderHistoryDBLayer(Globals.sosHibernateConnection);
        Long estimatedDurationInMillis = dbLayer.getOrderEstimatedDuration(order.getOrderId());
        if (estimatedDurationInMillis != null) {
            return estimatedDurationInMillis.intValue()/1000;
        }
        return null;
    }

}