package com.sos.joc.plan.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.plan.Plan;
import com.sos.joc.model.plan.PlanFilter;
import com.sos.joc.model.plan.PlanItem;
import com.sos.joc.plan.resource.IPlanResource;

@Path("plan")
public class PlanImpl extends JOCResourceImpl implements IPlanResource {

    private static final String API_CALL = "./plan";

    @Override
    public JOCDefaultResponse postPlan(String accessToken, PlanFilter planFilter) throws Exception {
        try {
            initLogging(API_CALL, planFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, planFilter.getJobschedulerId(), getPermissons(accessToken).getDailyPlan()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Globals.beginTransaction();
            Plan entity = new Plan();
            // TODO select items from database
            entity.setPlanItems(new ArrayList<PlanItem>());
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }
}
