package com.sos.joc.jobchains.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobchains.resource.IJobChainsResourceModifyJobChains;
import com.sos.joc.model.jobChain.JobChain_____;
import com.sos.joc.model.jobChain.ModifySchema;
import com.sos.scheduler.model.commands.JSCmdJobChainModify;
import com.sos.scheduler.model.commands.JSCmdJobChainModify.enu4State;

@Path("job_chains")
public class JobChainsResourceModifyJobChainsImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChains {
    private static final String UNSTOP = "unstop";
    private static final String STOP = "stop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceModifyJobChainsImpl.class);  
     
    
    private JOCDefaultResponse executeModifyJobChainCommand(String jobChain,String cmd) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdJobChainModify jsCmdJobChainModify = new JSCmdJobChainModify(Globals.schedulerObjectFactory);
            jsCmdJobChainModify.setJobChainIfNotEmpty(jobChain);
            if (STOP.equals(cmd)){
                jsCmdJobChainModify.setState(enu4State.STOPPED);
             }
            if (UNSTOP.equals(cmd)){
                jsCmdJobChainModify.setState(enu4State.RUNNING);
             }

            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdJobChainModify);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing jobchain.%s %s:%s",cmd, e.getCause(), e.getMessage()));
        }
    }


    private JOCDefaultResponse postJobChainsCommand(String command, String accessToken, boolean permission, ModifySchema  modifySchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, modifySchema.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (JobChain_____ jobChain : modifySchema.getJobChains()){
                jocDefaultResponse = executeModifyJobChainCommand(jobChain.getJobChain(), command);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }
    
    
    
    @Override
    public JOCDefaultResponse postJobChainsStop(String accessToken, ModifySchema  modifySchema) throws Exception {
        LOGGER.debug("init job_chains/stop");
        return postJobChainsCommand(STOP, accessToken, getPermissons(accessToken).getJobChain().isStop(), modifySchema);
    }

    @Override
    public JOCDefaultResponse postJobChainsUnStop(String accessToken, ModifySchema  modifySchema) throws Exception {
        LOGGER.debug("init job_chains/unstop");
        return postJobChainsCommand(UNSTOP, accessToken,getPermissons(accessToken).getJobChain().isUnstop(), modifySchema);
    }


}
