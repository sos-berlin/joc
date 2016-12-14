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

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassesVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassV;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.resource.IProcessClassesResource;

@Path("process_classes")
public class ProcessClassesResourceImpl extends JOCResourceImpl implements IProcessClassesResource {

    private static final String API_CALL = "./process_classes";

    @Override
    public JOCDefaultResponse postProcessClasses(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        try {
            initLogging(API_CALL, processClassFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, processClassFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getProcessClass().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForProcessClasses();
            // always false otherwise no processes info
            // command.addProcessClassCompactQuery(jobSchedulerAgentClustersBody.getCompact());
            command.addProcessClassCompactQuery(false);

            List<ProcessClassesVCallable> tasks = new ArrayList<ProcessClassesVCallable>();
            Set<ProcessClassPath> processClasses = new HashSet<ProcessClassPath>(processClassFilter.getProcessClasses());
            List<Folder> folders = processClassFilter.getFolders();
            List<ProcessClassV> listProcessClasses = new ArrayList<ProcessClassV>();
            ProcessClassesV entity = new ProcessClassesV();
            if (processClasses != null && !processClasses.isEmpty()) {
                for (ProcessClassPath processClass : processClasses) {
                    checkRequiredParameter("processClass", processClass.getProcessClass());
                    tasks.add(new ProcessClassesVCallable(normalizePath(processClass.getProcessClass()), command, accessToken));
                }
                entity.setProcessClasses(listProcessClasses);
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    folder.setFolder(normalizeFolder(folder.getFolder()));
                    tasks.add(new ProcessClassesVCallable(folder, processClassFilter.getRegex(), processClassFilter.getIsAgentCluster(), command,
                            accessToken));
                }
            } else {
                Folder rootFolder = new Folder();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                ProcessClassesVCallable callable = new ProcessClassesVCallable(rootFolder, processClassFilter.getRegex(), processClassFilter
                        .getIsAgentCluster(), command, accessToken);
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

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}