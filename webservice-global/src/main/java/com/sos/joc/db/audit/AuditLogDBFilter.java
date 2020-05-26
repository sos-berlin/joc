package com.sos.joc.db.audit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;

public class AuditLogDBFilter {

	private Date createdFrom;
	private Date createdTo;
	private String schedulerId = "";
	private List<String> listOfJobs;
	private List<OrderPath> listOfOrders;
	private List<Folder> listOfFolders;
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

	public void setListOfFolders(List<Folder> listOfFolders) {
		this.listOfFolders = listOfFolders;
	}

	public void addFolderPaths(Set<Folder> folders) {
		if (listOfFolders == null) {
			listOfFolders = new ArrayList<Folder>();
		}
		if (folders != null) {
			for (Folder f : folders) {
				listOfFolders.add(f);
			}
		}
	}

	public void addOrder(OrderPath orderPath) {
		if (listOfOrders == null) {
			listOfOrders = new ArrayList<OrderPath>();
		}
		if (orderPath != null) {
			listOfOrders.add(orderPath);
		}
	}

	public void addFolderPath(Folder folder) {
		if (listOfFolders == null) {
			listOfFolders = new ArrayList<Folder>();
		}
		if (folder != null) {
			listOfFolders.add(folder);
		}
	}

	public void addJob(String job) {
		if (listOfJobs == null) {
			listOfJobs = new ArrayList<String>();
		}
		if (job != null) {
			listOfJobs.add(job);
		}
	}

	public void addCalendar(String calendar) {
		if (listOfCalendars == null) {
			listOfCalendars = new ArrayList<String>();
		}
		if (calendar != null) {
			listOfCalendars.add(calendar);
		}
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(final String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public List<OrderPath>  getListOfOrders() {
		return listOfOrders;
	}

	public void setListOfOrders(List<OrderPath>  listOfOrders) {
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

	public List<String> getListOfJobs() {
		return listOfJobs;
	}

	public void setListOfJobs(List<String> listOfJobs) {
		this.listOfJobs = listOfJobs;
	}

 
	public List<Folder> getListOfFolders() {
		return listOfFolders;
	}

	public List<String> getStringListOfFolders() {
		return listOfFolders.stream().map(Folder::getFolder).collect(Collectors.toList());
	}

    public Set<String> getRequests() {
        return requests;
    }

    public void setRequests(Collection<String> request) {
        if (request != null) {
            this.requests = request.stream().map(r -> r.replaceFirst("^(\\.?/)?", "./")).collect(Collectors.toSet());
        } else {
            this.requests = null;
        }
    }
	 
}