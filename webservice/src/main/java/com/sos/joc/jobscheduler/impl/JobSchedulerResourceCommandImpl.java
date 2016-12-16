package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCommand;
import com.sos.joc.model.commands.JobschedulerCommand;

@Path("jobscheduler")
public class JobSchedulerResourceCommandImpl extends JOCResourceImpl implements IJobSchedulerResourceCommand {

    private static final String API_CALL = "./jobscheduler/command";

    @Override
    public JOCDefaultResponse postJobschedulerCommand(String accessToken, JobschedulerCommand jobschedulerCommand) {

        try {
            initLogging(API_CALL, jobschedulerCommand.getJobschedulerId());
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerCommand.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobschedulerCommand.getJobschedulerId());
            if ("".equals(jobschedulerCommand.getUrl()) || jobschedulerCommand.getUrl() == null){
                jobschedulerCommand.setUrl(dbItemInventoryInstance.getUrl());
            }

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() > 1){
                String message = String.format("There are %s commands specified. Only one command is allowed",jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size());
                throw new JobSchedulerBadRequestException(message);
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobschedulerCommand.getUrl());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() < 1){
                throw new JobSchedulerBadRequestException("Unknown command");
            }
            
            String xml = jobSchedulerCommandFactory.getXml(jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().get(0));
            if (!jobSchedulerCommandFactory.isPermitted(getPermissonsCommands(accessToken))){
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

}
