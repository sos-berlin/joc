package com.sos.joc.classes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.joe.processClass.ProcessClassConfiguration;

public class ClusterMemberHandler {

    private DBItemInventoryInstance dbItemInventoryInstance;
    private String identifier;
    private String accessToken;
    private String fileExtension;
    private String apiCall;

    public ClusterMemberHandler(DBItemInventoryInstance dbItemInventoryInstance, String accessToken, String identifier, String fileExtension,
            String apiCall) {
        super();
        this.dbItemInventoryInstance = dbItemInventoryInstance;
        this.accessToken = accessToken;
        this.identifier = identifier;
        this.fileExtension = fileExtension;
        this.apiCall = apiCall;
    }

    public void deleteAtOtherClusterMembers(String command) throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException,
            DBConnectionRefusedException {

        if (!dbItemInventoryInstance.standalone()) {
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            dbItem.setPath(this.identifier + fileExtension);
            dbItem.setToDelete(true);
            updateOtherClusterMembers("delete", command, dbItem);
        }
    }

    public void updateAtOtherClusterMembers(String command, ProcessClassConfiguration jsObject) throws JocConfigurationException,
            DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException, JsonProcessingException {

        if (!dbItemInventoryInstance.standalone()) {
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            dbItem.setPath(this.identifier + fileExtension);
            dbItem.setToDelete(false);
            ObjectMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
            String jsObjectXml = xmlMapper.writeValueAsString(jsObject);
            dbItem.setContent(jsObjectXml);
            updateOtherClusterMembers("save", command, dbItem);
        }
    }

    private void updateOtherClusterMembers(String sessionIdentifier, String command, DBItemSubmittedObject dbItem) throws JocConfigurationException,
            DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        // ask db for other cluster members
        SOSHibernateSession connection = null;
        JOCXmlCommand jocXmlCommand = null;
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
                        jocXmlCommand = new JOCXmlCommand(clusterMember);
                        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
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
