package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobSchedulerAudit;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;
import com.sos.schema.JsonValidator;

@Path("jobscheduler")
public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl
		implements IJobSchedulerResourceModifyJobScheduler {

	private static final String[] TERMINATE = { "terminate", "terminate" };
	private static final String[] RESTART = { "restart", "terminate_and_restart" };
	private static final String[] ABORT = { "abort", "abort_immediately" };
	private static final String[] ABORT_AND_RESTART = { "abort_and_restart", "abort_immediately_and_restart" };
	private static final String[] PAUSE = { "pause", "pause" };
	private static final String[] CONTINUE = { "continue", "continue" };
	private static String API_CALL = "./jobscheduler/";

	@Override
	public JOCDefaultResponse postJobschedulerTerminate(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
		    HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
			boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().isTerminate();
			return executeModifyJobSchedulerCommand(TERMINATE, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
            HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
			boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().getRestart().isTerminate();
			return executeModifyJobSchedulerCommand(RESTART, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobschedulerAbort(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
            HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
			boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().isAbort();
			return executeModifyJobSchedulerCommand(ABORT, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobschedulerRestartAbort(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
            HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
			boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().getRestart().isAbort();
			return executeModifyJobSchedulerCommand(ABORT_AND_RESTART, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobschedulerPause(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
            HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
			boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().isPause();
			return executeModifyJobSchedulerCommand(PAUSE, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	@Override
	public JOCDefaultResponse postJobschedulerContinue(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortTimeOutParameter.class);
            HostPortTimeOutParameter urlTimeoutParamSchema = Globals.objectMapper.readValue(filterBytes, HostPortTimeOutParameter.class);
            
            boolean permission = getPermissonsJocCockpit(urlTimeoutParamSchema.getJobschedulerId(), accessToken)
					.getJobschedulerMaster().getExecute().isContinue();
			return executeModifyJobSchedulerCommand(CONTINUE, urlTimeoutParamSchema, accessToken, permission);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	private JOCDefaultResponse executeModifyJobSchedulerCommand(String[] request,
			HostPortTimeOutParameter urlTimeoutParamSchema, String accessToken, boolean permission) throws Exception {
		String cmd = request[1];
		JOCDefaultResponse jocDefaultResponse = init(API_CALL + request[0], urlTimeoutParamSchema, accessToken,
				urlTimeoutParamSchema.getJobschedulerId(), permission);
		if (jocDefaultResponse != null) {
			return jocDefaultResponse;
		}

		getJobSchedulerInstanceByHostPort(urlTimeoutParamSchema.getHost(), urlTimeoutParamSchema.getPort(),
				urlTimeoutParamSchema.getJobschedulerId());

		checkRequiredComment(urlTimeoutParamSchema.getAuditLog());
		ModifyJobSchedulerAudit jobschedulerAudit = new ModifyJobSchedulerAudit(urlTimeoutParamSchema);
		logAuditMessage(jobschedulerAudit);

		XMLBuilder xml = new XMLBuilder("modify_spooler");
		xml.addAttribute("cmd", cmd);
		if (urlTimeoutParamSchema.getTimeout() != null) {
			xml.addAttribute("timeout", urlTimeoutParamSchema.getTimeout().toString());
		}
		JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
		if (cmd.contains(ABORT[0])) {
			try {
				jocXmlCommand.executePost(xml.asXML(), getAccessToken());
			} catch (JobSchedulerNoResponseException e) {
				// JobScheduler sends always no response if "abort" is called
			} catch (JobSchedulerConnectionRefusedException e) {
				throw e;
			}
		} else {
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
		}
		storeAuditLogEntry(jobschedulerAudit);
		return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
	}

}
