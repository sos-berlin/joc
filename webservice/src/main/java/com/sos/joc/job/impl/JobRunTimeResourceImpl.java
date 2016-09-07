package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.job.resource.IJobRunTimeResource;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.common.RuntimeSchema;
import com.sos.joc.model.job.JobFilterSchema;
 
@Path("job")
public class JobRunTimeResourceImpl extends JOCResourceImpl implements IJobRunTimeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobRunTime(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {
        LOGGER.debug("init Job RunTime");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            Runtime200Schema runTimeAnswer = new Runtime200Schema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = RunTime.createJobRuntimePostCommand(jobFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            runTimeAnswer.setDeliveryDate(new Date());
            Node runtimeNode = jocXmlCommand.getSosxml().selectSingleNode("//job/run_time");

            RuntimeSchema runTime = new RuntimeSchema();
            runTime.setRunTime(RunTime.getRuntimeXmlString(runtimeNode));
            runTimeAnswer.setRunTime(runTime);
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
  
}
