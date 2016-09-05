package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.jobs.resource.IJobsResourceCommandStartJob;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.StartJobSchema;
import com.sos.joc.model.job.StartJobsSchema;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdStartJob;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobs")
public class JobsResourceCommandStartJobsImpl extends JOCResourceImpl implements IJobsResourceCommandStartJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceCommandStartJobsImpl.class);

    private String[] getParams(List<NameValuePairsSchema> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            NameValuePairsSchema param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeStartJobCommand(StartJobSchema startJobSchema) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdStartJob objStartJob = new JSCmdStartJob(objFactory);
            objStartJob.setJobIfNotEmpty(startJobSchema.getJob());
            objStartJob.setForceIfNotEmpty(WebserviceConstants.YES);
            objStartJob.setAtIfNotEmpty(startJobSchema.getAt());
            objStartJob.setNameIfNotEmpty(startJobSchema.getJob());
            String[] jobParams = getParams(startJobSchema.getParams());
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
    public JOCDefaultResponse postJobsStart(String accessToken, StartJobsSchema startJobsSchema) throws Exception {
        LOGGER.debug("init Orders: Add");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(startJobsSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getStart().isTask());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (StartJobSchema startJobSchemab : startJobsSchema.getJobs()) {
                jocDefaultResponse = executeStartJobCommand(startJobSchemab);
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
