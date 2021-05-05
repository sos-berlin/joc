package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderPermanent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;
import com.sos.joc.orders.resource.IOrdersResourceP;
import com.sos.schema.JsonValidator;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {

    private static final String API_CALL = "./orders/p";

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, byte[] ordersFilterBytes) {
        SOSHibernateSession connection = null;

        try {
            JsonValidator.validateFailFast(ordersFilterBytes, OrdersFilter.class);
            OrdersFilter ordersFilter = Globals.objectMapper.readValue(ordersFilterBytes, OrdersFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken, ordersFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(ordersFilter.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            OrdersP entity = new OrdersP();
            Long instanceId = dbItemInventoryInstance.getId();
            DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
            Map<String,String> documentations = dbDocLayer.getDocumentationPaths(ordersFilter.getJobschedulerId(), JobSchedulerObjectType.ORDER);
            
            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
            String regex = ordersFilter.getRegex();
            List<OrderPath> orderPaths = ordersFilter.getOrders();
            boolean withFolderFilter = ordersFilter.getFolders() != null && !ordersFilter.getFolders().isEmpty();
            Set<Folder> folders = addPermittedFolders(ordersFilter.getFolders());
            
            List<DBItemInventoryOrder> ordersFromDB = new ArrayList<DBItemInventoryOrder>();
            if (orderPaths != null && !orderPaths.isEmpty()) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                for (OrderPath order : orderPaths) {
                    if (order != null) {
                        if (canAdd(order.getJobChain(), permittedFolders)) {
                            List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByOrders(normalizePath(order.getJobChain()),
                                    order.getOrderId(), instanceId);
                            if (filteredOrders != null && !filteredOrders.isEmpty()) {
                                ordersFromDB.addAll(filteredOrders);
                            }
                        }
                    }
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                // no folder permissions
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByFolders(normalizeFolder(folder.getFolder()),
                            folder.getRecursive(), instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (regex != null && !regex.isEmpty()) {
                List<DBItemInventoryOrder> unfilteredOrders = dbLayer.getInventoryOrders(instanceId);
                Set<Folder> foldersSet = folderPermissions.getListOfFolders();
                if (unfilteredOrders != null) {
                    for (DBItemInventoryOrder unfilteredOrder : unfilteredOrders) {
                        Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredOrder.getName());
                        if (regExMatcher.find() && canAdd(unfilteredOrder.getName(), foldersSet)) {
                            ordersFromDB.add(unfilteredOrder);
                        }
                    }
                }
            } else {
                List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrders(instanceId);
                if (filteredOrders != null && !filteredOrders.isEmpty()) {
                    ordersFromDB.addAll(filteredOrders);
                }
            }
            entity.setOrders(OrderPermanent.fillOutputOrders(ordersFromDB, dbLayer, documentations, ordersFilter.getCompact()));
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
}