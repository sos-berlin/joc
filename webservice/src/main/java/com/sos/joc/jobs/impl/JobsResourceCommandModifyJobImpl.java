package com.sos.joc.jobs.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.post.commands.modify.Job;
import com.sos.joc.jobs.post.commands.modify.ModifyJobsBody;
import com.sos.joc.jobs.resource.IJobsResourceCommandModifyJob;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyJob;
import com.sos.scheduler.model.objects.RunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobs")
public class JobsResourceCommandModifyJobImpl extends JOCResourceImpl implements IJobsResourceCommandModifyJob {
    private static final Logger LOGGER = Logger.getLogger(JobsResourceCommandModifyJobImpl.class);

    private JOCDefaultResponse executeModifyJobCommand(Job job, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifyJob jsCmdModifyJob = schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(command);
            jsCmdModifyJob.setJobIfNotEmpty(job.getJob());
            if ("set_run_time".equals(command)) {
                RunTime runtime = (RunTime) schedulerObjectFactory.unMarshall(job.getRunTime());
                jsCmdModifyJob.setRunTime(runtime);
            }
            String xml = schedulerObjectFactory.toXMLString(jsCmdModifyJob);

            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobsBody modifyJobsBody) {
        LOGGER.debug("init Jobs: Stop");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isStop());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "stop");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobsBody modifyJobsBody) throws Exception {
        LOGGER.debug("init Jobs: unstop");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isUnstop());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "unstop");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobsBody modifyJobsBody) {
        LOGGER.debug("init Jobs: set_run_time");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isSetRunTime());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "set_run_time");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobsBody modifyJobsBody) {
        LOGGER.debug("init Jobs: end_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isEndAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "end");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobsBody modifyJobsBody) {
        LOGGER.debug("init Jobs: suspend_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isSuspendAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "suspend");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobsBody modifyJobsBody) {
        LOGGER.debug("init Jobs: continue_all_tasks");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().isContinueAllTasks());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : modifyJobsBody.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(job, "continue");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
