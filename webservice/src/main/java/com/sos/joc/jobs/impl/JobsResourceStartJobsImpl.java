package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceStartJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.scheduler.model.commands.JSCmdStartJob;
import com.sos.scheduler.model.objects.Environment;

@Path("jobs")
public class JobsResourceStartJobsImpl extends JOCResourceImpl implements IJobsResourceStartJob {

    private static final String API_CALL = "./jobs/start";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    
    @Override
    public JOCDefaultResponse postJobsStart(String accessToken, StartJobs startJobs) throws Exception {
        try {
            initLogging(API_CALL, startJobs);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, startJobs.getJobschedulerId(), getPermissons(accessToken).getJob().getStart()
                    .isTask());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (startJobs.getJobs().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobs'");
            }
            Date surveyDate = new Date();
            for (StartJob job : startJobs.getJobs()) {
                surveyDate = executeStartJobCommand(job);
            }
            if (listOfErrors.size() > 0) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeStartJobCommand(StartJob startJob) {

        try {
            if (startJob.getParams() != null && startJob.getParams().isEmpty()) {
                startJob.setParams(null);
            }
            if (startJob.getEnvironment() != null && startJob.getEnvironment().isEmpty()) {
                startJob.setEnvironment(null);
            }
            logAuditMessage(startJob);

            checkRequiredParameter("job", startJob.getJob());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdStartJob jsCmdStartJob = new JSCmdStartJob(Globals.schedulerObjectFactory);
            jsCmdStartJob.setJob(normalizePath(startJob.getJob()));
            jsCmdStartJob.setForce(WebserviceConstants.YES);
            if (startJob.getAt() == null || "".equals(startJob.getAt())) {
                jsCmdStartJob.setAt("now");
            } else {
                jsCmdStartJob.setAt(startJob.getAt());
            }
            if (startJob.getParams() != null) {
                jsCmdStartJob.setParams(getParams(startJob.getParams()));
            }
            if (startJob.getEnvironment() != null) {
                jsCmdStartJob.setEnvironment(getEnvironments(startJob.getEnvironment()));
            }
            String xml = jsCmdStartJob.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        }
        return null;
    }

    private Map<String, String> getParams(List<NameValuePair> params) {
        Map<String, String> jobParams = new HashMap<String, String>();
        for (NameValuePair param : params) {
            jobParams.put(param.getName(), param.getValue());
        }
        return jobParams;
    }

    private Environment getEnvironments(List<NameValuePair> envs) {
        Environment jobEnv = new Environment();
        for (NameValuePair env : envs) {
            Environment.Variable var = new Environment.Variable();
            var.setName(env.getName());
            var.setValue(env.getValue());
            jobEnv.getVariable().add(var);
        }
        return jobEnv;
    }
}
