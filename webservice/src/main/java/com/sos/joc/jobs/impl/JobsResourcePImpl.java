package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceP;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsP;

@Path("jobs")
public class JobsResourcePImpl extends JOCResourceImpl implements IJobsResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourcePImpl.class);
    private String regex;
    private List<Folder> folders;
    private List<JobPath> jobs;
    private Boolean isOrderJob;
    private Long instanceId;

    @Override
    public JOCDefaultResponse postJobsP(String accessToken, JobsFilter jobsFilterSchema) throws Exception {
        LOGGER.debug("init jobs p");
        try {
            Globals.beginTransaction();
            JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // FILTER
            Boolean compact = jobsFilterSchema.getCompact();
            regex = jobsFilterSchema.getRegex();
            folders = jobsFilterSchema.getFolders();
            jobs = jobsFilterSchema.getJobs();
            isOrderJob = jobsFilterSchema.getIsOrderJob();
            List<JobP> listJobs = new ArrayList<JobP>();

            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(jobsFilterSchema.getJobschedulerId());
            instanceId = instance.getId();
            List<DBItemInventoryJob> listOfJobs = processFilters(dbLayer);
            for (DBItemInventoryJob inventoryJob : listOfJobs) {
                JobP job = JobPermanent.getJob(inventoryJob, dbLayer, compact, instanceId);
                if (job != null) {
                    listJobs.add(job);
                }
            }
            JobsP entity = new JobsP();
            entity.setDeliveryDate(new Date());
            entity.setJobs(listJobs);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
        finally{
            Globals.rollback();
        }
    }
    
    private List<DBItemInventoryJob> filterByRegex(List<DBItemInventoryJob> unfilteredJobs, String regex) throws Exception {
        List<DBItemInventoryJob> filteredJobs = new ArrayList<DBItemInventoryJob>();
        for (DBItemInventoryJob unfilteredJob : unfilteredJobs) {
            Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredJob.getName());
            if (regExMatcher.find()) {
                filteredJobs.add(unfilteredJob); 
            }
        }
        if(!filteredJobs.isEmpty()) {
            return filteredJobs;
        } else {
            return null;
        }
    }
    
    private List<DBItemInventoryJob> processFilters(InventoryJobsDBLayer dbLayer) throws Exception {
        List<DBItemInventoryJob> listOfJobs = null;
        if (jobs != null && !jobs.isEmpty()) {
            listOfJobs = new ArrayList<DBItemInventoryJob>();
            List<DBItemInventoryJob> filteredJobs = null;
            for (JobPath jobPathFilter : jobs) {
                if (isOrderJob != null) {
                    filteredJobs = dbLayer.getInventoryJobsFilteredByJobPath(jobPathFilter.getJob(), isOrderJob, instanceId);
                } else {
                    filteredJobs = dbLayer.getInventoryJobsFilteredByJobPath(jobPathFilter.getJob(), null, instanceId);
                }
                if (filteredJobs != null) {
                    listOfJobs.addAll(filteredJobs);
                }
            }
        } else if (folders != null && !folders.isEmpty()) {
            listOfJobs = new ArrayList<DBItemInventoryJob>();
            List<DBItemInventoryJob> filteredJobs = null;
            for (Folder folderFilter : folders) {
                filteredJobs = dbLayer.getInventoryJobsFilteredByFolder(folderFilter.getFolder(), isOrderJob, folderFilter.getRecursive(),
                        instanceId);
                if (filteredJobs != null && !filteredJobs.isEmpty()) {
                    if (regex != null && !regex.isEmpty()) {
                        List<DBItemInventoryJob> jobsFilteredByRegex = filterByRegex(filteredJobs, regex);
                        if (jobsFilteredByRegex != null) {
                            listOfJobs.addAll(jobsFilteredByRegex);
                        }
                    } else {
                        listOfJobs.addAll(filteredJobs);
                    }
                }
            }
        } else {
            listOfJobs = new ArrayList<DBItemInventoryJob>();
            List<DBItemInventoryJob> unfilteredJobs = null;
            if (isOrderJob != null) {
                unfilteredJobs = dbLayer.getInventoryJobs(isOrderJob, instanceId);
            } else {
                unfilteredJobs = dbLayer.getInventoryJobs(instanceId);
            }
            if (unfilteredJobs != null && !unfilteredJobs.isEmpty()) {
                if (regex != null && !regex.isEmpty()) {
                    List<DBItemInventoryJob> jobsFilteredByRegex = filterByRegex(unfilteredJobs, regex);
                    if (jobsFilteredByRegex != null) {
                        listOfJobs.addAll(jobsFilteredByRegex);
                    }
                } else {
                    listOfJobs.addAll(unfilteredJobs);
                }
            }
        }
        return listOfJobs;
    }

}