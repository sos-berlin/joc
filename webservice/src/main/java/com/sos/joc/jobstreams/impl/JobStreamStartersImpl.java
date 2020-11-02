package com.sos.joc.jobstreams.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamParameter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarterJob;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamParameters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamStarters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamsStarterJobs;
import com.sos.jitl.jobstreams.db.FilterJobStreamParameters;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarterJobs;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarters;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.EditJobStreamStarterAudit;
import com.sos.joc.classes.audit.StartJobAudit;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IJobStreamStartersResource;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreamStarters;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamStartersImpl extends JOCResourceImpl implements IJobStreamStartersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamStartersImpl.class);
    private static final String API_CALL_ADD_JOBSTREAM_STARTER = "./jobstreams/edit_jobstream_starter";
    private static final String API_CALL_START = "jobstreams/start_jobstream";

    @Override
    public JOCDefaultResponse editJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStreamId", jobStreamStarters.getJobStreamId());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            sosHibernateSession.setAutoCommit(false);
            
            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(jobStreamStarters.getJobStreamId());

            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerJobStreamStarters.deleteInsert(jobStreamStarters, dbItemInventoryInstance.getTimeZone());
            sosHibernateSession.commit();

            for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                EditJobStreamStarterAudit editJobStreamStartersAudit = new EditJobStreamStarterAudit(dbItemJobStream.getJobStream(), jobStreamStarters, jobStreamStarter);
                logAuditMessage(editJobStreamStartersAudit);
                storeAuditLogEntry(editJobStreamStartersAudit);
            }

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
    public JOCDefaultResponse deleteJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStreamId", jobStreamStarters.getJobStreamId());
            

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            sosHibernateSession.setAutoCommit(false);
            
            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(jobStreamStarters.getJobStreamId());

            
            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerJobStreamStarters.deleteStarters(jobStreamStarters, dbItemInventoryInstance.getTimeZone());
            sosHibernateSession.commit();

            for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                EditJobStreamStarterAudit editJobStreamStartersAudit = new EditJobStreamStarterAudit(dbItemJobStream.getJobStream(), jobStreamStarters, jobStreamStarter);
                logAuditMessage(editJobStreamStartersAudit);
                storeAuditLogEntry(editJobStreamStartersAudit);
            }
            
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

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_START, jobStreamStarters, accessToken, jobStreamStarters.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                
                if (jobStreamStarters.getJobStream() != null) {
                    try {
                        sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_START);
                        DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
                        FilterJobStreams filter = new FilterJobStreams();
                        filter.setJobStream(jobStreamStarters.getJobStream()); 

                        List<DBItemJobStream> listOfStreams = dbLayerJobStreams.getJobStreamsList(filter, 0);
                        if (listOfStreams.size() > 0) {
                            jobStreamStarters.setJobStreamId(listOfStreams.get(0).getId());
                        }
                    }finally{
                        Globals.disconnect(sosHibernateSession);
                    }
                }
                
                
                for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                    if (jobStreamStarter.getJobStreamStarterId() == null) {
                        try {
                            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_START);
                            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
                            FilterJobStreamStarters filter = new FilterJobStreamStarters();
                            filter.setTitle(jobStreamStarter.getTitle()); 
                            filter.setJobStreamId(jobStreamStarters.getJobStreamId());
                            List<DBItemJobStreamStarter> listOfStartes = dbLayerJobStreamStarters.getJobStreamStartersList(filter, 0);
                            if (listOfStartes.size() > 0) {
                                jobStreamStarter.setJobStreamStarterId(listOfStartes.get(0).getId());
                            }
                        }finally{
                            Globals.disconnect(sosHibernateSession);
                        }
                    }
                    setAudit(jobStreamStarter, jobStreamStarters);
                    notifyEventHandlerStart(accessToken, jobStreamStarter);
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

    private void setAudit(JobStreamStarter jobStreamStarter, JobStreamStarters jobStreamStarters) throws JocConfigurationException,
            DBOpenSessionException, SOSHibernateException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_START);
            DBLayerJobStreamParameters dbLayerJobStreamParameters = new DBLayerJobStreamParameters(sosHibernateSession);
            DBLayerJobStreamsStarterJobs dbLayerJobStreamStarterJobs = new DBLayerJobStreamsStarterJobs(sosHibernateSession);
            FilterJobStreamStarterJobs filter = new FilterJobStreamStarterJobs();
            filter.setJobStreamStarter(jobStreamStarter.getJobStreamStarterId());

            List<DBItemJobStreamStarterJob> listOfStartedJobs = dbLayerJobStreamStarterJobs.getJobStreamStarterJobsList(filter, 0);

            for (DBItemJobStreamStarterJob dbItemJobStreamStarterJob : listOfStartedJobs) {
                FilterJobStreamParameters filterJobStreamParameters = new FilterJobStreamParameters();
                filterJobStreamParameters.setJobStreamStarterId(jobStreamStarter.getJobStreamStarterId());
                List<DBItemJobStreamParameter> listOfJobStreamParameters = dbLayerJobStreamParameters.getJobStreamParametersList(
                        filterJobStreamParameters, 0);

                StartJob startJob = new StartJob();
                startJob.setAt("now");
                startJob.setJob(dbItemJobStreamStarterJob.getJob());
                startJob.setJobStream(jobStreamStarters.getJobStream());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (DBItemJobStreamParameter dbItemJobStreamParameter : listOfJobStreamParameters) {
                    NameValuePair param = new NameValuePair();
                    param.setName(dbItemJobStreamParameter.getName());
                    param.setValue(dbItemJobStreamParameter.getValue());
                    params.add(param);
                }
                startJob.setParams(params);

                StartJobs startJobs = new StartJobs();
                startJobs.setAuditLog(jobStreamStarters.getAuditLog());
                startJobs.setJobschedulerId(jobStreamStarters.getJobschedulerId());

                StartJobAudit jobAudit = new StartJobAudit(startJob, startJobs);
                logAuditMessage(jobAudit);
                storeAuditLogEntry(jobAudit);

            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandlerStart(String accessToken, JobStreamStarter jobStreamStarter) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("starterId", String.valueOf(jobStreamStarter.getJobStreamStarterId()));
       
        for (NameValuePair param: jobStreamStarter.getParams()) {
            parameters.put("#" + param.getName(), param.getValue());
         }

        customEventsUtil.addEvent("StartJobStream", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }
}