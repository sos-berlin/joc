package com.sos.joc.joe.processclass.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.ClusterMemberHandler;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyProcessClassAudit;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.joe.common.XmlDeserializer;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.joe.processclass.resource.IAgentConfigurationResource;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.processclass.ProcessClassEdit;
import com.sos.joc.model.processClass.ConfigurationEdit;
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
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }
            
            checkRequiredParameter("processClass", agentFilter.getProcessClass());

            JOCHotFolder httpClient = new JOCHotFolder(this);
            byte[] fileContent = httpClient.getFile(agentFilter.getProcessClass() + FILE_EXTENSION);

            ProcessClassEdit entity = new ProcessClassEdit();
            entity.setPath(agentFilter.getProcessClass());
            entity.setConfiguration(XmlDeserializer.deserialize(fileContent, ProcessClass.class));
            entity.setConfigurationDate(httpClient.getLastModifiedDate());
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(entity));

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse saveAgentConfiguration(String accessToken, ConfigurationEdit configuration) throws Exception {
        try {
            //ProcessClassEdit configuration = Globals.objectMapper.readValue(bytes, ProcessClassEdit.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "save", configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(configuration.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }
            
            //checkRequiredParameter("processClass", configuration.getPath());
            checkRequiredParameter("processClass", configuration.getProcessClass());
            ProcessClass agent = configuration.getConfiguration();
            if (agent == null) {
                throw new JocMissingRequiredParameterException("undefined 'configuration'");
            }

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(configuration);
            logAuditMessage(audit);

            String processClassPath = configuration.getProcessClass() + FILE_EXTENSION;
            JOCHotFolder httpClient = new JOCHotFolder(this);
            String xmlContent = XmlSerializer.serializeToStringWithHeader(XmlSerializer.serializeProcessClass(agent));

            httpClient.putFile(processClassPath, xmlContent);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, API_CALL);
            clusterMemberHandler.updateAtOtherClusterMembers(processClassPath, false, xmlContent);

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse deleteAgentConfiguration(String accessToken, ConfigurationEdit agentFilter) throws Exception {
        try {
            //ProcessClassEdit agentFilter = Globals.objectMapper.readValue(bytes, ProcessClassEdit.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "delete", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }
            //checkRequiredParameter("processClass", agentFilter.getPath());
            checkRequiredParameter("processClass", agentFilter.getProcessClass());

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(agentFilter);
            logAuditMessage(audit);

            JOCHotFolder httpClient = new JOCHotFolder(this);
            String processClassPath = agentFilter.getProcessClass() + FILE_EXTENSION;
            httpClient.deleteFile(processClassPath);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, API_CALL);
            clusterMemberHandler.deleteAtOtherClusterMembers(processClassPath, false);
            clusterMemberHandler.executeHandlerCalls();

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
