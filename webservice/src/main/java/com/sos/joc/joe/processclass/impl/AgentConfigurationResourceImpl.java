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
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.joe.processclass.resource.IAgentConfigurationResource;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.processclass.ProcessClassEdit;
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

            JOCHotFolder httpClient = new JOCHotFolder(this);
            byte[] fileContent = httpClient.getFile(agentFilter.getProcessClass() + FILE_EXTENSION);

            ProcessClassEdit entity = new ProcessClassEdit();
            entity.setPath(agentFilter.getProcessClass());
            entity.setConfiguration(Globals.xmlMapper.readValue(fileContent, ProcessClass.class));
            entity.setConfigurationDate(httpClient.getLastModifiedDate());
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
    public JOCDefaultResponse saveAgentConfiguration(String accessToken, ProcessClassEdit configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "save", configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(configuration.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("processClass", configuration.getPath());
            ProcessClass agent = configuration.getConfiguration();
            if (agent == null) {
                throw new JocMissingRequiredParameterException("undefined 'configuration'");
            }
            if (agent.getRemoteSchedulers() != null && (agent.getRemoteSchedulers().getRemoteSchedulerList() == null || agent.getRemoteSchedulers()
                    .getRemoteSchedulerList() != null && agent.getRemoteSchedulers().getRemoteSchedulerList().isEmpty())) {
                agent.setRemoteSchedulers(null);
            }

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(configuration);
            logAuditMessage(audit);

            String processClassPath = configuration.getPath() + FILE_EXTENSION;
            JOCHotFolder httpClient = new JOCHotFolder(this);
            String xmlContent = writeXmlPoJoAsString(agent);
            
            httpClient.putFile(processClassPath, xmlContent);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, processClassPath, API_CALL);
            clusterMemberHandler.updateAtOtherClusterMembers(xmlContent);

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse deleteAgentConfiguration(String accessToken, ProcessClassEdit agentFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "delete", agentFilter, accessToken, agentFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken).getProcessClass().getChange().isHotFolder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("processClass", agentFilter.getPath());

            ModifyProcessClassAudit audit = new ModifyProcessClassAudit(agentFilter);
            logAuditMessage(audit);

            JOCHotFolder httpClient = new JOCHotFolder(this);
            String processClassPath = agentFilter.getPath() + FILE_EXTENSION;
            httpClient.delete(processClassPath);

            storeAuditLogEntry(audit);

            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, processClassPath, API_CALL);
            clusterMemberHandler.deleteAtOtherClusterMembers();

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
