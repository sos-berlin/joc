package com.sos.joc.tasks.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.job.Job____;
import com.sos.joc.model.job.ModifyTasksSchema;
import com.sos.joc.tasks.resource.ITasksResourceEnd;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyJob;
import com.sos.scheduler.model.objects.Spooler;

@Path("tasks")
public class TasksResourceEndImpl extends JOCResourceImpl implements ITasksResourceEnd {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceEndImpl.class);
     
    private JOCDefaultResponse executeModifyJobCommand(Job____ job, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifyJob jsCmdModifyJob = schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(command);
            jsCmdModifyJob.setJobIfNotEmpty(job.getJob());
            
            String xml = schedulerObjectFactory.toXMLString(jsCmdModifyJob);

            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing job." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

  
    @Override
    public JOCDefaultResponse postTasksEnd(String accessToken, ModifyTasksSchema modifyTasksSchema) {
        LOGGER.debug("init Tasks: end");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyTasksSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isEndAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job____ job : modifyTasksSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job,"end");
            }
            
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
