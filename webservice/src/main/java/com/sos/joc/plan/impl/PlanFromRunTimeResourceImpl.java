package com.sos.joc.plan.impl;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jobscheduler.RuntimeResolver;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.plan.RunTime;
import com.sos.joc.model.plan.RunTimePlanFilter;
import com.sos.joc.plan.resource.IPlanFromRunTimeResource;

import sos.xml.SOSXMLXPath;

@Path("plan")
public class PlanFromRunTimeResourceImpl extends JOCResourceImpl implements IPlanFromRunTimeResource {

    private static final String API_CALL = "./plan/from_run_time";

    @Override
    public JOCDefaultResponse postPlan(String accessToken, RunTimePlanFilter planFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, planFilter, accessToken, planFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("dateTo", planFilter.getDateTo());
            checkRequiredParameter("runTime", planFilter.getRunTime());

            RunTime entity = null;
            SOSXMLXPath xml = new SOSXMLXPath(new StringBuffer(planFilter.getRunTime()));
            String schedule = xml.getRoot().getAttribute("schedule");

            if (schedule != null && !schedule.isEmpty()) {
                String schedulePath = normalizePath(schedule);
                String scheduleParent = getParent(schedulePath);

                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
                String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders", scheduleParent);
                jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

                String xPath = String.format("/spooler/answer//schedules/schedule[@path='%1$s']", schedulePath);

                entity = new RuntimeResolver().resolve(jocXmlCommand.getSosxml(), (Element) jocXmlCommand.getSosxml().selectSingleNode(xPath),
                        planFilter.getDateFrom(), planFilter.getDateTo(), dbItemInventoryInstance.getTimeZone());

            } else {
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