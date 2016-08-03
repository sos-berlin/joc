package com.sos.joc.jobscheduler.post;

public class JobSchedulerModifyJobSchedulerBody {

    String jobschedulerId;
    Integer timeout;
    String host;
    Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

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
        if (timeout == null) {
            return null;
        } else {
            return String.valueOf(timeout);
        }
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
