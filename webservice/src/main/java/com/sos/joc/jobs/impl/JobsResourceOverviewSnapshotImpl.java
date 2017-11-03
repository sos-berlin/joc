package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceOverviewSnapshot;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.job.JobsSnapshot;
import com.sos.joc.model.job.JobsSummary;

@Path("jobs")
public class JobsResourceOverviewSnapshotImpl extends JOCResourceImpl implements IJobsResourceOverviewSnapshot {

    private static final String API_CALL = "./jobs/overview/snapshot";

    @Override
    public JOCDefaultResponse postJobsOverviewSnapshot(String accessToken, JobSchedulerId jobScheduler) throws Exception {
        String folders = "";
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobScheduler, accessToken, jobScheduler.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

//            if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
//                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
//                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
//                    folders = folders + folder.getFolder() + ",";
//                    com.sos.joc.model.common.Folder f = new com.sos.joc.model.common.Folder();
//                    f.setFolder(folder.getFolder());
//                    f.setRecursive(folder.isRecursive());
//                    jobChainsFilter.getFolders().add(f);
//                }
//            }
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry("<subsystem.show what=\"statistics\"/>", accessToken);
            NodeList jobStatistics = jocXmlCommand.getSosxml().selectNodeList("//job.statistics/job.statistic");
            JobsSummary jobs = new JobsSummary();
            jobs.setPending(0);
            jobs.setRunning(0);
            jobs.setStopped(0);
            jobs.setWaitingForProcess(0);
            jobs.setTasks(0);
            for (int i = 0; i < jobStatistics.getLength(); i++) {
                Element jobStatistic = (Element) jobStatistics.item(i);
                Integer count = 0;
                try {
                    count = Integer.valueOf(jobStatistic.getAttribute("count"));
                } catch (Exception e) {}
                if (jobStatistic.hasAttribute("job_state")) {
                    switch (jobStatistic.getAttribute("job_state")) {
                    case "pending":
                        jobs.setPending(count);
                        break;
                    case "running":
                        jobs.setRunning(count);
                        break;
                    case "stopped":
                        jobs.setStopped(count);
                        break;
                    }
                } else if (jobStatistic.hasAttribute("need_process")) {
                    jobs.setWaitingForProcess(count);
                }
            }
            try {
                jobs.setTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNode("task.statistic[@task_state='exist']/@count").getNodeValue()));
            } catch (Exception e) {}
            
            JobsSnapshot entity = new JobsSnapshot();
            entity.setSurveyDate(jocXmlCommand.getSurveyDate());entity.setJobs(jobs);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JobSchedulerObjectNotExistException e) {
            JocError err = new JocError();
            err.setMessage(String.format("%s: Please check your folders in the Account Management (%s)", e.getMessage(), folders));
            JocException ee = new JocException(err);
            return JOCDefaultResponse.responseStatusJSError(ee);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }
}
