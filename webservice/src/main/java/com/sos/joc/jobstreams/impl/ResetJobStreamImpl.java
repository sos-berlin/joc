package com.sos.joc.jobstreams.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IResetJobStreamResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ResetJobStream;

@Path("conditions")
public class ResetJobStreamImpl extends JOCResourceImpl implements IResetJobStreamResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResetJobStreamImpl.class);
	private static final String API_CALL = "./conditions/resetjobstreams";

	@Override
	public JOCDefaultResponse resetJobStream(String accessToken, ResetJobStream resetJobStream) throws Exception {
		try {

			JOCDefaultResponse jocDefaultResponse = init(API_CALL, resetJobStream, accessToken,
					resetJobStream.getJobschedulerId(),
					getPermissonsJocCockpit(resetJobStream.getJobschedulerId(), accessToken).getCondition().getChange()
							.isConditions());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			notifyEventHandler(accessToken, resetJobStream);

			return JOCDefaultResponse.responseStatus200(resetJobStream);

		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {

		}
	}

	private String notifyEventHandler(String accessToken, ResetJobStream resetJobStream)
			throws JsonProcessingException, JocException {
		CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

		Map<String, String> parameters = new HashMap<String, String>();
		if (resetJobStream.getJob() != null) {
			parameters.put("job", resetJobStream.getJob());
		}
		parameters.put("session", com.sos.eventhandlerservice.classes.Constants.getSession());
		if (resetJobStream.getJobStream() != null) {
			parameters.put("jobStream", resetJobStream.getJobStream());
		}

		customEventsUtil.addEvent("ResetConditionResolver", parameters);
		String notifyCommand = customEventsUtil.getEventCommandAsXml();
		com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(
				dbItemInventoryInstance);
		return jocXmlCommand.executePost(notifyCommand, accessToken);
	}

}