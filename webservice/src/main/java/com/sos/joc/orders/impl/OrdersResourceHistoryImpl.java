package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTrigger;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrderHistoryItem;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.orders.resource.IOrdersResourceHistory;

@Path("orders")
public class OrdersResourceHistoryImpl extends JOCResourceImpl implements IOrdersResourceHistory {

    private static final String API_CALL = "./orders/history";

    @Override
    public JOCDefaultResponse postOrdersHistory(String xAccessToken, String accessToken, OrdersFilter ordersFilter) throws Exception {
        return postOrdersHistory(getAccessToken(xAccessToken, accessToken), ordersFilter);
    }

    public JOCDefaultResponse postOrdersHistory(String accessToken, OrdersFilter ordersFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            if (ordersFilter.getJobschedulerId() == null) {
                ordersFilter.setJobschedulerId("");
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken, ordersFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getHistory().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);

            List<OrderHistoryItem> listHistory = new ArrayList<OrderHistoryItem>();

            ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
            reportTriggerDBLayer.getFilter().setSchedulerId(ordersFilter.getJobschedulerId());
            if (ordersFilter.getDateFrom() != null) {
                reportTriggerDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDateFrom(ordersFilter.getDateFrom(), ordersFilter
                        .getTimeZone()));
            }
            if (ordersFilter.getDateTo() != null) {
                reportTriggerDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDateTo(ordersFilter.getDateTo(), ordersFilter.getTimeZone()));
            }

            if (ordersFilter.getHistoryStates().size() > 0) {
                for (HistoryStateText historyStateText : ordersFilter.getHistoryStates()) {
                    reportTriggerDBLayer.getFilter().addState(historyStateText.toString());
                }
            }

            if (ordersFilter.getOrders().size() > 0) {
                InventoryJobChainsDBLayer jobChainDbLayer = new InventoryJobChainsDBLayer(connection);
                Long instanceId = null;
                if (!ordersFilter.getJobschedulerId().isEmpty()) {
                    instanceId = dbItemInventoryInstance.getId();
                }
                for (OrderPath orderPath : ordersFilter.getOrders()) {
                    String normalizeJobChain = normalizePath(orderPath.getJobChain());
                    List<String> innerChains = jobChainDbLayer.getInnerJobChains(normalizeJobChain, instanceId);
                    if (innerChains == null) {
                        reportTriggerDBLayer.getFilter().addOrderPath(normalizeJobChain, orderPath.getOrderId());
                    } else {
                        for (String innerChain : innerChains) {
                            reportTriggerDBLayer.getFilter().addOrderPath(innerChain, orderPath.getOrderId());
                        }
                    }
                }
                ordersFilter.setRegex("");
            } else {

                if (ordersFilter.getExcludeOrders().size() > 0) {
                    for (OrderPath orderPath : ordersFilter.getExcludeOrders()) {
                        reportTriggerDBLayer.getFilter().addIgnoreItems(normalizePath(orderPath.getJobChain()), orderPath.getOrderId());
                    }
                }

                if (ordersFilter.getFolders().size() > 0) {
                    for (Folder folder : ordersFilter.getFolders()) {
                        reportTriggerDBLayer.getFilter().addFolderPath(normalizeFolder(folder.getFolder()), folder.getRecursive());
                    }
                }
            }

            if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
                    reportTriggerDBLayer.getFilter().addFolderPath(normalizeFolder(folder.getFolder()), folder.isRecursive());
                }
            }

            if (ordersFilter.getLimit() == null) {
                ordersFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
            }
            reportTriggerDBLayer.getFilter().setLimit(ordersFilter.getLimit());
            List<DBItemReportTrigger> listOfDBItemReportTrigger = reportTriggerDBLayer.getSchedulerOrderHistoryListFromTo();

            Matcher regExMatcher = null;
            if (ordersFilter.getRegex() != null && !ordersFilter.getRegex().isEmpty()) {
                regExMatcher = Pattern.compile(ordersFilter.getRegex()).matcher("");
            }

            for (DBItemReportTrigger dbItemReportTrigger : listOfDBItemReportTrigger) {

                boolean add = true;

                OrderHistoryItem history = new OrderHistoryItem();
                if (ordersFilter.getJobschedulerId().isEmpty()) {
                    history.setJobschedulerId(dbItemReportTrigger.getSchedulerId());
                }
                history.setEndTime(dbItemReportTrigger.getEndTime());
                history.setHistoryId(String.valueOf(dbItemReportTrigger.getHistoryId()));
                history.setJobChain(dbItemReportTrigger.getParentName());
                history.setNode(dbItemReportTrigger.getState());
                history.setOrderId(dbItemReportTrigger.getName());
                history.setPath(dbItemReportTrigger.getFullOrderQualifier());
                history.setStartTime(dbItemReportTrigger.getStartTime());
                HistoryState state = new HistoryState();

                if (dbItemReportTrigger.getStartTime() != null && dbItemReportTrigger.getEndTime() == null) {
                    state.setSeverity(1);
                    state.set_text(HistoryStateText.INCOMPLETE);
                } else {
                    if (dbItemReportTrigger.getResultError()) {
                        state.setSeverity(2);
                        state.set_text(HistoryStateText.FAILED);
                    } else {
                        if (dbItemReportTrigger.getEndTime() != null && !dbItemReportTrigger.getResultError()) {
                            state.setSeverity(0);
                            state.set_text(HistoryStateText.SUCCESSFUL);
                        }
                    }

                }
                history.setState(state);
                history.setSurveyDate(dbItemReportTrigger.getCreated());

                if (regExMatcher != null) {
                    regExMatcher.reset(dbItemReportTrigger.getParentName() + "," + dbItemReportTrigger.getName());
                    add = regExMatcher.find();
                }

                if (add && FilterAfterResponse.filterStateHasState(ordersFilter.getHistoryStates(), history.getState().get_text())) {
                    listHistory.add(history);
                }

            }

            OrderHistory entity = new OrderHistory();
            entity.setDeliveryDate(new Date());
            entity.setHistory(listHistory);

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
