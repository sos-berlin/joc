package com.sos.joc.db.configuration;

import com.sos.hibernate.classes.SOSHibernateFilter;

public class JocConfigurationFilter extends SOSHibernateFilter{

    private Long id;
    private String schedulerId;
    private String account;
    private String objectType;
    private String configurationType;
    private String name;
    private Boolean shared;

    
    public JocConfigurationFilter() {
        super();
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getSchedulerId() {
        return schedulerId;
    }


    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }


    public String getAccount() {
        return account;
    }


    public void setAccount(String account) {
        this.account = account;
    }


    public String getObjectType() {
        return objectType;
    }


    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }


    public String getConfigurationType() {
        return configurationType;
    }


    public void setConfigurationType(String configurationType) {
        this.configurationType = configurationType;
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


    public void setShared(Boolean shared) {
        this.shared = shared;
    }
    
    
    
}
