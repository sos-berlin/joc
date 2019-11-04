package com.sos.joc.db.joe;

import java.util.Arrays;
import java.util.List;

import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.JSObjectEdit;

public class FilterJoeObjects {

    private String schedulerId;
    private JobSchedulerObjectType objectType;
    private List<String> objectTypes;
    private String account;
    private String path;
    private Object orderCriteria;
    private String sortMode="asc";

    public void setConstraint(Filter filter) {
        schedulerId = filter.getJobschedulerId();
        objectType = filter.getObjectType();
        path = filter.getPath();
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }
    
    public List<String> getObjectTypes() {
        return objectTypes;
    }

    public void setObjectTypes(String ...objectTypes) {
        this.objectTypes = Arrays.asList(objectTypes);
    }
    
    public void setObjectTypes(List<String> objectTypes) {
        this.objectTypes = objectTypes;
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
        objectType = body.getObjectType();
        path = body.getPath();
    }

    public boolean isRecursive() {
        return path.endsWith("%");
    }

    public void setRecursive() {
        if (!path.endsWith("%")) {
            path = (path + "/").replaceAll("//+", "/") + "%";
        }
    }
    
    public String getSortMode() {
        if (orderCriteria == null || "".equals(orderCriteria) || sortMode == null) {
            return "";
        } else {
            return " " + sortMode;
        }
    }

    public void setSortMode(String sortMode) {
        this.sortMode = sortMode;
    }

    public String getOrderCriteria() {
        if (orderCriteria == null || "".equals(orderCriteria)) {
            return "";
        } else {
            return " order by " + orderCriteria;
        }
    }

    public void setOrderCriteria(String orderCriteria) {
        this.orderCriteria = orderCriteria;
    }


}