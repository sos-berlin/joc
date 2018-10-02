package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.classes.audit.JobSchedulerCommandAudit;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingCommentException;
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
            boolean missingAuditLogComment = false;
            if (Globals.auditLogCommentsAreRequired) {
                if (jobSchedulerCommands.getComment() == null || jobSchedulerCommands.getComment().isEmpty()) {
                    missingAuditLogComment = true;
                }
            }
            if (jobSchedulerCommands.getUrl() == null || jobSchedulerCommands.getUrl().isEmpty()) {
                jobSchedulerCommands.setUrl(dbItemInventoryInstance.getUrl());
            }

            if (!jobschedulerUser.resetTimeOut()) {
                return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser));
            }
            
            JobSchedulerCommandFactory jobSchedulerCommandFactory = null;
            SOSPermissionCommands permissionCommands = getPermissonsCommands(jobSchedulerCommands.getJobschedulerId(), accessToken);
            if (permissionCommands == null) {
			    return accessDeniedResponse();
            }

            String xml = "";
            boolean withAudit = false;
            for (Object jobschedulerCommand : jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask()) {
                
                jobSchedulerCommandFactory = new JobSchedulerCommandFactory(jobschedulerCommand);
                if (!jobSchedulerCommandFactory.isPermitted(permissionCommands, folderPermissions)) {
                    if (jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask().size() == 1) {
                        return accessDeniedResponse();
                    } else {
                        LOGGER.warn("Command: Access denied for " + jobSchedulerCommandFactory.getCommandName());
                    }
                }
                
                if (!withAudit) {
                    withAudit = jobSchedulerCommandFactory.withAuditLog();
                }
                if (withAudit && missingAuditLogComment) {
                    throw new JocMissingCommentException();
                }
                
                String xmlCommand = jobSchedulerCommandFactory.asXml();
                xml = xml + xmlCommand;
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
