package com.sos.joc.jobchain.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceHistory;
import com.sos.joc.model.jobChain.JobChainHistoryFilterSchema;
import com.sos.joc.model.order.History;
import com.sos.joc.model.order.HistorySchema;
import com.sos.joc.model.order.State;
import com.sos.scheduler.model.commands.JSCmdShowJobChain;

@Path("job_chain")
public class JobChainResourceHistoryImpl extends JOCResourceImpl implements IJobChainResourceHistory {
    private static final int DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String ORDER_HISTORY = "order_history";
    private static final String KEY_FOR_ERROR_NODE_LIST = "nodes";
    private static final String XPATH_FOR_ERROR_NODES = "//job_chain_node[@error_state='%s']";
    private static final String XPATH_FOR_ORDER_HISTORY = "//spooler/answer/job_chain/order_history/order";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceHistoryImpl.class);

    @Override
    public JOCDefaultResponse postJobChainHistory(String accessToken, JobChainHistoryFilterSchema jobChainHistoryFilterSchema) throws Exception {

        LOGGER.debug("init job_chain/history");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainHistoryFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isHistory());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            HistorySchema entity = new HistorySchema();

            entity.setDeliveryDate(new Date());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            if (jobChainHistoryFilterSchema.getMaxLastHistoryItems() == null) {
                jobChainHistoryFilterSchema.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }
            String postCommand = createJobchainHistoryPostCommand(jobChainHistoryFilterSchema);
            jocXmlCommand.excutePost(postCommand);

            jocXmlCommand.createNodeList(XPATH_FOR_ORDER_HISTORY);

            int count = jocXmlCommand.getNodeList().getLength();

            List<History> listOfHistory = new ArrayList<History>();

            for (int i = 0; i < count; i++) {
                State state = new State();
                jocXmlCommand.getElementFromList(i);
                String node = jocXmlCommand.getAttribute("state");
                History history = new History();
                if (jocXmlCommand.getAttributeAsDate("end_time") != null) {
                    history.setEndTime(jocXmlCommand.getAttributeAsDate("end_time"));
                    jocXmlCommand.createNodeList(KEY_FOR_ERROR_NODE_LIST, String.format(XPATH_FOR_ERROR_NODES, node));
                    if (jocXmlCommand.getNodeList(KEY_FOR_ERROR_NODE_LIST).getLength() == 0) {
                        state.setSeverity(0);
                        state.setText(State.Text.SUCCESSFUL);
                    } else {
                        state.setSeverity(2);
                        state.setText(State.Text.FAILED);
                    }
                    history.setState(state);
                } else {
                    state.setSeverity(1);
                    state.setText(State.Text.INCOMPLETE);
                    history.setState(state);

                }
                history.setHistoryId(jocXmlCommand.getAttributeAsInteger("history_id"));
                history.setJobChain(normalizePath(jocXmlCommand.getAttribute("job_chain")));
                history.setNode(node);
                history.setOrderId(jocXmlCommand.getAttribute("id"));
                history.setPath(jocXmlCommand.getAttribute("path"));
                history.setStartTime(jocXmlCommand.getAttributeAsDate("start_time"));

                listOfHistory.add(history);
            }

            entity.setHistory(listOfHistory);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

    public String createJobchainHistoryPostCommand(final JobChainHistoryFilterSchema jobChainHistoryFilterSchema) {

        JSCmdShowJobChain showJob = Globals.schedulerObjectFactory.createShowJobChain();
        showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxOrderHistory(BigInteger.valueOf(jobChainHistoryFilterSchema.getMaxLastHistoryItems()));
        showJob.setWhat(ORDER_HISTORY);
        showJob.setJobChain(normalizePath(jobChainHistoryFilterSchema.getJobChain()));
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }

}
