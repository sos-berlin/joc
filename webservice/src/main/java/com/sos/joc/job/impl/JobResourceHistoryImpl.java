package com.sos.joc.job.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.job.resource.IJobResourceHistory;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.schema.JsonValidator;

@Path("job")
public class JobResourceHistoryImpl extends JOCResourceImpl implements IJobResourceHistory {

    private static final Integer DEFAULT_MAX_HISTORY_ITEMS = 25;
    private static final String API_CALL = "./job/history";

    @Override
    public JOCDefaultResponse postJobHistory(String accessToken, byte[] taskHistoryFilterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(taskHistoryFilterBytes, TaskHistoryFilter.class);
            TaskHistoryFilter taskHistoryFilter = Globals.objectMapper.readValue(taskHistoryFilterBytes, TaskHistoryFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, taskHistoryFilter, accessToken, taskHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(taskHistoryFilter.getJobschedulerId(), accessToken).getJob().getView().isHistory());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("job", taskHistoryFilter.getJob());
            checkFolderPermissions(taskHistoryFilter.getJob());
            
            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();
            if (taskHistoryFilter.getMaxLastHistoryItems() == null) {
                taskHistoryFilter.setMaxLastHistoryItems(DEFAULT_MAX_HISTORY_ITEMS);
            }
            boolean getJobHistoryFromXMLCommand = Globals.rollbackJobHistoryWithJSON;
            if (!getJobHistoryFromXMLCommand) {
                getJobHistoryFromXMLCommand = versionIsOlderThan("1.12.8");
            }
            try {
                if (getJobHistoryFromXMLCommand) {
                    JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
                    String postCommand = jocXmlCommand.getShowJobCommand(normalizePath(taskHistoryFilter.getJob()), "task_history", null,
                            taskHistoryFilter.getMaxLastHistoryItems());
                    jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);

                    jocXmlCommand.createNodeList("/spooler/answer/job/history/history.entry");

                    int count = jocXmlCommand.getNodeList().getLength();

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
                } else {
                    JOCJsonCommand command = new JOCJsonCommand(this);
                    command.setUriBuilderForJobs();
                    command.addJobHistoryQuery(taskHistoryFilter.getMaxLastHistoryItems());
                    JsonArray json = command.getJsonObjectFromPostWithRetry(getServiceBody(taskHistoryFilter.getJob()), accessToken);
                    json.stream().forEach(i -> listOfHistory.add(getHistoryItem((JsonObject) i)));
                }
            } catch (JobSchedulerConnectionRefusedException e) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(connection);
                reportTaskExecutionsDBLayer.getFilter().setSchedulerId(taskHistoryFilter.getJobschedulerId());
                reportTaskExecutionsDBLayer.getFilter().addJobPath(taskHistoryFilter.getJob());
                reportTaskExecutionsDBLayer.getFilter().setLimit(taskHistoryFilter.getMaxLastHistoryItems());
                List<DBItemReportTask> listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer.getSchedulerHistoryListFromTo();
                if (listOfDBItemReportTaskDBItems != null) {
                    for (DBItemReportTask dbItemReportTask : listOfDBItemReportTaskDBItems) {
                        TaskHistoryItem history = new TaskHistoryItem();
                        history.setAgent(dbItemReportTask.getAgentUrl());
                        history.setClusterMember(dbItemReportTask.getClusterMemberId());
                        history.setEndTime(dbItemReportTask.getEndTime());
                        if (dbItemReportTask.getError()) {
                            Err error = new Err();
                            error.setCode(dbItemReportTask.getErrorCode());
                            error.setMessage(dbItemReportTask.getErrorText());
                            history.setError(error);
                        }
                        history.setExitCode(dbItemReportTask.getExitCode());
                        history.setJob(dbItemReportTask.getName());
                        history.setStartTime(dbItemReportTask.getStartTime());
                        history.setSteps(1);
                        history.setTaskId(dbItemReportTask.getHistoryIdAsString());
                        HistoryState state = new HistoryState();
                        if (dbItemReportTask.isSuccessFull()) {
                            state.setSeverity(0);
                            state.set_text(HistoryStateText.SUCCESSFUL);
                        }
                        if (dbItemReportTask.isInComplete()) {
                            state.setSeverity(1);
                            state.set_text(HistoryStateText.INCOMPLETE);
                        }
                        if (dbItemReportTask.isFailed()) {
                            state.setSeverity(2);
                            state.set_text(HistoryStateText.FAILED);
                        }
                        history.setState(state);
                        history.setSurveyDate(dbItemReportTask.getCreated());
                        listOfHistory.add(history);
                    }
                }
            }
            
            TaskHistory entity = new TaskHistory();
            entity.setHistory(listOfHistory);
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
    
    private Err getErr(JsonObject errorElem) {
        if (errorElem != null) {
            Err err = new Err();
            err.setCode(errorElem.getString("code", null));
            err.setMessage(errorElem.getString("message", null));
            return err;
        }
        return null;
    }
    
    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", job);

        return builder.build().toString();
    }
    
    private TaskHistoryItem getHistoryItem(JsonObject item) {
        TaskHistoryItem history = new TaskHistoryItem();
        history.setAgent(item.getString("agentUri", null));
        history.setClusterMember(item.getString("clusterMemberId", null));
        history.setEndTime(JobSchedulerDate.getDateFromISO8601String(item.getString("endedAt", null)));
        history.setError(getErr(item.getJsonObject("error")));
        history.setExitCode(item.getInt("returnCode", Integer.MIN_VALUE));
        if (history.getExitCode() == Integer.MIN_VALUE) {
            history.setExitCode(null);
        }
        history.setJob(item.getString("jobPath", null));
        history.setStartTime(JobSchedulerDate.getDateFromISO8601String(item.getString("startedAt", null)));
        history.setSteps(item.getInt("stepCount", 1));
        history.setTaskId(item.getString("taskId", null));
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
        return history;
    }
}
