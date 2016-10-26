package com.sos.joc.jobchainnodes.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobchainnodes.resources.IJobChainsResourceModifyJobChainNodes;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;
import com.sos.scheduler.model.commands.JSCmdJobChainNodeModify;
import com.sos.scheduler.model.objects.JobChainNodeAction;

@Path("job_chain_nodes")
public class JobChainsResourceModifyJobChainNodesImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChainNodes {

    private static final String ACTIVATE = "activate";
    private static final String SKIP = "skip";
    private static final String STOP = "stop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceModifyJobChainNodesImpl.class);
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private static String API_CALL = "./job_chain_nodes/";

    @Override
    public JOCDefaultResponse postJobChainNodesStop(String accessToken, ModifyJobChainNodes modifyNodes) {
        API_CALL += STOP;
        LOGGER.debug(API_CALL);
        try {
            return postJobChainNodesCommands(accessToken, STOP, getPermissons(accessToken).getJobChain().isStopJobChainNode(), modifyNodes);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobChainNodesSkip(String accessToken, ModifyJobChainNodes modifyNodes) {
        API_CALL += SKIP;
        LOGGER.debug(API_CALL);
        try {
            return postJobChainNodesCommands(accessToken, SKIP, getPermissons(accessToken).getJobChain().isSkipJobChainNode(), modifyNodes);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    public JOCDefaultResponse postJobChainNodesActivate(String accessToken, ModifyJobChainNodes modifyNodes) {
        API_CALL += ACTIVATE;
        LOGGER.debug(API_CALL);
        try {
            return postJobChainNodesCommands(accessToken, ACTIVATE, getPermissons(accessToken).getJobChain().isUnskipJobChainNode() || getPermissons(
                    accessToken).getJobChain().isUnstopJobChainNode(), modifyNodes);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyNodes));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private JOCDefaultResponse postJobChainNodesCommands(String accessToken, String command, boolean permission, ModifyJobChainNodes jobChainNodes) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainNodes.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        if (jobChainNodes.getNodes().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'nodes'");
        }
        Date surveyDate = new Date();
        for (ModifyJobChainNode jobChainNode : jobChainNodes.getNodes()) {
            surveyDate = executeModifyJobChainNodeCommand(jobChainNode, command);
        }
        if (listOfErrors.size() > 0) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobChainNodes));
            return JOCDefaultResponse.responseStatus419(listOfErrors, err);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }

    private Date executeModifyJobChainNodeCommand(ModifyJobChainNode jobChainNode, String cmd) {
        try {
            checkRequiredParameter("jobChain", jobChainNode.getJobChain());
            checkRequiredParameter("node", jobChainNode.getNode());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdJobChainNodeModify jsCmdJobChainNodeModify = Globals.schedulerObjectFactory.createJobChainNodeModify();
            jsCmdJobChainNodeModify.setJobChain(jobChainNode.getJobChain());
            jsCmdJobChainNodeModify.setState(jobChainNode.getNode());
            switch (cmd) {
            case STOP:
                jsCmdJobChainNodeModify.setAction(JobChainNodeAction.STOP);
                break;
            case SKIP:
                jsCmdJobChainNodeModify.setAction(JobChainNodeAction.NEXT_STATE);
                break;
            case ACTIVATE:
                jsCmdJobChainNodeModify.setAction(JobChainNodeAction.PROCESS);
                break;
            }
            String xml = jsCmdJobChainNodeModify.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, jobChainNode));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, jobChainNode));
        }
        return null;
    }
}
