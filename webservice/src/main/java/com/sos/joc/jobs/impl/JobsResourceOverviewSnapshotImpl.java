package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceOverviewSnapshot;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobsSnapshot;
import com.sos.joc.model.job.JobsSummary;

@Path("jobs")
public class JobsResourceOverviewSnapshotImpl extends JOCResourceImpl implements IJobsResourceOverviewSnapshot {

	private static final String API_CALL = "./jobs/overview/snapshot";

	@Override
	public JOCDefaultResponse postJobsOverviewSnapshot(String accessToken, JobSchedulerId jobScheduler)
			throws Exception {
		String folders = "";
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobScheduler, accessToken,
					jobScheduler.getJobschedulerId(),
					getPermissonsJocCockpit(jobScheduler.getJobschedulerId(), accessToken).getJob().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			Set<String> recursiveFolderSet = new HashSet<String>();
			Set<String> nonRecursiveFolderSet = new HashSet<String>();
			FilterFolder folder = new FilterFolder();
			if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
				for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions()
						.size(); i++) {
					folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
					if (folder.isRecursive()) {
						recursiveFolderSet.add(folder.getFolder());
					} else {
						nonRecursiveFolderSet.add(folder.getFolder());
					}
					folders = folders + folder.getFolder() + ",";
				}
			}

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
			String command = jocXmlCommand.getShowStateCommand("job", "", "", 0, 0);
			jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
			Integer countPending = 0;
			Integer countRunning = 0;
			Integer countStopped = 0;
			Integer countWaitingForResource = 0;
			Integer countRunningTasks = 0;
			Integer countQueuedTasks = 0;
			Integer curRunningTasks = 0;
			Integer curQueuedTasks = 0;
			NodeList jobStatistics = jocXmlCommand.getSosxml()
					.selectNodeList("//job[not(@path='/scheduler_file_order_sink') and @enabled='yes']");
			for (int i = 0; i < jobStatistics.getLength(); i++) {
				Element job = (Element) jobStatistics.item(i);
				if (!jobIsInFolder(job.getAttribute("path"), recursiveFolderSet, nonRecursiveFolderSet)) {
					continue;
				}
				curRunningTasks = getNumOfRunningTasks(jocXmlCommand, job);
				curQueuedTasks = getNumOfQueuedTasks(jocXmlCommand, job);
				countRunningTasks = countRunningTasks + curRunningTasks;
				countQueuedTasks = countQueuedTasks + curQueuedTasks;
				JobStateText jobStateText = setState(jocXmlCommand, job, curRunningTasks, curQueuedTasks);
				switch (jobStateText) {
				case PENDING:
					countPending++;
					break;
				case STOPPED:
					countStopped++;
					break;
				case STOPPING:
				case RUNNING:
					countRunning++;
					break;
				case ERROR:
				case NOT_IN_PERIOD:
				case NOT_INITIALIZED:
				case WAITING_FOR_AGENT:
				case WAITING_FOR_LOCK:
				case WAITING_FOR_PROCESS:
				case WAITING_FOR_TASK:
					countWaitingForResource++;
					break;
				case UNKNOWN:
				case LOADED:
				case INITIALIZED:
				case DISABLED:
					break;
				}
			}
			JobsSummary jobs = new JobsSummary();
			jobs.setPending(countPending);
			jobs.setRunning(countRunning);
			jobs.setStopped(countStopped);
			jobs.setWaitingForResource(countWaitingForResource);
			jobs.setTasks(countRunningTasks);

			JobsSnapshot entity = new JobsSnapshot();
			entity.setSurveyDate(jocXmlCommand.getSurveyDate());
			entity.setJobs(jobs);
			entity.setDeliveryDate(Date.from(Instant.now()));
			return JOCDefaultResponse.responseStatus200(entity);

		} catch (JobSchedulerConnectionResetException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatus434JSError(e);
		} catch (JobSchedulerObjectNotExistException e) {
			JocError err = new JocError();
			err.setMessage(String.format("%s: Please check your folders in the Account Management (%s)", e.getMessage(),
					folders));
			JocException ee = new JocException(err);
			return JOCDefaultResponse.responseStatusJSError(ee);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}

	}

	private JobStateText setState(JOCXmlCommand jocXmlCommand, Element job, Integer runningTasks, Integer queuedTasks)
			throws Exception {
		JobStateText jobStateText = JobStateText.UNKNOWN;
		ConfigurationState confState = ConfigurationStatus.getConfigurationStatus(job);
		if (!jocXmlCommand.getBoolValue(job.getAttribute("enabled"), true)) {
			jobStateText = JobStateText.DISABLED;
		} else if (confState != null && confState.getSeverity() == 2) {
			jobStateText = JobStateText.ERROR;
		} else {
			try {
				jobStateText = JobStateText.fromValue(job.getAttribute(WebserviceConstants.STATE).toUpperCase());
			} catch (Exception e) {
				jobStateText = JobStateText.fromValue("UNKNOWN");
			}
			jocXmlCommand.getSosxml().selectNodeList(job, "tasks/task[@waiting_for_remote_scheduler='true']")
					.getLength();
			if (jocXmlCommand.getSosxml().selectNodeList(job, "tasks/task[@waiting_for_remote_scheduler='true']")
					.getLength() > 0) {
				jobStateText = JobStateText.WAITING_FOR_AGENT;
			} else if (jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_PROCESS), false)) {
				jobStateText = JobStateText.WAITING_FOR_PROCESS;
			} else if (jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use[@is_available='no']")
					.getLength() > 0) {
				jobStateText = JobStateText.WAITING_FOR_LOCK;
				// } else if (runningTasks == Integer.parseInt(getAttributeValue(job, "tasks",
				// "1")) && queuedTasks > 0) {
				// TODO: WaitingForTask has to be improved
				// Look into queue items where start_time in the past
				// it could be that a task is queued caused of a delayed
				// start instead of max tasks is reached
				// jobStateText = JobStateText.WAITING_FOR_TASK;
			} else if (isOrderJob(job) && jobStateText == JobStateText.PENDING
					&& !jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.IN_PERIOD), true)) {
				jobStateText = JobStateText.NOT_IN_PERIOD;
			}
		}
		return jobStateText;
	}

	private String getAttributeValue(Element job, String attributeName, String default_) {
		String val = job.getAttribute(attributeName);
		if (val == null || val.isEmpty()) {
			val = default_;
		}
		return val;
	}

	private Integer getNumOfRunningTasks(JOCXmlCommand jocXmlCommand, Element job) {
		try {
			return Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "tasks/@count", "0"));
		} catch (Exception e) {
			return 0;
		}
	}

	private Integer getNumOfQueuedTasks(JOCXmlCommand jocXmlCommand, Element job) {
		try {
			return Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "queued_tasks/@length", "0"));
		} catch (Exception e) {
			return 0;
		}
	}

	private Boolean isOrderJob(Element job) {
		return "yes".equals(getAttributeValue(job, "order", "no"));
	}

	private boolean jobIsInFolder(String path, Set<String> recursiveFolderSet, Set<String> nonRecursiveFolderSet) {
		if (path == null || path.isEmpty()) {
			return false;
		}
		if (nonRecursiveFolderSet.isEmpty() || nonRecursiveFolderSet.contains(path)) {
			return true;
		}
		if (recursiveFolderSet.isEmpty() || recursiveFolderSet.contains(path)) {
			return true;
		}
		for (String s : recursiveFolderSet) {
			path.startsWith(s);
			return true;
		}
		return false;
	}
}
