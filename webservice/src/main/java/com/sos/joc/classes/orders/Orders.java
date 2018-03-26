package com.sos.joc.classes.orders;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.shiro.session.Session;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.model.order.OrdersSummary;

public class Orders {
    
    public static OrdersSnapshot getSnapshot(JOCJsonCommand command, String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        return getSnapshot(command, null, accessToken, null, null, jobChainsFilter);
    }
    
    public static OrdersSnapshot getSnapshot(JOCJsonCommand command, Session session, String accessToken, String httpClientPropKey, String eventIdPropKey, JobChainsFilter jobChainsFilter) throws Exception {
        command.setUriBuilderForOrders();
        command.addOrderStatisticsQuery();
        command.createHttpClient();
        if (session != null && httpClientPropKey != null) {
            session.setAttribute(httpClientPropKey, command.getHttpClient());
        }

        Set<String> jobChains = getJobChainsWithoutDuplicates(jobChainsFilter.getJobChains());
        Set<String> folders = getFoldersWithoutDuplicatesAndSubfolders(jobChainsFilter.getFolders());
        Set<OrdersSnapshotCallable> tasks = new HashSet<OrdersSnapshotCallable>();

        if (jobChains.size() > 0) {
            for (String jobChain : jobChains) {
                tasks.add(new OrdersSnapshotCallable(jobChain, new JOCJsonCommand(command), accessToken));
            }
        } else if (folders.size() > 0) {
            for (String folder : folders) {
                tasks.add(new OrdersSnapshotCallable(folder, new JOCJsonCommand(command), accessToken));
            }
        } else {
            tasks.add(new OrdersSnapshotCallable("/", command, accessToken));
        }
        
        Long eventId = 0L;
        OrdersSummary summary = new OrdersSummary();
        summary.setBlacklist(0);
        summary.setPending(0);
        summary.setRunning(0);
        summary.setSetback(0);
        summary.setSuspended(0);
        summary.setWaitingForResource(0);
        
        if (!tasks.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
            try {
                for (Future<OrdersSnapshotEvent> result : executorService.invokeAll(tasks)) {
                    try {
                        OrdersSnapshotEvent o = result.get();
                        summary.setBlacklist(summary.getBlacklist() + o.getBlacklist());
                        summary.setPending(summary.getPending() + o.getPending());
                        summary.setRunning(summary.getRunning() + o.getRunning());
                        summary.setSetback(summary.getSetback() + o.getSetback());
                        summary.setSuspended(summary.getSuspended() + o.getSuspended());
                        summary.setWaitingForResource(summary.getWaitingForResource() + o.getWaitingForResource());
                        long event = o.getEventId();
                        if (event > eventId) {
                            eventId = event;
                        }
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof JobSchedulerObjectNotExistException) {
                            //
                        } else if (e.getCause() instanceof JocException) {
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
        
        //System.setProperty(eventIdPropKey, eventId.toString());
        if (session != null && eventIdPropKey != null) {
            session.setAttribute(eventIdPropKey, eventId.toString());
        }
        
        OrdersSnapshot entity = new OrdersSnapshot();
        entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(eventId));
        entity.setOrders(summary);
        entity.setDeliveryDate(Date.from(Instant.now()));
        return entity;
    }
    
    private static String getPostBody(Folder folder) throws JocMissingRequiredParameterException {
        String path = folder.getFolder();
        if (path == null) {
            throw new JocMissingRequiredParameterException("folder");
        } else {
            path = ("/"+path.trim()+"/").replaceAll("//+","/");
            if (!folder.getRecursive()) {
                path += "*";
            }
        }
        return path;
    }
    
    private static Set<String> getFoldersWithoutDuplicatesAndSubfolders(List<Folder> folders) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (folders == null || folders.size() == 0) {
            return set;
        }
        SortedSet<String> sortedSet = new TreeSet<String>();
        for (Folder folder : folders) {
            sortedSet.add(getPostBody(folder));
        }
        String[] strA = new String[sortedSet.size()];
        int index = 0;
        for (String str : sortedSet) {
            if (index > 0) {
                if (!str.contains(strA[index - 1])) {
                    strA[index] = str;
                    index += 1;
                    set.add(str);
                }
            } else {
                strA[index] = str;
                index += 1;
                set.add(str);
            }
        }
        return set;
    }
    
    private static Set<String> getJobChainsWithoutDuplicates(List<JobChainPath> jobChains) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (jobChains == null || jobChains.size() == 0) {
            return set;
        }
        for (JobChainPath jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("jobChain");
            }
            set.add(("/"+jobChain.getJobChain()).replaceAll("//+","/").replaceFirst("/$",""));
        }
        return set;
    }

}
