package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.HashSet;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceOverviewSummary;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsHistoricSummary;
import com.sos.joc.model.job.JobsOverView;

@Path("jobs")
public class JobsResourceOverviewSummaryImpl extends JOCResourceImpl implements IJobsResourceOverviewSummary {

    private static final String API_CALL = "./jobs/overview/summary";

    @Override
    public JOCDefaultResponse postJobsOverviewSummary(String accessToken, JobsFilter jobsFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobsFilter.getJobschedulerId(), accessToken).getOrder().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            JobsHistoricSummary jobsHistoricSummary = new JobsHistoricSummary();
            Globals.beginTransaction(connection);

            ReportTaskExecutionsDBLayer reportTaskExecDBLayer = new ReportTaskExecutionsDBLayer(connection);
            reportTaskExecDBLayer.getFilter().setSchedulerId(jobsFilter.getJobschedulerId());

            if (jobsFilter.getDateFrom() != null) {
                reportTaskExecDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDateFrom(jobsFilter.getDateFrom(), jobsFilter.getTimeZone()));
            }
            if (jobsFilter.getDateTo() != null) {
                reportTaskExecDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDateTo(jobsFilter.getDateTo(), jobsFilter.getTimeZone()));
            }

            reportTaskExecDBLayer.getFilter().setListOfFolders(folderPermissions.getListOfFolders());
            if (jobsFilter.getFolders().size() > 0) {
                reportTaskExecDBLayer.getFilter().addFolderPaths(new HashSet<Folder>(jobsFilter.getFolders()));
            }

            if (jobsFilter.getJobs().size() > 0) {
                for (JobPath jobPath : jobsFilter.getJobs()) {
                    reportTaskExecDBLayer.getFilter().addJobPath(normalizePath(jobPath.getJob()));
                }
            }

            JobsOverView entity = new JobsOverView();
            entity.setSurveyDate(new Date());
            entity.setJobs(jobsHistoricSummary);
            jobsHistoricSummary.setFailed(reportTaskExecDBLayer.getCountSchedulerJobHistoryListFromTo(false).intValue());
            jobsHistoricSummary.setSuccessful(reportTaskExecDBLayer.getCountSchedulerJobHistoryListFromTo(true).intValue());
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
