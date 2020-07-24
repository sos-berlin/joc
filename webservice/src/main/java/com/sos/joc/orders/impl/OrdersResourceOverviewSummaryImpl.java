package com.sos.joc.orders.impl;

import java.util.Date;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersHistoricSummary;
import com.sos.joc.model.order.OrdersOverView;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSummary;
import com.sos.schema.JsonValidator;

@Path("orders")
public class OrdersResourceOverviewSummaryImpl extends JOCResourceImpl implements IOrdersResourceOverviewSummary {

    private static final String API_CALL = "./orders/overview/summary";

    @Override
    public JOCDefaultResponse postOrdersOverviewSummary(String accessToken, byte[] ordersFilterBytes) {
        SOSHibernateSession connection = null;

		try {
		    JsonValidator.validateFailFast(ordersFilterBytes, OrdersFilter.class);
            OrdersFilter ordersFilter = Globals.objectMapper.readValue(ordersFilterBytes, OrdersFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken,
					ordersFilter.getJobschedulerId(),
					getPermissonsJocCockpit(ordersFilter.getJobschedulerId(), accessToken).getOrder().getView()
							.isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);

            ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
            reportTriggerDBLayer.getFilter().setSchedulerId(ordersFilter.getJobschedulerId());
            boolean withFolderFilter = ordersFilter.getFolders() != null && !ordersFilter.getFolders().isEmpty();
            boolean hasPermission = true;
            Set<Folder> folders = addPermittedFolders(ordersFilter.getFolders());

            if (ordersFilter.getDateFrom() != null) {
                reportTriggerDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDateFrom(ordersFilter.getDateFrom(), ordersFilter
                        .getTimeZone()));
            }
            if (ordersFilter.getDateTo() != null) {
                reportTriggerDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDateTo(ordersFilter.getDateTo(), ordersFilter.getTimeZone()));
            }

            if (ordersFilter.getOrders() != null && !ordersFilter.getOrders().isEmpty()) {
                hasPermission = false;
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                String unpermittedObject = null;
                for (OrderPath orderPath : ordersFilter.getOrders()) {
                    if (orderPath != null) {
                        if (canAdd(orderPath.getJobChain(), permittedFolders)) {
                            reportTriggerDBLayer.getFilter().addOrderPath(normalizePath(orderPath.getJobChain()), orderPath.getOrderId());
                            hasPermission = true;
                        } else {
                            unpermittedObject = orderPath.getJobChain();
                        }
                    }
                }
                if (!hasPermission && unpermittedObject != null) {
                    throw new JocFolderPermissionsException(getParent(unpermittedObject));
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                hasPermission = false;
                throw new JocFolderPermissionsException(ordersFilter.getFolders().get(0).getFolder());
            } else if (folders != null && !folders.isEmpty()) {
                reportTriggerDBLayer.getFilter().addFolderPaths(folders);
            }

            OrdersOverView entity = new OrdersOverView();
            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            if (hasPermission) {
                OrdersHistoricSummary ordersHistoricSummary = new OrdersHistoricSummary();
                entity.setOrders(ordersHistoricSummary);
                reportTriggerDBLayer.getFilter().setState(HistoryStateText.FAILED.toString());
                ordersHistoricSummary.setFailed(reportTriggerDBLayer.getCountSchedulerOrderHistoryListFromTo().intValue());

                reportTriggerDBLayer.getFilter().setState(HistoryStateText.SUCCESSFUL.toString());
                ordersHistoricSummary.setSuccessful(reportTriggerDBLayer.getCountSchedulerOrderHistoryListFromTo().intValue());
            } else {
                entity.setOrders(null);
            }

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
