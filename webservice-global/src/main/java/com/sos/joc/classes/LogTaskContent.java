package com.sos.joc.classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.job.TaskFilter;

public class LogTaskContent extends LogContent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTaskContent.class);
    private static final String XPATH_TASK_LOG = "/spooler/answer/task/log";
    private TaskFilter taskFilter;
    private String job = null;
    
    public LogTaskContent(TaskFilter taskFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.taskFilter = taskFilter;
    }
    
    public String getLog() throws Exception {

        String log = getLogFromDB();
        if (log == null) {
            log = getTaskLogFromXmlCommand();
        }
        if (log == null) {
            String msg = String.format("Task log of %s with id %s is missing", job, taskFilter.getTaskId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return log;
    }

    public Path writeLogFile() throws Exception {

        Path path = writeLogFileFromDB();
        if (path == null) {
            path = writeTaskLogFileFromXmlCommand();
        }
        if (path == null) {
            String msg = String.format("Task log of %s with id %s is missing", job, taskFilter.getTaskId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return path;
    }

    private String getLogFromDB() throws DBMissingDataException, IOException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(taskFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getTaskLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateSession);
                job = jobSchedulerTaskHistoryDBLayer.getJob();
                return jobSchedulerTaskHistoryDBLayer.getLogAsString(taskFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } catch (JocConfigurationException | SOSHibernateException | NumberFormatException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private Path writeLogFileFromDB() throws DBMissingDataException, IOException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(taskFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getTaskLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateSession);
                job = jobSchedulerTaskHistoryDBLayer.getJob();
                return jobSchedulerTaskHistoryDBLayer.writeLogFile(taskFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } catch (JocConfigurationException | SOSHibernateException | NumberFormatException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private String getTaskLogFromXmlCommand() throws Exception {

        String xml = String.format("<show_task id=\"%1$s\" what=\"log\" />", taskFilter.getTaskId());
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_TASK_LOG, null);
    }
    
    private Path writeTaskLogFileFromXmlCommand() throws Exception {
        String taskLog = getTaskLogFromXmlCommand();
        if (taskLog == null) {
            return null;
        }
        Path path = null;
        try {
            path = Files.createTempFile("sos-download-", null);
            Files.write(path, taskLog.getBytes());
            return path;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(path);
            } catch (Exception e1) {}
            throw e;
        }
    }

    public String getJob() {
        if (job == null) {
            return "";
        }
        return job;
    }
}
