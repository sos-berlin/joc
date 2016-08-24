package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics;
import com.sos.joc.model.jobscheduler.JobChains;
import com.sos.joc.model.jobscheduler.Jobs;
import com.sos.joc.model.jobscheduler.Orders;
import com.sos.joc.model.jobscheduler.StatisticsSchema;
import com.sos.joc.model.jobscheduler.Tasks;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceStatisticsImpl extends JOCResourceImpl implements IJobSchedulerResourceStatistics {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    private JOCDefaultResponse getStatistics(String schedulerId, String accessToken) {

        LOGGER.debug("init Statistics");
        try {
            JOCDefaultResponse jocDefaultResponse = init(schedulerId,getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
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

            // TODO JOC Cockpit Webservice

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

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

    @Override
    public JOCDefaultResponse getJobschedulerStatistics(String schedulerId, String accessToken) throws Exception {
        return getStatistics(schedulerId, accessToken);
    }

    @Override
    public JOCDefaultResponse postJobschedulerStatistics(String accessToken, JobSchedulerDefaultBody jobSchedulerStatisticsBody) throws Exception {
        return getStatistics(jobSchedulerStatisticsBody.getJobschedulerId(), accessToken);
    }

}
