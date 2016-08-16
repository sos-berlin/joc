package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics;
import com.sos.joc.model.jobscheduler.JobChains;
import com.sos.joc.model.jobscheduler.Jobs;
import com.sos.joc.model.jobscheduler.Orders;
import com.sos.joc.model.jobscheduler.StatisticsSchema;
import com.sos.joc.model.jobscheduler.Tasks;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceStatisticsImpl extends JOCResourceImpl implements IJobSchedulerResourceStatistics {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    private JobschedulerStatisticsResponse getStatistics(String schedulerId, String accessToken) {
        JobschedulerStatisticsResponse jobschedulerStatisticsResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);
        
        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return JobschedulerStatisticsResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JobschedulerStatisticsResponse.responseStatus440(JocCockpitResponse.getError401Schema(jobschedulerUser,e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }


        if (!getPermissons().getJobschedulerMaster().getView().isStatus()){
            return JobschedulerStatisticsResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (schedulerId == null) {
            return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
          
            if (dbItemInventoryInstance == null) {
                return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerIdentifier.getSchedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }
            
            StatisticsSchema entity = new StatisticsSchema();
            entity.setDeliveryDate(new Date());
            Jobs jobschedulerJobs = new Jobs();
            Tasks jobschedulerTasks = new Tasks();
            Orders jobschedulerOrders = new Orders();
            JobChains jobschedulerJobChains = new JobChains();

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.excutePost(" <subsystem.show what=\"statistics\"/>");

            Date surveyDate = jocXmlCommand.getSurveyDate();
            if (surveyDate != null) {
                entity.setSurveyDate(jocXmlCommand.getSurveyDate());
            }

            jocXmlCommand.executeXPath("//subsystem[@name='job']//file_based.statistics");
            jobschedulerJobs.setAny(jocXmlCommand.getAttributAsIntegerOr0("count"));
            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@need_process='true']");
            jobschedulerJobs.setNeedProcess(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='running']");
            jobschedulerJobs.setRunning(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='stopped']");
            jobschedulerJobs.setStopped(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='exist']");
            jobschedulerTasks.setAny(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='running']");
            jobschedulerTasks.setRunning(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='any']");
            jobschedulerOrders.setAny(jocXmlCommand.getAttributAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='clustered']");
            jobschedulerOrders.setClustered(jocXmlCommand.getAttributAsIntegerOr0("count"));

            //TODO JOC Cockpit Webservice

            jobschedulerOrders.setSuspended(-1);
            jobschedulerOrders.setBlacklist(-1);
            jobschedulerOrders.setPending(-1);
            jobschedulerOrders.setWaitingForResource(-1);
            jobschedulerOrders.setRunning(-1);
            jobschedulerOrders.setSetback(-1);

            jobschedulerJobChains.setAny(-1);
            jobschedulerJobChains.setStopped(-1);

            entity.setJobs(jobschedulerJobs);
            entity.setTasks(jobschedulerTasks);
            entity.setOrders(jobschedulerOrders);
            entity.setJobChains(jobschedulerJobChains);

            jobschedulerStatisticsResponse = JobschedulerStatisticsResponse.responseStatus200(entity);

            return jobschedulerStatisticsResponse;
        } catch (Exception e) {

            return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

    @Override
    public JobschedulerStatisticsResponse getJobschedulerStatistics(String schedulerId, String accessToken) throws Exception {
        return getStatistics(schedulerId, accessToken);
    }

    @Override
    public JobschedulerStatisticsResponse postJobschedulerStatistics(String accessToken, JobSchedulerDefaultBody jobSchedulerStatisticsBody) throws Exception {
        return getStatistics(jobSchedulerStatisticsBody.getJobschedulerId(), accessToken);
    }

}
