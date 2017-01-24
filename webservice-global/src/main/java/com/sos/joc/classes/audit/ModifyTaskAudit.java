package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;


public class ModifyTaskAudit extends ModifyTasks implements IAuditLog {
    
    @JsonIgnore
    private String comment;
    @JsonIgnore
    private String folder;
    @JsonIgnore
    private String job;
    @JsonIgnore
    private String jobChain;
    @JsonIgnore
    private String orderId;

    public ModifyTaskAudit(TasksFilter job, TaskId taskId, ModifyTasks modifyTasks) {
        Path p = Paths.get(job.getJob());
        this.comment = job.getComment();
        this.folder = p.getParent().toString().replace('\\', '/');
        this.job = p.getFileName().toString();
        this.jobChain = null;
        this.orderId = null;
        job.setComment(null);
        job.getTaskIds().clear();
        job.getTaskIds().add(taskId);
        getJobs().add(job);
        setTimeout(modifyTasks.getTimeout());
        setJobschedulerId(modifyTasks.getJobschedulerId());
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
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
        return jobChain;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return orderId;
    }
}
