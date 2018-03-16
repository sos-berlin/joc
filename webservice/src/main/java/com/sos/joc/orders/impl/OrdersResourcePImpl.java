package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderPermanent;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.lock.LockP;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {

    private static final String API_CALL = "./orders/p";
    private String regex = null;
    private List<OrderPath> orderPaths = null;
    private List<Folder> folders = null;
    private Boolean compact = null;

    @Override
    public JOCDefaultResponse postOrdersP(String xAccessToken, String accessToken, OrdersFilter ordersFilter) throws Exception {
        return postOrdersP(getAccessToken(xAccessToken, accessToken), ordersFilter);
    }

    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilter ordersFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken, ordersFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            OrdersP entity = new OrdersP();
            Long instanceId = dbItemInventoryInstance.getId();
            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
            regex = ordersFilter.getRegex();
            orderPaths = ordersFilter.getOrders();
            folders = addPermittedFolder(ordersFilter.getFolders());
            
            compact = ordersFilter.getCompact();
            List<DBItemInventoryOrder> ordersFromDB = new ArrayList<DBItemInventoryOrder>();
            if (orderPaths != null && !orderPaths.isEmpty()) {
                for (OrderPath order : orderPaths) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByOrders(normalizePath(order.getJobChain()), order
                            .getOrderId(), instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryOrder> filteredOrders = null;
                    filteredOrders = dbLayer.getInventoryOrdersFilteredByFolders(normalizeFolder(folder.getFolder()), folder.getRecursive(),
                            instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (regex != null && !regex.isEmpty()) {
                List<DBItemInventoryOrder> unfilteredOrders = dbLayer.getInventoryOrders(instanceId);
                for (DBItemInventoryOrder unfilteredOrder : unfilteredOrders) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredOrder.getName());
                    if (regExMatcher.find() && canAdd(unfilteredOrder,unfilteredOrder.getName())) {
                        ordersFromDB.add(unfilteredOrder);
                    }
                }
            } else {
                List<DBItemInventoryOrder>  filteredOrders = dbLayer.getInventoryOrders(instanceId);
                if (filteredOrders != null && !filteredOrders.isEmpty()) {
                    ordersFromDB.addAll(filteredOrders);
                }
            }
            ordersFromDB = addAllPermittedOrder(ordersFromDB);
            entity.setOrders(OrderPermanent.fillOutputOrders(ordersFromDB, dbLayer, compact));
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    private List<DBItemInventoryOrder> addAllPermittedOrder(List<DBItemInventoryOrder> ordersToAdd) throws SessionNotExistException{
        List<DBItemInventoryOrder> listOfOrders = new ArrayList<DBItemInventoryOrder>();
        if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
            for (DBItemInventoryOrder order : ordersToAdd)
                if (canAdd(order, order.getName())) {
                    listOfOrders.add(order);
                }
        } else {
            listOfOrders.addAll(ordersToAdd);
        }
        return listOfOrders;

    }
}