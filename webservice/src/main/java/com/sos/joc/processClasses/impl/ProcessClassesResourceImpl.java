package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassesVCallable;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.plan.PlanFilter;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassV;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.resource.IProcessClassesResource;

@Path("process_classes")
public class ProcessClassesResourceImpl extends JOCResourceImpl implements IProcessClassesResource {

    private static final String API_CALL = "./process_classes";

    @Override
    public JOCDefaultResponse postProcessClasses(String xAccessToken, String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        return postProcessClasses(getAccessToken(xAccessToken, accessToken), processClassFilter);
    }

    public JOCDefaultResponse postProcessClasses(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        try {
            SOSPermissionJocCockpit perms = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, processClassFilter, accessToken, processClassFilter.getJobschedulerId(), perms
                    .getProcessClass().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            boolean jobViewStatusEnabled = perms.getJob().getView().isStatus();

            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForProcessClasses();
            // always false otherwise no processes info
            // command.addProcessClassCompactQuery(jobSchedulerAgentClustersBody.getCompact());
            command.addProcessClassCompactQuery(false);

            List<ProcessClassesVCallable> tasks = new ArrayList<ProcessClassesVCallable>();
            Set<ProcessClassPath> processClasses = new HashSet<ProcessClassPath>(processClassFilter.getProcessClasses());
            List<Folder> folders = addPermittedFolder(processClassFilter.getFolders());
            
            List<ProcessClassV> listProcessClasses = new ArrayList<ProcessClassV>();
            ProcessClassesV entity = new ProcessClassesV();
            if (processClasses != null && !processClasses.isEmpty()) {
                for (ProcessClassPath processClass : processClasses) {
                    checkRequiredParameter("processClass", processClass.getProcessClass());
                    tasks.add(new ProcessClassesVCallable(normalizePath(processClass.getProcessClass()), new JOCJsonCommand(command), accessToken,
                            jobViewStatusEnabled));
                }
                entity.setProcessClasses(listProcessClasses);
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    folder.setFolder(normalizeFolder(folder.getFolder()));
                    tasks.add(new ProcessClassesVCallable(folder, processClassFilter.getRegex(), processClassFilter.getIsAgentCluster(),
                            new JOCJsonCommand(command), accessToken, jobViewStatusEnabled));
                }
            } else {
                Folder rootFolder = new Folder();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                ProcessClassesVCallable callable = new ProcessClassesVCallable(rootFolder, processClassFilter.getRegex(), processClassFilter
                        .getIsAgentCluster(), command, accessToken, jobViewStatusEnabled);
                entity.setProcessClasses(callable.getProcessClasses());
            }
            if (tasks.size() > 0) {
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                for (Future<List<ProcessClassV>> result : executorService.invokeAll(tasks)) {
                    try {
                        listProcessClasses.addAll(result.get());
                    } catch (ExecutionException e) {
                        executorService.shutdown();
                        if (e.getCause() instanceof JocException) {
                            throw (JocException) e.getCause();
                        } else {
                            throw (Exception) e.getCause();
                        }
                    }
                }
                executorService.shutdown();
                entity.setProcessClasses(listProcessClasses);
            }

            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JobSchedulerConnectionResetException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatus434JSError(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}