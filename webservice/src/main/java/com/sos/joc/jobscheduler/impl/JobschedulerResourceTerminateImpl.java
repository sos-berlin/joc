package com.sos.joc.jobscheduler.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.joc.classes.SOSJobschedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerTerminateBody;
import com.sos.joc.jobscheduler.resource.IJobschedulerResourceTerminate;
import com.sos.joc.model.common.OkSchema;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdTerminate;
import com.sos.scheduler.model.objects.Spooler;
import com.sos.xml.SOSXmlCommand;

@Path("jobscheduler")
public class JobschedulerResourceTerminateImpl implements IJobschedulerResourceTerminate {

    @Override
    public JocCockpitResponse postjobschedulerTerminate(String accessToken, JobSchedulerTerminateBody jobSchedulerTerminateBody) throws Exception {
        SOSJobschedulerUser sosJobschedulerUser = new SOSJobschedulerUser(accessToken);

        if (sosJobschedulerUser.isTimedOut()) {
            return JocCockpitResponse.responseStatus440(accessToken);
        }

        if (!sosJobschedulerUser.isAuthenticated()) {
            return JocCockpitResponse.responseStatus401(accessToken);
        }

        if (jobSchedulerTerminateBody.getJobschedulerId() == null) {
            return JocCockpitResponse.responseStatus420("schedulerId is null");
        }

        try {

            SchedulerInstancesDBItem schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance(jobSchedulerTerminateBody.getJobschedulerId());
            SOSXmlCommand sosXmlCommand = new SOSXmlCommand(schedulerInstancesDBItem.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdTerminate jsCmdTerminate = new JSCmdTerminate(schedulerObjectFactory);
            jsCmdTerminate.setAllSchedulersIfNotEmpty("no");
            jsCmdTerminate.setContinueExclusiveOperationIfNotEmpty("no");
            jsCmdTerminate.setRestartIfNotEmpty("no");
            jsCmdTerminate.setTimeoutIfNotEmpty(jobSchedulerTerminateBody.getTimeoutAsString());
            String xml = schedulerObjectFactory.toXMLString(jsCmdTerminate);
            sosXmlCommand.excutePost(xml);
 
            sosXmlCommand.executeXPath("/spooler/answer");
             return JocCockpitResponse.responseStatus200(sosXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JocCockpitResponse.responseStatus420(e.getMessage());
        }
    }

}
