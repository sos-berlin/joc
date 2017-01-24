package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;


public class StartJobAudit extends StartJobs implements IAuditLog {
    
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

    public StartJobAudit(StartJob startJob, String jobschedulerId) {
        Path p = Paths.get(startJob.getJob());
        this.comment = startJob.getComment();
        this.folder = p.getParent().toString().replace('\\', '/');
        this.job = p.getFileName().toString();
        this.jobChain = null;
        this.orderId = null;
        startJob.setComment(null);
        getJobs().add(startJob);
        setJobschedulerId(jobschedulerId);
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
