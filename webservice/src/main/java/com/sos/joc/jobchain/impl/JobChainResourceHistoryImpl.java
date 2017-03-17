package com.sos.joc.jobchain.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.Path;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTrigger;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceHistory;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrderHistoryItem;

@Path("job_chain")
public class JobChainResourceHistoryImpl extends JOCResourceImpl implements IJobChainResourceHistory {

    private static final int DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String XPATH_FOR_ORDER_HISTORY = "/spooler/answer/job_chain/order_history/order";
    private static final String API_CALL = "./job_chain/history";
    private ReportTriggerDBLayer reportTriggerDBLayer;
    private InventoryJobChainsDBLayer jobChainsLayer;

    private SortedSet<OrderHistoryItem> handleJobChain(JOCXmlCommand jocXmlCommand, JobChainHistoryFilter jobChainHistoryFilter) throws Exception {

        SortedSet<OrderHistoryItem> orderHistoryItems = new TreeSet<OrderHistoryItem>(new Comparator<OrderHistoryItem>() {

            public int compare(OrderHistoryItem a, OrderHistoryItem b) {
                if (b.getEndTime() != null && a.getEndTime() != null) {
                    int comp = b.getEndTime().compareTo(a.getEndTime());
                    if (comp == 0 && b.getStartTime() != null && a.getStartTime() != null) {
                        comp = b.getStartTime().compareTo(a.getStartTime()); 
                    }
                    if (comp == 0) {
                       comp = 1; 
                    }
                    return comp;
                } else if (b.getEndTime() == null && a.getEndTime() != null) {
                    return 1;
                } else if (b.getEndTime() != null && a.getEndTime() == null) {
                    return -1;
                } else if (b.getStartTime() != null && a.getStartTime() != null) {
                    int comp = b.getStartTime().compareTo(a.getStartTime());
                    if (comp == 0) {
                        comp = 1; 
                    }
                }
                return 1; // this case should not happens
            }
        });

        jocXmlCommand.createNodeList(XPATH_FOR_ORDER_HISTORY);

        int count = jocXmlCommand.getNodeList().getLength();

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

            HistoryStateText status = getStatus(reportTriggerDBLayer, jobChainsLayer, history);
            state.setSeverity(getSeverityFromHistoryStateText(status));
            state.set_text(status);
            history.setState(state);
            orderHistoryItems.add(history);
            if (orderHistoryItems.size() >= jobChainHistoryFilter.getMaxLastHistoryItems()) {
                break;
            }

        }
        return orderHistoryItems;

    }

    @Override
    public JOCDefaultResponse postJobChainHistory(String accessToken, JobChainHistoryFilter jobChainHistoryFilter) throws Exception {

        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainHistoryFilter, accessToken, jobChainHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getView().isHistory());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);

            reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
            reportTriggerDBLayer.getFilter().setSchedulerId(jobChainHistoryFilter.getJobschedulerId());
            jobChainsLayer = new InventoryJobChainsDBLayer(connection);

            if (jobChainHistoryFilter.getMaxLastHistoryItems() == null) {
                jobChainHistoryFilter.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);

            String jobChainCommand = jocXmlCommand.getShowJobChainCommand(normalizePath(jobChainHistoryFilter.getJobChain()), "order_history", 0,
                    jobChainHistoryFilter.getMaxLastHistoryItems());
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(jobChainCommand, accessToken);

            // nested Job chains ermitteln
            Node jobChain = jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/job_chain");
            NodeList jobChainNodes = jocXmlCommand.getSosxml().selectNodeList(jobChain, "job_chain_node/@job_chain | job_chain_node.job_chain/@job_chain");

            List<OrderHistoryItem> listOfHistory = new ArrayList<OrderHistoryItem>();

            if (jobChainNodes.getLength() > 0) {
                boolean validInnerChainExist = false;
                StringBuilder str = new StringBuilder();
                str.append("<commands>");
                for (int i = 0; i < jobChainNodes.getLength(); i++) {
                    String innerChainPath = normalizePath(jobChainNodes.item(i).getNodeValue());
                    if (jocXmlCommand.getSosxml().selectSingleNode(jobChain, "file_based/requisites/requisite[@path='" + innerChainPath.toLowerCase()
                            + "' and @is_missing='yes']") != null) {
                        continue;
                    }
                    validInnerChainExist = true;
                    str.append(jocXmlCommand.getShowJobChainCommand(innerChainPath, "order_history", 0, jobChainHistoryFilter
                            .getMaxLastHistoryItems()));
                }
                str.append("</commands>");
                if (validInnerChainExist) {
                    jocXmlCommand.executePostWithThrowBadRequestAfterRetry(str.toString(), accessToken); 
                    listOfHistory.addAll(handleJobChain(jocXmlCommand, jobChainHistoryFilter));
                }
            } else {
                listOfHistory.addAll(handleJobChain(jocXmlCommand, jobChainHistoryFilter));
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

    private Integer getSeverityFromHistoryStateText(HistoryStateText status) {
        switch (status) {
        case FAILED:
            return 2;
        case SUCCESSFUL:
            return 0;
        default:
            return 1;
        }
    }

    private HistoryStateText getStatus(ReportTriggerDBLayer reportTriggerDBLayer, InventoryJobChainsDBLayer jobChainsLayer, OrderHistoryItem history)
            throws Exception {

        if (history.getEndTime() == null) {
            return HistoryStateText.INCOMPLETE;
        }
        reportTriggerDBLayer.getFilter().clearReportItems();
        reportTriggerDBLayer.getFilter().setLimit(1);
        reportTriggerDBLayer.getFilter().addOrderHistoryId(new Long(history.getHistoryId()));
        List<DBItemReportTrigger> listOfReportTriggerDBItems = reportTriggerDBLayer.getSchedulerOrderHistoryListFromTo();
        DBItemReportTrigger dbItemReportTrigger = null;
        if (listOfReportTriggerDBItems.size() > 0) {
            dbItemReportTrigger = listOfReportTriggerDBItems.get(0);
        } else {
            if (!jobChainsLayer.isErrorNode(history.getJobChain(), history.getNode(), dbItemInventoryInstance.getId())) {
                return HistoryStateText.SUCCESSFUL;
            } else {
                return HistoryStateText.FAILED;
            }
        }

        if (dbItemReportTrigger == null || (dbItemReportTrigger.getStartTime() != null && dbItemReportTrigger.getEndTime() == null)) {
            if (!jobChainsLayer.isErrorNode(history.getJobChain(), history.getNode(), dbItemInventoryInstance.getId())) {
                return HistoryStateText.SUCCESSFUL;
            } else {
                return HistoryStateText.FAILED;
            }
        } else if (dbItemReportTrigger.getResultError()) {
            return HistoryStateText.FAILED;
        } else {
            return HistoryStateText.SUCCESSFUL;
        }
    }
}
