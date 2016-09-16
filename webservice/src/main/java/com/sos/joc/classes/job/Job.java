package com.sos.joc.classes.job;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.State_.Text;


public class Job {
    
    public static Job_ getJob_(Element jobElem, JOCXmlCommand jocXmlCommand, boolean compact) throws Exception {
        return getJob_(jobElem, jocXmlCommand, compact, false);
    }

    public static Job_ getJob_(Element jobElem, JOCXmlCommand jocXmlCommand, boolean compact, boolean withOrderQueue) throws Exception {
        Job_ job = new Job_();
        job.setSurveyDate(jocXmlCommand.getSurveyDate());
        job.setName(jobElem.getAttribute(WebserviceConstants.NAME));
        job.setPath(jobElem.getAttribute(WebserviceConstants.PATH));
        
        job.setNumOfQueuedTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNodeValue(jobElem, "queued_tasks/@length", "0")));
        job.setLocks(JobsUtils.getLocks_(jocXmlCommand.getSosxml().selectNodeList(jobElem, "lock.requestor/lock.use")));
        
        job.setStateText(jobElem.getAttribute("state_text"));
        
        boolean isOrderJob = "yes".equals(jocXmlCommand.getAttributeValue(jobElem, "order", "no"));
        if (isOrderJob) {
            
//        OrdersSummary ordersSummary = new OrdersSummary();
//        ordersSummary.setPending(-1);
//        ordersSummary.setRunning(-1);
//        ordersSummary.setSetback(-1);
//        ordersSummary.setSuspended(-1);
//        ordersSummary.setWaitingForResource(-1);
//        job.setOrdersSummary(ordersSummary);
        }    
        
        job.setNumOfRunningTasks(Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(jobElem, "tasks/@count", "0")));
        job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(jobElem));
        
        if (job.getNumOfRunningTasks() == Integer.valueOf(jocXmlCommand.getAttributeValue(jobElem, "tasks", "1")) && job.getNumOfQueuedTasks() > 0) {
            
        }
        job.setState(JobsUtils.getOutputState_(jobElem));
        
        if (!compact) {
            job.setAllSteps(Integer.valueOf(jocXmlCommand.getAttributeValue(jobElem, "all_steps", "0")));
            job.setAllTasks(Integer.valueOf(jocXmlCommand.getAttributeValue(jobElem, "all_tasks", "0")));
            job.setParams(Parameters.getParameters(jobElem));
            job.setTaskQueue(JobsUtils.getQueuedTasks(jobElem));
            job.setRunningTasks(JobsUtils.getRunningTasks(jobElem));
            job.setTemporary(WebserviceConstants.YES.equals(jobElem.getAttribute("temporary")));
            
            if (isOrderJob) {
                if (job.getState().getText() == Text.NOT_IN_PERIOD) {
                  //job.setNextPeriodBegin("myNextPeriodBegin");//TODO Joacim?
                }
            } else {
                job.setDelayUntil(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(jobElem, "delay_after_error", null)));
                job.setNextStartTime(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(jobElem, WebserviceConstants.NEXT_START_TIME, null)));
            }
            // TODO: Joacim
//            job.setNextPeriodBegin("myNextPeriodBegin");
            
        }
        if (withOrderQueue) {
            
        } else {
            job.setOrderQueue(null);
        }
        return job;
    }    
}
