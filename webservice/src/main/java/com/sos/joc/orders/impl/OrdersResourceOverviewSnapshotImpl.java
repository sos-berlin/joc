package com.sos.joc.orders.impl;

import java.net.URI;
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

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersSnapshotCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.model.order.OrdersSummary;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {

    private static final String API_CALL = "./orders/overview/snapshot";

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        try {
            initLogging(API_CALL, jobChainsFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainsFilter.getJobschedulerId(), getPermissons(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand();
            command.setUriBuilderForOrders(dbItemInventoryInstance.getUrl());
            command.addOrderStatisticsQuery();
            URI uri = command.getURI();

            Set<String> jobChains = getJobChainsWithoutDuplicates(jobChainsFilter.getJobChains());
            Set<String> folders = getFoldersWithoutDuplicatesAndSubfolders(jobChainsFilter.getFolders());
            Set<OrdersSnapshotCallable> tasks = new HashSet<OrdersSnapshotCallable>();

            if (jobChains.size() > 0) {
                for (String jobChain : jobChains) {
                    tasks.add(new OrdersSnapshotCallable(jobChain, uri, accessToken));
                }
            } else if (folders.size() > 0) {
                for (String folder : folders) {
                    tasks.add(new OrdersSnapshotCallable(folder, uri, accessToken));
                }
            } else {
                tasks.add(new OrdersSnapshotCallable("/", uri, accessToken));
            }

            OrdersSummary summary = new OrdersSummary();
            summary.setBlacklist(0);
            summary.setPending(0);
            summary.setRunning(0);
            summary.setSetback(0);
            summary.setSuspended(0);
            summary.setWaitingForResource(0);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<OrdersSummary> result : executorService.invokeAll(tasks)) {
                try {
                    OrdersSummary o = result.get();
                    summary.setBlacklist(summary.getBlacklist() + o.getBlacklist());
                    summary.setPending(summary.getPending() + o.getPending());
                    summary.setRunning(summary.getRunning() + o.getRunning());
                    summary.setSetback(summary.getSetback() + o.getSetback());
                    summary.setSuspended(summary.getSuspended() + o.getSuspended());
                    summary.setWaitingForResource(summary.getWaitingForResource() + o.getWaitingForResource());
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof JocException) {
                        throw (JocException) e.getCause();
                    } else {
                        throw (Exception) e.getCause();
                    }
                }
            }

            OrdersSnapshot entity = new OrdersSnapshot();
            entity.setOrders(summary);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }

    private String getPath(Folder folder) throws JocMissingRequiredParameterException {
        String path = folder.getFolder();
        if (path == null) {
            throw new JocMissingRequiredParameterException("folder");
        } else {
            path = normalizePath(path) + "/";
            if (!folder.getRecursive()) {
                path += "*";
            }
        }
        return path;
    }

    private Set<String> getFoldersWithoutDuplicatesAndSubfolders(List<Folder> folders) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (folders == null || folders.size() == 0) {
            return set;
        }
        SortedSet<String> sortedSet = new TreeSet<String>();
        for (Folder folder : folders) {
            sortedSet.add(getPath(folder));
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

    private Set<String> getJobChainsWithoutDuplicates(List<JobChainPath> jobChains) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (jobChains == null || jobChains.size() == 0) {
            return set;
        }
        for (JobChainPath jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("jobChain");
            }
            set.add(normalizePath(jobChain.getJobChain()));
        }
        return set;
    }
}
