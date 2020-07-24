package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobs.resource.IJobsResourceP;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsP;
import com.sos.schema.JsonValidator;

@Path("jobs")
public class JobsResourcePImpl extends JOCResourceImpl implements IJobsResourceP {

    private static final String API_CALL = "./jobs/p";

    @Override
    public JOCDefaultResponse postJobsP(String accessToken, byte[] jobsFilterBytes) {

        SOSHibernateSession session = null;

        try {
            JsonValidator.validateFailFast(jobsFilterBytes, JobsFilter.class);
            JobsFilter jobsFilter = Globals.objectMapper.readValue(jobsFilterBytes, JobsFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobsFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            session = Globals.createSosHibernateStatelessConnection(API_CALL);
            session.beginTransaction();

            List<JobP> listJobs = new ArrayList<JobP>();
            InventoryJobsDBLayer dbJobsLayer = new InventoryJobsDBLayer(session);
            DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(session);
            Map<String,String> documentations = dbDocLayer.getDocumentationPaths(jobsFilter.getJobschedulerId(), JobSchedulerObjectType.JOB);
            Long instanceId = dbItemInventoryInstance.getId();
            List<DBItemInventoryJob> listOfJobs = processFilters(dbJobsLayer, jobsFilter);
            if (listOfJobs != null) {
                for (DBItemInventoryJob inventoryJob : listOfJobs) {
                    JobP job = JobPermanent.getJob(inventoryJob, dbJobsLayer, documentations.get(inventoryJob.getName()), jobsFilter.getCompact(), instanceId);
                    if (job != null) {
                        listJobs.add(job);
                    }
                }
            }
            JobsP entity = new JobsP();
            entity.setJobs(listJobs);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
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
        if (!filteredJobs.isEmpty()) {
            return filteredJobs;
        } else {
            return null;
        }
    }

    private List<DBItemInventoryJob> processFilters(InventoryJobsDBLayer dbLayer, JobsFilter jobsFilter) throws Exception {
        List<DBItemInventoryJob> listOfJobs = null;
        List<JobPath> jobs = jobsFilter.getJobs();
        boolean withFolderFilter = jobsFilter.getFolders() != null && !jobsFilter.getFolders().isEmpty();
        Set<Folder> folders = addPermittedFolders(jobsFilter.getFolders());
        String regex = jobsFilter.getRegex();
        
        Set<String> criticalities = null;
        if (jobsFilter.getCriticality() != null && !jobsFilter.getCriticality().isEmpty()) {
            criticalities = jobsFilter.getCriticality().stream().map(c -> c.value().toLowerCase()).collect(Collectors.toSet());
        }

        if (jobs != null && !jobs.isEmpty()) {
            listOfJobs = new ArrayList<DBItemInventoryJob>();
            List<DBItemInventoryJob> filteredJobs = null;
            
            Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
            String unpermittedObject = null;
            for (JobPath job : jobs) {
                if (job != null) {
                    if (canAdd(job.getJob(), permittedFolders)) {
                        filteredJobs = dbLayer.getInventoryJobsFilteredByJobPath(normalizePath(job.getJob()), jobsFilter.getIsOrderJob(),
                                criticalities, dbItemInventoryInstance.getId());
                        if (filteredJobs != null) {
                            listOfJobs.addAll(filteredJobs);
                        }
                    } else {
                        unpermittedObject = job.getJob();
                    }
                }
                if (listOfJobs.isEmpty() && unpermittedObject != null) {
                    throw new JocFolderPermissionsException(getParent(unpermittedObject));
                }
            }
        } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
            throw new JocFolderPermissionsException(jobsFilter.getFolders().get(0).getFolder());
        } else if (folders != null && !folders.isEmpty()) {
            listOfJobs = new ArrayList<DBItemInventoryJob>();
            List<DBItemInventoryJob> filteredJobs = null;
            for (Folder folderFilter : folders) {
                filteredJobs = dbLayer.getInventoryJobsFilteredByFolder(normalizeFolder(folderFilter.getFolder()), jobsFilter.getIsOrderJob(),
                        criticalities, folderFilter.getRecursive(), dbItemInventoryInstance.getId());
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
            unfilteredJobs = dbLayer.getInventoryJobs(jobsFilter.getIsOrderJob(), jobsFilter.getCriticality(), dbItemInventoryInstance.getId());
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