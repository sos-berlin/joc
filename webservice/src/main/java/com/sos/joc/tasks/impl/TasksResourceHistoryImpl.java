package com.sos.joc.tasks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemReportExecution;
import com.sos.jitl.reporting.db.ReportExecutionsDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.joc.tasks.resource.ITasksResourceHistory;

@Path("tasks")
public class TasksResourceHistoryImpl extends JOCResourceImpl implements ITasksResourceHistory {

    private static final String API_CALL = "./tasks/history";

    @Override
    public JOCDefaultResponse postTasksHistory(String accessToken, JobsFilter jobsFilter) throws Exception {
        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection();
            
            initLogging(API_CALL, jobsFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getHistory().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Globals.beginTransaction(connection);

            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();

            ReportExecutionsDBLayer reportExecutionsDBLayer = new ReportExecutionsDBLayer(connection);
            reportExecutionsDBLayer.getFilter().setSchedulerId(jobsFilter.getJobschedulerId());
            if (jobsFilter.getDateFrom() != null) {
                reportExecutionsDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDate(jobsFilter.getDateFrom(), jobsFilter.getTimeZone()));
            }
            if (jobsFilter.getDateTo() != null) {
                reportExecutionsDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDate(jobsFilter.getDateTo(), jobsFilter.getTimeZone()));
            }

            if (jobsFilter.getJobs().size() > 0) {
                for (JobPath jobPath : jobsFilter.getJobs()) {
                    reportExecutionsDBLayer.getFilter().addJobPath(jobPath.getJob());
                }
                jobsFilter.setRegex("");
            } else {
                if (jobsFilter.getHistoryStates().size() > 0) {
                    for (HistoryStateText historyStateText : jobsFilter.getHistoryStates()) {
                        reportExecutionsDBLayer.getFilter().addState(historyStateText.toString());
                    }
                }

                if (jobsFilter.getFolders().size() > 0) {
                    for (Folder folder : jobsFilter.getFolders()) {
                        reportExecutionsDBLayer.getFilter().addFolderPath(folder.getFolder(),folder.getRecursive());
                    }
                }
            }

            if (jobsFilter.getLimit() == null) {
                jobsFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
            }
            reportExecutionsDBLayer.getFilter().setLimit(jobsFilter.getLimit());

            List<DBItemReportExecution> listOfDBItemReportExecutionDBItems = reportExecutionsDBLayer.getSchedulerHistoryListFromTo();

            for (DBItemReportExecution dbItemReportExecution : listOfDBItemReportExecutionDBItems) {
                boolean add = true;
                TaskHistoryItem taskHistoryItem = new TaskHistoryItem();
                taskHistoryItem.setAgent(dbItemReportExecution.getAgentUrl());
                taskHistoryItem.setClusterMember(dbItemReportExecution.getClusterMemberId());
                taskHistoryItem.setEndTime(dbItemReportExecution.getEndTime());
                if (dbItemReportExecution.getError()) {
                    Err error = new Err();
                    error.setCode(dbItemReportExecution.getErrorCode());
                    error.setMessage(dbItemReportExecution.getErrorText());
                    taskHistoryItem.setError(error);
                }

                taskHistoryItem.setExitCode(dbItemReportExecution.getExitCode());
                taskHistoryItem.setJob(dbItemReportExecution.getName());
                taskHistoryItem.setStartTime(dbItemReportExecution.getStartTime());

                HistoryState state = new HistoryState();
                if (dbItemReportExecution.isSuccessFull()) {
                    state.setSeverity(0);
                    state.set_text(HistoryStateText.SUCCESSFUL);
                }
                if (dbItemReportExecution.isInComplete()) {
                    state.setSeverity(1);
                    state.set_text(HistoryStateText.INCOMPLETE);
                }
                if (dbItemReportExecution.isFailed()) {
                    state.setSeverity(2);
                    state.set_text(HistoryStateText.FAILED);
                }
                taskHistoryItem.setState(state);
                taskHistoryItem.setSurveyDate(dbItemReportExecution.getCreated());

                // taskHistoryItem.setSteps(dbItemReportExecution.getStep());
                taskHistoryItem.setTaskId(dbItemReportExecution.getHistoryIdAsString());

                if (jobsFilter.getRegex() != null && !jobsFilter.getRegex().isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(jobsFilter.getRegex()).matcher(dbItemReportExecution.getName());
                    add = regExMatcher.find();
                }

                if (add) {
                    listOfHistory.add(taskHistoryItem);
                }

            }

            TaskHistory entity = new TaskHistory();
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
}
