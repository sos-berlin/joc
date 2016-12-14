package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceHistory;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;
import com.sos.joc.model.job.TaskHistoryItem;

@Path("job")
public class JobResourceHistoryImpl extends JOCResourceImpl implements IJobResourceHistory {

    private static final Integer DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String API_CALL = "./job/configuration";

    @Override
    public JOCDefaultResponse postJobHistory(String accessToken, TaskHistoryFilter taskHistoryFilter) throws Exception {

        try {
            initLogging(API_CALL, taskHistoryFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, taskHistoryFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJob()
                    .getView().isHistory());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("job", taskHistoryFilter.getJob());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (taskHistoryFilter.getMaxLastHistoryItems() == null) {
                taskHistoryFilter.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }
            String postCommand = jocXmlCommand.getShowJobCommand(normalizePath(taskHistoryFilter.getJob()), "task_history", null, taskHistoryFilter
                    .getMaxLastHistoryItems());
            jocXmlCommand.executePostWithThrowBadRequest(postCommand, accessToken);

            jocXmlCommand.createNodeList("/spooler/answer/job/history/history.entry");

            int count = jocXmlCommand.getNodeList().getLength();

            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();

            for (int i = 0; i < count; i++) {
                Element historyEntry = jocXmlCommand.getElementFromList(i);
                TaskHistoryItem history = new TaskHistoryItem();
                // TODO JobScheduler muss Agent in der Antwort liefern
                // history.setAgent("myAgent");
                history.setClusterMember(jocXmlCommand.getAttribute("cluster_member_id"));
                history.setEndTime(jocXmlCommand.getAttributeAsDate("end_time"));
                Element errElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(historyEntry, "ERROR");
                if (errElem != null) {
                    Err error = new Err();
                    error.setCode(errElem.getAttribute("code"));
                    error.setMessage(errElem.getAttribute("text"));
                    history.setError(error);
                }
                history.setExitCode(jocXmlCommand.getAttributeAsInteger("exit_code"));
                history.setJob(jocXmlCommand.getAttribute("job_name"));
                history.setStartTime(jocXmlCommand.getAttributeAsDate("start_time"));

                HistoryState state = new HistoryState();
                if (history.getEndTime() == null) {
                    state.setSeverity(1);
                    state.set_text(HistoryStateText.INCOMPLETE);
                } else if (history.getExitCode() != null && history.getExitCode() == 0) {
                    state.setSeverity(0);
                    state.set_text(HistoryStateText.SUCCESSFUL);
                } else {
                    state.setSeverity(2);
                    state.set_text(HistoryStateText.FAILED);
                }
                history.setState(state);
                history.setSteps(jocXmlCommand.getAttributeAsInteger("steps"));
                history.setTaskId(jocXmlCommand.getAttribute("task"));
                listOfHistory.add(history);
            }
            TaskHistory entity = new TaskHistory();
            entity.setHistory(listOfHistory);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());

        }
    }
}
