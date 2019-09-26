package com.sos.joc.db.joe;

import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.JSObjectEdit;

public class FilterJoeObjects {

    private String schedulerId;
    private String objectType;
    private String account;
    private String path;

    public void setConstraint(Filter filter) {
        schedulerId = filter.getJobschedulerId();
        objectType = filter.getObjectType().value();
        path = filter.getPath();
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setConstraint(JSObjectEdit body) {
        schedulerId = body.getJobschedulerId();
        objectType = body.getObjectType().value();
        path = body.getPath();
    }

    public boolean isRecursive() {
        return path.endsWith("%");
    }

    public void setRecursive() {
        if (!path.endsWith("%")) {
            path = path + "%";
        }

    }

}