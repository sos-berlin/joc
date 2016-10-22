package com.sos.joc.tasks.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;
import com.sos.joc.tasks.resource.ITasksResourceKill;
import com.sos.scheduler.model.commands.JSCmdKillTask;

@Path("tasks")
public class TasksResourceKillImpl extends JOCResourceImpl implements ITasksResourceKill {
    private static final String KILL = "kill";
    private static final String TERMINATE_WITHIN = "terminate_within";
    private static final String TERMINATE = "terminate";
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceKillImpl.class);
    private ModifyTasks modifyTasksSchema = null;

    private JOCDefaultResponse executeKillCommand(TasksFilter job, TaskId taskId, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdKillTask jsCmdKillTask = Globals.schedulerObjectFactory.createKillTask();
            jsCmdKillTask.setImmediately(WebserviceConstants.YES);
            jsCmdKillTask.setIdIfNotEmpty(taskId.getTaskId());
            jsCmdKillTask.setJobIfNotEmpty(job.getJob());

            if (TERMINATE.equals(command)) {
                jsCmdKillTask.setTimeoutIfNotEmpty(WebserviceConstants.NEVER);
            }

            if (TERMINATE_WITHIN.equals(command)) {
                jsCmdKillTask.setTimeout(modifyTasksSchema.getTimeout());
            }

            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdKillTask);
            jocXmlCommand.executePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing task.%s %s:%s",command, e.getCause(), e.getMessage()));
        }
    }

    private JOCDefaultResponse postTasksCommand(String accessToken, String command, boolean permission, ModifyTasks modifyTasksSchema) {
        LOGGER.debug("init tasks/teminate_within");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        this.modifyTasksSchema = modifyTasksSchema;
        try {
            jocDefaultResponse = init(accessToken, modifyTasksSchema.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (TasksFilter job : modifyTasksSchema.getJobs()) {
                for (TaskId task : job.getTaskIds()) {
                    jocDefaultResponse = executeKillCommand(job, task, command);
                }
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postTasksTerminate(String accessToken, ModifyTasks modifyTasksSchema) {
        LOGGER.debug("init tasks/terminate");
        try {
            return postTasksCommand(accessToken, TERMINATE, getPermissons(accessToken).getJob().isTerminate(), modifyTasksSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postTasksTerminateWithin(String accessToken, ModifyTasks modifyTasksSchema) {
        LOGGER.debug("init tasks/teminate_within");
        try {
            return postTasksCommand(accessToken, TERMINATE_WITHIN, getPermissons(accessToken).getJob().isTerminate(), modifyTasksSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postTasksKill(String accessToken, ModifyTasks modifyTasksSchema) {
        LOGGER.debug("init tasks/kill");
        try {
            return postTasksCommand(accessToken, KILL, getPermissons(accessToken).getJob().isKill(), modifyTasksSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
