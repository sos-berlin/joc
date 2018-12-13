package com.sos.joc.db.documentation;

import com.sos.joc.model.common.JobSchedulerObject;
import com.sos.joc.model.common.JobSchedulerObjectType;

public class DocumentationOfObject {
    private String docPath;
    private String objPath;
    private JobSchedulerObject docUsage;
    
    public DocumentationOfObject(String docPath, String objPath) {
        this.docPath = docPath;
        this.objPath = objPath;
    }
    
    public DocumentationOfObject(String docPath, String objPath, String objType) {
        this.docPath = docPath;
        this.docUsage = new JobSchedulerObject();
        try {
            this.docUsage.setType(JobSchedulerObjectType.fromValue(objType));
        } catch (IllegalArgumentException e) {
            this.docUsage.setType(JobSchedulerObjectType.OTHER);
        }
        this.docUsage.setPath(objPath);
    }

    public String getDocPath() {
        return docPath;
    }
    
    public String getObjPath() {
        return objPath;
    }
    
    public JobSchedulerObject getDocUsage() {
        return docUsage;
    }

}
