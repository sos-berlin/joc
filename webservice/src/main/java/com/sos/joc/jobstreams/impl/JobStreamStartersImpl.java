package com.sos.joc.jobstreams.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamStarters;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IJobStreamStartersResource;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreamStarters;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamStartersImpl extends JOCResourceImpl implements IJobStreamStartersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamStartersImpl.class);
    private static final String API_CALL_ADD_JOBSTREAM_STARTER = "./jobstreams/edit_jobstream_starter";

    @Override
    public JOCDefaultResponse editJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.checkRequiredParameter("jobStream", jobStreamStarters.getJobStream());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            sosHibernateSession.setAutoCommit(false);
            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerJobStreamStarters.deleteInsert(jobStreamStarters);
            sosHibernateSession.commit();
            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Add JobStreamStarter: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Data are stored in Database");
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStarters);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse startJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                    notifyEventHandlerStart(accessToken, jobStreamStarter.getJobStreamStarterId());
                }
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Add JobStreamStarter: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running.");
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStarters);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private String notifyEventHandlerStart(String accessToken, Long starterId) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("starterId", String.valueOf(starterId));

        customEventsUtil.addEvent("StartJobStream", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        return jocXmlCommand.executePost(notifyCommand, accessToken);
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}