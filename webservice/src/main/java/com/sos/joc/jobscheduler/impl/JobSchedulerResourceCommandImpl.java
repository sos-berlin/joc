package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCommand;
import com.sos.joc.model.commands.JobschedulerCommand;
import com.sos.joc.model.commands.JobschedulerCommands;

@Path("jobscheduler")
public class JobSchedulerResourceCommandImpl extends JOCResourceImpl implements IJobSchedulerResourceCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceCommandImpl.class);
    private static final String API_CALL_COMMAND = "./jobscheduler/command";
    private static final String API_CALL_COMMANDS = "./jobscheduler/commands";

    @Override
    public JOCDefaultResponse postJobschedulerCommand(String accessToken, JobschedulerCommand jobschedulerCommand) {

        try {
            initLogging(API_CALL_COMMAND, jobschedulerCommand.getJobschedulerId());
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerCommand.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobschedulerCommand.getJobschedulerId());
            if ("".equals(jobschedulerCommand.getUrl()) || jobschedulerCommand.getUrl() == null) {
                jobschedulerCommand.setUrl(dbItemInventoryInstance.getUrl());
            }

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() > 1) {
                String message = String.format("There are %s commands specified. Only one command is allowed", jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size());
                throw new JobSchedulerBadRequestException(message);
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobschedulerCommand.getUrl());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() < 1) {
                throw new JobSchedulerBadRequestException("Unknown command");
            }

            String xml = jobSchedulerCommandFactory.getXml(jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().get(0));
            if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(accessToken))) {
                return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
            }

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

    @Override
    public JOCDefaultResponse postJobschedulerCommands(String accessToken, JobschedulerCommands jobSchedulerCommands) throws Exception {

        try {
            initLogging(API_CALL_COMMANDS, jobSchedulerCommands.getJobschedulerId());
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerCommands.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobSchedulerCommands.getJobschedulerId());
            if ("".equals(jobSchedulerCommands.getUrl()) || jobSchedulerCommands.getUrl() == null) {
                jobSchedulerCommands.setUrl(dbItemInventoryInstance.getUrl());
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobSchedulerCommands.getUrl());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            String xml = "";
            for (JobschedulerCommand jobschedulerCommand : jobSchedulerCommands.getJobschedulerCommand()) {

                if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() < 1) {
                   // throw new JobSchedulerBadRequestException("Unknown command");
                    LOGGER.warn("Unknown command");
                }

                xml = xml + jobSchedulerCommandFactory.getXml(jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().get(0));
                if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(accessToken))) {
                    LOGGER.warn("Command: Access denied");
//                    return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
                }

            }
            xml = "<commands>" + xml + "</commands>";
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
