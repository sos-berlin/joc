package com.sos.joc.tasks.post;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.tasks.post.kill.JobKill;

public class TasksHistoryBody {

    private String jobschedulerId;
    private List<JobKill> jobs = new ArrayList<JobKill>();
    private Boolean compact = false;
    private String regex;
    private Boolean isOrderJob;
    private String dateFrom;
    private String dateTo;
    private String timeZone;
    private List<Folder> folders = new ArrayList<Folder>();
    private List<State> state = new ArrayList<State>();

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

    public Boolean getCompact() {
        return compact;
    }

    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

}