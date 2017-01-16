package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceStartJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;

@Path("jobs")
public class JobsResourceStartJobsImpl extends JOCResourceImpl implements IJobsResourceStartJob {

    private static final String API_CALL = "./jobs/start";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postJobsStart(String accessToken, StartJobs startJobs) throws Exception {
        try {
            initLogging(API_CALL, startJobs);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, startJobs.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJob().isStart());
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
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);

            XMLBuilder xml = new XMLBuilder("start_job");
            xml.addAttribute("job", normalizePath(startJob.getJob())).addAttribute("force", "yes");
            if (startJob.getAt() == null || "".equals(startJob.getAt())) {
                xml.addAttribute("at", "now");
            } else {
                xml.addAttribute("at", startJob.getAt());
            }
            if (startJob.getParams() != null && !startJob.getParams().isEmpty()) {
                Element params = XMLBuilder.create("params");
                for (NameValuePair param : startJob.getParams()) {
                    params.addElement("param").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
                }
                xml.add(params);
            }
            if (startJob.getEnvironment() != null && !startJob.getEnvironment().isEmpty()) {
                Element envs = XMLBuilder.create("environment");
                for (NameValuePair param : startJob.getEnvironment()) {
                    envs.addElement("variable").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
                }
                xml.add(envs);
            }
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        }
        return null;
    }
}
