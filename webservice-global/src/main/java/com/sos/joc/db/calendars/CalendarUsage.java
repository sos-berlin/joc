package com.sos.joc.db.calendars;

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
                setJobs(Collections.singletonList(path));
                break;
            case "ORDER":
                setOrders(Collections.singletonList(path));
                break;
            case "SCHEDULE":
                setSchedules(Collections.singletonList(path));
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
