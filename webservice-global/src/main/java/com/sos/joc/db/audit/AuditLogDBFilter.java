package com.sos.joc.db.audit;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.audit.JobPath;
import com.sos.joc.model.audit.JobStreamPath;
import com.sos.joc.model.audit.OrderPath;
import com.sos.joc.model.common.Folder;


public class AuditLogDBFilter {

    private Date createdFrom;
    private Date createdTo;
    private String schedulerId = "";
    private Set<String> listOfJobs;
    private Set<String> listOfJobStreams;
    private List<OrderPath> listOfOrders;
    private Set<String> listOfFolders;
    private List<String> listOfCalendars;
    private String ticketLink;
    private String account;
    private String reason;
    private Set<String> requests;

    public AuditLogDBFilter() {
        super();
    }

    public AuditLogDBFilter(AuditLogFilter auditLogFilter) throws JobSchedulerInvalidResponseDataException {
        setSchedulerId(auditLogFilter.getJobschedulerId());
        setTicketLink(auditLogFilter.getTicketLink());
        setAccount(auditLogFilter.getAccount());
        setRequests(auditLogFilter.getRequests());
        setCreatedFrom(JobSchedulerDate.getDateFrom(auditLogFilter.getDateFrom(), auditLogFilter.getTimeZone()));
        setCreatedTo(JobSchedulerDate.getDateTo(auditLogFilter.getDateTo(), auditLogFilter.getTimeZone()));
        setListOfFolders(auditLogFilter.getFolders());
        setListOfJobs(auditLogFilter.getJobs());
        setListOfJobStreams(auditLogFilter.getJobStreams());
        setListOfCalendars(auditLogFilter.getCalendars());
        setListOfOrders(auditLogFilter.getOrders());
    }

    public List<String> getListOfCalendars() {
        return listOfCalendars;
    }

    public void setListOfCalendars(List<String> listOfCalendars) {
        this.listOfCalendars = listOfCalendars;
    }

    public String getTicketLink() {
        return ticketLink;
    }

    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setListOfFolders(Collection<Folder> listOfFolders) {
        if (listOfFolders != null) {
            this.listOfFolders = listOfFolders.stream().map(Folder::getFolder).collect(Collectors.toSet());
        } else {
            this.listOfFolders = null;
        }
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(final String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public List<OrderPath> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<OrderPath> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public Set<String> getListOfJobs() {
        return listOfJobs;
    }

    public Set<String> getListOfJobStreams() {
        return listOfJobStreams;
    }

    public void setListOfJobStreams(Collection<JobStreamPath> listOfJobStreams) {
        if (listOfJobStreams != null) {
            this.listOfJobStreams = listOfJobStreams.stream().map(JobStreamPath::getJobStream).collect(Collectors.toSet());
        } else {
            this.listOfJobStreams = null;
        }
    }
    
    public void setListOfJobs(Collection<JobPath> listOfJobs) {
        if (listOfJobs != null) {
            this.listOfJobs = listOfJobs.stream().map(JobPath::getJob).collect(Collectors.toSet());
        } else {
            this.listOfJobs = null;
        }
    }

    public Set<String> getListOfFolders() {
        return listOfFolders;
    }

    public Set<String> getRequests() {
        return requests;
    }

    public void setRequests(Collection<String> request) {
        if (request != null) {
            // this.requests = request.stream().map(r -> r.replaceFirst("^(\\.?/)?", "./")).collect(Collectors.toSet());
            this.requests = request.stream().collect(Collectors.toSet());
        } else {
            this.requests = null;
        }
    }

}