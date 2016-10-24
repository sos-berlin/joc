package com.sos.joc.tasks.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;
import com.sos.joc.tasks.resource.ITasksResourceKill;
import com.sos.scheduler.model.commands.JSCmdKillTask;
import com.sos.scheduler.model.commands.JSCmdShowJob;

@Path("tasks")
public class TasksResourceKillImpl extends JOCResourceImpl implements ITasksResourceKill {
    private static final String KILL = "kill";
    private static final String TERMINATE_WITHIN = "terminate_within";
    private static final String TERMINATE = "terminate";
    private static final String END = "end";
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceKillImpl.class);
    private static String API_CALL = "./tasks/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postTasksTerminate(String accessToken, ModifyTasks modifyTasks) {
        API_CALL += TERMINATE;
        LOGGER.debug(API_CALL);
        try {
            return postTasksCommand(accessToken, TERMINATE, getPermissons(accessToken).getJob().isTerminate(), modifyTasks);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postTasksTerminateWithin(String accessToken, ModifyTasks modifyTasks) {
        API_CALL += TERMINATE_WITHIN;
        LOGGER.debug(API_CALL);
        try {
            return postTasksCommand(accessToken, TERMINATE_WITHIN, getPermissons(accessToken).getJob().isTerminate(), modifyTasks);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postTasksKill(String accessToken, ModifyTasks modifyTasks) {
        API_CALL += KILL;
        LOGGER.debug(API_CALL);
        try {
            return postTasksCommand(accessToken, KILL, getPermissons(accessToken).getJob().isKill(), modifyTasks);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    @Override
    public JOCDefaultResponse postTasksEnd(String accessToken, ModifyTasks modifyTasks) {
        API_CALL += END;
        LOGGER.debug(API_CALL);
        try {
            return postTasksCommand(accessToken, END, getPermissons(accessToken).getJob().isKill(), modifyTasks);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private JOCDefaultResponse postTasksCommand(String accessToken, String command, boolean permission, ModifyTasks modifyTasks) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyTasks.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        Date surveyDate = Date.from(Instant.now());
        for (TasksFilter job : modifyTasks.getJobs()) {
            List<TaskId> taskIds = job.getTaskIds();
            if (taskIds == null || taskIds.isEmpty()) {
                //terminate all tasks of job
                taskIds = getTaskIds(job);
            }
            if (taskIds != null) {
                for (TaskId task : taskIds) {
                    surveyDate = executeKillCommand(job, task, command, modifyTasks);
                }
            }
        }
        if (listOfErrors.size() > 0) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyTasks));
            return JOCDefaultResponse.responseStatus419(listOfErrors, err);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }
    
    private List<TaskId> getTaskIds(TasksFilter job) {
        try {
            checkRequiredParameter("job", job.getJob());
            JSCmdShowJob jsCmdShowJob = Globals.schedulerObjectFactory.createShowJob();
            jsCmdShowJob.setJobName(job.getJob());
            String xml = jsCmdShowJob.toXMLString();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            
            List<TaskId> taskIds = new ArrayList<TaskId>();
            NodeList tasks = jocXmlCommand.getSosxml().selectNodeList("//tasks/tasks/@id");
            for (int i=0; i < tasks.getLength(); i++) {
                TaskId taskId = new TaskId();
                taskId.setTaskId(tasks.item(i).getNodeValue());
                taskIds.add(taskId);
            }
            return taskIds;
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, job, null));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, job, null));
        }
        return null;
    }

    private Date executeKillCommand(TasksFilter job, TaskId taskId, String command, ModifyTasks modifyTasks) {
        try {
            checkRequiredParameter("job", job.getJob());
            checkRequiredParameter("taskId", taskId.getTaskId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdKillTask jsCmdKillTask = Globals.schedulerObjectFactory.createKillTask();
            jsCmdKillTask.setId(new BigInteger(taskId.getTaskId()));
            jsCmdKillTask.setJob(job.getJob());
            
            switch(command) {
            case KILL:
                jsCmdKillTask.setImmediately(WebserviceConstants.YES);
                break;
            case TERMINATE:
                jsCmdKillTask.setImmediately(WebserviceConstants.YES);
                jsCmdKillTask.setTimeout(WebserviceConstants.NEVER);
                break;
            case TERMINATE_WITHIN:
                jsCmdKillTask.setImmediately(WebserviceConstants.YES);
                Integer timeout = modifyTasks.getTimeout();
                if (timeout == null) {
                    timeout = 0;
                }
                jsCmdKillTask.setTimeout(timeout);
                break;
            }

            String xml = jsCmdKillTask.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, job, taskId));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, job, taskId));
        }
        return null;
    }
}
