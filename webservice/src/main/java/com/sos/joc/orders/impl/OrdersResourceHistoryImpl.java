package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemReportTriggerWithResult;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
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
    public JOCDefaultResponse postOrdersHistory(String accessToken, OrdersFilter ordersFilter) throws Exception {
        try {
            initLogging(API_CALL, ordersFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, ordersFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Globals.beginTransaction();

            List<OrderHistoryItem> listHistory = new ArrayList<OrderHistoryItem>();

            ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(Globals.sosHibernateConnection);
            reportTriggerDBLayer.getFilter().setSchedulerId(ordersFilter.getJobschedulerId());
            if (ordersFilter.getDateFrom() != null) {
                reportTriggerDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDate(ordersFilter.getDateFrom(), ordersFilter.getTimeZone()));
            }
            if (ordersFilter.getDateTo() != null) {
                reportTriggerDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDate(ordersFilter.getDateTo(), ordersFilter.getTimeZone()));
            }

            if (ordersFilter.getOrders().size() > 0) {
                for (OrderPath orderPath : ordersFilter.getOrders()) {
                    reportTriggerDBLayer.getFilter().addOrderPath(orderPath.getJobChain(), orderPath.getOrderId());
                }
                ordersFilter.setRegex("");
            } else {
                if (ordersFilter.getHistoryStates().size() > 0) {
                    for (HistoryStateText historyStateText : ordersFilter.getHistoryStates()) {
                        reportTriggerDBLayer.getFilter().addState(historyStateText.toString());
                    }
                }
                if (ordersFilter.getFolders().size() > 0) {
                    for (Folder folder : ordersFilter.getFolders()) {
                        reportTriggerDBLayer.getFilter().addFolderPath(folder.getFolder(), folder.getRecursive());
                    }
                }
            }

            if (ordersFilter.getLimit() == null) {
                ordersFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
            }
            reportTriggerDBLayer.getFilter().setLimit(ordersFilter.getLimit());
            List<DBItemReportTriggerWithResult> listOfReportTriggerWithResultDBItems = reportTriggerDBLayer.getSchedulerOrderHistoryListFromTo();

            for (DBItemReportTriggerWithResult dbItemReportTriggerWithResult : listOfReportTriggerWithResultDBItems) {

                boolean add = true;
                OrderHistoryItem history = new OrderHistoryItem();
                history.setEndTime(dbItemReportTriggerWithResult.getDbItemReportTrigger().getEndTime());
                history.setHistoryId(String.valueOf(dbItemReportTriggerWithResult.getDbItemReportTrigger().getHistoryId()));
                history.setJobChain(dbItemReportTriggerWithResult.getDbItemReportTrigger().getParentName());
                history.setNode(dbItemReportTriggerWithResult.getDbItemReportTrigger().getState());
                history.setOrderId(dbItemReportTriggerWithResult.getDbItemReportTrigger().getName());
                history.setPath(dbItemReportTriggerWithResult.getDbItemReportTrigger().getFullOrderQualifier());
                history.setStartTime(dbItemReportTriggerWithResult.getDbItemReportTrigger().getStartTime());
                HistoryState state = new HistoryState();

                if (dbItemReportTriggerWithResult.getDbItemReportTrigger().getStartTime() != null && dbItemReportTriggerWithResult.getDbItemReportTrigger().getEndTime() == null) {
                    state.setSeverity(1);
                    state.set_text(HistoryStateText.INCOMPLETE);
                } else {
                    if (dbItemReportTriggerWithResult.haveError()) {
                        state.setSeverity(2);
                        state.set_text(HistoryStateText.FAILED);
                    } else {
                        if (dbItemReportTriggerWithResult.getDbItemReportTrigger().getEndTime() != null && !dbItemReportTriggerWithResult.haveError()) {
                            state.setSeverity(0);
                            state.set_text(HistoryStateText.SUCCESSFUL);
                        }
                    }

                }
                history.setState(state);
                history.setSurveyDate(dbItemReportTriggerWithResult.getDbItemReportTrigger().getCreated());

                if (ordersFilter.getRegex() != null && !ordersFilter.getRegex().isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(ordersFilter.getRegex()).matcher(dbItemReportTriggerWithResult.getDbItemReportTrigger().getParentName() + ","
                            + dbItemReportTriggerWithResult.getDbItemReportTrigger().getName());
                    add = regExMatcher.find();
                }

                if (add) {
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
            Globals.rollback();
        }
    }

}
