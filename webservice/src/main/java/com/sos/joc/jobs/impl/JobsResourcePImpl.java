package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
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

	private static final String API_CALL = "./jobs/p";
	private String regex;
	private List<Folder> folders;
	private List<JobPath> jobs;
	private Boolean isOrderJob;

	@Override
	public JOCDefaultResponse postJobsP(String xAccessToken, String accessToken, JobsFilter jobsFilter) {
		return postJobsP(getAccessToken(xAccessToken, accessToken), jobsFilter);
	}

	public JOCDefaultResponse postJobsP(String accessToken, JobsFilter jobsFilter) {

		SOSHibernateSession session = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken,
					jobsFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobsFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			session = Globals.createSosHibernateStatelessConnection(API_CALL);
			session.beginTransaction();
			Boolean compact = jobsFilter.getCompact();
			regex = jobsFilter.getRegex();
			folders = addPermittedFolder(jobsFilter.getFolders());

			jobs = jobsFilter.getJobs();
			isOrderJob = jobsFilter.getIsOrderJob();

			List<JobP> listJobs = new ArrayList<JobP>();
			InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(session);
			Long instanceId = dbItemInventoryInstance.getId();
			List<DBItemInventoryJob> listOfJobs = processFilters(dbLayer);
			for (DBItemInventoryJob inventoryJob : listOfJobs) {
				JobP job = JobPermanent.getJob(inventoryJob, dbLayer, compact, instanceId);
				if (job != null) {
					listJobs.add(job);
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

	private List<DBItemInventoryJob> filterByRegex(List<DBItemInventoryJob> unfilteredJobs, String regex)
			throws Exception {
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

	private List<DBItemInventoryJob> processFilters(InventoryJobsDBLayer dbLayer) throws Exception {
		List<DBItemInventoryJob> listOfJobs = null;
		if (jobs != null && !jobs.isEmpty()) {
			listOfJobs = new ArrayList<DBItemInventoryJob>();
			List<DBItemInventoryJob> filteredJobs = null;
			for (JobPath jobPathFilter : jobs) {
				filteredJobs = dbLayer.getInventoryJobsFilteredByJobPath(normalizePath(jobPathFilter.getJob()),
						isOrderJob, dbItemInventoryInstance.getId());
				if (filteredJobs != null) {
					listOfJobs.addAll(filteredJobs);
				}
			}
		} else if (folders != null && !folders.isEmpty()) {
			listOfJobs = new ArrayList<DBItemInventoryJob>();
			List<DBItemInventoryJob> filteredJobs = null;
			for (Folder folderFilter : folders) {
				filteredJobs = dbLayer.getInventoryJobsFilteredByFolder(normalizeFolder(folderFilter.getFolder()),
						isOrderJob, folderFilter.getRecursive(), dbItemInventoryInstance.getId());
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
			unfilteredJobs = dbLayer.getInventoryJobs(isOrderJob, dbItemInventoryInstance.getId());
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
        listOfJobs = addAllPermittedOrder(listOfJobs);
        return listOfJobs;
    }
    
    private List<DBItemInventoryJob> addAllPermittedOrder(List<DBItemInventoryJob> jobsToAdd) {
        if (folderPermissions == null) {
            return jobsToAdd;
        }
        Set<Folder> folders = folderPermissions.getListOfFolders();
        if (folders.isEmpty()) {
            return jobsToAdd;
        }
        List<DBItemInventoryJob> listOfJobs = new ArrayList<DBItemInventoryJob>();
        for (DBItemInventoryJob job : jobsToAdd) {
            if (job != null && canAdd(job.getName(), folders)) {
                listOfJobs.add(job);
            }
        }
        return listOfJobs;
    }

}