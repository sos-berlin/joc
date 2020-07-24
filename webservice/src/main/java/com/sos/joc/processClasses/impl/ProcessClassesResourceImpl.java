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
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassesVCallable;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassV;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.resource.IProcessClassesResource;
import com.sos.schema.JsonValidator;

@Path("process_classes")
public class ProcessClassesResourceImpl extends JOCResourceImpl implements IProcessClassesResource {

    private static final String API_CALL = "./process_classes";

    @Override
    public JOCDefaultResponse postProcessClasses(String accessToken, byte[] processClassFilterBytes) {
        try {
            JsonValidator.validateFailFast(processClassFilterBytes, ProcessClassesFilter.class);
            ProcessClassesFilter processClassFilter = Globals.objectMapper.readValue(processClassFilterBytes, ProcessClassesFilter.class);
            
            SOSPermissionJocCockpit perms = getPermissonsJocCockpit(processClassFilter.getJobschedulerId(), accessToken);
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
            boolean withFolderFilter = processClassFilter.getFolders() != null && !processClassFilter.getFolders().isEmpty();
            Set<Folder> folders = addPermittedFolders(processClassFilter.getFolders());

            List<ProcessClassV> listProcessClasses = new ArrayList<ProcessClassV>();
            ProcessClassesV entity = new ProcessClassesV();
            if (processClasses != null && !processClasses.isEmpty()) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                String unpermittedObject = null;
                for (ProcessClassPath processClass : processClasses) {
                    if (processClass != null) {
                        checkRequiredParameter("processClass", processClass.getProcessClass());
                        String processClassPath = normalizePath(processClass.getProcessClass());
                        if (canAdd(processClassPath, permittedFolders)) {
                            tasks.add(new ProcessClassesVCallable(processClassPath, new JOCJsonCommand(command), accessToken, jobViewStatusEnabled));
                        } else {
                            unpermittedObject = processClassPath;
                        }
                    }
                }
                if (tasks.isEmpty() && unpermittedObject != null) {
                    throw new JocFolderPermissionsException(getParent(unpermittedObject));
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                throw new JocFolderPermissionsException(processClassFilter.getFolders().get(0).getFolder());
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
                listProcessClasses = callable.getProcessClasses();
            }
            if (!tasks.isEmpty()) {
                if (tasks.size() == 1) {
                    listProcessClasses = tasks.get(0).call();
                } else {
                    ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                    try {
                        for (Future<List<ProcessClassV>> result : executorService.invokeAll(tasks)) {
                            try {
                                listProcessClasses.addAll(result.get());
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

            entity.setProcessClasses(listProcessClasses);
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