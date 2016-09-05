package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.JobChains;
import com.sos.joc.model.jobscheduler.Jobs;
import com.sos.joc.model.jobscheduler.Orders;
import com.sos.joc.model.jobscheduler.StatisticsSchema;
import com.sos.joc.model.jobscheduler.Tasks;

@Path("jobscheduler")
public class JobSchedulerResourceStatisticsImpl extends JOCResourceImpl implements IJobSchedulerResourceStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerStatistics(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) throws Exception {

        LOGGER.debug("init Statistics");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
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
            jobschedulerJobs.setAny(jocXmlCommand.getAttributeAsIntegerOr0("count"));
            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@need_process='true']");
            jobschedulerJobs.setNeedProcess(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='running']");
            jobschedulerJobs.setRunning(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='stopped']");
            jobschedulerJobs.setStopped(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='exist']");
            jobschedulerTasks.setAny(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='task']//task.statistic[@task_state='running']");
            jobschedulerTasks.setRunning(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='any']");
            jobschedulerOrders.setAny(jocXmlCommand.getAttributeAsIntegerOr0("count"));

            jocXmlCommand.executeXPath("//subsystem[@name='order']//order.statistic[@order_state='clustered']");
            jobschedulerOrders.setClustered(jocXmlCommand.getAttributeAsIntegerOr0("count"));

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

 
}
