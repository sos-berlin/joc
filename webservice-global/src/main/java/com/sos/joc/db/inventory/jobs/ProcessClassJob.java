package com.sos.joc.db.inventory.jobs;

public class ProcessClassJob {

    private Long id;
    private String processClassName;

    public ProcessClassJob(Long id, String processClassName) {
        this.id = id;
        this.processClassName = processClassName;
    }

    public String getProcessClassName() {
        return processClassName;
    }

    public Long getId() {
        return id;
    }
}
