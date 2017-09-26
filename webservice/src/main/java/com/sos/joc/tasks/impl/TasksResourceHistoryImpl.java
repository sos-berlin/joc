package com.sos.joc.tasks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.jitl.reporting.db.filter.FilterFolder;
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
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.joc.tasks.resource.ITasksResourceHistory;

@Path("tasks")
public class TasksResourceHistoryImpl extends JOCResourceImpl implements ITasksResourceHistory {

    private static final String API_CALL = "./tasks/history";

    @Override
    public JOCDefaultResponse postTasksHistory(String xAccessToken, String accessToken, JobsFilter jobsFilter) throws Exception {
        return postTasksHistory(getAccessToken(xAccessToken, accessToken), jobsFilter);
    }

    public JOCDefaultResponse postTasksHistory(String accessToken, JobsFilter jobsFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getHistory().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);

            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();

            ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(connection);
            reportTaskExecutionsDBLayer.getFilter().setSchedulerId(jobsFilter.getJobschedulerId());
            if (jobsFilter.getDateFrom() != null) {
                reportTaskExecutionsDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDateFrom(jobsFilter.getDateFrom(), jobsFilter
                        .getTimeZone()));
            }
            if (jobsFilter.getDateTo() != null) {
                reportTaskExecutionsDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDateTo(jobsFilter.getDateTo(), jobsFilter.getTimeZone()));
            }

            if (jobsFilter.getHistoryStates().size() > 0) {
                for (HistoryStateText historyStateText : jobsFilter.getHistoryStates()) {
                    reportTaskExecutionsDBLayer.getFilter().addState(historyStateText.toString());
                }
            }

            if (jobsFilter.getJobs().size() > 0) {
                for (JobPath jobPath : jobsFilter.getJobs()) {
                    reportTaskExecutionsDBLayer.getFilter().addJobPath(jobPath.getJob());
                }
                jobsFilter.setRegex("");
            } else {
                if (jobsFilter.getExcludeJobs().size() > 0) {
                    for (JobPath jobPath : jobsFilter.getExcludeJobs()) {
                        reportTaskExecutionsDBLayer.getFilter().addExcludedJob(jobPath.getJob());
                    }
                }

                if (jobsFilter.getFolders().size() > 0) {
                    for (Folder folder : jobsFilter.getFolders()) {
                        reportTaskExecutionsDBLayer.getFilter().addFolderPath(folder.getFolder(), folder.getRecursive());
                    }
                }
            }

            if (jobsFilter.getLimit() == null) {
                jobsFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
            }

            reportTaskExecutionsDBLayer.getFilter().setLimit(jobsFilter.getLimit());

            if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
                    reportTaskExecutionsDBLayer.getFilter().addFolderPath(normalizeFolder(folder.getFolder()), folder.isRecursive());
                }
            }

            List<DBItemReportTask> listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer.getSchedulerHistoryListFromTo();

            Matcher regExMatcher = null;
            if (jobsFilter.getRegex() != null && !jobsFilter.getRegex().isEmpty()) {
                regExMatcher = Pattern.compile(jobsFilter.getRegex()).matcher("");
            }

            for (DBItemReportTask dbItemReportTask : listOfDBItemReportTaskDBItems) {
                boolean add = true;
                TaskHistoryItem taskHistoryItem = new TaskHistoryItem();
                if (jobsFilter.getJobschedulerId().isEmpty()) {
                    taskHistoryItem.setJobschedulerId(dbItemReportTask.getSchedulerId());
                }
                taskHistoryItem.setAgent(dbItemReportTask.getAgentUrl());
                taskHistoryItem.setClusterMember(dbItemReportTask.getClusterMemberId());
                taskHistoryItem.setEndTime(dbItemReportTask.getEndTime());
                if (dbItemReportTask.getError()) {
                    Err error = new Err();
                    error.setCode(dbItemReportTask.getErrorCode());
                    error.setMessage(dbItemReportTask.getErrorText());
                    taskHistoryItem.setError(error);
                }

                taskHistoryItem.setExitCode(dbItemReportTask.getExitCode());
                taskHistoryItem.setJob(dbItemReportTask.getName());
                taskHistoryItem.setStartTime(dbItemReportTask.getStartTime());

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
                taskHistoryItem.setState(state);
                taskHistoryItem.setSurveyDate(dbItemReportTask.getCreated());

                // taskHistoryItem.setSteps(dbItemReportExecution.getStep());
                taskHistoryItem.setTaskId(dbItemReportTask.getHistoryIdAsString());

                if (regExMatcher != null) {
                    regExMatcher.reset(dbItemReportTask.getName());
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
