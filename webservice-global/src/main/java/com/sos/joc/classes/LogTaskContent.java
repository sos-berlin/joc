package com.sos.joc.classes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.job.TaskFilter;

public class LogTaskContent extends LogContent {

    private TaskFilter taskFilter;
    private String job = null;

    public LogTaskContent(TaskFilter taskFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.taskFilter = taskFilter;
    }

    public Path writeGzipLogFile() throws Exception {

        Path path = writeGzipLogFileFromDB();
        if (path == null) {
            try {
                path = writeGzipTaskLogFileFromXmlCommand();
            } catch (JobSchedulerNoResponseException e) {
                // occurs if log is deleted on disk but doesn't still exist in database
                // retry database
                path = writeGzipLogFileFromDB();
                if (path == null) {
                    throw e;
                }
            }
        }
        if (path == null) {
            String msg = String.format("Task log of %s with id %s is missing", job, taskFilter.getTaskId());
            throw new JobSchedulerObjectNotExistException(msg);
        }
        return path;
    }

    private Path writeGzipLogFileFromDB() throws DBMissingDataException, IOException, NumberFormatException, SOSHibernateException, JocConfigurationException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(taskFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getTaskLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateSession);
                Path path = jobSchedulerTaskHistoryDBLayer.writeGzipLogFile(taskFilter);
                job = jobSchedulerTaskHistoryDBLayer.getJob();
                return path;
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private Path writeGzipTaskLogFileFromXmlCommand() throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        String logFilename = null;
        try {
            if (job == null || job.isEmpty()) {
                throw new DBMissingDataException("Job of task with id " + taskFilter.getTaskId() + " could not determine");

            } else {
                sosHibernateSession = Globals.createSosHibernateStatelessConnection("taskLog");
                InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(sosHibernateSession);
                DBItemInventoryJob jobItem = dbLayer.getInventoryJobByName(("/" + job).replaceAll("/+", "/"), dbItemInventoryInstance.getId());
                if (jobItem != null) {
                    String j = job.replaceFirst("^/+", "").replaceAll("/", ",");
                    if (jobItem.getMaxTasks() <= 1) {
                        logFilename = "task." + j + ".log";
                    } else {
                        logFilename = "task." + j + "." + taskFilter.getTaskId() + ".log";
                    }
                }
            }
            if (logFilename != null && !logFilename.isEmpty()) {
                return writeGzipTaskLogFileFromGet(logFilename, getPrefix());
            } else {
                throw new DBMissingDataException("Filename of runnig task log with id " + taskFilter.getTaskId() + " could not determine");
            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private String getPrefix() {
        String prefix = taskFilter.getTaskId() + "";
        if (job != null && !job.isEmpty()) {
            prefix = Paths.get(job).getFileName().toString() + "." + prefix;
        }
        prefix = "sos-" + prefix + ".task.log-download-";
        return prefix.replace(',', '.').replaceAll("[/\\\\:;*?!&\"'<>|^]", "");
    }

    public String getJob() {
        if (job == null) {
            return "";
        }
        return job;
    }
}
