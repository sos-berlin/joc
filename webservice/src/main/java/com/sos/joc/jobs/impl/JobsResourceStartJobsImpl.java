package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.StartJobAudit;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceStartJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.joc.model.job.StartedTasks;
import com.sos.joc.model.job.TaskPath200;

@Path("jobs")
public class JobsResourceStartJobsImpl extends JOCResourceImpl implements IJobsResourceStartJob {

    private static final String API_CALL = "./jobs/start";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private List<TaskPath200> taskPaths = new ArrayList<TaskPath200>();

    @Override
    public JOCDefaultResponse postJobsStart(String xAccessToken, String accessToken, StartJobs startJobs) throws Exception {
        return postJobsStart(getAccessToken(xAccessToken, accessToken), startJobs);
    }

    public JOCDefaultResponse postJobsStart(String accessToken, StartJobs startJobs) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, startJobs, accessToken, startJobs.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getJob().getExecute().isStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(startJobs.getAuditLog());
            if (startJobs.getJobs().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobs'");
            }
            StartedTasks entity = new StartedTasks();
            for (StartJob job : startJobs.getJobs()) {
                executeStartJobCommand(job, startJobs);
            }
            entity.setTasks(taskPaths);
            if (taskPaths.isEmpty()) {
                entity.setTasks(null);
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            if (listOfErrors.size() > 0) {
                entity.setErrors(listOfErrors);
                entity.setOk(null);
                return JOCDefaultResponse.responseStatus419(entity);
            } else {
                entity.setErrors(null);
                entity.setOk(true);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void executeStartJobCommand(StartJob startJob, StartJobs startJobs) {

        try {
            if (startJob.getParams() != null && startJob.getParams().isEmpty()) {
                startJob.setParams(null);
            }
            if (startJob.getEnvironment() != null && startJob.getEnvironment().isEmpty()) {
                startJob.setEnvironment(null);
            }
            StartJobAudit jobAudit = new StartJobAudit(startJob, startJobs);
            logAuditMessage(jobAudit);

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
            storeAuditLogEntry(jobAudit);
            
            TaskPath200 task = new TaskPath200();
            task.setSurveyDate(jocXmlCommand.getSurveyDate());
            task.setJob(startJob.getJob());
            try {
                task.setTaskId(jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/ok/task/@id"));
            } catch (Exception e) {
                task.setTaskId(null);
            }
            taskPaths.add(task);
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), startJob));
        }
    }
}
