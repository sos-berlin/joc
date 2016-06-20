package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.joc.classes.SOSJobschedulerUser;
import com.sos.joc.jobscheduler.model.FiltersForDateFromAndDateTo;
import com.sos.joc.jobscheduler.model.Jobscheduler;
import com.sos.joc.jobscheduler.model.JobschedulerStatistics;
import com.sos.joc.jobscheduler.model.JobschedulerStatisticsJobChains;
import com.sos.joc.jobscheduler.model.JobschedulerStatisticsJobs;
import com.sos.joc.jobscheduler.model.JobschedulerStatisticsOrders;
import com.sos.joc.jobscheduler.model.JobschedulerStatisticsTasks;
import com.sos.joc.jobscheduler.model.JobschedulerVolatilePart;
import com.sos.joc.jobscheduler.post.JobSchedulerStatisticsBody;
import com.sos.joc.jobscheduler.resource.IJobschedulerResource;
import com.sos.joc.jobscheduler.resource.IJobschedulerResourceStatistics;
import com.sos.xml.SOSXmlCommand;

@Path("jobscheduler")
public class JobschedulerResourceStatisticsImpl implements IJobschedulerResourceStatistics {

    private GetJobschedulerStatisticsResponse getStatistics(String host, Long port, String accessToken) throws Exception {
        GetJobschedulerStatisticsResponse jobschedulerStatisticsResponse;
        SOSJobschedulerUser sosJobschedulerUser = new SOSJobschedulerUser(accessToken);
        SOSShiroCurrentUser sosShiroCurrentUser = SOSServicePermissionShiro.currentUsersList.getUser(accessToken);
        if (!sosJobschedulerUser.isAuthenticated()) {
            jobschedulerStatisticsResponse = GetJobschedulerStatisticsResponse.responseStatus420(new JobschedulerStatistics());
        } else {
            JobschedulerStatistics entity = new JobschedulerStatistics();
            entity.setDeliveryDate(new Date());
            JobschedulerStatisticsJobs jobschedulerJobs = new JobschedulerStatisticsJobs();
            JobschedulerStatisticsTasks jobschedulerTasks = new JobschedulerStatisticsTasks();
            JobschedulerStatisticsOrders jobschedulerOrders = new JobschedulerStatisticsOrders();
            JobschedulerStatisticsJobChains jobschedulerJobChains = new JobschedulerStatisticsJobChains();

            SOSXmlCommand sosXmlCommand = new SOSXmlCommand(host, port);
            String response = sosXmlCommand.excutePost(" <subsystem.show what=\"statistics\"/>");

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
            jobschedulerOrders.setQueued(-1);
            jobschedulerOrders.setRunning(-1);
        //   jobschedulerOrders.setSetback(-1);

            jobschedulerJobChains.setAny(-1);
            jobschedulerJobChains.setStopped(-1);

            entity.setJobs(jobschedulerJobs);
            entity.setTasks(jobschedulerTasks);
            entity.setOrders(jobschedulerOrders);
            entity.setJobChains(jobschedulerJobChains);

            jobschedulerStatisticsResponse = GetJobschedulerStatisticsResponse.responseStatus200(entity);
        }
        return jobschedulerStatisticsResponse;
    }

    @Override
    public GetJobschedulerStatisticsResponse getJobschedulerStatistics(String host, Long port, String accessToken) throws Exception {
        return getStatistics(host, port, accessToken);
    }

    @Override
    public GetJobschedulerStatisticsResponse postJobschedulerStatistics(JobSchedulerStatisticsBody jobSchedulerStatisticsBody) throws Exception {
        return getStatistics(jobSchedulerStatisticsBody.getHost(), jobSchedulerStatisticsBody.getPort(), jobSchedulerStatisticsBody.getAccessToken());
    }

}
