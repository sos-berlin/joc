package com.sos.joc.plan.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.dailyplan.db.Calendar2DB;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.jitl.dailyplan.db.DailyPlanWithReportExecutionDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanWithReportTriggerDBItem;
import com.sos.jitl.dailyplan.job.CreateDailyPlanOptions;
import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.plan.Period;
import com.sos.joc.model.plan.Plan;
import com.sos.joc.model.plan.PlanCreated;
import com.sos.joc.model.plan.PlanFilter;
import com.sos.joc.model.plan.PlanItem;
import com.sos.joc.model.plan.PlanState;
import com.sos.joc.model.plan.PlanStateText;
import com.sos.joc.plan.resource.IPlanResource;

@Path("plan")
public class PlanImpl extends JOCResourceImpl implements IPlanResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanImpl.class);

    private static final int SUCCESSFUL = 0;
    private static final int SUCCESSFUL_LATE = 1;
    private static final int INCOMPLETE = 6;
    private static final int INCOMPLETE_LATE = 5;
    private static final int FAILED = 2;
    private static final int PLANNED_LATE = 5;
    private static final Integer PLANNED = 4;
    private static final String API_CALL = "./plan";

    private PlanItem createPlanItem(DailyPlanDBItem dailyPlanDBItem) {
        PlanItem p = new PlanItem();
        p.setLate(dailyPlanDBItem.getIsLate());

        Period period = new Period();
        period.setBegin(dailyPlanDBItem.getPeriodBegin());
        period.setEnd(dailyPlanDBItem.getPeriodEnd());
        period.setRepeat(dailyPlanDBItem.getRepeatInterval());
        p.setPeriod(period);

        p.setPlannedStartTime(dailyPlanDBItem.getPlannedStart());
        p.setExpectedEndTime(dailyPlanDBItem.getExpectedEnd());

        PlanState planState = new PlanState();

        if (PlanStateText.FAILED.name().equals(dailyPlanDBItem.getState())) {
            planState.set_text(PlanStateText.FAILED);
            planState.setSeverity(FAILED);
        }

        if (PlanStateText.PLANNED.name().equals(dailyPlanDBItem.getState())) {
            planState.set_text(PlanStateText.PLANNED);
            if (dailyPlanDBItem.getIsLate()) {
                planState.setSeverity(PLANNED_LATE);
            } else {
                planState.setSeverity(PLANNED);
            }
        }

        if (PlanStateText.INCOMPLETE.name().equals(dailyPlanDBItem.getState())) {
            planState.set_text(PlanStateText.INCOMPLETE);
            if (dailyPlanDBItem.getIsLate()) {
                planState.setSeverity(INCOMPLETE_LATE);
            } else {
                planState.setSeverity(INCOMPLETE);
            }
        }
        if (PlanStateText.SUCCESSFUL.name().equals(dailyPlanDBItem.getState())) {
            planState.set_text(PlanStateText.SUCCESSFUL);
            if (dailyPlanDBItem.getIsLate()) {
                planState.setSeverity(SUCCESSFUL_LATE);
            } else {
                planState.setSeverity(SUCCESSFUL);
            }
        }
        p.setState(planState);
        p.setSurveyDate(dailyPlanDBItem.getCreated());

        return p;

    }

    private Date getMaxPlannedStart(DailyPlanDBLayer dailyPlanDBLayer, String schedulerId) {
        Date maxPlannedTime = dailyPlanDBLayer.getMaxPlannedStart(schedulerId);
        return maxPlannedTime;
    }

    @Override
    public JOCDefaultResponse postPlan(String accessToken, PlanFilter planFilter) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {
            LOGGER.debug("Reading the daily plan");
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, planFilter, accessToken, planFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getDailyPlan().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection("postPlan");
            DailyPlanDBLayer dailyPlanDBLayer = new DailyPlanDBLayer(sosHibernateSession);

            Globals.beginTransaction(sosHibernateSession);

            Date maxDate = getMaxPlannedStart(dailyPlanDBLayer, planFilter.getJobschedulerId());
            Date fromDate = null;
            Date toDate = null;

            dailyPlanDBLayer.getFilter().setSchedulerId(planFilter.getJobschedulerId());
            dailyPlanDBLayer.getFilter().setJob(planFilter.getJob());
            dailyPlanDBLayer.getFilter().setJobChain(planFilter.getJobChain());
            dailyPlanDBLayer.getFilter().setOrderId(planFilter.getOrderId());
            if (planFilter.getDateFrom() != null) {
                fromDate = JobSchedulerDate.getDateFrom(planFilter.getDateFrom(), planFilter.getTimeZone());
                dailyPlanDBLayer.getFilter().setPlannedStartFrom(fromDate);
            }
            if (planFilter.getDateTo() != null) {
                toDate = JobSchedulerDate.getDateTo(planFilter.getDateTo(), planFilter.getTimeZone());
                dailyPlanDBLayer.getFilter().setPlannedStartTo(toDate);
            }
            dailyPlanDBLayer.getFilter().setLate(planFilter.getLate());

            for (PlanStateText state : planFilter.getStates()) {
                dailyPlanDBLayer.getFilter().addState(state.name());
            }

            if (planFilter.getFolders().size() > 0) {
                for (com.sos.joc.model.common.Folder folder : planFilter.getFolders()) {
                    dailyPlanDBLayer.getFilter().addFolderPath(folder.getFolder(), folder.getRecursive());
                }
            }

            if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
                    dailyPlanDBLayer.getFilter().addFolderPath(normalizeFolder(folder.getFolder()), folder.isRecursive());
                }
            }

            Matcher regExMatcher = null;
            if (planFilter.getRegex() != null && !planFilter.getRegex().isEmpty()) {
                regExMatcher = Pattern.compile(planFilter.getRegex()).matcher("");
            }

            List<DailyPlanWithReportTriggerDBItem> listOfWaitingDailyPlanOrderDBItems = dailyPlanDBLayer.getWaitingDailyPlanOrderList(0);
            List<DailyPlanWithReportExecutionDBItem> listOfWaitingDailyPlanStandaloneDBItems = dailyPlanDBLayer.getWaitingDailyPlanStandaloneList(0);
            List<DailyPlanWithReportTriggerDBItem> listOfDailyPlanOrderDBItems = dailyPlanDBLayer.getDailyPlanListOrder(0);
            List<DailyPlanWithReportExecutionDBItem> listOfDailyPlanStandaloneDBItems = dailyPlanDBLayer.getDailyPlanListStandalone(0);
            ArrayList<PlanItem> result = new ArrayList<PlanItem>();

            Plan entity = new Plan();

            for (DailyPlanWithReportExecutionDBItem dailyPlanDBItem : listOfWaitingDailyPlanStandaloneDBItems) {

                boolean add = true;
                PlanItem p = createPlanItem(dailyPlanDBItem.getDailyPlanDbItem());
                p.setStartMode(dailyPlanDBItem.getStartMode());

                if (regExMatcher != null) {
                    regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJob());
                    add = regExMatcher.find();
                }
                p.setJob(dailyPlanDBItem.getDailyPlanDbItem().getJobOrNull());

                if (dailyPlanDBItem.getDbItemReportTask() != null) {
                    p.setStartTime(dailyPlanDBItem.getDbItemReportTask().getStartTime());
                    p.setEndTime(dailyPlanDBItem.getDbItemReportTask().getEndTime());
                    p.setHistoryId(dailyPlanDBItem.getDbItemReportTask().getHistoryIdAsString());
                }

                if (add && dailyPlanDBItem.getDailyPlanDbItem().getReportExecutionId() == null) {
                    result.add(p);
                }
            }

            for (DailyPlanWithReportTriggerDBItem dailyPlanDBItem : listOfWaitingDailyPlanOrderDBItems) {

                boolean add = true;

                PlanItem p = createPlanItem(dailyPlanDBItem.getDailyPlanDbItem());
                p.setStartMode(dailyPlanDBItem.getStartMode());

                if (regExMatcher != null) {
                    regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJobChain() + "," + dailyPlanDBItem.getDailyPlanDbItem().getOrderId());
                    add = regExMatcher.find();
                }
                p.setJobChain(dailyPlanDBItem.getDailyPlanDbItem().getJobChainOrNull());
                p.setOrderId(dailyPlanDBItem.getDailyPlanDbItem().getOrderIdOrNull());

                if (add && dailyPlanDBItem.getDailyPlanDbItem().getReportTriggerId() == null) {
                    result.add(p);
                }
            }

            for (DailyPlanWithReportExecutionDBItem dailyPlanDBItem : listOfDailyPlanStandaloneDBItems) {

                boolean add = true;
                PlanItem p = createPlanItem(dailyPlanDBItem.getDailyPlanDbItem());
                p.setStartMode(dailyPlanDBItem.getStartMode());

                Err err = new Err();

                if (regExMatcher != null) {
                    regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJob());
                    add = regExMatcher.find();
                }
                p.setJob(dailyPlanDBItem.getDailyPlanDbItem().getJobOrNull());

                if (dailyPlanDBItem.getDbItemReportTask() != null) {
                    p.setEndTime(dailyPlanDBItem.getDbItemReportTask().getEndTime());
                    p.setHistoryId(dailyPlanDBItem.getDbItemReportTask().getHistoryIdAsString());
                    p.setStartTime(dailyPlanDBItem.getDbItemReportTask().getStartTime());
                    p.setExitCode(dailyPlanDBItem.getDbItemReportTask().getExitCode());
                    err.setCode(dailyPlanDBItem.getDbItemReportTask().getErrorCode());
                    err.setMessage(dailyPlanDBItem.getDbItemReportTask().getErrorText());
                    p.setError(err);
                }
                if (add && dailyPlanDBItem.getDailyPlanDbItem().getReportExecutionId() != null) {
                    result.add(p);
                }
            }

            for (DailyPlanWithReportTriggerDBItem dailyPlanDBItem : listOfDailyPlanOrderDBItems) {

                boolean add = true;

                PlanItem p = createPlanItem(dailyPlanDBItem.getDailyPlanDbItem());

                p.setStartMode(dailyPlanDBItem.getStartMode());

                if (regExMatcher != null) {
                    regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJobChain() + "," + dailyPlanDBItem.getDailyPlanDbItem().getOrderId());
                    add = regExMatcher.find();
                }
                p.setJobChain(dailyPlanDBItem.getDailyPlanDbItem().getJobChainOrNull());
                p.setOrderId(dailyPlanDBItem.getDailyPlanDbItem().getOrderIdOrNull());

                if (dailyPlanDBItem.getDbItemReportTrigger() != null) {
                    p.setEndTime(dailyPlanDBItem.getDbItemReportTrigger().getEndTime());
                    p.setHistoryId(dailyPlanDBItem.getDbItemReportTrigger().getHistoryId().toString());
                    p.setStartTime(dailyPlanDBItem.getDbItemReportTrigger().getStartTime());
                    p.setNode(dailyPlanDBItem.getDbItemReportTrigger().getState());
                    p.setOrderId(dailyPlanDBItem.getDbItemReportTrigger().getName());
                }

                if (add && dailyPlanDBItem.getDailyPlanDbItem().getReportTriggerId() != null) {
                    result.add(p);
                }
            }

            if (fromDate != null && toDate != null && (planFilter.getLate() == null || !planFilter.getLate()) && (planFilter.getStates() == null
                    || planFilter.getStates().size() == 0 || planFilter.getStates().get(0).name() == "PLANNED")) {
                Calendar2DB calendar2Db = null;
                try {
                    CreateDailyPlanOptions createDailyPlanOptions = new CreateDailyPlanOptions();
                    createDailyPlanOptions.dayOffset.value(0);
                    String commandUrl = dbItemInventoryInstance.getUrl() + "/jobscheduler/master/api/command";
                    createDailyPlanOptions.commandUrl.setValue(commandUrl);
                    calendar2Db = new Calendar2DB(sosHibernateSession);
                    calendar2Db.setOptions(createDailyPlanOptions);

                    if (maxDate.before(toDate)) {
                        Date f = calendar2Db.addCalendar(maxDate, 1, java.util.Calendar.DAY_OF_MONTH);
                        if (f.before(fromDate)) {
                            f = fromDate;
                        }
                        List<DailyPlanDBItem> listOfCalenderItems = calendar2Db.getStartTimesFromScheduler(f, toDate);

                        for (DailyPlanDBItem dailyPlanDBItem : listOfCalenderItems) {
                            DailyPlanWithReportTriggerDBItem dailyPlanWithReportTriggerDBItem = new DailyPlanWithReportTriggerDBItem(dailyPlanDBItem,
                                    null);

                            boolean add = true;

                            PlanItem p = createPlanItem(dailyPlanDBItem);

                            p.setStartMode(dailyPlanWithReportTriggerDBItem.getStartMode());

                            if (regExMatcher != null) {
                                regExMatcher.reset(dailyPlanDBItem.getJobChain() + "," + dailyPlanDBItem.getOrderId());
                                add = regExMatcher.find();
                            }
                            p.setJobChain(dailyPlanDBItem.getJobChainOrNull());
                            p.setOrderId(dailyPlanDBItem.getOrderIdOrNull());
                            p.setJob(dailyPlanDBItem.getJobOrNull());

                            String path;
                            if (dailyPlanDBItem.getJob() != null) {
                                path = dailyPlanDBItem.getJob();
                            } else {
                                path = dailyPlanDBItem.getJobChain();
                            }

                            add = add && (planFilter.getJob() == null || planFilter.getJob().equals(dailyPlanDBItem.getJob()));
                            add = add && (planFilter.getJobChain() == null || planFilter.getJobChain().equals(dailyPlanDBItem.getJobChain()));
                            add = add && (planFilter.getOrderId() == null || planFilter.getOrderId().equals(dailyPlanDBItem.getOrderId()));
                            add = add && (dailyPlanDBLayer.getFilter().containsFolder(path));

                            if (add) {
                                result.add(p);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (calendar2Db != null) {
                        PlanCreated planCreated = new PlanCreated();
                        planCreated.setDays(calendar2Db.getDayOffset());
                        planCreated.setUntil(calendar2Db.getMaxPlannedTime());
                        entity.setCreated(planCreated);
                    }
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
            Globals.disconnect(sosHibernateSession);
        }
    }
}
