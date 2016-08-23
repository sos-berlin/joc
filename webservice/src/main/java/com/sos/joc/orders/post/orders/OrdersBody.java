package com.sos.joc.orders.post.orders;

import java.util.ArrayList;
import java.util.List;

public class OrdersBody {

    private String jobschedulerId;
    private List<Order> orders = new ArrayList<Order>();
    private Boolean compact = false;
    private String regex;
    private List<ProcessingState> processingState = new ArrayList<ProcessingState>();
    private List<Type> type = new ArrayList<Type>();
    private String dateFrom;
    private String dateTo;
    private String timeZone;
    private List<Folder> folders = new ArrayList<Folder>();

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Boolean getCompact() {
        return compact;
    }

    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<ProcessingState> getProcessingState() {
        return processingState;
    }

    public void setProcessingState(List<ProcessingState> processingState) {
        this.processingState = processingState;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

}