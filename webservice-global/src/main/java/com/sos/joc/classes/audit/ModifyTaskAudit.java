package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;


public class ModifyTaskAudit extends ModifyTasks implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    @JsonIgnore
    private String job;
    
    public ModifyTaskAudit(TasksFilter job, TaskId taskId, ModifyTasks modifyTasks) {
        if (modifyTasks != null) {
            setComment(modifyTasks.getComment());
            setTimeout(modifyTasks.getTimeout());
            setJobschedulerId(modifyTasks.getJobschedulerId()); 
        }
        if (job != null) {
            job.getTaskIds().clear();
            job.getTaskIds().add(taskId);
            getJobs().add(job);
            if (job.getJob() != null) {
                Path p = Paths.get(job.getJob()); 
                this.folder = p.getParent().toString().replace('\\', '/');
                this.job = p.getFileName().toString();
            }
        }
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return super.getComment();
    }
    
    @Override
    @JsonIgnore
    public String getFolder() {
        return folder;
    }

    @Override
    @JsonIgnore
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
}
