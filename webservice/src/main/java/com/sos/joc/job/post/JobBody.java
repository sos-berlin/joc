package com.sos.joc.job.post;

public class JobBody {

    private String jobschedulerId;
    /**
     * absolute path based on live folder of a JobScheduler object. (Required)
     *
     */
    private String job;
    /**
     * controls if the object view is compact or detailed
     *
     */
    private Boolean compact = false;

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Boolean getCompact() {
        return compact;
    }

    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

}