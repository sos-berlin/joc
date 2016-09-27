package com.sos.joc.job.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceHistory;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.job.History;
import com.sos.joc.model.job.HistorySchema;
import com.sos.joc.model.job.JobHistoryFilterSchema;
import com.sos.joc.model.job.State;
import com.sos.scheduler.model.commands.JSCmdShowJob;

@Path("job")
public class JobResourceHistoryImpl extends JOCResourceImpl implements IJobResourceHistory {
    private static final String TASK_HISTORY = "task_history";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceHistoryImpl.class);
    private static final Integer DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String XPATH_FOR_TASK_HISTORY = "//spooler/answer/job/history/history.entry";

    @Override
    public JOCDefaultResponse postJobHistory(String accessToken, JobHistoryFilterSchema jobHistoryFilterSchema) throws Exception {

        LOGGER.debug("init job/history");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobHistoryFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isHistory());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            HistorySchema entity = new HistorySchema();

            entity.setDeliveryDate(new Date());

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            if (jobHistoryFilterSchema.getMaxLastHistoryItems() == null) {
                jobHistoryFilterSchema.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }
            String postCommand = createJobHistoryPostCommand(jobHistoryFilterSchema);
            jocXmlCommand.excutePost(postCommand);

            jocXmlCommand.createNodeList(XPATH_FOR_TASK_HISTORY);

            int count = jocXmlCommand.getNodeList().getLength();

            List<History> listOfHistory = new ArrayList<History>();

            for (int i = 0; i < count; i++) {
                Element historyEntry = jocXmlCommand.getElementFromList(i);
                History history = new History();
                // TODO JobScheduler muss Agent in der Antwort liefern
                // history.setAgent("myAgent");
                history.setClusterMember(jocXmlCommand.getAttribute("cluster_member_id"));
                Date endTime = jocXmlCommand.getAttributeAsDate("end_time");
                history.setEndTime(endTime);

                NodeList n = historyEntry.getElementsByTagName("ERROR");
                if (n != null && n.getLength() > 0) {
                    ErrorSchema errorSchema = new ErrorSchema();
                    errorSchema.setCode(n.item(0).getAttributes().getNamedItem("code").getNodeValue());
                    errorSchema.setMessage(n.item(0).getAttributes().getNamedItem("text").getNodeValue());
                    history.setError(errorSchema);
                }

                Integer exitCode = jocXmlCommand.getAttributeAsInteger("exit_code");
                history.setExitCode(exitCode);
                history.setJob(jocXmlCommand.getAttribute("job_name"));
                history.setStartTime(jocXmlCommand.getAttributeAsDate("start_time"));

                State state = new State();
                if (endTime == null) {
                    state.setSeverity(1);
                    state.setText(State.Text.INCOMPLETE);
                } else {
                    if ("0".equals(exitCode)) {
                        state.setSeverity(0);
                        state.setText(State.Text.SUCCESSFUL);
                    } else {
                        state.setSeverity(2);
                        state.setText(State.Text.FAILED);
                    }
                }
                history.setState(state);
                history.setSteps(jocXmlCommand.getAttributeAsInteger("steps"));
                history.setTaskId(jocXmlCommand.getAttributeAsInteger("task"));
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

    public String createJobHistoryPostCommand(final JobHistoryFilterSchema jobHistoryFilterSchema) {
        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        showJob.setMaxTaskHistory(BigInteger.valueOf(jobHistoryFilterSchema.getMaxLastHistoryItems()));
        showJob.setWhat(TASK_HISTORY);
        showJob.setJob(normalizePath(jobHistoryFilterSchema.getJob()));
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }

}
