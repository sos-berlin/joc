package com.sos.joc.jobchain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemReportTriggerWithResult;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceHistory;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrderHistoryItem;

@Path("job_chain")
public class JobChainResourceHistoryImpl extends JOCResourceImpl implements IJobChainResourceHistory {

    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_HAVE_ERROR = 2;
    private static final int STATUS_INCOMPLETE = 1;
    private static final int DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String XPATH_FOR_ORDER_HISTORY = "/spooler/answer/job_chain/order_history/order";
    private static final String API_CALL = "./job_chain/history";

    @Override
    public JOCDefaultResponse postJobChainHistory(String accessToken, JobChainHistoryFilter jobChainHistoryFilter) throws Exception {

        SOSHibernateConnection connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainHistoryFilter, accessToken, jobChainHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getView().isHistory());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);

            ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
            reportTriggerDBLayer.getFilter().setSchedulerId(jobChainHistoryFilter.getJobschedulerId());

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            if (jobChainHistoryFilter.getMaxLastHistoryItems() == null) {
                jobChainHistoryFilter.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }
            // TODO nested job chains have to consider too
            String jobChainCommand = jocXmlCommand.getShowJobChainCommand(normalizePath(jobChainHistoryFilter.getJobChain()), "order_history", 0,
                    jobChainHistoryFilter.getMaxLastHistoryItems());
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(jobChainCommand, accessToken);

            jocXmlCommand.createNodeList(XPATH_FOR_ORDER_HISTORY);

            int count = jocXmlCommand.getNodeList().getLength();

            List<OrderHistoryItem> listOfHistory = new ArrayList<OrderHistoryItem>();

            for (int i = 0; i < count; i++) {
                HistoryState state = new HistoryState();
                jocXmlCommand.getElementFromList(i);
                String node = jocXmlCommand.getAttribute("state");
                OrderHistoryItem history = new OrderHistoryItem();
                history.setEndTime(jocXmlCommand.getAttributeAsDate("end_time"));
                history.setHistoryId(jocXmlCommand.getAttribute("history_id"));
                history.setJobChain(normalizePath(jocXmlCommand.getAttribute("job_chain")));
                history.setNode(node);
                history.setOrderId(jocXmlCommand.getAttribute("id"));
                history.setPath(jocXmlCommand.getAttribute("path"));
                history.setStartTime(jocXmlCommand.getAttributeAsDate("start_time"));

                reportTriggerDBLayer.getFilter().clearOrderPath();
                reportTriggerDBLayer.getFilter().addOrderPath(jobChainHistoryFilter.getJobChain(), history.getOrderId());
                if (jocXmlCommand.getAttributeAsDate("end_time") != null) {

                    int status = getStatus(reportTriggerDBLayer);
                    switch (status) {
                    case STATUS_HAVE_ERROR: {
                        state.setSeverity(STATUS_HAVE_ERROR);
                        state.set_text(HistoryStateText.FAILED);
                        break;
                    }
                    case STATUS_SUCCESS: {
                        state.setSeverity(STATUS_SUCCESS);
                        state.set_text(HistoryStateText.SUCCESSFUL);
                        break;
                    }
                    case STATUS_INCOMPLETE: {
                        state.setSeverity(STATUS_INCOMPLETE);
                        state.set_text(HistoryStateText.INCOMPLETE);
                        break;
                    }
                    default: {
                        state.setSeverity(STATUS_INCOMPLETE);
                        state.set_text(HistoryStateText.INCOMPLETE);
                        break;
                    }
                    }
                } else {
                    state.setSeverity(STATUS_INCOMPLETE);
                    state.set_text(HistoryStateText.INCOMPLETE);
                }

                history.setState(state);

                listOfHistory.add(history);
            }

            OrderHistory entity = new OrderHistory();
            entity.setDeliveryDate(new Date());
            entity.setHistory(listOfHistory);

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

    private int getStatus(ReportTriggerDBLayer reportTriggerDBLayer) throws Exception {

        reportTriggerDBLayer.getFilter().setLimit(1);
        List<DBItemReportTriggerWithResult> listOfReportTriggerWithResultDBItems = reportTriggerDBLayer.getSchedulerOrderHistoryListFromTo();
        DBItemReportTriggerWithResult dbItemReportTriggerWithResult = null;
        if (listOfReportTriggerWithResultDBItems.size() > 0) {
            dbItemReportTriggerWithResult = listOfReportTriggerWithResultDBItems.get(0);
        } else {
            return STATUS_INCOMPLETE;
        }

        if (dbItemReportTriggerWithResult.getDbItemReportTrigger() == null || (dbItemReportTriggerWithResult.getDbItemReportTrigger()
                .getStartTime() != null && dbItemReportTriggerWithResult.getDbItemReportTrigger().getEndTime() == null)) {
            return STATUS_INCOMPLETE;
        } else {
            if (dbItemReportTriggerWithResult.haveError()) {
                return STATUS_HAVE_ERROR;
            } else {
                return STATUS_SUCCESS;
            }
        }
    }
}
