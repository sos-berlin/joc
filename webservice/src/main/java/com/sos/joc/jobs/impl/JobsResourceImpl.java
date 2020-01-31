package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.classes.jobs.JobVolatileJson;
import com.sos.joc.classes.jobs.JobsVCallable;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsV;
import com.sos.schema.JsonValidator;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {

    private static final String API_CALL = "./jobs";

    @Override
    public JOCDefaultResponse postJobs(String accessToken, byte[] jobsFilterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(jobsFilterBytes, JobsFilter.class);
            JobsFilter jobsFilter = Globals.objectMapper.readValue(jobsFilterBytes, JobsFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobsFilter, accessToken, jobsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobsFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            jobsFilter.setRegex(SearchStringHelper.getRegexValue(jobsFilter.getRegex()));

            // connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(connection);
            // List<String> jobsWithTempRunTime = dbLayer.getJobsWithTemporaryRuntime(dbItemInventoryInstance.getId());
            JobsV entity = new JobsV();
            List<JobV> listOfJobs = null;
            // JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken, jobsWithTempRunTime);
            List<JobPath> jobs = jobsFilter.getJobs();
            boolean withFolderFilter = jobsFilter.getFolders() != null && !jobsFilter.getFolders().isEmpty();
            List<Folder> folders = addPermittedFolder(jobsFilter.getFolders());

            if (versionIsOlderThan("1.12.6")) {
                JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken);

                if (jobs != null && !jobs.isEmpty()) {
                    List<JobPath> permittedJobs = new ArrayList<JobPath>();
                    Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                    for (JobPath job : jobs) {
                        if (job != null && canAdd(job.getJob(), permittedFolders)) {
                            permittedJobs.add(job);
                        }
                    }
                    if (!permittedJobs.isEmpty()) {
                        listOfJobs = jocXmlCommand.getJobsFromShowJob(permittedJobs, jobsFilter);
                    }
                } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                    // no permission
                } else if (folders != null && !folders.isEmpty()) {
                    listOfJobs = jocXmlCommand.getJobsFromShowState(folders, jobsFilter);
                } else {
                    listOfJobs = jocXmlCommand.getJobsFromShowState(jobsFilter);
                }

            } else {
                JsonObject summary = null;
                if ((jobsFilter.getIsOrderJob() == null || jobsFilter.getIsOrderJob()) && jobsFilter.getCompactView() != Boolean.TRUE) {
                    JOCJsonCommand jocSummaryCommand = new JOCJsonCommand(this);
                    jocSummaryCommand.setUriBuilderForJobs();
                    jocSummaryCommand.addOrderStatisticsQuery(false);
                    summary = jocSummaryCommand.getJsonObjectFromPostWithRetry(getServiceBody("/"), accessToken);
                }

                JOCJsonCommand command = new JOCJsonCommand(this);
                command.setUriBuilderForJobs();
                command.addJobCompactQuery(jobsFilter.getCompact());
                Map<String, JobVolatileJson> listJobs = new HashMap<String, JobVolatileJson>();
                List<JobsVCallable> tasks = new ArrayList<JobsVCallable>();

                if (jobs != null && !jobs.isEmpty()) {
                    Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                    if (versionIsOlderThan("1.12.10")) {
                        for (JobPath job : jobs) {
                            if (job != null && canAdd(job.getJob(), permittedFolders)) {
                                tasks.add(new JobsVCallable(job, jobsFilter, new JOCJsonCommand(command), accessToken, summary));
                            }
                        }
                    } else {
                        Set<String> jobPaths = new HashSet<String>();
                        for (JobPath job : jobs) {
                            if (job != null && canAdd(job.getJob(), permittedFolders)) {
                                jobPaths.add(job.getJob());
                            }
                        }
                        if (!jobPaths.isEmpty()) {
                            JobsVCallable callable = new JobsVCallable(jobPaths, jobsFilter, command, accessToken, summary);
                            listJobs.putAll(callable.call());
                        }
                    }
                } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                    // no permission
                } else if (folders != null && !folders.isEmpty()) {
                    for (Folder folder : folders) {
                        folder.setFolder(normalizeFolder(folder.getFolder()));
                        tasks.add(new JobsVCallable(folder, jobsFilter, new JOCJsonCommand(command), accessToken, summary));
                    }
                } else {
                    Folder rootFolder = new Folder();
                    rootFolder.setFolder("/");
                    rootFolder.setRecursive(true);
                    JobsVCallable callable = new JobsVCallable(rootFolder, jobsFilter, command, accessToken, summary);
                    listJobs.putAll(callable.call());
                }

                if (tasks != null && !tasks.isEmpty()) {
                    if (tasks.size() == 1) {
                        listJobs = tasks.get(0).call();
                    } else {
                        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                        try {
                            for (Future<Map<String, JobVolatileJson>> result : executorService.invokeAll(tasks)) {
                                try {
                                    listJobs.putAll(result.get());
                                } catch (ExecutionException e) {
                                    if (e.getCause() instanceof JocException) {
                                        throw (JocException) e.getCause();
                                    } else {
                                        throw (Exception) e.getCause();
                                    }
                                }
                            }
                        } finally {
                            executorService.shutdown();
                        }
                    }
                }

                listOfJobs = new ArrayList<JobV>(listJobs.values());
            }

            // JOC-678
            if (listOfJobs != null && listOfJobs.stream().filter(j -> j.getState() != null && j.getState().get_text() == JobStateText.STOPPED)
                    .findAny().isPresent()) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                final AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
                listOfJobs.stream().filter(j -> j.getState() != null && j.getState().get_text() == JobStateText.STOPPED).forEach(j -> j.getState()
                        .setManually(dbLayer.isManuallyStopped(jobsFilter.getJobschedulerId(), j.getPath())));
            }
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

    private String getServiceBody(String path) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", path);
        return builder.build().toString();
    }
}