package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsV;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {

    private static final String API_CALL = "./jobs";

    @Override
    public JOCDefaultResponse postJobs(String xAccessToken, String accessToken, JobsFilter jobsFilter) throws Exception {
        return postJobs(getAccessToken(xAccessToken, accessToken), jobsFilter);
    }

    public JOCDefaultResponse postJobs(String accessToken, JobsFilter jobsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobsFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(connection);
            List<String> jobsWithTempRunTime = dbLayer.getJobsWithTemporaryRuntime(dbItemInventoryInstance.getId());
            JobsV entity = new JobsV();
            List<JobV> listOfJobs=null;
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken, jobsWithTempRunTime);
            List<JobPath> jobs = jobsFilter.getJobs();
            List<Folder> folders = addPermittedFolder(jobsFilter.getFolders());

            if (jobs != null && !jobs.isEmpty()) {
                listOfJobs = jocXmlCommand.getJobsFromShowJob(jobs, jobsFilter);
            } else if (folders != null && !folders.isEmpty()) {
                listOfJobs = jocXmlCommand.getJobsFromShowState(folders, jobsFilter);
            } else {
                listOfJobs = jocXmlCommand.getJobsFromShowState(jobsFilter);
            }
            listOfJobs = addAllPermittedJobs(listOfJobs);
            entity.setJobs(listOfJobs);
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

    private List<JobV> addAllPermittedJobs(List<JobV> jobsToAdd) throws SessionNotExistException{
        List<JobV> listOfJobs = new ArrayList<JobV>();
        if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
            for (JobV job : jobsToAdd)
                if (canAdd(job, job.getPath())) {
                    listOfJobs.add(job);
                }
        } else {
            listOfJobs.addAll(jobsToAdd);
        }
        return listOfJobs;

    }

}