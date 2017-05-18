package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyJobAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceModifyJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;

@Path("jobs")
public class JobsResourceModifyJobImpl extends JOCResourceImpl implements IJobsResourceModifyJob {

    private static final String CONTINUE = "continue";
    private static final String END = "end";
    private static final String SUSPEND = "suspend";
    private static final String STOP = "stop";
    private static final String SET_RUN_TIME = "set_run_time";
    private static final String RESET_RUN_TIME = "reset_run_time";
    private static final String UNSTOP = "unstop";
    private static String API_CALL = "./jobs/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private SOSHibernateSession connection = null;
    private InventoryJobsDBLayer dbLayer = null;

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, STOP, getPermissonsJocCockpit(accessToken).getJob().getExecute().isStop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, UNSTOP, getPermissonsJocCockpit(accessToken).getJob().getExecute().isUnstop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, SET_RUN_TIME, getPermissonsJocCockpit(accessToken).getJob().getChange().isRunTime(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
    
    @Override
    public JOCDefaultResponse postJobsResetRunTime(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, RESET_RUN_TIME, getPermissonsJocCockpit(accessToken).getJob().getChange().isRunTime(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, END, getPermissonsJocCockpit(accessToken).getJob().getExecute().isEndAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, SUSPEND, getPermissonsJocCockpit(accessToken).getJob().getExecute().isSuspendAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, CONTINUE, getPermissonsJocCockpit(accessToken).getJob().getExecute().isContinueAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyJobCommand(ModifyJob modifyJob, ModifyJobs modifyJobs, String command) {

        try {
            ModifyJobAudit jobAudit = new ModifyJobAudit(modifyJob, modifyJobs);
            logAuditMessage(jobAudit);

            checkRequiredParameter("job", modifyJob.getJob());
            if (SET_RUN_TIME.equals(command)) {
                checkRequiredParameter("runTime", modifyJob.getRunTime());
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            XMLBuilder xml = new XMLBuilder("modify_job");
            String jobPath = normalizePath(modifyJob.getJob());
            xml.addAttribute("job", jobPath);
            switch (command) {
            case SET_RUN_TIME:
                try {
                    ValidateXML.validateRunTimeAgainstJobSchedulerSchema(modifyJob.getRunTime());
                    xml.add(XMLBuilder.parse(modifyJob.getRunTime()));
                    jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                    updateRunTimeIsTemporary(jobPath, true);
                    storeAuditLogEntry(jobAudit);
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(modifyJob.getRunTime());
                }
                break;
            case RESET_RUN_TIME:
                try {
                    DBItemInventoryJob dbItem = getDBItem(jobPath);
                    if (dbItem == null) {
                        throw new DBMissingDataException(String.format("no entry found in DB: %1$s", jobPath));
                    }
                    if (dbItem.getRunTimeIsTemporary() == null) {
                        dbItem.setRunTimeIsTemporary(false); 
                    }
                    if (dbItem.getRunTimeIsTemporary()) {
                        String runTimeCommand = jocXmlCommand.getShowJobCommand(jobPath, "source", 0, 0);
                        String runTime = RunTime.getRuntimeXmlString(jobPath, jocXmlCommand, runTimeCommand, "//source/job/run_time", getAccessToken());
                        xml.add(XMLBuilder.parse(runTime));
                        jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                        updateRunTimeIsTemporary(dbItem, false);
                        storeAuditLogEntry(jobAudit);
                    } else {
                        //nothing to do
                    }
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(modifyJob.getRunTime());
                }
                break;
            default:
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
        JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyJobs, accessToken, modifyJobs.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        checkRequiredComment(modifyJobs.getAuditLog());
        if (modifyJobs.getJobs().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'jobs'");
        }
        Date surveyDate = new Date();
        for (ModifyJob job : modifyJobs.getJobs()) {
            surveyDate = executeModifyJobCommand(job, modifyJobs, command);
        }
        Globals.disconnect(connection);
        if (listOfErrors.size() > 0) {
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }
    
    private DBItemInventoryJob getDBItem(String jobPath) throws JocException {
        if (connection == null) {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        if (dbLayer == null) {
            dbLayer = new InventoryJobsDBLayer(connection);
        }
        return dbLayer.getInventoryJobByName(jobPath, dbItemInventoryInstance.getId());
    }
    
    private void updateRunTimeIsTemporary(String jobPath, boolean value) throws JocException {
        DBItemInventoryJob dbItem = getDBItem(jobPath);
        if (dbItem == null) {
            throw new DBMissingDataException(String.format("no entry found in DB: %1$s", jobPath));
        }
        updateRunTimeIsTemporary(dbItem, value);
    }
    
    private void updateRunTimeIsTemporary(DBItemInventoryJob dbItem, boolean value) throws JocException {
        dbItem.setRunTimeIsTemporary(value);
        try {
            connection.update(dbItem);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
}
