package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceStartJob;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.scheduler.model.commands.JSCmdStartJob;

@Path("jobs")
public class JobsResourceStartJobsImpl extends JOCResourceImpl implements IJobsResourceStartJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceStartJobsImpl.class);

    private String[] getParams(List<NameValuePair> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            NameValuePair param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeStartJobCommand(StartJob startJob) {

        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdStartJob jsCmdStartJob = new JSCmdStartJob(Globals.schedulerObjectFactory);
            jsCmdStartJob.setJobIfNotEmpty(startJob.getJob());
            jsCmdStartJob.setForceIfNotEmpty(WebserviceConstants.YES);
            jsCmdStartJob.setAtIfNotEmpty(startJob.getAt());
            jsCmdStartJob.setNameIfNotEmpty(startJob.getJob());
            String[] jobParams = getParams(startJob.getParams());
            if (jobParams != null) {
                jsCmdStartJob.setParams(jobParams);
            }
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdStartJob);
            jocXmlCommand.executePost(xml);
            listOfErrors = addError(listOfErrors, jocXmlCommand, startJob.getJob());

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing start job %s:%s", e.getCause(), e.getMessage()));
        }
    }

    @Override
    public JOCDefaultResponse postJobsStart(String accessToken, StartJobs startJobs) throws Exception {
        LOGGER.debug("init orders: add");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(startJobs.getJobschedulerId(), getPermissons(accessToken).getJob().getStart().isTask());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (StartJob startJob : startJobs.getJobs()) {
                jocDefaultResponse = executeStartJobCommand(startJob);
            }
            if (listOfErrors != null) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

        return jocDefaultResponse;

    }

}
