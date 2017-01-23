package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
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
            initLogging(API_CALL_COMMAND, jobSchedulerCommands.getJobschedulerId());
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerCommands.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobSchedulerCommands.getJobschedulerId());
            if ("".equals(jobSchedulerCommands.getUrl()) || jobSchedulerCommands.getUrl() == null) {
                jobSchedulerCommands.setUrl(dbItemInventoryInstance.getUrl());
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobSchedulerCommands.getUrl());
            jocXmlCommand.setBasicAuthorization(getBasicAuthorization());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            String xml = "";
            for (Object jobschedulerCommand : jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask()) {

                xml = xml + jobSchedulerCommandFactory.getXml(jobschedulerCommand);
                if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(accessToken))) {
                    if (jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask().size() == 1) {
                        return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
                    } else {
                        LOGGER.warn("Command: Access denied");
                    }
                }

            }
            if (jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask().size() > 1) {
                xml = "<commands>" + xml + "</commands>";
            }
            logAuditMessage(jobSchedulerCommands);
            String answer = jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());

            return JOCDefaultResponse.responseStatus200(answer, "application/xml");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }

    }
 

}
