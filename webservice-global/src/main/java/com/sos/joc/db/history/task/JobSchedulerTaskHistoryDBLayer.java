package com.sos.joc.db.history.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.schedulerhistory.db.SchedulerTaskHistoryDBItem;
import com.sos.jitl.schedulerhistory.db.SchedulerTaskHistoryLogDBItemPostgres;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.model.job.TaskFilter;

public class JobSchedulerTaskHistoryDBLayer extends DBLayer {

    private String job = null;
    private String clusterMemberId = null;

    public JobSchedulerTaskHistoryDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public byte[] getLogAsByteArray(TaskFilter taskFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException, IOException {
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerTaskHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerTaskHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerTaskHistoryLogDBItemPostgres.class, Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.getLogAsByteArray();
            }
        } else {
            SchedulerTaskHistoryDBItem schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getSession().get(SchedulerTaskHistoryDBItem.class,
                    Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.getLogAsByteArray();
            }
        }
    }

    public Path writeLogFile(TaskFilter taskFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException, IOException {
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerTaskHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerTaskHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerTaskHistoryLogDBItemPostgres.class, Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.writeLogFile(getPrefix(taskFilter.getTaskId(), job));
            }
        } else {
            SchedulerTaskHistoryDBItem schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getSession().get(SchedulerTaskHistoryDBItem.class,
                    Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.writeLogFile(getPrefix(taskFilter.getTaskId(), job));
            }
        }
    }

    public Path writeGzipLogFile(TaskFilter taskFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException, IOException {
        if (this.getSession().getFactory().dbmsIsPostgres()) {
            SchedulerTaskHistoryLogDBItemPostgres schedulerHistoryDBItem = (SchedulerTaskHistoryLogDBItemPostgres) this.getSession().get(
                    SchedulerTaskHistoryLogDBItemPostgres.class, Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.writeGzipLogFile(getPrefix(taskFilter.getTaskId(), job));
            }
        } else {
            SchedulerTaskHistoryDBItem schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getSession().get(SchedulerTaskHistoryDBItem.class,
                    Long.parseLong(taskFilter.getTaskId()));
            if (schedulerHistoryDBItem == null) {
                // in case of task which doesn't get connection to agent the db is also empty.
                // throw new DBMissingDataException("Task log with id " +taskFilter.getTaskId()+ " is missing");
                return null;
            } else {
                job = schedulerHistoryDBItem.getJobName();
                clusterMemberId = schedulerHistoryDBItem.getClusterMemberId();
                if (taskFilter.getJobschedulerId() != null && !taskFilter.getJobschedulerId().equals(schedulerHistoryDBItem.getSchedulerId())) {
                    throw new DBMissingDataException("Task log of " + job + " with id " + taskFilter.getTaskId() + " is missing");
                }
                return schedulerHistoryDBItem.writeGzipLogFile(getPrefix(taskFilter.getTaskId(), job));
            }
        }
    }

    public String getLogAsString(TaskFilter taskFilter) throws NumberFormatException, SOSHibernateException, DBMissingDataException, IOException {
        byte[] bytes = getLogAsByteArray(taskFilter);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public String getJob() {
        return job;
    }
    
    public String getClusterMemberId() {
        return clusterMemberId;
    }

    private String getPrefix(String taskId, String jobName) {
        String prefix = taskId + "";
        if (jobName != null && !jobName.isEmpty()) {
            prefix = Paths.get(jobName).getFileName().toString() + "." + prefix;
        }
        prefix = "sos-" + prefix + ".task.log-download-";
        return prefix.replace(',', '.').replaceAll("[/\\\\:;*?!&\"'<>|^]", "");
    }

}