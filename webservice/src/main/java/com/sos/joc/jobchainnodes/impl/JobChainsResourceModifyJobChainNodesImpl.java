package com.sos.joc.jobchainnodes.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobchainnodes.resources.IJobChainsResourceModifyJobChainNodes;
import com.sos.joc.model.jobChain.ModifyNodeSchema;
import com.sos.joc.model.jobChain.Node____;
import com.sos.scheduler.model.commands.JSCmdJobChainNodeModify;

@Path("job_chain_nodes")
public class JobChainsResourceModifyJobChainNodesImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChainNodes {
    private static final String ACTIVATE = "process";
    private static final String SKIP = "next_state";
    private static final String STOP = "stop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceModifyJobChainNodesImpl.class);

    private JOCDefaultResponse executeModifyJobChainNodeCommand(Node____ jobChainNode, String cmd) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdJobChainNodeModify jsCmdJobChainNodeModify = Globals.schedulerObjectFactory.createJobChainNodeModify();
            jsCmdJobChainNodeModify.setJobChainIfNotEmpty(jobChainNode.getJobChain());
            jsCmdJobChainNodeModify.setStateIfNotEmpty(jobChainNode.getNode());
            jsCmdJobChainNodeModify.setActionIfNotEmpty(cmd);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdJobChainNodeModify);
            jocXmlCommand.excutePost(xml);
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing jobchain_node.%s %s:%s",cmd, e.getCause(), e.getMessage()));
        }
    }

    private JOCDefaultResponse postJobChainNodesCommands(String accessToken, String command, boolean permission, ModifyNodeSchema modifyNodeSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken,modifyNodeSchema.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Node____ jobChainNode : modifyNodeSchema.getNodes()) {
                jocDefaultResponse = executeModifyJobChainNodeCommand(jobChainNode, command);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postJobChainNodesStop(String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception {
        LOGGER.debug("init job_chain_nodes/stop");
        return postJobChainNodesCommands(accessToken,STOP,getPermissons(accessToken).getJobChain().isStopJobChainNode(), modifyNodeSchema);

    }

    @Override
    public JOCDefaultResponse postJobChainNodesSkip(String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception {
        LOGGER.debug("init job_chain_nodes/skip");
        return postJobChainNodesCommands(accessToken, SKIP, getPermissons(accessToken).getJobChain().isSkipJobChainNode(), modifyNodeSchema);
    }

    public JOCDefaultResponse postJobChainNodesActivate(String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception {
        LOGGER.debug("init job_chain_nodes/activate");
        return postJobChainNodesCommands(accessToken,ACTIVATE, getPermissons(accessToken).getJobChain().isUnskipJobChainNode() || getPermissons(accessToken).getJobChain().isUnstopJobChainNode(), modifyNodeSchema);

    }

}
