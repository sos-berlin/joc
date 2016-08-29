package com.sos.joc.jobs.post.commands.start;

import java.util.ArrayList;
import java.util.List;
 
public class Job {

    private String job;
    private String comment;
    private String at;
    private List<Param> params = new ArrayList<Param>();
    private List<Environment> environment = new ArrayList<Environment>();

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public List<Environment> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<Environment> environment) {
        this.environment = environment;
    }

}