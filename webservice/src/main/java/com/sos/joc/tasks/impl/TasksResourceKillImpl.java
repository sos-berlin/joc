package com.sos.joc.tasks.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.tasks.post.kill.JobKill;
import com.sos.joc.tasks.post.kill.TaskId;
import com.sos.joc.tasks.post.kill.TasksKillBody;
import com.sos.joc.tasks.resource.ITasksResourceKill;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdKillTask;
import com.sos.scheduler.model.objects.Spooler;

@Path("tasks")
public class TasksResourceKillImpl extends JOCResourceImpl implements ITasksResourceKill {
    private static final Logger LOGGER = Logger.getLogger(TasksResourceKillImpl.class);
    private TasksKillBody tasksKillBody=null;
    
    private JOCDefaultResponse executeKillCommand(JobKill job, TaskId taskId, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdKillTask jsCmdKillTask = schedulerObjectFactory.createKillTask();
            jsCmdKillTask.setImmediately(WebserviceConstants.YES);

            if (taskId != null) {
                jsCmdKillTask.setIdIfNotEmpty(taskId.getTaskId());
            }
            
            if (job != null) {
                jsCmdKillTask.setJobIfNotEmpty(job.getJob());
            }
            
            if ("terminate".equals(command)){
                jsCmdKillTask.setTimeoutIfNotEmpty(WebserviceConstants.NEVER);
            }

            if ("terminate_within".equals(command)){
                jsCmdKillTask.setTimeout(tasksKillBody.getTimeout());
            }

            String xml = schedulerObjectFactory.toXMLString(jsCmdKillTask);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postTasksTerminate(String accessToken, TasksKillBody tasksKillBody) {
        LOGGER.debug("init Tasks: Terminate");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(tasksKillBody.getJobschedulerId(), getPermissons(accessToken).getJob().isKill());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (JobKill job : tasksKillBody.getJobs()) {
                jocDefaultResponse = executeKillCommand(job, null, "terminate");
            }
            for (TaskId task : tasksKillBody.getTaskIds()) {
                jocDefaultResponse = executeKillCommand(null, task, "terminate");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postTasksTerminateWithin(String accessToken, TasksKillBody tasksKillBody) throws Exception {
        LOGGER.debug("init Tasks: teminate_within");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        this.tasksKillBody =  tasksKillBody;
        try {
            jocDefaultResponse = init(tasksKillBody.getJobschedulerId(), getPermissons(accessToken).getJob().isKill());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (JobKill job : tasksKillBody.getJobs()) {
                jocDefaultResponse = executeKillCommand(job, null, "terminate_within");
            }
            for (TaskId task : tasksKillBody.getTaskIds()) {
                jocDefaultResponse = executeKillCommand(null, task, "terminate_within");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postTasksKill(String accessToken, TasksKillBody tasksKillBody) {
        LOGGER.debug("init Tasks: kill");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(tasksKillBody.getJobschedulerId(), getPermissons(accessToken).getJob().isSetRunTime());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (JobKill job : tasksKillBody.getJobs()) {
                jocDefaultResponse = executeKillCommand(job, null, "kill");
            }
            for (TaskId task : tasksKillBody.getTaskIds()) {
                jocDefaultResponse = executeKillCommand(null, task, "kill");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }
}
