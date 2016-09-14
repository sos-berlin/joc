package com.sos.joc.job.impl;

import java.math.BigInteger;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobRunTimeResource;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.job.JobFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJob;
 
@Path("job")
public class JobRunTimeResourceImpl extends JOCResourceImpl implements IJobRunTimeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobRunTime(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {
        LOGGER.debug("init job/run_time");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            Runtime200Schema runTimeAnswer = new Runtime200Schema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("job", jobFilterSchema.getJob())) {
                runTimeAnswer = RunTime.set(jocXmlCommand, createJobRuntimePostCommand(jobFilterSchema), "//job/run_time");
            }
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
    
    private String createJobRuntimePostCommand(JobFilterSchema body) {

        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        showJob.setWhat("run_time");
        showJob.setJob(body.getJob());
        showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }
  
}
