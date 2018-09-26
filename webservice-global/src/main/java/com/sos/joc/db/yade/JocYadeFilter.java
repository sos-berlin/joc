package com.sos.joc.db.yade;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class JocYadeFilter{

    private String jobschedulerId;
    private List<Long> transferIds; 
    private Set<Integer> operations; 
    private Set<Integer> states;
    private String mandator; 
    private Set<String> sourceHosts; 
    private Set<Integer> sourceProtocols; 
    private Set<String> targetHosts; 
    private Set<Integer> targetProtocols; 
    private Boolean isIntervention; 
    private Boolean hasInterventions; 
    private List<String> profiles; 
    private Integer limit; 
    private Date dateFrom; 
    private Date dateTo;
    
    public String getJobschedulerId() {
        return jobschedulerId;
    }
    
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }
    
    public List<Long> getTransferIds() {
        return transferIds;
    }
    
    public void setTransferIds(List<Long> transferIds) {
        this.transferIds = transferIds;
    }
    
    public Set<Integer> getOperations() {
        return operations;
    }
    
    public void setOperations(Set<Integer> operations) {
        this.operations = operations;
    }
    
    public Set<Integer> getStates() {
        return states;
    }
    
    public void setStates(Set<Integer> states) {
        this.states = states;
    }
    
    public String getMandator() {
        return mandator;
    }
    
    public void setMandator(String mandator) {
        this.mandator = mandator;
    }
    
    public Set<String> getSourceHosts() {
        return sourceHosts;
    }
    
    public void setSourceHosts(Set<String> sourceHosts) {
        this.sourceHosts = sourceHosts;
    }
    
    public Set<Integer> getSourceProtocols() {
        return sourceProtocols;
    }
    
    public void setSourceProtocols(Set<Integer> sourceProtocols) {
        this.sourceProtocols = sourceProtocols;
    }
    
    public Set<String> getTargetHosts() {
        return targetHosts;
    }
    
    public void setTargetHosts(Set<String> targetHosts) {
        this.targetHosts = targetHosts;
    }
    
    public Set<Integer> getTargetProtocols() {
        return targetProtocols;
    }
    
    public void setTargetProtocols(Set<Integer> targetProtocols) {
        this.targetProtocols = targetProtocols;
    }
    
    public Boolean getIsIntervention() {
        return isIntervention;
    }
    
    public void setIsIntervention(Boolean isIntervention) {
        this.isIntervention = isIntervention;
    }
    
    public Boolean getHasInterventions() {
        return hasInterventions;
    }
    
    public void setHasInterventions(Boolean hasInterventions) {
        this.hasInterventions = hasInterventions;
    }
    
    public List<String> getProfiles() {
        return profiles;
    }
    
    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public Date getDateFrom() {
        return dateFrom;
    }
    
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    public Date getDateTo() {
        return dateTo;
    }
    
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
}
