package com.sos.joc.db.configuration;

import com.sos.hibernate.classes.SOSHibernateFilter;

public class JocConfigurationFilter extends SOSHibernateFilter{

    private Long instanceId;
    private String owner;
    private String objectType;
    private String objectSource;
    private String name;
    private Boolean shared;

    
    public JocConfigurationFilter() {
        super();
    }


    public Long getInstanceId() {
        return instanceId;
    }


    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }


    public String getOwner() {
        return owner;
    }


    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getObjectType() {
        return objectType;
    }


    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }


    public String getObjectSource() {
        return objectSource;
    }


    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Boolean isShared() {
        return shared;
    }


    public void setShare(Boolean shared) {
        this.shared = shared;
    }
    
    
    
}
