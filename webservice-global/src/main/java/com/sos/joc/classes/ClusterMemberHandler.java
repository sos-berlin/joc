package com.sos.joc.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        InventoryInstancesDBLayer dbInstancesLayer = new InventoryInstancesDBLayer(connection);
        List<DBItemInventoryInstance> clusterMembers = dbInstancesLayer.getInventoryInstancesBySchedulerId(dbItemInventoryInstance.getSchedulerId());
        SubmissionsDBLayer dbSubmissionsLayer = new SubmissionsDBLayer(connection);
        for (HandlerCall handlerCall : handlerCalls) {
            updateOtherClusterMembers(handlerCall, clusterMembers, dbSubmissionsLayer);
        }
    }

    private void updateOtherClusterMembers(HandlerCall handlerCall, List<DBItemInventoryInstance> clusterMembers,
            SubmissionsDBLayer dbSubmissionsLayer) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException,
            DBConnectionRefusedException {
        // ask db for other cluster members
        JOCHotFolder httpClient = null;
        boolean isUpdated = false;
        DBItemSubmittedObject dbItem = handlerCall.getDbItem();
        DBItemSubmittedObject oldDbItem = dbSubmissionsLayer.getSubmittedObject(dbItem.getSchedulerId(), dbItem.getPath());
        List<DBItemSubmission> submissions = new ArrayList<DBItemSubmission>();
        if (oldDbItem != null) {
            dbItem.setId(oldDbItem.getId());
            submissions = dbSubmissionsLayer.getSubmissionsBySubmissionId(oldDbItem.getId());
        }
        if (clusterMembers != null) {
            for (DBItemInventoryInstance clusterMember : clusterMembers) {
                if (dbItemInventoryInstance.getId().equals(clusterMember.getId())) {
                    // current JobScheduler is already updated
                    continue;
                }
                try {
                    if (Globals.jocConfigurationProperties != null) {
                        clusterMember = Globals.jocConfigurationProperties.setUrlMapping(clusterMember);
                    }
                    successfulConnections.putIfAbsent(clusterMember.getId(), true);
                    if (!successfulConnections.get(clusterMember.getId())) {
                        throw new JobSchedulerConnectionRefusedException();
                    }
                    httpClient = new JOCHotFolder(clusterMember);
                    if ("save".equals(handlerCall.getAction())) {
                        if (handlerCall.getIsFolder()) {
                            httpClient.putFolder(dbItem.getPath());
                        } else {
                            httpClient.putFile(dbItem.getPath(), dbItem.getContent());
                        }
                    } else {
                        if (handlerCall.getIsFolder()) {
                            httpClient.deleteFolder(dbItem.getPath());
                        } else {
                            httpClient.deleteFile(dbItem.getPath());
                        }
                    }
                } catch (JobSchedulerObjectNotExistException e) {
                    //
                } catch (JobSchedulerConnectionResetException | JobSchedulerConnectionRefusedException e) {
                    successfulConnections.put(clusterMember.getId(), false);
                    // if error then store object conf in db for inventory plugin
                    if (!isUpdated) {
                        dbItem = dbSubmissionsLayer.saveOrUpdateSubmittedObject(dbItem);
                        isUpdated = true;
                    }
                    DBItemSubmission dbItemSubmission = new DBItemSubmission();
                    dbItemSubmission.setInstanceId(clusterMember.getId());
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
                    dbItemSubmission.setInstanceId(clusterMember.getId());
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
