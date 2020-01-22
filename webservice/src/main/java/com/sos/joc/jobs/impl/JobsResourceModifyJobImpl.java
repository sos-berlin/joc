package com.sos.joc.jobs.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Document;
import org.dom4j.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.configuration.JSObjectConfiguration;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceModifyJob;
import com.sos.joc.joe.common.XmlDeserializer;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;
import com.sos.schema.JsonValidator;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.xml.XMLBuilder;

@Path("jobs")
public class JobsResourceModifyJobImpl extends JOCResourceImpl implements IJobsResourceModifyJob {

    private static final String CONTINUE = "continue";
    private static final String END = "end";
    private static final String SUSPEND = "suspend";
    private static final String STOP = "stop";
    private static final String SET_RUN_TIME = "set_run_time";
    private static final String UNSTOP = "unstop";
    private static String API_CALL = "./jobs/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, STOP, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob().getExecute()
                    .isStop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, UNSTOP, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob().getExecute()
                    .isUnstop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, SET_RUN_TIME, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob()
                    .getChange().isRunTime(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, END, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob().getExecute()
                    .isEndAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, SUSPEND, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob().getExecute()
                    .isSuspendAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, byte[] modifyJobsBytes) {
        try {
            JsonValidator.validateFailFast(modifyJobsBytes, ModifyJobs.class);
            ModifyJobs modifyJobs = Globals.objectMapper.readValue(modifyJobsBytes, ModifyJobs.class);
            return postJobsCommand(accessToken, CONTINUE, getPermissonsJocCockpit(modifyJobs.getJobschedulerId(), accessToken).getJob().getExecute()
                    .isContinueAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyJobCommand(ModifyJob modifyJob, ModifyJobs modifyJobs, String command, List<DBItemInventoryInstance> clusterMembers,
            SOSHibernateSession connection) {

        try {
            if (modifyJob.getCalendars() != null && modifyJob.getCalendars().isEmpty()) {
                modifyJob.setCalendars(null);
            }
            ModifyJobAudit jobAudit = new ModifyJobAudit(modifyJob, modifyJobs);
            logAuditMessage(jobAudit);

            checkRequiredParameter("job", modifyJob.getJob());
            if (SET_RUN_TIME.equals(command) && modifyJob.getRunTime() == null) {
                throw new JocMissingRequiredParameterException("undefined 'runTime'");
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String jobPath = normalizePath(modifyJob.getJob());
            switch (command) {
            case SET_RUN_TIME:
                try {
                    RunTime runTime = XmlSerializer.serializeAbstractSchedule(modifyJob.getRunTime());
                    modifyJob.setRunTimeXml(Globals.xmlMapper.writeValueAsString(runTime));
                    JSObjectConfiguration jocConfiguration = new JSObjectConfiguration();
                    String configuration = jocConfiguration.modifyJobRuntime(modifyJob.getRunTimeXml(), this, jobPath);
                    Document doc = ValidateXML.validateAgainstJobSchedulerSchema(configuration);
                    
                    if (versionIsOlderThan("1.13.1")) {

                        XMLBuilder xmlBuilder = new XMLBuilder("modify_hot_folder");
                        if (doc != null) {
                            Element jobElement = doc.getRootElement();
                            jobElement.addAttribute("name", Paths.get(jobPath).getFileName().toString());
                            xmlBuilder.addAttribute("folder", getParent(jobPath)).add(jobElement);
                        }
                        jocXmlCommand.executePostWithThrowBadRequest(xmlBuilder.asXML(), getAccessToken());
                    } else {
                        
                        Job jobPojo = XmlDeserializer.deserialize(configuration, Job.class);
                        if (jobPojo.getRunTime() != null && modifyJob.getCalendars() != null && !modifyJob.getCalendars().isEmpty()) {
                            Calendars calendars = new Calendars();
                            calendars.setCalendars(modifyJob.getCalendars());
                            jobPojo.getRunTime().setCalendars(Globals.objectMapper.writeValueAsString(calendars)); 
                        }
                        JOCHotFolder jocHotFolder = new JOCHotFolder(this);
                        jocHotFolder.putFile(jobPath + ".job.xml", XmlSerializer.serializeToStringWithHeader(XmlSerializer.serializeJob(jobPojo)));
                    }

                    CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(connection, dbItemInventoryInstance.getSchedulerId(),
                            CalendarObjectType.JOB, jobPath, modifyJob.getRunTimeXml(), modifyJob.getCalendars());
                    calendarUsedByWriter.updateUsedBy();
                    CalendarEvent calEvt = calendarUsedByWriter.getCalendarEvent();
                    if (calEvt != null) {
                        if (clusterMembers != null) {
                            SendCalendarEventsUtil.sendEvent(calEvt, clusterMembers, getAccessToken());
                        } else {
                            SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
                        }
                    }

                    storeAuditLogEntry(jobAudit);
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(e);
                }
                break;
            default:
                XMLBuilder xml = new XMLBuilder("modify_job");
                xml.addAttribute("job", jobPath);
                xml.addAttribute("cmd", command);
                jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                storeAuditLogEntry(jobAudit);
                break;
            }

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        }
        return null;
    }

    private JOCDefaultResponse postJobsCommand(String accessToken, String command, boolean permission, ModifyJobs modifyJobs) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyJobs, accessToken, modifyJobs.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(modifyJobs.getAuditLog());
            if (modifyJobs.getJobs().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobs'");
            }

            List<DBItemInventoryInstance> clusterMembers = null;
            if (SET_RUN_TIME.equals(command)) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL + SET_RUN_TIME);

                if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                    InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                    clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(modifyJobs.getJobschedulerId());
                }
            }

            Date surveyDate = new Date();
            for (ModifyJob job : modifyJobs.getJobs()) {
                surveyDate = executeModifyJobCommand(job, modifyJobs, command, clusterMembers, connection);
            }
            if (listOfErrors.size() > 0) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } finally {
            Globals.disconnect(connection);
        }
    }

}
