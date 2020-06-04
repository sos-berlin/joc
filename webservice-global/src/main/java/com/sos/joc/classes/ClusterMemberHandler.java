package com.sos.joc.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemSubmission;
import com.sos.jitl.reporting.db.DBItemSubmittedObject;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.submissions.SubmissionsDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;

public class ClusterMemberHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMemberHandler.class);
    private DBItemInventoryInstance dbItemInventoryInstance;
    private Map<Long, Boolean> successfulConnections = new HashMap<Long, Boolean>();
    private List<HandlerCall> handlerCalls = new ArrayList<HandlerCall>();
    
    private class HandlerCall {
        private boolean isFolder = false;
        private String action;
        private DBItemSubmittedObject dbItem;
        
        public HandlerCall(String action, boolean isFolder, DBItemSubmittedObject dbItem) {
            this.isFolder = isFolder;
            this.action = action;
            this.dbItem = dbItem;
        }
        
        public boolean getIsFolder() {
            return isFolder;
        }
        
        public String getAction() {
            return action;
        }
        
        public DBItemSubmittedObject getDbItem() {
            return dbItem;
        }
    }

    public ClusterMemberHandler(DBItemInventoryInstance dbItemInventoryInstance) {
        this.dbItemInventoryInstance = dbItemInventoryInstance;
    }
    
    public void deleteAtOtherClusterMembers(String path, boolean isFolder) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException,
            DBConnectionRefusedException {

        if (!dbItemInventoryInstance.standalone()) {
            if (isFolder) {
                path = (path + "/").replaceAll("//+", "/");
            }
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            dbItem.setPath(path);
            dbItem.setToDelete(true);
            handlerCalls.add(new HandlerCall("delete", isFolder, dbItem));
        }
    }

    public void updateAtOtherClusterMembers(String path, boolean isFolder, String content) throws JocConfigurationException,
            DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException, JsonProcessingException {

        if (!dbItemInventoryInstance.standalone()) {
            if (isFolder) {
                path = (path + "/").replaceAll("//+", "/");
            }
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            dbItem.setPath(path);
            dbItem.setToDelete(false);
            dbItem.setContent(content);
            handlerCalls.add(new HandlerCall("save", isFolder, dbItem));
        }
    }
    
    public void executeHandlerCalls(String apiCall) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            executeHandlerCalls(connection);
        } finally {
            Globals.disconnect(connection);
        }
    }
    
    public void executeHandlerCalls(SOSHibernateSession connection) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException,
            DBConnectionRefusedException {
        if (!handlerCalls.isEmpty()) {
            InventoryInstancesDBLayer dbInstancesLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> clusterMembers = dbInstancesLayer.getInventoryInstancesBySchedulerId(dbItemInventoryInstance
                    .getSchedulerId());
            Map<Long, JOCHotFolder> httpClientPerMember = null;
            if (clusterMembers != null) {
                httpClientPerMember = clusterMembers.stream().map(cm -> {
                    if (Globals.jocConfigurationProperties != null) {
                        return Globals.jocConfigurationProperties.setUrlMapping(cm);
                    } else {
                        return cm;
                    }
                }).collect(Collectors.toMap(DBItemInventoryInstance::getId, cm -> new JOCHotFolder(cm)));
            }
            SubmissionsDBLayer dbSubmissionsLayer = new SubmissionsDBLayer(connection);
            for (HandlerCall handlerCall : handlerCalls) {
                updateOtherClusterMembers(handlerCall, httpClientPerMember, dbSubmissionsLayer);
            }
            if (httpClientPerMember != null) {
                httpClientPerMember.values().stream().forEach(httpCLient -> httpCLient.closeHttpClient());
            }
        }
    }

    private void updateOtherClusterMembers(HandlerCall handlerCall, Map<Long, JOCHotFolder> httpClientPerMember,
            SubmissionsDBLayer dbSubmissionsLayer) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException,
            DBConnectionRefusedException {
        // ask db for other cluster members
        // JOCHotFolder httpClient = null;
        boolean isUpdated = false;
        DBItemSubmittedObject dbItem = handlerCall.getDbItem();
        DBItemSubmittedObject oldDbItem = dbSubmissionsLayer.getSubmittedObject(dbItem.getSchedulerId(), dbItem.getPath());
        List<DBItemSubmission> submissions = new ArrayList<DBItemSubmission>();
        if (oldDbItem != null) {
            dbItem.setId(oldDbItem.getId());
            submissions = dbSubmissionsLayer.getSubmissionsBySubmissionId(oldDbItem.getId());
        }
        if (httpClientPerMember != null) {
            for (Entry<Long, JOCHotFolder> clusterMember : httpClientPerMember.entrySet()) {
                if (dbItemInventoryInstance.getId().equals(clusterMember.getKey())) {
                    // current JobScheduler is already updated
                    continue;
                }
                try {
//                    if (Globals.jocConfigurationProperties != null) {
//                        clusterMember = Globals.jocConfigurationProperties.setUrlMapping(clusterMember);
//                    }
                    successfulConnections.putIfAbsent(clusterMember.getKey(), true);
                    if (!successfulConnections.get(clusterMember.getKey())) {
                        throw new JobSchedulerConnectionRefusedException();
                    }
                    //httpClient = new JOCHotFolder(clusterMember);
                    clusterMember.getValue().setAutoCloseHttpClient(false);
                    if ("save".equals(handlerCall.getAction())) {
                        if (handlerCall.getIsFolder()) {
                            clusterMember.getValue().putFolder(dbItem.getPath());
                        } else {
                            clusterMember.getValue().putFile(dbItem.getPath(), dbItem.getContent());
                        }
                    } else {
                        if (handlerCall.getIsFolder()) {
                            clusterMember.getValue().deleteFolder(dbItem.getPath());
                        } else {
                            clusterMember.getValue().deleteFile(dbItem.getPath());
                        }
                    }
                } catch (JobSchedulerObjectNotExistException e) {
                    //
                } catch (JobSchedulerConnectionResetException | JobSchedulerConnectionRefusedException e) {
                    successfulConnections.put(clusterMember.getKey(), false);
                    // if error then store object conf in db for inventory plugin
                    if (!isUpdated) {
                        dbItem = dbSubmissionsLayer.saveOrUpdateSubmittedObject(dbItem);
                        isUpdated = true;
                    }
                    DBItemSubmission dbItemSubmission = new DBItemSubmission();
                    dbItemSubmission.setInstanceId(clusterMember.getKey());
                    dbItemSubmission.setSubmissionId(dbItem.getId());
                    if (!submissions.remove(dbItemSubmission)) {
                        dbItemSubmission = dbSubmissionsLayer.saveSubmission(dbItemSubmission);
                    }
                } catch (Exception e) {
                    LOGGER.warn("", e);
                    // if error then store object conf in db for inventory plugin
                    if (!isUpdated) {
                        dbItem = dbSubmissionsLayer.saveOrUpdateSubmittedObject(dbItem);
                        isUpdated = true;
                    }
                    DBItemSubmission dbItemSubmission = new DBItemSubmission();
                    dbItemSubmission.setInstanceId(clusterMember.getKey());
                    dbItemSubmission.setSubmissionId(dbItem.getId());
                    if (!submissions.remove(dbItemSubmission)) {
                        dbItemSubmission = dbSubmissionsLayer.saveSubmission(dbItemSubmission);
                    }
                }
            }
        }
        if (isUpdated) {
            for (DBItemSubmission submission : submissions) {
                dbSubmissionsLayer.deleteSubmission(submission);
            }
        } else if (oldDbItem != null) {
            dbSubmissionsLayer.deleteSubmittedObject(oldDbItem);
            for (DBItemSubmission submission : submissions) {
                dbSubmissionsLayer.deleteSubmission(submission);
            }
        }
    }

}
