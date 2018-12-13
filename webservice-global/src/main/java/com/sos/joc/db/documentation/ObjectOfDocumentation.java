package com.sos.joc.db.documentation;

import com.sos.joc.model.common.JobSchedulerObjectType;

public class ObjectOfDocumentation {
    
    private JobSchedulerObjectType type;
    private String path;
    
    public ObjectOfDocumentation(String path, String type) {
        try {
            this.type = JobSchedulerObjectType.fromValue(type);
        } catch (IllegalArgumentException e) {
            this.type = JobSchedulerObjectType.OTHER;
        }
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public JobSchedulerObjectType getType() {
        return type;
    }

}
