package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.db.reporting.ReportDBLayer;
import com.sos.joc.exceptions.JocError;
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
    private static final String API_CALL = "./orders/p";
    private String regex = null;
    private List<OrderPath> ordersFilter = null;
    private List<Folder> foldersFilter = null;
    private Boolean compact = null;

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilter ordersFilterSchema) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            OrdersP entity = new OrdersP();
            Long instanceId = dbItemInventoryInstance.getId();
            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection);
            regex = ordersFilterSchema.getRegex();
            ordersFilter = ordersFilterSchema.getOrders();
            foldersFilter = ordersFilterSchema.getFolders();
            compact = ordersFilterSchema.getCompact();
            List<DBItemInventoryOrder> ordersFromDB = new ArrayList<DBItemInventoryOrder>();
            if (ordersFilter != null && !ordersFilter.isEmpty()) {
                for (OrderPath order : ordersFilter) {
                    List<DBItemInventoryOrder> filteredOrders =
                            dbLayer.getInventoryOrdersFilteredByOrders(order.getJobChain(), order.getOrderId(), instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (foldersFilter != null && !foldersFilter.isEmpty()) {
                for (Folder folder : foldersFilter) {
                    List<DBItemInventoryOrder> filteredOrders =
                            dbLayer.getInventoryOrdersFilteredByFolders(folder.getFolder(), folder.getRecursive(), instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (regex != null && !regex.isEmpty()) {
                List<DBItemInventoryOrder> unfilteredOrders = dbLayer.getInventoryOrders(instanceId);
                for (DBItemInventoryOrder unfilteredOrder : unfilteredOrders) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredOrder.getName());
                    if (regExMatcher.find()) {
                        ordersFromDB.add(unfilteredOrder); 
                    }
                }
            } else {
                ordersFromDB = dbLayer.getInventoryOrders(instanceId);
            }
            entity.setOrders(fillOutputOrders(ordersFromDB, dbLayer));
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, ordersFilterSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, ordersFilterSchema));
            return JOCDefaultResponse.responseStatusJSError(e, err);
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
        ReportDBLayer dbLayer = new ReportDBLayer(Globals.sosHibernateConnection);
        Long estimatedDurationInMillis = dbLayer.getOrderEstimatedDuration(order.getOrderId());
        if (estimatedDurationInMillis != null) {
            return estimatedDurationInMillis.intValue()/1000;
        }
        return null;
    }

}