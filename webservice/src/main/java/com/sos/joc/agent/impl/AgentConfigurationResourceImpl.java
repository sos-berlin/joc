package com.sos.joc.agent.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.agent.resource.IAgentConfigurationResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.agent.AgentConfiguration;
import com.sos.joc.model.agent.AgentConfiguration200;
import com.sos.joc.model.agent.AgentConfigurationFilter;
import com.sos.joc.model.agent.ModifyAgent;

@Path("agent")
public class AgentConfigurationResourceImpl extends JOCResourceImpl implements IAgentConfigurationResource {

    private static final String API_CALL = "./agent/";

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
        } catch (Exception e) {
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

            java.nio.file.Path path = Paths.get(configuration.getPath());
            agent.setName(path.getFileName().toString());
            
            ModifyAgent modifyAgent = new ModifyAgent();
            modifyAgent.setFolder(path.getParent().toString().replace('\\', '/'));
            modifyAgent.setProcessClass(agent);

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(new XmlMapper().writeValueAsString(modifyAgent), accessToken);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
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
            
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
