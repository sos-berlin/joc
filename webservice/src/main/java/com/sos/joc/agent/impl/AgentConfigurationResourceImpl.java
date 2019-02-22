package com.sos.joc.agent.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemSubmission;
import com.sos.jitl.reporting.db.DBItemSubmittedObject;
import com.sos.joc.Globals;
import com.sos.joc.agent.resource.IAgentConfigurationResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.submissions.SubmissionsDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.agent.AgentConfiguration;
import com.sos.joc.model.agent.AgentConfiguration200;
import com.sos.joc.model.agent.AgentConfigurationFilter;
import com.sos.joc.model.agent.ModifyAgent;

@Path("agent")
public class AgentConfigurationResourceImpl extends JOCResourceImpl implements IAgentConfigurationResource {

    private static final String API_CALL = "./agent/";
    private static final String FILE_EXTENSION = ".process_class.xml";

    @Override
    public JOCDefaultResponse readAgentConfiguration(String accessToken, AgentConfigurationFilter agentFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "read", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("path", agentFilter.getPath());

            AgentConfiguration200 entity = new AgentConfiguration200();
            entity.setJobschedulerId(agentFilter.getJobschedulerId());
            entity.setPath(normalizePath(agentFilter.getPath()));

            String xPath = String.format("/spooler/answer//process_classes/process_class[@path='%s']/source/process_class", entity.getPath());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String command = jocXmlCommand.getShowStateCommand("folder process_class", "folders no_subfolders source", getParent(entity.getPath()));
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
            Node sourceNode = jocXmlCommand.getSosxml().selectSingleNode(xPath);
            Element fileBased = (Element) jocXmlCommand.getSosxml().selectSingleNode(sourceNode.getParentNode().getParentNode(), "file_based");
            if (fileBased != null) {
                entity.setConfigurationDate(JobSchedulerDate.getDateFromISO8601String(fileBased.getAttribute("last_write_time")));
            }
            entity.setConfiguration(new XmlMapper().readValue(ConfigurationUtils.getSourceXmlBytes(sourceNode), AgentConfiguration.class));
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse saveAgentConfiguration(String accessToken, AgentConfiguration200 configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "save", configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(configuration.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("path", configuration.getPath());
            AgentConfiguration agent = configuration.getConfiguration();
            if (agent == null) {
                throw new JocMissingRequiredParameterException("undefined 'configuration'");
            }
            if (agent.getRemoteSchedulers() != null && agent.getRemoteSchedulers().isEmpty()) {
                agent.setRemoteSchedulers(null);
            }

            java.nio.file.Path path = Paths.get(configuration.getPath());
            agent.setName(path.getFileName().toString());

            ModifyAgent modifyAgent = new ModifyAgent();
            modifyAgent.setFolder(path.getParent().toString().replace('\\', '/'));
            modifyAgent.setProcessClass(agent);

            String command = new XmlMapper().writeValueAsString(modifyAgent).replaceAll("<remote_schedulers ", "<remote_scheduler ");

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

            updateAtOtherClusterMembers("save", command, configuration, agent);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse deleteAgentConfiguration(String accessToken, AgentConfigurationFilter agentFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "delete", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("path", agentFilter.getPath());
            String command = XMLBuilder.create("process_class.remove").addAttribute("process_class", agentFilter.getPath()).asXML();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

            deleteAtOtherClusterMembers("delete", command, agentFilter);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void deleteAtOtherClusterMembers(String identifier, String command, AgentConfigurationFilter agentFilter)
            throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        if (!"standalone".equals(dbItemInventoryInstance.getClusterType())) {
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(agentFilter.getJobschedulerId());
            dbItem.setPath(agentFilter.getPath() + FILE_EXTENSION);
            dbItem.setToDelete(true);
            updateOtherClusterMembers(identifier, command, dbItem);
        }
    }

    private void updateAtOtherClusterMembers(String identifier, String command, AgentConfiguration200 configuration, AgentConfiguration agent)
            throws JocConfigurationException, DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException, JsonProcessingException {
        if (!"standalone".equals(dbItemInventoryInstance.getClusterType())) {
            DBItemSubmittedObject dbItem = new DBItemSubmittedObject();
            dbItem.setSchedulerId(configuration.getJobschedulerId());
            dbItem.setPath(configuration.getPath() + FILE_EXTENSION);
            dbItem.setToDelete(false);
            agent.setName(null);
            ObjectMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
            String agentXml = xmlMapper.writeValueAsString(agent).replaceAll("<remote_schedulers ", "<remote_scheduler ");
            //dbItem.setContent("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + agentXml);
            dbItem.setContent(agentXml);
            updateOtherClusterMembers(identifier, command, dbItem);
        }
    }

    private void updateOtherClusterMembers(String identifier, String command, DBItemSubmittedObject dbItem) throws JocConfigurationException,
            DBOpenSessionException, DBInvalidDataException, DBConnectionRefusedException {
        // ask db for other cluster members
        SOSHibernateSession connection = null;
        JOCXmlCommand jocXmlCommand = null;
        boolean agentIsUpdated = false;
        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL + identifier);
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
                        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, getAccessToken());
                    } catch (Exception e) {
                        // if error then store agent conf in db for inventory plugin
                        if (!agentIsUpdated) {
                            dbItem = dbSubmissionsLayer.saveOrUpdateSubmittedObject(dbItem);
                            agentIsUpdated = true;
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
            if (agentIsUpdated) {
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
