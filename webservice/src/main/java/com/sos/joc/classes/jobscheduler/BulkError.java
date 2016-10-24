package com.sos.joc.classes.jobscheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.order.ModifyOrder;


public class BulkError extends Err419 {
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkError.class);
    private static final String JOC_419 = "JOC-419";

    public BulkError() {
        setSurveyDate(new Date());
    }
    
    public Err419 get(JocException e, ModifyOrder order) {
        setCodeAndMessage(e);
        setPath(order);
        return this;
    }
    
    public Err419 get(Throwable e, ModifyOrder order) {
        setCodeAndMessage(e);
        setPath(order);
        return this;
    }
    
    public Err419 get(JocException e, ModifyJob job) {
        setCodeAndMessage(e);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(Throwable e, ModifyJob job) {
        setCodeAndMessage(e);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(JocException e, StartJob job) {
        setCodeAndMessage(e);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(Throwable e, StartJob job) {
        setCodeAndMessage(e);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(JocException e, ModifyJobChain jobChain) {
        setCodeAndMessage(e);
        setPath(jobChain.getJobChain());
        return this;
    }
    
    public Err419 get(Throwable e, ModifyJobChain jobChain) {
        setCodeAndMessage(e);
        setPath(jobChain.getJobChain());
        return this;
    }
    
    private void setCodeAndMessage(JocException e) {
        if (e instanceof JobSchedulerBadRequestException) {
            setSurveyDate(((JobSchedulerBadRequestException) e).getSurveyDate());
        }
        setCode(e.getError().getCode());
        setMessage(e.getError().getMessage());
        LOGGER.error(getMessage());
    }
    
    private void setCodeAndMessage(Throwable e) {
        setCode(JOC_419);
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
        setMessage(errorMsg);
        LOGGER.error(getMessage());
    }
    
    private void setPath(ModifyOrder order) {
        StringBuilder path = new StringBuilder().append(order.getJobChain());
        if (order.getOrderId() != null) {
            path.append(",").append(order.getOrderId());
        }
        setPath(order.getJobChain());
    }
}
