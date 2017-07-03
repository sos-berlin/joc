package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.classes.audit.JobSchedulerCommandAudit;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCommand;
import com.sos.joc.model.commands.JobschedulerCommands;

@Path("jobscheduler")
public class JobSchedulerResourceCommandImpl extends JOCResourceImpl implements IJobSchedulerResourceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceCommandImpl.class);
    private static final String API_CALL_COMMAND = "./jobscheduler/commands";

    @Override
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

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobSchedulerCommands.getUrl());
            jocXmlCommand.setBasicAuthorization(getBasicAuthorization());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            String xml = "";
            SOSShiroFolderPermissions sosShiroFolderPermissions = this.getJobschedulerUser().getSosShiroCurrentUser().getSosShiroFolderPermissions();
            for (Object jobschedulerCommand : jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask()) {

                xml = xml + jobSchedulerCommandFactory.getXml(jobschedulerCommand);
                if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(accessToken),sosShiroFolderPermissions)) {
                    if (jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask().size() == 1) {
                        return accessDeniedResponse();
                    } else {
                        LOGGER.warn("Command: Access denied");
                    }
                }

            }
            if (!xml.startsWith("<params.get") && ! xml.contains("param.get")){
                 xml = "<commands>" + xml + "</commands>";
            }

            JobSchedulerCommandAudit jobschedulerAudit = new JobSchedulerCommandAudit(xml, jobSchedulerCommands);
            logAuditMessage(jobschedulerAudit);
            String answer = jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
            storeAuditLogEntry(jobschedulerAudit);

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
