package com.sos.joc.plan.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
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

    private static final int SUCCESSFUL = 0;
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
            if (planFilter.getDateFrom() != null) {
                dailyPlanDBLayer.getFilter().setPlannedStartFrom(JobSchedulerDate.getDate(planFilter.getDateFrom(), planFilter.getTimeZone()));
            }
            if (planFilter.getDateTo() != null) {
                dailyPlanDBLayer.getFilter().setPlannedStartTo(JobSchedulerDate.getDate(planFilter.getDateTo(), planFilter.getTimeZone()));
            }
            dailyPlanDBLayer.getFilter().setLate(planFilter.getLate());

            for (PlanStateText state : planFilter.getStates()) {
                dailyPlanDBLayer.getFilter().addState(state.name());
            }

            List<DailyPlanDBItem> listOfDailyPlanDBItems = dailyPlanDBLayer.getDailyPlanList(0);
            ArrayList<PlanItem> result = new ArrayList<PlanItem>();

            Plan entity = new Plan();
            for (DailyPlanDBItem dailyPlanDBItem : listOfDailyPlanDBItems) {

                boolean add = true;

                PlanItem p = new PlanItem();
                Err err = new Err();
                p.setLate(dailyPlanDBItem.getIsLate());

                Period period = new Period();
                period.setBegin(dailyPlanDBItem.getPeriodBegin());
                period.setEnd(dailyPlanDBItem.getPeriodEnd());
                period.setRepeat(dailyPlanDBItem.getRepeatInterval());
                p.setPeriod(period);

                p.setPlannedStartTime(dailyPlanDBItem.getPlannedStart());
                p.setStartMode(dailyPlanDBItem.getStartMode());
                p.setExpectedEndTime(dailyPlanDBItem.getExpectedEnd());

                PlanState planState = new PlanState();

                if (PlanStateText.FAILED.name().equals(dailyPlanDBItem.getState())) {
                    planState.set_text(PlanStateText.FAILED);
                    planState.setSeverity(FAILED);
                }

                if (PlanStateText.PLANNED.name().equals(dailyPlanDBItem.getState())) {
                    planState.set_text(PlanStateText.PLANNED);
                    planState.setSeverity(PLANNED);
                }

                if (PlanStateText.INCOMPLETE.name().equals(dailyPlanDBItem.getState())) {
                    planState.set_text(PlanStateText.INCOMPLETE);
                    planState.setSeverity(INCOMPLETE);
                }
                if (PlanStateText.SUCCESSFUL.name().equals(dailyPlanDBItem.getState())) {
                    planState.set_text(PlanStateText.SUCCESSFUL);
                    planState.setSeverity(SUCCESSFUL);
                }
                p.setState(planState);
                p.setSurveyDate(dailyPlanDBItem.getCreated());

                if (dailyPlanDBItem.isStandalone()) {
                    if (planFilter.getRegex() != null && !planFilter.getRegex().isEmpty()) {
                        Matcher regExMatcher = Pattern.compile(planFilter.getRegex()).matcher(dailyPlanDBItem.getJob());
                        add = regExMatcher.find();
                    }
                    p.setJob(dailyPlanDBItem.getJob());

                    if (dailyPlanDBItem.getDbItemReportExecution() != null) {
                        p.setEndTime(dailyPlanDBItem.getDbItemReportExecution().getEndTime());
                        p.setHistoryId(dailyPlanDBItem.getDbItemReportExecution().getId().toString());
                        p.setStartTime(dailyPlanDBItem.getDbItemReportExecution().getStartTime());
                        p.setExitCode(dailyPlanDBItem.getDbItemReportExecution().getExitCode());
                        err.setCode(dailyPlanDBItem.getDbItemReportExecution().getErrorCode());
                        err.setMessage(dailyPlanDBItem.getDbItemReportExecution().getErrorText());
                        p.setError(err);
                    }
                } else {
                    if (planFilter.getRegex() != null && !planFilter.getRegex().isEmpty()) {
                        Matcher regExMatcher = Pattern.compile(planFilter.getRegex()).matcher(dailyPlanDBItem.getJobChain() + "," + dailyPlanDBItem.getOrderId());
                        add = regExMatcher.find();
                    }
                    p.setJobChain(dailyPlanDBItem.getJobChain());
                    p.setOrderId(dailyPlanDBItem.getOrderId());

                    if (dailyPlanDBItem.getDbItemReportTrigger() != null) {
                        p.setEndTime(dailyPlanDBItem.getDbItemReportTrigger().getEndTime());
                        p.setHistoryId(dailyPlanDBItem.getDbItemReportTrigger().getHistoryId().toString());
                        p.setStartTime(dailyPlanDBItem.getDbItemReportTrigger().getStartTime());
                        p.setNode(dailyPlanDBItem.getDbItemReportTrigger().getState());
                        p.setOrderId(dailyPlanDBItem.getDbItemReportTrigger().getName());
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
            e.printStackTrace();
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            e.printStackTrace();
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }
}
