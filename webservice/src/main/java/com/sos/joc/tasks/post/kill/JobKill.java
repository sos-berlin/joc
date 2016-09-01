package com.sos.joc.tasks.post.kill;

import java.util.ArrayList;
import java.util.List;

public class JobKill {

    private String job;
    private String comment;
    private List<TaskId> taskIds = new ArrayList<TaskId>();

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

    public List<TaskId> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<TaskId> taskIds) {
        this.taskIds = taskIds;
    }

}