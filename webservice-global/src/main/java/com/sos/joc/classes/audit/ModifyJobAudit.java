package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;


public class ModifyJobAudit extends ModifyJobs implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    @JsonIgnore
    private String job;
    
    public ModifyJobAudit(ModifyJob modifyJob, ModifyJobs modifyJobs) {
        if (modifyJob != null) {
            getJobs().add(modifyJob);
            if (modifyJob.getJob() != null) {
                Path p = Paths.get(modifyJob.getJob());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.job = p.getFileName().toString();
            }
        }
        if (modifyJobs != null) {
            setComment(modifyJobs.getComment());
            setJobschedulerId(modifyJobs.getJobschedulerId());
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
