package com.sos.joc.joe.processclass.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.classes.ClusterMemberHandler;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JoeConfigurationHandler;
import com.sos.joc.classes.JoeConfigurationHandlerReturn;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyProcessClassAudit;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.joe.processclass.resource.IAgentConfigurationResource;
import com.sos.joc.model.joe.processClass.ProcessClassConfiguration;
import com.sos.joc.model.joe.processClass.ProcessClassConfigurationEdit;
import com.sos.joc.model.joe.processClass.ProcessClassModify;
import com.sos.joc.model.processClass.ProcessClassConfigurationFilter;

@Path("process_class")
public class AgentConfigurationResourceImpl extends JOCResourceImpl implements IAgentConfigurationResource {

    private static final String API_CALL = "./process_class/";
    private static final String FILE_EXTENSION = ".process_class.xml";

    @Override
    public JOCDefaultResponse readAgentConfiguration(String accessToken, ProcessClassConfigurationFilter agentFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "read", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("processClass", agentFilter.getProcessClass());
            JoeConfigurationHandler joeConfigurationHandler = new JoeConfigurationHandler();
            ProcessClassConfigurationEdit entity = new ProcessClassConfigurationEdit();
            entity.setProcessClass(agentFilter.getProcessClass());

            String xPath = String.format("/spooler/answer//process_classes/process_class[@path='%s']/source/process_class", entity.getProcessClass());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String command = jocXmlCommand.getShowStateCommand("folder process_class", "folders no_subfolders source", this.getParent(entity
                    .getProcessClass()));

            JoeConfigurationHandlerReturn joeConfiguationHandlerReturn = joeConfigurationHandler.read(xPath, command, this);
            entity.setConfigurationDate(joeConfiguationHandlerReturn.getLastWrite());
            entity.setDeliveryDate(Date.from(Instant.now()));

            XmlMapper xmlMapper = joeConfigurationHandler.getXmlMapper();
            entity.setConfiguration(xmlMapper.readValue(joeConfigurationHandler.getXmlConfiguration(joeConfiguationHandlerReturn), ProcessClassConfiguration.class));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse saveAgentConfiguration(String accessToken, ProcessClassConfigurationEdit configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "save", configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(configuration.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("processClass", configuration.getProcessClass());
            ProcessClassConfiguration agent = configuration.getConfiguration();
            if (agent == null) {
                throw new JocMissingRequiredParameterException("undefined 'configuration'");
            }
            if (agent.getRemoteSchedulers() != null && (agent.getRemoteSchedulers().getList() == null || agent.getRemoteSchedulers().getList() != null
                    && agent.getRemoteSchedulers().getList().isEmpty())) {
                agent.setRemoteSchedulers(null);
            }

            java.nio.file.Path path = Paths.get(configuration.getProcessClass());
            agent.setName(path.getFileName().toString());

            ProcessClassModify modifyAgent = new ProcessClassModify();
            modifyAgent.setFolder(path.getParent().toString().replace('\\', '/'));
            modifyAgent.setProcessClass(agent);

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(configuration);
            logAuditMessage(audit);

            String command = new XmlMapper().writeValueAsString(modifyAgent);

            JoeConfigurationHandler joeConfigurationHandler = new JoeConfigurationHandler();
            Date surveyDate = joeConfigurationHandler.executeCommand(command, this);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, accessToken, configuration
                    .getProcessClass(), FILE_EXTENSION, API_CALL);
            agent.setName(null);
            clusterMemberHandler.updateAtOtherClusterMembers(command, agent);

            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse deleteAgentConfiguration(String accessToken, ProcessClassConfigurationEdit agentFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "delete", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("processClass", agentFilter.getProcessClass());
            String command = XMLBuilder.create("process_class.remove").addAttribute("process_class", agentFilter.getProcessClass()).asXML();

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(agentFilter);
            logAuditMessage(audit);

            JoeConfigurationHandler joeConfigurationHandler = new JoeConfigurationHandler();
            Date surveyDate = joeConfigurationHandler.executeCommand(command, this);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, accessToken, agentFilter.getProcessClass(),
                    FILE_EXTENSION, API_CALL);
            clusterMemberHandler.deleteAtOtherClusterMembers(command);

            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
