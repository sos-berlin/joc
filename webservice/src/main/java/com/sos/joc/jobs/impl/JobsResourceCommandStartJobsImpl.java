package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.post.commands.start.Job;
import com.sos.joc.jobs.post.commands.start.Param;
import com.sos.joc.jobs.post.commands.start.StartJobsBody;
import com.sos.joc.jobs.resource.IJobsResourceCommandStartJob;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdStartJob;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobs")
public class JobsResourceCommandStartJobsImpl extends JOCResourceImpl implements IJobsResourceCommandStartJob {
    private static final Logger LOGGER = Logger.getLogger(JobsResourceCommandStartJobsImpl.class);

    private String[] getParams(List<Param> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            Param param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeStartJobCommand(Job job) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdStartJob objStartJob = new JSCmdStartJob(objFactory);
            objStartJob.setJobIfNotEmpty(job.getJob());
            objStartJob.setForceIfNotEmpty(YES);
            objStartJob.setAtIfNotEmpty(job.getAt());
            objStartJob.setNameIfNotEmpty(job.getJob());
            String[] jobParams = getParams(job.getParams());
            if (jobParams != null) {
                objStartJob.setParams(jobParams);
            }
            String xml = objFactory.toXMLString(objStartJob);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order.add:" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postJobsStart(String accessToken, StartJobsBody startJobsBody) throws Exception {
        LOGGER.debug("init Orders: Add");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(startJobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().getStart().isTask());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Job job : startJobsBody.getJobs()) {
                jocDefaultResponse = executeStartJobCommand(job);
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
