package com.sos.joc.db.calendars;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.calendar.UsedBy;

public class CalendarUsage extends UsedBy {
    
    @JsonIgnore
    private Long instanceId;

    public CalendarUsage(Long instanceId, String path, String objectType, String jobschedulerId, String host, Integer port) {
        this.instanceId = instanceId;
        setJobschedulerId(jobschedulerId);
        setHost(host);
        setPort(port);
        if (objectType != null && path != null) {
            switch (objectType.toUpperCase()) {
            case "JOB":
                setJobs(new ArrayList<String>());
                getJobs().add(path);
                break;
            case "ORDER":
                setOrders(new ArrayList<String>());
                getOrders().add(path);
                break;
            case "SCHEDULE":
                setSchedules(new ArrayList<String>());
                getSchedules().add(path);
                break;
            default:
                break;
            }
        }
    }
    
    @JsonIgnore
    public Long getInstanceId() {
        return instanceId;
    }
}
