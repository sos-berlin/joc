package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.classes.audit.JobSchedulerCommandAudit;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCommand;
import com.sos.joc.model.commands.JobschedulerCommands;
import com.sos.xml.SOSXmlCommand.ResponseStream;

@Path("jobscheduler")
public class JobSchedulerResourceCommandImpl extends JOCResourceImpl implements IJobSchedulerResourceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceCommandImpl.class);
    private static final String API_CALL_COMMAND = "./jobscheduler/commands";

    @Override
    public JOCDefaultResponse postJobschedulerCommands(String xAccessToken, String accessToken, JobschedulerCommands jobSchedulerCommands)
            throws Exception {
        return postJobschedulerCommands(getAccessToken(xAccessToken, accessToken), jobSchedulerCommands);
    }

    public JOCDefaultResponse postJobschedulerCommands(String accessToken, JobschedulerCommands jobSchedulerCommands) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_COMMAND, jobSchedulerCommands, accessToken, jobSchedulerCommands
                    .getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobSchedulerCommands.getJobschedulerId());
            checkRequiredComment(jobSchedulerCommands.getComment());
            if (jobSchedulerCommands.getUrl() == null || jobSchedulerCommands.getUrl().isEmpty()) {
                jobSchedulerCommands.setUrl(dbItemInventoryInstance.getUrl());
            }

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            if (!jobschedulerUser.resetTimeOut()) {
                return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser));
            }

            if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(jobSchedulerCommands.getJobschedulerId(), accessToken),
                    folderPermissions)) {
                if (jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask().size() == 1) {
                    return accessDeniedResponse();
                } else {
                    LOGGER.warn("Command: Access denied");
                }
            }

            String xml = "";
            boolean withAudit = false;
            for (Object jobschedulerCommand : jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask()) {
                String xmlCommand = jobSchedulerCommandFactory.getXml(jobschedulerCommand);
                xml = xml + xmlCommand;
                if (!withAudit && !xmlCommand.matches("^<(show_|params?\\.get|job\\.why|scheduler_log).*")) {
                    withAudit = true;
                }
            }
            if (!xml.startsWith("<params.get") && !xml.contains("param.get")) {
                xml = "<commands>" + xml + "</commands>";
            }
            JobSchedulerCommandAudit jobschedulerAudit = null;
            if (withAudit) {
                jobschedulerAudit = new JobSchedulerCommandAudit(xml, jobSchedulerCommands);
            }
            if (jobschedulerAudit != null) {
                logAuditMessage(jobschedulerAudit);
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobSchedulerCommands.getUrl());
            jocXmlCommand.setBasicAuthorization(getBasicAuthorization());
            String answer = jocXmlCommand.executePost(xml, ResponseStream.TO_STRING, getAccessToken());
            if (jobschedulerAudit != null) {
                storeAuditLogEntry(jobschedulerAudit);
            }

            return JOCDefaultResponse.responseStatus200(answer, "application/xml");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
        }

    }

}
