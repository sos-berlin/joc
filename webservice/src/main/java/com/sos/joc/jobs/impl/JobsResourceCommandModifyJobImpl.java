package com.sos.joc.jobs.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.resource.IJobsResourceCommandModifyJob;
import com.sos.joc.model.job.ModifyJobSchema;
import com.sos.joc.model.job.ModifyJobsSchema;
import com.sos.scheduler.model.commands.JSCmdModifyJob;
import com.sos.scheduler.model.objects.RunTime;

@Path("jobs")
public class JobsResourceCommandModifyJobImpl extends JOCResourceImpl implements IJobsResourceCommandModifyJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceCommandModifyJobImpl.class);

    private JOCDefaultResponse executeModifyJobCommand(ModifyJobSchema modifyJobsSchema, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdModifyJob jsCmdModifyJob = Globals.schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(command);
            jsCmdModifyJob.setJobIfNotEmpty(modifyJobsSchema.getJob());
            if ("set_run_time".equals(command)) {
                RunTime runtime = (RunTime) Globals.schedulerObjectFactory.unMarshall(modifyJobsSchema.getRunTime());
                jsCmdModifyJob.setRunTime(runtime);
            }
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdModifyJob);

            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/stop");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isStop());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "stop");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception {
        LOGGER.debug("init jobs/unstop");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isUnstop());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "unstop");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/set_run_time");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isSetRunTime());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "set_run_time");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/end_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isEndAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "end");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init job/suspend_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isSuspendAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "suspend");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/continue_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().isContinueAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, "continue");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
