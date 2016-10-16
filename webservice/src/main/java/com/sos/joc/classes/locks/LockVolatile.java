package com.sos.joc.classes.locks;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.lock.LockHolder;
import com.sos.joc.model.lock.LockHolders;
import com.sos.joc.model.lock.LockV;
import com.sos.joc.model.lock.Queue;


public class LockVolatile extends LockV {
    private Element lock;
    private final JOCXmlCommand jocXmlCommand;

    public LockVolatile(Element lock, JOCXmlCommand jocXmlCommand) {
        this.lock = lock;
        this.jocXmlCommand = jocXmlCommand;
    }
    
    public void setFields() throws Exception {
        setPath(lock.getAttribute(WebserviceConstants.PATH));
        setName(lock.getAttribute(WebserviceConstants.NAME));
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(lock));
        String max_non_exclusive = jocXmlCommand.getAttributeValue(lock, "max_non_exclusive", null);
        if (max_non_exclusive != null) {
            setMaxNonExclusive(Integer.valueOf(max_non_exclusive));
        }
        setHolders(getLockHolders());
        setQueue(getLockQueueList());
    }

    private List<Queue> getLockQueueList() throws Exception {
        NodeList nonExclusiveLockQueueNodes = jocXmlCommand.getSosxml().selectNodeList(lock, "lock.queue[not(@exclusive='no')]/lock.queue.entry");
        NodeList exclusiveLockQueueNodes = jocXmlCommand.getSosxml().selectNodeList(lock, "lock.queue[@exclusive='no']/lock.queue.entry");
        if (nonExclusiveLockQueueNodes == null && exclusiveLockQueueNodes == null 
                || nonExclusiveLockQueueNodes.getLength() + exclusiveLockQueueNodes.getLength() == 0) {
            return null; 
        } else {
            List<Queue> lockQueueList = new ArrayList<Queue>();
            if (nonExclusiveLockQueueNodes != null) {
                for (int j=0; j < nonExclusiveLockQueueNodes.getLength(); j++) {
                    Element lockQueueEntry = (Element) nonExclusiveLockQueueNodes.item(j);
                    lockQueueList.add(getLockQueue(lockQueueEntry, false));
                }
            }
            if (exclusiveLockQueueNodes != null) {
                for (int j=0; j < exclusiveLockQueueNodes.getLength(); j++) {
                    Element lockQueueEntry = (Element) exclusiveLockQueueNodes.item(j);
                    lockQueueList.add(getLockQueue(lockQueueEntry, true));
                }
            }
            return lockQueueList;
        }
    }

    private Queue getLockQueue(Element lockQueueEntry, boolean exclusive) {
        Queue lockQueue = new Queue();
        lockQueue.setExclusive(exclusive);
        lockQueue.setJob(lockQueueEntry.getAttribute(WebserviceConstants.JOB));
        return lockQueue;
    }

    private LockHolders getLockHolders() throws Exception {
        Element lockHoldersElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(lock, "lock.holders");
        if (lockHoldersElem == null) {
            return null; 
        } else {
            LockHolders lockHolders = new LockHolders();
            lockHolders.setExclusive(jocXmlCommand.getBoolValue(lockHoldersElem.getAttribute(WebserviceConstants.EXCLUSIVE),true));
            lockHolders.setTasks(getLockHolderList(lockHoldersElem));
            return lockHolders;
        }
    }

    private List<LockHolder> getLockHolderList(Element lockHoldersElem) throws Exception {
        NodeList lockHolderNodes = jocXmlCommand.getSosxml().selectNodeList(lockHoldersElem, "lock.holder");
        if (lockHolderNodes == null || lockHolderNodes.getLength() == 0) {
            return null; 
        } else {
            List<LockHolder> lockHolderList = new ArrayList<LockHolder>();
            for (int j=0; j < lockHolderNodes.getLength(); j++) {
                Element lockHolderNodesItem = (Element) lockHolderNodes.item(j);
                lockHolderList.add(getLockHolder(lockHolderNodesItem));
            }
            return lockHolderList;
        }
    }

    private LockHolder getLockHolder(Element lockHolderNodesItem) {
        LockHolder lockHolder = new LockHolder();
        lockHolder.setJob(lockHolderNodesItem.getAttribute(WebserviceConstants.JOB));
        lockHolder.setTaskId(lockHolderNodesItem.getAttribute("task_id"));
        return lockHolder;
    }

}
