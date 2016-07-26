package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.SOSJobschedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerStatisticsBody;
import com.sos.joc.jobscheduler.resource.IJobschedulerResourceStatistics;
import com.sos.joc.model.jobscheduler.JobChains;
import com.sos.joc.model.jobscheduler.Jobs;
import com.sos.joc.model.jobscheduler.Orders;
import com.sos.joc.model.jobscheduler.StatisticsSchema;
import com.sos.joc.model.jobscheduler.Tasks;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.xml.SOSXmlCommand;

@Path("jobscheduler")
public class JobschedulerResourceStatisticsImpl implements IJobschedulerResourceStatistics {

    private JobschedulerStatisticsResponse getStatistics(String schedulerId, String accessToken) {

        JobschedulerStatisticsResponse jobschedulerStatisticsResponse;
        SOSJobschedulerUser sosJobschedulerUser = new SOSJobschedulerUser(accessToken);

        if (sosJobschedulerUser.isTimedOut()) {
            return JobschedulerStatisticsResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }


        if (!sosJobschedulerUser.isAuthenticated()) {
            return JobschedulerStatisticsResponse.responseStatus401(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (schedulerId == null) {
            return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {
            SchedulerInstancesDBItem schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance(schedulerId);
            StatisticsSchema entity = new StatisticsSchema();
            entity.setDeliveryDate(new Date());
            Jobs jobschedulerJobs = new Jobs();
            Tasks jobschedulerTasks = new Tasks();
            Orders jobschedulerOrders = new Orders();
            JobChains jobschedulerJobChains = new JobChains();
System.out.println("1");            

            SOSXmlCommand sosXmlCommand = new SOSXmlCommand(schedulerInstancesDBItem.getUrl());
            System.out.println("2");            
            sosXmlCommand.excutePost(" <subsystem.show what=\"statistics\"/>");
            System.out.println("3");            
            
            Date surveyDate = sosXmlCommand.getSurveyDate();
            if (surveyDate != null){
                entity.setSurveyDate(sosXmlCommand.getSurveyDate()); 
            }
            System.out.println("4");            
 
            sosXmlCommand.executeXPath("//subsystem[@name='job']//file_based.statistics");
            jobschedulerJobs.setAny(sosXmlCommand.getAttributAsIntegerOr0("count"));
            sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@need_process='true']");
            jobschedulerJobs.setNeedProcess(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='running']");
            jobschedulerJobs.setRunning(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='stopped']");
            jobschedulerJobs.setStopped(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='exist']");
            jobschedulerTasks.setAny(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='running']");
            jobschedulerTasks.setRunning(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='any']");
            jobschedulerOrders.setAny(sosXmlCommand.getAttributAsIntegerOr0("count"));

            sosXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='clustered']");
            jobschedulerOrders.setClustered(sosXmlCommand.getAttributAsIntegerOr0("count"));

            jobschedulerOrders.setSuspended(-1);
            jobschedulerOrders.setBlacklist(-1);
            jobschedulerOrders.setPending(-1);
            jobschedulerOrders.setWaitingForRessource(-1);
            jobschedulerOrders.setRunning(-1);
            jobschedulerOrders.setSetback(-1);

            jobschedulerJobChains.setAny(-1);
            jobschedulerJobChains.setStopped(-1);

            System.out.println("5");            

            entity.setJobs(jobschedulerJobs);
            entity.setTasks(jobschedulerTasks);
            entity.setOrders(jobschedulerOrders);
            entity.setJobChains(jobschedulerJobChains);

            System.out.println("6");            
jobschedulerStatisticsResponse = JobschedulerStatisticsResponse.responseStatus200(entity);
            System.out.println("7");            

            return jobschedulerStatisticsResponse;
        } catch (Exception e) {
            System.out.println("8");
            System.out.println(e.getMessage());            


            return JobschedulerStatisticsResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

    @Override
    public JobschedulerStatisticsResponse getJobschedulerStatistics(String schedulerId, String accessToken) throws Exception {
        return getStatistics(schedulerId, accessToken);
    }

    @Override
    public JobschedulerStatisticsResponse postJobschedulerStatistics(String accessToken, JobSchedulerStatisticsBody jobSchedulerStatisticsBody) throws Exception {
        return getStatistics(jobSchedulerStatisticsBody.getJobschedulerId(), accessToken);
    }

}
