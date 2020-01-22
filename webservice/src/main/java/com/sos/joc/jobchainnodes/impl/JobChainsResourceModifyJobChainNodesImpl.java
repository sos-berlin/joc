package com.sos.joc.jobchainnodes.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobChainNodeAudit;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobchainnodes.resources.IJobChainsResourceModifyJobChainNodes;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

@Path("job_chain_nodes")
public class JobChainsResourceModifyJobChainNodesImpl extends JOCResourceImpl
		implements IJobChainsResourceModifyJobChainNodes {

	private static final String ACTIVATE = "activate";
	private static final String SKIP = "skip";
	private static final String STOP = "stop";
	private static String API_CALL = "./job_chain_nodes/";
	private List<Err419> listOfErrors = new ArrayList<Err419>();

	@Override
	public JOCDefaultResponse postJobChainNodesStop(String accessToken, byte[] modifyNodesBytes) {
		try {
		    JsonValidator.validateFailFast(modifyNodesBytes, ModifyJobChainNodes.class);
		    ModifyJobChainNodes modifyNodes = Globals.objectMapper.readValue(modifyNodesBytes, ModifyJobChainNodes.class);
            
			return postJobChainNodesCommands(accessToken, STOP,
					getPermissonsJocCockpit(modifyNodes.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isStopJobChainNode(),
					modifyNodes);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobChainNodesSkip(String accessToken, byte[] modifyNodesBytes) {
		try {
		    JsonValidator.validateFailFast(modifyNodesBytes, ModifyJobChainNodes.class);
            ModifyJobChainNodes modifyNodes = Globals.objectMapper.readValue(modifyNodesBytes, ModifyJobChainNodes.class);
            
            return postJobChainNodesCommands(accessToken, SKIP,
					getPermissonsJocCockpit(modifyNodes.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isSkipJobChainNode(),
					modifyNodes);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobChainNodesActivate(String accessToken, byte[] modifyNodesBytes) {
		try {
		    JsonValidator.validateFailFast(modifyNodesBytes, ModifyJobChainNodes.class);
            ModifyJobChainNodes modifyNodes = Globals.objectMapper.readValue(modifyNodesBytes, ModifyJobChainNodes.class);
            
            return postJobChainNodesCommands(accessToken, ACTIVATE,
					getPermissonsJocCockpit(modifyNodes.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isProcessJobChainNode(),
					modifyNodes);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	private JOCDefaultResponse postJobChainNodesCommands(String accessToken, String command, boolean permission,
			ModifyJobChainNodes jobChainNodes) throws Exception {
		JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, jobChainNodes, accessToken,
				jobChainNodes.getJobschedulerId(), permission);
		if (jocDefaultResponse != null) {
			return jocDefaultResponse;
		}
		checkRequiredComment(jobChainNodes.getAuditLog());
		if (jobChainNodes.getNodes().size() == 0) {
			throw new JocMissingRequiredParameterException("undefined 'nodes'");
		}
		Date surveyDate = new Date();
		for (ModifyJobChainNode jobChainNode : jobChainNodes.getNodes()) {
			surveyDate = executeModifyJobChainNodeCommand(jobChainNode, jobChainNodes, command);
		}
		if (listOfErrors.size() > 0) {
			return JOCDefaultResponse.responseStatus419(listOfErrors);
		}
		return JOCDefaultResponse.responseStatusJSOk(surveyDate);
	}

	private Date executeModifyJobChainNodeCommand(ModifyJobChainNode jobChainNode, ModifyJobChainNodes jobChainNodes,
			String cmd) {
		try {
			ModifyJobChainNodeAudit jobChainNodeAudit = new ModifyJobChainNodeAudit(jobChainNode, jobChainNodes);
			logAuditMessage(jobChainNodeAudit);

			checkRequiredParameter("jobChain", jobChainNode.getJobChain());
			checkRequiredParameter("node", jobChainNode.getNode());
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			XMLBuilder xml = new XMLBuilder("job_chain_node.modify");
			xml.addAttribute("job_chain", normalizePath(jobChainNode.getJobChain())).addAttribute("state",
					jobChainNode.getNode());
			switch (cmd) {
			case STOP:
				xml.addAttribute("action", "stop");
				break;
			case SKIP:
				xml.addAttribute("action", "next_state");
				break;
			case ACTIVATE:
				xml.addAttribute("action", "process");
				break;
			}
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
			storeAuditLogEntry(jobChainNodeAudit);

			return jocXmlCommand.getSurveyDate();
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), jobChainNode));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), jobChainNode));
		}
		return null;
	}
}
