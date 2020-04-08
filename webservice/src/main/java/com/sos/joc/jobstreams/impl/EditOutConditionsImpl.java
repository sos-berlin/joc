package com.sos.joc.jobstreams.impl;

import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IEditOutConditionsResource;
import com.sos.joc.model.jobstreams.JobOutCondition;
import com.sos.joc.model.jobstreams.OutConditions;
import com.sos.schema.JsonValidator;

@Path("jobstreams/edit")
public class EditOutConditionsImpl extends JOCResourceImpl implements IEditOutConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditOutConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/out_condition";

    @Override
    public JOCDefaultResponse editJobOutConditions(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, OutConditions.class);
            OutConditions outConditions = Globals.objectMapper.readValue(filterBytes, OutConditions.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, outConditions, accessToken, outConditions.getJobschedulerId(),
                    getPermissonsJocCockpit(outConditions.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            Globals.beginTransaction(sosHibernateSession);
            String jobStream = dbLayerOutConditions.deleteInsert(outConditions);

            if (!jobStream.isEmpty()) {
                boolean update = false;
                DailyPlanDBLayer dailyPlanDBLayer = new DailyPlanDBLayer(sosHibernateSession);
                dailyPlanDBLayer.getFilter().setSchedulerId(outConditions.getJobschedulerId());
                dailyPlanDBLayer.getFilter().setIsJobStream(true);
                for (JobOutCondition jobOutCondition : outConditions.getJobsOutconditions()) {
                    dailyPlanDBLayer.getFilter().setJob(jobOutCondition.getJob());
                    List<DailyPlanDBItem> l = dailyPlanDBLayer.getDailyPlanList(1, false);
                    if (l.size() > 0) {
                        dailyPlanDBLayer.getFilter().setJobStream(l.get(0).getJobStream());
                        update = true;
                        break;
                    }
                }
                if (update) {
                    dailyPlanDBLayer.updateDailyPlanJobStream(jobStream);
                }

            }
            sosHibernateSession.commit();

            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Edit Out Conditon: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Data are stored in Database");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());}
            return JOCDefaultResponse.responseStatus200(outConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditOutConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}