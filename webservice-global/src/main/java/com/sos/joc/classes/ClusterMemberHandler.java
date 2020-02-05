package com.sos.joc.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private DBItemInventoryInstance dbItemInventoryInstance;
    private String apiCall;
    private Map<Long, Boolean> successfulConnections = new HashMap<Long, Boolean>();
    private List<HandlerCall> handlerCalls = new ArrayList<HandlerCall>();
    
    private class HandlerCall {
        private boolean isFolder = false;
        private String path;
        private String action;
        private DBItemSubmittedObject dbItem;
        
        public HandlerCall(String action, String path, boolean isFolder, DBItemSubmittedObject dbItem) {
            this.isFolder = isFolder;
            this.path = path;
            this.action = action;
            this.dbItem = dbItem;
        }
        
        public boolean getIsFolder() {
            return isFolder;
        }
        
        public String getAction() {
            return action;
        }
        
        public String getPath() {
            return path;
        }
        
        public DBItemSubmittedObject getDbItem() {
            return dbItem;
        }
    }

    public ClusterMemberHandler(DBItemInventoryInstance dbItemInventoryInstance, String apiCall) {
        this.dbItemInventoryInstance = dbItemInventoryInstance;
        this.apiCall = apiCall;
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
            //updateOtherClusterMembers("delete", dbItem, path, isFolder);
            handlerCalls.add(new HandlerCall("delete", path, isFolder, dbItem));
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
            //updateOtherClusterMembers("save", dbItem, path, isFolder);
            handlerCalls.add(new HandlerCall("save", path, isFolder, dbItem));
        }
    }
    
    public void executeHandlerCalls() throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        for (HandlerCall handlerCall : handlerCalls) {
            updateOtherClusterMembers(handlerCall.getAction(), handlerCall.getDbItem(), handlerCall.getPath(), handlerCall.getIsFolder());
        }
    }

    private void updateOtherClusterMembers(String sessionIdentifier, DBItemSubmittedObject dbItem, String path, boolean isFolder) throws JocConfigurationException,
            DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        // ask db for other cluster members
        SOSHibernateSession connection = null;
        JOCHotFolder httpClient = null;
        boolean isUpdated = false;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall + sessionIdentifier);
            InventoryInstancesDBLayer dbInstancesLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> clusterMembers = dbInstancesLayer.getInventoryInstancesBySchedulerId(dbItem.getSchedulerId());
            SubmissionsDBLayer dbSubmissionsLayer = new SubmissionsDBLayer(connection);
            DBItemSubmittedObject oldDbItem = dbSubmissionsLayer.getSubmittedObject(dbItem.getSchedulerId(), dbItem.getPath());
            List<DBItemSubmission> submissions = new ArrayList<DBItemSubmission>();
            if (oldDbItem != null) {
                dbItem.setId(oldDbItem.getId());
                submissions = dbSubmissionsLayer.getSubmissionsBySubmissionId(oldDbItem.getId());
            }
            if (clusterMembers != null) {
                for (DBItemInventoryInstance clusterMember : clusterMembers) {
                    if (dbItemInventoryInstance.getId() == clusterMember.getId()) {
                        // current JobScheduler is already updated (see above)
                        continue;
                    }
                    try {
                        clusterMember = Globals.jocConfigurationProperties.setUrlMapping(clusterMember);
                        successfulConnections.putIfAbsent(clusterMember.getId(), true);
                        if (!successfulConnections.get(clusterMember.getId())) {
                            throw new JobSchedulerConnectionRefusedException();
                        }
                        httpClient = new JOCHotFolder(clusterMember);
                        if ("save".equals(sessionIdentifier)) {
                            if (isFolder) {
                                httpClient.putFolder(path);
                            } else {
                                httpClient.putFile(path, dbItem.getContent());
                            }
                        } else {
                            if (isFolder) {
                                httpClient.deleteFolder(path);
                            } else {
                                httpClient.deleteFile(path);
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
        } finally {
            Globals.disconnect(connection);
        }
    }

}
