package com.sos.joc.tasks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
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
import com.sos.joc.model.job.OrderPath;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.joc.tasks.resource.ITasksResourceHistory;

@Path("tasks")
public class TasksResourceHistoryImpl extends JOCResourceImpl implements ITasksResourceHistory {

	private static final String API_CALL = "./tasks/history";

	@Override
	public JOCDefaultResponse postTasksHistory(String xAccessToken, String accessToken, JobsFilter jobsFilter)
			throws Exception {
		return postTasksHistory(getAccessToken(xAccessToken, accessToken), jobsFilter);
	}

	public JOCDefaultResponse postTasksHistory(String accessToken, JobsFilter jobsFilter) throws Exception {
		SOSHibernateSession connection = null;

		try {
			if (jobsFilter.getJobschedulerId() == null) {
				jobsFilter.setJobschedulerId("");
			}
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken,
					jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(jobsFilter.getJobschedulerId(), accessToken)
							.getHistory().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Globals.beginTransaction(connection);

			List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();
			boolean withFolderFilter = jobsFilter.getFolders() != null && !jobsFilter.getFolders().isEmpty();
			boolean hasPermission = true;
			boolean getTaskFromHistoryIdAndNode = false;
			boolean getTaskFromOrderHistory = false;
			List<Folder> folders = addPermittedFolder(jobsFilter.getFolders());

			ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(connection);
			reportTaskExecutionsDBLayer.getFilter().setSchedulerId(jobsFilter.getJobschedulerId());
			if (jobsFilter.getTaskIds() != null && !jobsFilter.getTaskIds().isEmpty()) {
				reportTaskExecutionsDBLayer.getFilter().setTaskIds(jobsFilter.getTaskIds());
			} else {
				if (jobsFilter.getHistoryIds() != null && !jobsFilter.getHistoryIds().isEmpty()) {
					getTaskFromHistoryIdAndNode = true;
				} else if (jobsFilter.getOrders() != null && !jobsFilter.getOrders().isEmpty()) {
					getTaskFromOrderHistory = true;
				} else {
					if (jobsFilter.getDateFrom() != null) {
						reportTaskExecutionsDBLayer.getFilter().setExecutedFrom(
								JobSchedulerDate.getDateFrom(jobsFilter.getDateFrom(), jobsFilter.getTimeZone()));
					}
					if (jobsFilter.getDateTo() != null) {
						reportTaskExecutionsDBLayer.getFilter().setExecutedTo(
								JobSchedulerDate.getDateTo(jobsFilter.getDateTo(), jobsFilter.getTimeZone()));
					}

					if (SearchStringHelper.isDBWildcardSearch(jobsFilter.getRegex())) {
						String[] jobs = jobsFilter.getRegex().split(",");
						for (String j : jobs) {
							reportTaskExecutionsDBLayer.getFilter().addJobPath(j);
						}
						jobsFilter.setRegex("");
					}

					if (jobsFilter.getHistoryStates().size() > 0) {
						for (HistoryStateText historyStateText : jobsFilter.getHistoryStates()) {
							reportTaskExecutionsDBLayer.getFilter().addState(historyStateText.toString());
						}
					}

					if (jobsFilter.getJobs().size() > 0) {
						Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
						for (JobPath jobPath : jobsFilter.getJobs()) {
							if (jobPath != null && canAdd(jobPath.getJob(), permittedFolders)) {
								reportTaskExecutionsDBLayer.getFilter().addJobPath(jobPath.getJob());
							}
						}
						jobsFilter.setRegex("");
					} else {
						if (jobsFilter.getExcludeJobs().size() > 0) {
							for (JobPath jobPath : jobsFilter.getExcludeJobs()) {
								reportTaskExecutionsDBLayer.getFilter().addExcludedJob(jobPath.getJob());
							}
						}

						if (withFolderFilter && (folders == null || folders.isEmpty())) {
							hasPermission = false;
						} else if (folders != null && !folders.isEmpty()) {
							for (Folder folder : folders) {
								folder.setFolder(normalizeFolder(folder.getFolder()));
								reportTaskExecutionsDBLayer.getFilter().addFolderPath(folder);
							}
						}
					}
				}
			}

			if (hasPermission) {

				if (jobsFilter.getLimit() == null) {
					jobsFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
				}

				reportTaskExecutionsDBLayer.getFilter().setLimit(jobsFilter.getLimit());
				List<DBItemReportTask> listOfDBItemReportTaskDBItems = new ArrayList<DBItemReportTask>();

				if (getTaskFromHistoryIdAndNode) {
					listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer
							.getSchedulerHistoryListFromHistoryIdAndNode(jobsFilter.getHistoryIds());
				} else if (getTaskFromOrderHistory) {
					for (OrderPath orderPath : jobsFilter.getOrders()) {
						checkRequiredParameter("jobChain", orderPath.getJobChain());
						orderPath.setJobChain(normalizePath(orderPath.getJobChain()));
					}
					listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer
							.getSchedulerHistoryListFromOrder(jobsFilter.getOrders());
					if (listOfDBItemReportTaskDBItems != null) {
					    // JOC-574
					    Set<DBItemReportTask> setOfDBItemReportTaskDBItems = new HashSet<DBItemReportTask>(listOfDBItemReportTaskDBItems);
					    listOfDBItemReportTaskDBItems = new ArrayList<DBItemReportTask>(setOfDBItemReportTaskDBItems);
					}
				} else {
					listOfDBItemReportTaskDBItems = reportTaskExecutionsDBLayer.getSchedulerHistoryListFromTo();
				}

				Matcher regExMatcher = null;
				if (jobsFilter.getRegex() != null && !jobsFilter.getRegex().isEmpty()) {
					regExMatcher = Pattern.compile(jobsFilter.getRegex()).matcher("");
				}

				if (listOfDBItemReportTaskDBItems != null) {
					for (DBItemReportTask dbItemReportTask : listOfDBItemReportTaskDBItems) {
						TaskHistoryItem taskHistoryItem = new TaskHistoryItem();
						if (!getPermissonsJocCockpit(dbItemReportTask.getSchedulerId(), accessToken).getHistory()
								.getView().isStatus()) {
							continue;
						}
						if (jobsFilter.getJobschedulerId().isEmpty()) {
							taskHistoryItem.setJobschedulerId(dbItemReportTask.getSchedulerId());
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

						// taskHistoryItem.setSteps(dbItemReportExecution.getStep());
						taskHistoryItem.setTaskId(dbItemReportTask.getHistoryIdAsString());

						if (regExMatcher != null) {
							regExMatcher.reset(dbItemReportTask.getName());
							if (!regExMatcher.find()) {
								continue;
							}
						}
						listOfHistory.add(taskHistoryItem);
					}
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
