package com.sos.joc.tasks.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.job.Job____;
import com.sos.joc.model.job.ModifyTasksSchema;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.tasks.resource.ITasksResourceKill;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdKillTask;
import com.sos.scheduler.model.objects.Spooler;

@Path("tasks")
public class TasksResourceKillImpl extends JOCResourceImpl implements ITasksResourceKill {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceKillImpl.class);
    private ModifyTasksSchema modifyTasksSchema = null;

    private JOCDefaultResponse executeKillCommand(Job____ job, TaskId taskId, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdKillTask jsCmdKillTask = schedulerObjectFactory.createKillTask();
            jsCmdKillTask.setImmediately(WebserviceConstants.YES);
            jsCmdKillTask.setIdIfNotEmpty(taskId.getTaskId());
            jsCmdKillTask.setJobIfNotEmpty(job.getJob());

            if ("terminate".equals(command)) {
                jsCmdKillTask.setTimeoutIfNotEmpty(WebserviceConstants.NEVER);
            }

            if ("terminate_within".equals(command)) {
                jsCmdKillTask.setTimeout(modifyTasksSchema.getTimeout());
            }

            String xml = schedulerObjectFactory.toXMLString(jsCmdKillTask);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postTasksTerminate(String accessToken, ModifyTasksSchema modifyTasksSchema) {
        LOGGER.debug("init Tasks: Terminate");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyTasksSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isKill());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            for (Job____ job : modifyTasksSchema.getJobs()) {
                for (TaskId task : job.getTaskIds()) {
                    jocDefaultResponse = executeKillCommand(job, task, "terminate");
                }
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postTasksTerminateWithin(String accessToken, ModifyTasksSchema modifyTasksSchema) throws Exception {
        LOGGER.debug("init Tasks: teminate_within");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        this.modifyTasksSchema = modifyTasksSchema;
        try {
            jocDefaultResponse = init(modifyTasksSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isKill());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job____ job : modifyTasksSchema.getJobs()) {
                for (TaskId task : job.getTaskIds()) {
                    jocDefaultResponse = executeKillCommand(job, task, "terminate_within");
                }
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postTasksKill(String accessToken, ModifyTasksSchema modifyTasksSchema) {
        LOGGER.debug("init Tasks: kill");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyTasksSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isKill());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job____ job : modifyTasksSchema.getJobs()) {
                for (TaskId task : job.getTaskIds()) {
                    jocDefaultResponse = executeKillCommand(job, task, "kill");
                }
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }
}
