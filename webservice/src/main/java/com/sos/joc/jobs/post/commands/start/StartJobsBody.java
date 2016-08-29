package com.sos.joc.jobs.post.commands.start;

import java.util.ArrayList;
import java.util.List;

public class StartJobsBody {

    private String jobschedulerId;
    private List<Job> jobs = new ArrayList<Job>();

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}