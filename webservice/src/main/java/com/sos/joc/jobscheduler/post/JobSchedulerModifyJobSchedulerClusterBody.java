package com.sos.joc.jobscheduler.post;

public class JobSchedulerModifyJobSchedulerClusterBody {

    String jobschedulerId;
    Integer timeout;

     public Integer getTimeout() {
        return timeout;
    }

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getTimeoutAsString() {
        return String.valueOf(timeout);
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
