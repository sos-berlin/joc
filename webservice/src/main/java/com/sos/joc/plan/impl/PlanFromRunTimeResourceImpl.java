package com.sos.joc.plan.impl;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.jobscheduler.RuntimeResolver;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.model.plan.RunTime;
import com.sos.joc.model.plan.RunTimePlanFilter;
import com.sos.joc.plan.resource.IPlanFromRunTimeResource;
import com.sos.schema.JsonValidator;

import sos.xml.SOSXMLXPath;

@Path("plan")
public class PlanFromRunTimeResourceImpl extends JOCResourceImpl implements IPlanFromRunTimeResource {

    private static final String API_CALL = "./plan/from_run_time";

    @Override
	public JOCDefaultResponse postPlan(String accessToken, byte[] planFilterBytes) {
        try {
		    JsonValidator.validateFailFast(planFilterBytes, RunTimePlanFilter.class);
		    RunTimePlanFilter planFilter = Globals.objectMapper.readValue(planFilterBytes, RunTimePlanFilter.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, planFilter, accessToken,
					planFilter.getJobschedulerId(), getPermissonsJocCockpit(planFilter.getJobschedulerId(), accessToken)
							.getOrder().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

            checkRequiredParameter("dateTo", planFilter.getDateTo());
            if (planFilter.getRunTime() == null) {
                throw new JocMissingRequiredParameterException("undefined 'runTime'");
            }

            RunTime entity = null;
            String schedule = planFilter.getRunTime().getSchedule();

            if (schedule != null && !schedule.isEmpty()) {
                String schedulePath = normalizePath(schedule);
                String scheduleParent = getParent(schedulePath);

                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
                String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders", scheduleParent);
                jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

                String xPath = String.format("/spooler/answer//schedules/schedule[@path='%1$s']", schedulePath);
                String timeZone = planFilter.getRunTime().getTimeZone();
                if (timeZone == null || timeZone.isEmpty()) {
                    timeZone = dbItemInventoryInstance.getTimeZone();
                }

                entity = new RuntimeResolver().resolve(jocXmlCommand.getSosxml(), (Element) jocXmlCommand.getSosxml().selectSingleNode(xPath),
                        planFilter.getDateFrom(), planFilter.getDateTo(), timeZone);

            } else {
                SOSXMLXPath xml = new SOSXMLXPath(new StringBuffer(Globals.xmlMapper.writeValueAsString(XmlSerializer.serializeAbstractSchedule(
                        planFilter.getRunTime()))));
                entity = new RuntimeResolver().resolve(xml, planFilter.getDateFrom(), planFilter.getDateTo(), dbItemInventoryInstance.getTimeZone());
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }

}