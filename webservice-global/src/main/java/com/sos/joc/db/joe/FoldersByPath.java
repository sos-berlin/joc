package com.sos.joc.db.joe;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.lock.Lock;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.other.FolderItem;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.schedule.Schedule;
import com.sos.joc.model.tree.JoeTree;

public class FoldersByPath extends JoeTree {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FoldersByPath.class);
    
    public FoldersByPath(String path, String objectType, String operation) {
        if ("FOLDER".equals(objectType)) {
            setPath(path);
        } else {
            setPath(Paths.get(path).getParent().toString().replace('\\', '/'));
        }
        if ("delete".equals(operation)) {
            setDeleted(true);
        }
        setFolders(null);
    }
    
    public FoldersByPath(DBItemJoeObject dbItem) {
        if ("FOLDER".equals(dbItem.getObjectType())) {
            setPath(dbItem.getPath());
            setName("FOLDER");
            if (dbItem.operationIsDelete()) {
              setDeleted(true);
          }
        } else {
            setPath(Paths.get(dbItem.getPath()).getParent().toString().replace('\\', '/'));
            FolderItem folderItem = joeObjectToFolderItemMapper.apply(dbItem);
            Set<FolderItem> folderItemSet = new HashSet<FolderItem>();
            folderItemSet.add(folderItem);
            switch (dbItem.getObjectType()) {
            case "JOB":
                setJobs(folderItemSet);
                break;
            case "JOBCHAIN":
                setJobChains(folderItemSet);
                break;
            case "ORDER":
                setOrders(folderItemSet);
                break;
            case "AGENTCLUSTER":
                setAgentClusters(folderItemSet);
                break;
            case "PROCESSCLASS":
                setProcessClasses(folderItemSet);
                break;
            case "LOCK":
                setLocks(folderItemSet);
                break;
            case "SCHEDULE":
                setSchedules(folderItemSet);
                break;
            case "MONITOR":
                setMonitors(folderItemSet);
                break;
            case "NODEPARAMS":
                setNodeParams(folderItemSet);
                break;
            }
        }
        setFolders(null);
    }
    
    public static Function<DBItemJoeObject, FolderItem> joeObjectToFolderItemMapper = item -> {
        FolderItem folderItem = new FolderItem();
        folderItem.setName(Paths.get(item.getPath()).getFileName().toString());
        folderItem.setDeployed(false);
        folderItem.setDeleted(item.operationIsDelete());
        if (item.getConfiguration() != null) {
            try {
                switch (item.getObjectType()) {
                case "JOB":
                    Job job = Globals.objectMapper.readValue(item.getConfiguration(), Job.class);
                    folderItem.setTitle(job.getTitle());
                    folderItem.setProcessClass(job.getProcessClass());
                    folderItem.setIsOrderJob(job.getIsOrderJob() != null && "true,1,yes".contains(job.getIsOrderJob()));
                    break;
                case "JOBCHAIN":
                    JobChain jobChain = Globals.objectMapper.readValue(item.getConfiguration(), JobChain.class);
                    folderItem.setTitle(jobChain.getTitle());
                    folderItem.setProcessClass(jobChain.getProcessClass());
                    break;
                case "ORDER":
                    Order order = Globals.objectMapper.readValue(item.getConfiguration(), Order.class);
                    folderItem.setTitle(order.getTitle());
                    folderItem.setInitialState(order.getInitialState());
                    folderItem.setEndState(order.getEndState());
                    folderItem.setPriority(order.getPriority());
                    break;
                case "AGENTCLUSTER":
                case "PROCESSCLASS":
                    ProcessClass processClass = Globals.objectMapper.readValue(item.getConfiguration(), ProcessClass.class);
                    folderItem.setMaxProcesses(processClass.getMaxProcesses());
                    break;
                case "LOCK":
                    Lock lock = Globals.objectMapper.readValue(item.getConfiguration(), Lock.class);
                    folderItem.setMaxNonExclusive(lock.getMaxNonExclusive());
                    break;
                case "SCHEDULE":
                    Schedule schedule = Globals.objectMapper.readValue(item.getConfiguration(), Schedule.class);
                    folderItem.setTitle(schedule.getTitle());
                    folderItem.setSubstitute(schedule.getSubstitute());
                    folderItem.setValidFrom(schedule.getValidFrom());
                    folderItem.setValidTo(schedule.getValidTo());
                    break;
                }
            } catch (Exception e) {
                LOGGER.warn("", e);
            }
        }
        return folderItem;
    };
}
