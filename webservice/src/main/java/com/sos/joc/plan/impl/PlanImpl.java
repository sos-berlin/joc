package com.sos.joc.plan.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.plan.Period;
import com.sos.joc.model.plan.Plan;
import com.sos.joc.model.plan.PlanFilter;
import com.sos.joc.model.plan.PlanItem;
import com.sos.joc.model.plan.PlanState;
import com.sos.joc.model.plan.PlanStateText;
import com.sos.joc.plan.resource.IPlanResource;

@Path("plan")
public class PlanImpl extends JOCResourceImpl implements IPlanResource {

    private static final int INCOMPLETE = 1;
    private static final int FAILED = 2;
    private static final Integer PLANNED = 4;
    private static final String API_CALL = "./plan";

    @Override
    public JOCDefaultResponse postPlan(String accessToken, PlanFilter planFilter) throws Exception {
        try {
            initLogging(API_CALL, planFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, planFilter.getJobschedulerId(), getPermissons(accessToken).getDailyPlan().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Globals.beginTransaction();
            DailyPlanDBLayer dailyPlanDBLayer = new DailyPlanDBLayer(Globals.sosHibernateConnection);
            dailyPlanDBLayer.getFilter().setSchedulerId(planFilter.getJobschedulerId());
            dailyPlanDBLayer.getFilter().setJob(planFilter.getJob());
            dailyPlanDBLayer.getFilter().setJobChain(planFilter.getJobChain());
            dailyPlanDBLayer.getFilter().setOrderId(planFilter.getOrderId());
            dailyPlanDBLayer.getFilter().setPlannedStartFrom(planFilter.getDateFrom());
            dailyPlanDBLayer.getFilter().setPlannedStartTo(planFilter.getDateTo());
            dailyPlanDBLayer.getFilter().setTimeZone(planFilter.getTimeZone());

            List<DailyPlanDBItem> listOfDailyPlanDBItems = dailyPlanDBLayer.getDailyPlanList(0);
            ArrayList<PlanItem> result = new ArrayList<PlanItem>();

            Plan entity = new Plan();
            for (DailyPlanDBItem dailyPlanDBItem : listOfDailyPlanDBItems) {
                boolean add = true;
                PlanItem p = new PlanItem();
                Err err = new Err();
                p.setJob(dailyPlanDBItem.getJob());
                p.setJobChain(dailyPlanDBItem.getJobChain());
                p.setLate(dailyPlanDBItem.getExecutionState().isLate());
                add = (p.getLate() || planFilter.getLate() == null || !planFilter.getLate());

                Period period = new Period();
                period.setBegin(dailyPlanDBItem.getPeriodBegin());
                period.setEnd(dailyPlanDBItem.getPeriodEnd());
                period.setRepeat(dailyPlanDBItem.getRepeatInterval());
                p.setPeriod(period);

                p.setPlannedStartTime(dailyPlanDBItem.getPlannedStart());
                p.setStartMode(dailyPlanDBItem.getStartMode());
                p.setExpectedEndTime(dailyPlanDBItem.getExpectedEnd());

                PlanState planState = new PlanState();
                if (dailyPlanDBItem.haveError()) {
                    planState.set_text(PlanStateText.FAILED);
                    planState.setSeverity(FAILED);
                }

                if (!dailyPlanDBItem.getIsIsAssigned()) {
                    planState.set_text(PlanStateText.PLANNED);
                    planState.setSeverity(PLANNED);
                }

                if (!dailyPlanDBItem.isCompleted()) {
                    planState.set_text(PlanStateText.INCOMPLETE);
                    planState.setSeverity(INCOMPLETE);
                }
                p.setState(planState);
                p.setSurveyDate(dailyPlanDBItem.getCreated());

                if (dailyPlanDBItem.isStandalone()) {
                    if (dailyPlanDBItem.getSchedulerTaskHistoryDBItem() != null) {
                        p.setEndTime(dailyPlanDBItem.getSchedulerTaskHistoryDBItem().getEndTime());
                        p.setHistoryId(dailyPlanDBItem.getSchedulerTaskHistoryDBItem().getId());
                        p.setStartTime(dailyPlanDBItem.getSchedulerTaskHistoryDBItem().getStartTime());
                        err.setCode(dailyPlanDBItem.getSchedulerTaskHistoryDBItem().getErrorCode());
                        err.setMessage(dailyPlanDBItem.getSchedulerTaskHistoryDBItem().getErrorText());
                        p.setError(err);
                    }
                } else {
                    if (dailyPlanDBItem.getSchedulerOrderHistoryDBItem() != null) {
                        p.setEndTime(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getEndTime());
                        p.setHistoryId(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getHistoryId());
                        p.setStartTime(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getStartTime());
                        p.setExitCode(String.valueOf(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getState()));
                        p.setNode(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getState());
                        p.setOrderId(dailyPlanDBItem.getSchedulerOrderHistoryDBItem().getOrderId());
                    }

                }
                if (add) {
                    result.add(p);
                }
            }
            entity.setPlanItems(result);
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
