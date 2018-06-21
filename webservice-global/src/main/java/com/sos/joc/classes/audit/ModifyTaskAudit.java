package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;


public class ModifyTaskAudit extends ModifyTasks implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    //@JsonIgnore
    private String job;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public ModifyTaskAudit(TasksFilter job, TaskId taskId, ModifyTasks modifyTasks) {
        if (modifyTasks != null) {
            setAuditParams(modifyTasks.getAuditLog());
            setTimeout(modifyTasks.getTimeout());
            setJobschedulerId(modifyTasks.getJobschedulerId()); 
        }
        if (job != null) {
            List<TaskId> taskIds = new ArrayList<TaskId>();
            taskIds.add(taskId);
            TasksFilter jobClone = new TasksFilter();
            jobClone.setJob(job.getJob());
            jobClone.setTaskIds(taskIds);
            getJobs().add(jobClone);
            if (job.getJob() != null) {
                Path p = Paths.get(job.getJob()); 
                this.folder = p.getParent().toString().replace('\\', '/');
                this.job = p.toString().replace('\\', '/');
            }
        }
    }

    private void setAuditParams(AuditParams auditParams) {
        if (auditParams != null) {
            this.comment = auditParams.getComment();
            this.timeSpent = auditParams.getTimeSpent();
            this.ticketLink = auditParams.getTicketLink(); 
        }
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
    }

    @Override
    @JsonIgnore
    public Integer getTimeSpent() {
        return timeSpent;
    }

    @Override
    @JsonIgnore
    public String getTicketLink() {
        return ticketLink;
    }
    
    @Override
    @JsonIgnore
    public String getFolder() {
        return folder;
    }

    @Override
    //@JsonIgnore
    public String getJob() {
        return job;
    }

    @Override
    @JsonIgnore
    public String getJobChain() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }
}
