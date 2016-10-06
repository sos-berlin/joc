package com.sos.joc.orders.impl;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersSnapshotCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.jobChain.JobChain____;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.order.Orders;
import com.sos.joc.model.order.SnapshotSchema;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceOverviewSnapshotImpl.class);

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, JobChainsFilterSchema filterSchema) throws Exception {
        LOGGER.debug("init orders/overview/summary");
        try {
            JOCDefaultResponse jocDefaultResponse = init(filterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand(dbItemInventoryInstance.getUrl());
            command.addOrderStatisticsQuery();
            URI uri = command.getURI();
            
            Set<String> jobChains = getJobChainsWithoutDuplicates(filterSchema.getJobChains());
            Set<String> folders = getFoldersWithoutDuplicatesAndSubfolders(filterSchema.getFolders());
            Set<OrdersSnapshotCallable> tasks = new HashSet<OrdersSnapshotCallable>();
            
            if (jobChains.size() > 0) {
                for (String jobChain : jobChains) {
                    tasks.add(new OrdersSnapshotCallable(jobChain, uri));
                }
            } else if (folders.size() > 0) {
                for (String folder : folders) {
                    tasks.add(new OrdersSnapshotCallable(folder, uri));
                }
            } else {
                tasks.add(new OrdersSnapshotCallable("/", uri));
            }
            
            Orders summary = new Orders();
            summary.setBlacklist(0);
            summary.setPending(0);
            summary.setRunning(0);
            summary.setSetback(0);
            summary.setSuspended(0);
            summary.setWaitingForResource(0);
            
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<Orders> result : executorService.invokeAll(tasks)) {
                Orders o = result.get();
                summary.setBlacklist(summary.getBlacklist() + o.getBlacklist());
                summary.setPending(summary.getPending() + o.getPending());
                summary.setRunning(summary.getRunning() + o.getRunning());
                summary.setSetback(summary.getSetback() + o.getSetback());
                summary.setSuspended(summary.getSuspended() + o.getSuspended());
                summary.setWaitingForResource(summary.getWaitingForResource() + o.getWaitingForResource());
            }
            
            SnapshotSchema entity = new SnapshotSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrders(summary);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
    
    private String getPath(FoldersSchema folder) throws JocMissingRequiredParameterException {
        String path = folder.getFolder();
        if (path == null) {
            throw new JocMissingRequiredParameterException("folder");
        } else {
            path = ("/"+path.trim()+"/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
            } 
        }
        return path;
    }
    
    private Set<String> getFoldersWithoutDuplicatesAndSubfolders(List<FoldersSchema> folders) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (folders == null || folders.size() == 0) {
            return set;
        }
        SortedSet<String> sortedSet = new TreeSet<String>();
        for (FoldersSchema folder : folders) {
            sortedSet.add(getPath(folder));
        }
        String[] strA = new String[sortedSet.size()];
        int index = 0;
        for (String str : sortedSet) {
            if (index > 0) {
                if (!str.contains(strA[index-1])) {
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
    
    private Set<String> getJobChainsWithoutDuplicates(List<JobChain____> jobChains) throws JocMissingRequiredParameterException {
        Set<String> set = new HashSet<String>();
        if (jobChains == null || jobChains.size() == 0) {
            return set;
        }
        for (JobChain____ jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("jobChain");
            }
            set.add(("/"+jobChain.getJobChain().trim()).replaceAll("//+", "/").replaceFirst("/$", ""));
        }
        return set;
    }
}
