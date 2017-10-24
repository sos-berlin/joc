package com.sos.joc.exceptions;

import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.order.ModifyOrder;


public class BulkError extends Err419 {
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkError.class);
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);
    private static final String ERROR_CODE = "JOC-419";

    public BulkError() {
        setSurveyDate(new Date());
    }
    
    public Err419 get(JocException e, JocError jocError, String path) {
        setCodeAndMessage(e, jocError);
        setPath(path);
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, String path) {
        setCodeAndMessage(e, jocError);
        setPath(path);
        return this;
    }
    
    public Err419 get(JocException e, JocError jocError, ModifyOrder order) {
        setCodeAndMessage(e, jocError);
        setPath(order.getJobChain(), order.getOrderId());
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, ModifyOrder order) {
        setCodeAndMessage(e, jocError);
        setPath(order.getJobChain(), order.getOrderId());
        return this;
    }
    
    public Err419 get(JocException e, JocError jocError, ModifyJob job) {
        setCodeAndMessage(e, jocError);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, ModifyJob job) {
        setCodeAndMessage(e, jocError);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, @SuppressWarnings("rawtypes") JAXBElement j) {
        setCodeAndMessage(e, jocError);
        setPath(j.getClass().getName());
        return this;
    }
        
    public Err419 get(JocException e, JocError jocError, StartJob job) {
        setCodeAndMessage(e, jocError);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, StartJob job) {
        setCodeAndMessage(e, jocError);
        setPath(job.getJob());
        return this;
    }
    
    public Err419 get(JocException e, JocError jocError, ModifyJobChain jobChain) {
        setCodeAndMessage(e, jocError);
        setPath(jobChain.getJobChain());
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, ModifyJobChain jobChain) {
        setCodeAndMessage(e, jocError);
        setPath(jobChain.getJobChain());
        return this;
    }
    
    public Err419 get(JocException e, JocError jocError, ModifyJobChainNode node) {
        setCodeAndMessage(e, jocError);
        setPath(node);
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, ModifyJobChainNode node) {
        setCodeAndMessage(e, jocError);
        setPath(node);
        return this;
    }
    
    public Err419 get(JocException e, JocError jocError, TasksFilter job, TaskId taskId) {
        setCodeAndMessage(e, jocError);
        setPath(job, taskId);
        return this;
    }
    
    public Err419 get(Throwable e, JocError jocError, TasksFilter job, TaskId taskId) {
        setCodeAndMessage(e, jocError);
        setPath(job, taskId);
        return this;
    }
    
    private void setCodeAndMessage(JocException e, JocError jocError) {
        if (e instanceof JobSchedulerBadRequestException) {
            setSurveyDate(((JobSchedulerBadRequestException) e).getSurveyDate());
        }
        JocError err = e.getError();
        setCode(err.getCode());
        setMessage(err.getMessage());
        printMetaInfo(jocError);
        LOGGER.error(getMessage(),e);
        AUDIT_LOGGER.error(err.getMessage());
    }
    
    private void setCodeAndMessage(Throwable e, JocError jocError) {
        setCode(ERROR_CODE);
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
        setMessage(errorMsg);
        printMetaInfo(jocError);
        LOGGER.error(e.getMessage(),e);
        AUDIT_LOGGER.error(errorMsg);
    }
    
    private void setPath(String jobChain, String orderId) {
        StringBuilder path = new StringBuilder().append(jobChain);
        if (orderId != null) {
            path.append(",").append(orderId);
        }
        setPath(path.toString());
    }
    
    private void printMetaInfo(JocError jocError) {
        String metaInfo = jocError.printMetaInfo();
        if (!metaInfo.isEmpty()) {
            LOGGER.info(metaInfo);
            jocError.getMetaInfo().clear();
        }
    }
    
    private void setPath(ModifyJobChainNode node) {
        StringBuilder path = new StringBuilder().append(node.getJobChain()).append(",").append(node.getNode());
        setPath(path.toString());
    }
    
    private void setPath(TasksFilter job, TaskId taskId) {
        StringBuilder path = new StringBuilder().append(job.getJob());
        if (taskId != null) {
            path.append(",").append(taskId.getTaskId());
        }
        setPath(path.toString());
    }
}
