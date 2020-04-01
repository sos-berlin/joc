package com.sos.joc.jobstreams.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterJobStreamHistory;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.jitl.reporting.db.filter.ReportExecutionFilter;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IJobStreamHistoryResource;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamHistoryImpl extends JOCResourceImpl implements IJobStreamHistoryResource {

    private static final String API_CALL = "./jobstream/history";

    @Override
    public JOCDefaultResponse postJobStreamHistory(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter jobStreamFilter = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            Set<String> listOfJobs = new HashSet<String>();
            Set<Long> listOfTaskIds = new HashSet<Long>();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobStreamFilter, accessToken, jobStreamFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamFilter.getJobschedulerId(), accessToken).getHistory().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.checkRequiredParameter("jobschedulerid", jobStreamFilter.getJobschedulerId());
            this.checkRequiredParameter("jobStream", jobStreamFilter.getJobStream());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(sosHibernateSession);

            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions = new FilterInConditions();
            filterInConditions.setJobSchedulerId(jobStreamFilter.getJobschedulerId());
            filterInConditions.setJobStream(jobStreamFilter.getJobStream());
            List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);
            for (DBItemInCondition dbItemIncondition : listOfInConditions) {
                listOfJobs.add(dbItemIncondition.getJob());
            }

            if (jobStreamFilter.getSession() != null && !jobStreamFilter.getSession().isEmpty()) {
                DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);
                FilterJobStreamHistory filterJobStreamHistory = new FilterJobStreamHistory();
                filterJobStreamHistory.setContextId(jobStreamFilter.getSession());
                List<DBItemJobStreamHistory> listOfTasks = dbLayerJobStreamHistory.getJobStreamHistoryList(filterJobStreamHistory, 0);
                for (DBItemJobStreamHistory dbItemJobStreamHistory : listOfTasks) {
                    listOfTaskIds.add(dbItemJobStreamHistory.getId());
                }
            }

            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            FilterOutConditions filterOutConditions = new FilterOutConditions();
            filterOutConditions.setJobSchedulerId(jobStreamFilter.getJobschedulerId());
            filterOutConditions.setJobStream(jobStreamFilter.getJobStream());
            List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
            for (DBItemOutCondition dbItemOutcondition : listOfOutConditions) {
                listOfJobs.add(dbItemOutcondition.getJob());
            }

            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();

            ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(sosHibernateSession);
            ReportExecutionFilter filter = new ReportExecutionFilter();
            filter.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            filter.setOrderCriteria("startTime");
            filter.setSortMode("desc");
            filter.setSchedulerId(jobStreamFilter.getJobschedulerId());

            if (listOfJobs.size() > 0) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                for (String jobPath : listOfJobs) {
                    if (jobPath != null && canAdd(jobPath, permittedFolders)) {
                        filter.addJobPath(jobPath);
                    }
                }
            }

            if (listOfTaskIds.size() > 0) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                if (jobStreamFilter.getFolder() == null || canAdd(jobStreamFilter.getFolder(), permittedFolders)) {
                    filter.setTaskIds(listOfTaskIds);
                }
            }

            if (jobStreamFilter.getLimit() == null) {
                jobStreamFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
            }

            filter.setLimit(jobStreamFilter.getLimit());
            List<DBItemReportTask> listOfDBItemReportTaskDBItems = new ArrayList<DBItemReportTask>();

            listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer.getSchedulerHistoryListFromTo(filter);

            if (listOfDBItemReportTaskDBItems != null) {
                for (DBItemReportTask dbItemReportTask : listOfDBItemReportTaskDBItems) {
                    TaskHistoryItem taskHistoryItem = new TaskHistoryItem();
                    if (!getPermissonsJocCockpit(dbItemReportTask.getSchedulerId(), accessToken).getHistory().getView().isStatus()) {
                        continue;
                    }

                    taskHistoryItem.setJobschedulerId(dbItemReportTask.getSchedulerId());
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

                    taskHistoryItem.setTaskId(dbItemReportTask.getHistoryIdAsString());

                    listOfHistory.add(taskHistoryItem);
                }
            }

            TaskHistory entity = new TaskHistory();
            entity.setDeliveryDate(new Date());
            entity.setHistory(listOfHistory);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (

        JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}
