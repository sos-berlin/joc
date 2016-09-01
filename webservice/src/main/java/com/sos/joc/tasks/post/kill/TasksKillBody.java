package com.sos.joc.tasks.post.kill;

import java.util.ArrayList;
import java.util.List;

public class TasksKillBody {

    private String jobschedulerId;
    private List<JobKill> jobs = new ArrayList<JobKill>();
    private Integer timeout;

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public List<JobKill> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobKill> jobs) {
        this.jobs = jobs;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}