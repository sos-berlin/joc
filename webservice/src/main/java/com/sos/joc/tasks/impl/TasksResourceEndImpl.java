package com.sos.joc.tasks.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TasksFilter;
import com.sos.joc.tasks.resource.ITasksResourceEnd;
import com.sos.scheduler.model.commands.JSCmdModifyJob;

@Path("tasks")
public class TasksResourceEndImpl extends JOCResourceImpl implements ITasksResourceEnd {
    private static final String END = "end";
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceEndImpl.class);

    private JOCDefaultResponse executeModifyJobCommand(TasksFilter job) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdModifyJob jsCmdModifyJob = Globals.schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(END);
            jsCmdModifyJob.setJobIfNotEmpty(job.getJob());

            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdModifyJob);

            jocXmlCommand.executePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing task.%s %s:%s", END, e.getCause(), e.getMessage()));
        }
    }

    @Override
    public JOCDefaultResponse postTasksEnd(String accessToken, ModifyTasks modifyTasks) {
        LOGGER.debug("init tasks/end");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyTasks.getJobschedulerId(), getPermissons(accessToken).getJob().isEndAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (TasksFilter job : modifyTasks.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job);
            }
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
