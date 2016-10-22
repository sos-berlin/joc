package com.sos.joc.jobchainnodes.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchainnodes.resources.IJobChainsResourceModifyJobChainNodes;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;
import com.sos.scheduler.model.commands.JSCmdJobChainNodeModify;

@Path("job_chain_nodes")
public class JobChainsResourceModifyJobChainNodesImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChainNodes {
    private static final String ACTIVATE = "process";
    private static final String SKIP = "next_state";
    private static final String STOP = "stop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceModifyJobChainNodesImpl.class);

    private JOCDefaultResponse executeModifyJobChainNodeCommand(ModifyJobChainNode jobChainNode, String cmd) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdJobChainNodeModify jsCmdJobChainNodeModify = Globals.schedulerObjectFactory.createJobChainNodeModify();
            jsCmdJobChainNodeModify.setJobChainIfNotEmpty(jobChainNode.getJobChain());
            jsCmdJobChainNodeModify.setStateIfNotEmpty(jobChainNode.getNode());
            jsCmdJobChainNodeModify.setActionIfNotEmpty(cmd);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdJobChainNodeModify);
            jocXmlCommand.executePost(xml);
            jocXmlCommand.throwJobSchedulerError();
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private JOCDefaultResponse postJobChainNodesCommands(String accessToken, String command, boolean permission, ModifyJobChainNodes jobChainNodes) {
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, jobChainNodes.getJobschedulerId(), permission);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception ee) {
            return JOCDefaultResponse.responseStatusJSError(ee);
        }
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        for (ModifyJobChainNode jobChainNode : jobChainNodes.getNodes()) {
            jocDefaultResponse = executeModifyJobChainNodeCommand(jobChainNode, command);
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobChainNodesStop(String accessToken, ModifyJobChainNodes modifyNodes) {
        LOGGER.debug("init job_chain_nodes/stop");
        try {
            return postJobChainNodesCommands(accessToken, STOP, getPermissons(accessToken).getJobChain().isStopJobChainNode(), modifyNodes);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }

    @Override
    public JOCDefaultResponse postJobChainNodesSkip(String accessToken, ModifyJobChainNodes modifyNodes) {
        LOGGER.debug("init job_chain_nodes/skip");
        try {
            return postJobChainNodesCommands(accessToken, SKIP, getPermissons(accessToken).getJobChain().isSkipJobChainNode(), modifyNodes);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    public JOCDefaultResponse postJobChainNodesActivate(String accessToken, ModifyJobChainNodes modifyNodes) {
        LOGGER.debug("init job_chain_nodes/activate");
        try {
            return postJobChainNodesCommands(accessToken, ACTIVATE, getPermissons(accessToken).getJobChain().isUnskipJobChainNode() || getPermissons(accessToken).getJobChain()
                    .isUnstopJobChainNode(), modifyNodes);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }

}
