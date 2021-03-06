package com.sos.joc.jobchains.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobChainAudit;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobchains.resource.IJobChainsResourceModifyJobChains;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

@Path("job_chains")
public class JobChainsResourceModifyJobChainsImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChains {

	private static final String UNSTOP = "unstop";
	private static final String STOP = "stop";
	private static String API_CALL = "./job_chains/";
	private List<Err419> listOfErrors = new ArrayList<Err419>();

	@Override
    public JOCDefaultResponse postJobChainsStop(String accessToken,  byte[] modifyJobChainsBytes) {
		try {
		    JsonValidator.validateFailFast(modifyJobChainsBytes, ModifyJobChains.class);
		    ModifyJobChains modifyJobChains = Globals.objectMapper.readValue(modifyJobChainsBytes, ModifyJobChains.class);
            
            return postJobChainsCommand(STOP, accessToken,
					getPermissonsJocCockpit(modifyJobChains.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isStop(),
					modifyJobChains);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
    public JOCDefaultResponse postJobChainsUnStop(String accessToken, byte[] modifyJobChainsBytes) {
		try {
		    JsonValidator.validateFailFast(modifyJobChainsBytes, ModifyJobChains.class);
            ModifyJobChains modifyJobChains = Globals.objectMapper.readValue(modifyJobChainsBytes, ModifyJobChains.class);
            
            return postJobChainsCommand(UNSTOP, accessToken,
					getPermissonsJocCockpit(modifyJobChains.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isUnstop(),
					modifyJobChains);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	private JOCDefaultResponse postJobChainsCommand(String command, String accessToken, boolean permission,
			ModifyJobChains modifyJobChains) throws Exception {
		JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyJobChains, accessToken,
				modifyJobChains.getJobschedulerId(), permission);
		if (jocDefaultResponse != null) {
			return jocDefaultResponse;
		}
		checkRequiredComment(modifyJobChains.getAuditLog());
		if (modifyJobChains.getJobChains().size() == 0) {
			throw new JocMissingRequiredParameterException("undefined 'jobChains'");
		}
		
		Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
		Date surveyDate = new Date();
		for (ModifyJobChain jobChain : modifyJobChains.getJobChains()) {
			surveyDate = executeModifyJobChainCommand(jobChain, modifyJobChains, command, permittedFolders);
		}
		if (listOfErrors.size() > 0) {
			return JOCDefaultResponse.responseStatus419(listOfErrors);
		}
		return JOCDefaultResponse.responseStatusJSOk(surveyDate);
	}

	private Date executeModifyJobChainCommand(ModifyJobChain jobChain, ModifyJobChains modifyJobChains, String cmd, Set<Folder> permittedFolders) {
		try {
			ModifyJobChainAudit jobChainAudit = new ModifyJobChainAudit(jobChain, modifyJobChains);
			logAuditMessage(jobChainAudit);

			checkRequiredParameter("jobChain", jobChain.getJobChain());
			checkFolderPermissions(jobChain.getJobChain(), permittedFolders);
			
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			XMLBuilder xml = new XMLBuilder("job_chain.modify");
			xml.addAttribute("job_chain", normalizePath(jobChain.getJobChain()));
			switch (cmd) {
			case STOP:
				xml.addAttribute("state", "stopped");
				break;
			case UNSTOP:
				xml.addAttribute("state", "running");
				break;
			}
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
			storeAuditLogEntry(jobChainAudit);

			return jocXmlCommand.getSurveyDate();
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), jobChain));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), jobChain));
		}
		return null;
	}
}
