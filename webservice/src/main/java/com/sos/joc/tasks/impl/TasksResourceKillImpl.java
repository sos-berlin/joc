package com.sos.joc.tasks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyTaskAudit;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;
import com.sos.joc.tasks.resource.ITasksResourceKill;
import com.sos.xml.XMLBuilder;

@Path("tasks")
public class TasksResourceKillImpl extends JOCResourceImpl implements ITasksResourceKill {

	private static final String KILL = "kill";
	private static final String TERMINATE_WITHIN = "terminate_within";
	private static final String TERMINATE = "terminate";
	private static final String END = "end";
	private static String API_CALL = "./tasks/";
	private List<Err419> listOfErrors = new ArrayList<Err419>();

	@Override
	public JOCDefaultResponse postTasksTerminate(String xAccessToken, String accessToken, ModifyTasks modifyTasks) {
		return postTasksTerminate(getAccessToken(xAccessToken, accessToken), modifyTasks);
	}

	public JOCDefaultResponse postTasksTerminate(String accessToken, ModifyTasks modifyTasks) {
		try {
			return postTasksCommand(accessToken, TERMINATE,
					getPermissonsJocCockpit(modifyTasks.getJobschedulerId(), accessToken).getJob().getExecute()
							.isTerminate(),
					modifyTasks);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postTasksTerminateWithin(String xAccessToken, String accessToken,
			ModifyTasks modifyTasks) {
		return postTasksTerminateWithin(getAccessToken(xAccessToken, accessToken), modifyTasks);
	}

	public JOCDefaultResponse postTasksTerminateWithin(String accessToken, ModifyTasks modifyTasks) {
		try {
			return postTasksCommand(accessToken, TERMINATE_WITHIN,
					getPermissonsJocCockpit(modifyTasks.getJobschedulerId(), accessToken).getJob().getExecute()
							.isTerminate(),
					modifyTasks);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postTasksKill(String xAccessToken, String accessToken, ModifyTasks modifyTasks) {
		return postTasksKill(getAccessToken(xAccessToken, accessToken), modifyTasks);
	}

	public JOCDefaultResponse postTasksKill(String accessToken, ModifyTasks modifyTasks) {
		try {
			return postTasksCommand(accessToken, KILL,
					getPermissonsJocCockpit(modifyTasks.getJobschedulerId(), accessToken).getJob().getExecute()
							.isKill(),
					modifyTasks);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postTasksEnd(String xAccessToken, String accessToken, ModifyTasks modifyTasks) {
		return postTasksEnd(getAccessToken(xAccessToken, accessToken), modifyTasks);
	}

	public JOCDefaultResponse postTasksEnd(String accessToken, ModifyTasks modifyTasks) {
		try {
			return postTasksCommand(accessToken, END,
					getPermissonsJocCockpit(modifyTasks.getJobschedulerId(), accessToken).getJob().getExecute()
							.isKill(),
					modifyTasks);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	private JOCDefaultResponse postTasksCommand(String accessToken, String command, boolean permission,
			ModifyTasks modifyTasks) throws Exception {
		JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyTasks, accessToken,
				modifyTasks.getJobschedulerId(), permission);
		if (jocDefaultResponse != null) {
			return jocDefaultResponse;
		}
		checkRequiredComment(modifyTasks.getAuditLog());
		Date surveyDate = Date.from(Instant.now());
		for (TasksFilter job : modifyTasks.getJobs()) {
			List<TaskId> taskIds = job.getTaskIds();
			if (taskIds == null || taskIds.isEmpty()) {
				// terminate all tasks of job
				taskIds = getTaskIds(job);
			}
			if (taskIds != null) {
				for (TaskId task : taskIds) {
					surveyDate = executeKillCommand(job, task, command, modifyTasks);
				}
			}
		}
		if (listOfErrors.size() > 0) {
			return JOCDefaultResponse.responseStatus419(listOfErrors);
		}
		return JOCDefaultResponse.responseStatusJSOk(surveyDate);
	}

	private List<TaskId> getTaskIds(TasksFilter job) {
		try {
			checkRequiredParameter("job", job.getJob());
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			String command = jocXmlCommand.getShowJobCommand(normalizePath(job.getJob()), null);
			jocXmlCommand.executePostWithThrowBadRequest(command, getAccessToken());

			List<TaskId> taskIds = new ArrayList<TaskId>();
			NodeList tasks = jocXmlCommand.getSosxml().selectNodeList("//tasks/task/@id");
			for (int i = 0; i < tasks.getLength(); i++) {
				TaskId taskId = new TaskId();
				taskId.setTaskId(tasks.item(i).getNodeValue());
				taskIds.add(taskId);
			}
			return taskIds;
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), job, null));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), job, null));
		}
		return null;
	}

	private Date executeKillCommand(TasksFilter job, TaskId taskId, String command, ModifyTasks modifyTasks) {
		try {

			ModifyTaskAudit taskAudit = new ModifyTaskAudit(job, taskId, modifyTasks);
			logAuditMessage(taskAudit);

			checkRequiredParameter("job", job.getJob());
			checkRequiredParameter("taskId", taskId.getTaskId());
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);

			XMLBuilder xml = new XMLBuilder("kill_task");
			xml.addAttribute("id", taskId.getTaskId()).addAttribute("job", normalizePath(job.getJob()));

			switch (command) {
			case KILL:
				xml.addAttribute("immediately", "yes");
				break;
			case TERMINATE:
				xml.addAttribute("immediately", "yes").addAttribute("timeout", "never");
				break;
			case TERMINATE_WITHIN:
				Integer timeout = modifyTasks.getTimeout();
				if (timeout == null) {
					timeout = 0;
				}
				xml.addAttribute("immediately", "yes").addAttribute("timeout", timeout.toString());
				break;
			}
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
			storeAuditLogEntry(taskAudit);

			return jocXmlCommand.getSurveyDate();
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), job, taskId));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), job, taskId));
		}
		return null;
	}
}
