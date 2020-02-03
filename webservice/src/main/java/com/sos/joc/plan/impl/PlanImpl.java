package com.sos.joc.plan.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.jitl.dailyplan.db.Calendar2DB;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.jitl.dailyplan.db.DailyPlanWithReportExecutionDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanWithReportTriggerDBItem;
import com.sos.jitl.dailyplan.job.CreateDailyPlanOptions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.plan.Period;
import com.sos.joc.model.plan.Plan;
import com.sos.joc.model.plan.PlanCreated;
import com.sos.joc.model.plan.PlanFilter;
import com.sos.joc.model.plan.PlanItem;
import com.sos.joc.model.plan.PlanState;
import com.sos.joc.model.plan.PlanStateText;
import com.sos.joc.plan.resource.IPlanResource;
import com.sos.schema.JsonValidator;

@Path("plan")
public class PlanImpl extends JOCResourceImpl implements IPlanResource {

    private static final int MAX_PLAN_ENTRIED = 2000;

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
        p.setAuditLogId(dailyPlanDBItem.getAuditLogId());

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
    public JOCDefaultResponse postPlan(String accessToken, byte[] planFilterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            LOGGER.debug("Reading the daily plan");
            JsonValidator.validateFailFast(planFilterBytes, PlanFilter.class);
            PlanFilter planFilter = Globals.objectMapper.readValue(planFilterBytes, PlanFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, planFilter, accessToken, planFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    planFilter.getJobschedulerId(), accessToken).getDailyPlan().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection("postPlan");
            DailyPlanDBLayer dailyPlanDBLayer = new DailyPlanDBLayer(sosHibernateSession);
            boolean withFolderFilter = planFilter.getFolders() != null && !planFilter.getFolders().isEmpty();
            boolean hasPermission = true;
            List<Folder> folders = addPermittedFolder(planFilter.getFolders());

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

            if (withFolderFilter && (folders == null || folders.isEmpty())) {
                hasPermission = false;
            } else if (folders != null && !folders.isEmpty()) {
                dailyPlanDBLayer.getFilter().addFolderPaths(new HashSet<Folder>(folders));
            }

            Matcher regExMatcher = null;
            if (planFilter.getRegex() != null && !planFilter.getRegex().isEmpty()) {
                planFilter.setRegex(SearchStringHelper.getRegexValue(planFilter.getRegex()));
                regExMatcher = Pattern.compile(planFilter.getRegex()).matcher("");
            }

            ArrayList<PlanItem> result = new ArrayList<PlanItem>();
            Plan entity = new Plan();

            if (hasPermission) {
                List<DailyPlanWithReportTriggerDBItem> listOfWaitingDailyPlanOrderDBItems = dailyPlanDBLayer.getWaitingDailyPlanOrderList(MAX_PLAN_ENTRIED);
                List<DailyPlanWithReportExecutionDBItem> listOfWaitingDailyPlanStandaloneDBItems = dailyPlanDBLayer.getWaitingDailyPlanStandaloneList(
                		2000);
                List<DailyPlanWithReportTriggerDBItem> listOfDailyPlanOrderDBItems = dailyPlanDBLayer.getDailyPlanListOrder(2000);
                List<DailyPlanWithReportExecutionDBItem> listOfDailyPlanStandaloneDBItems = dailyPlanDBLayer.getDailyPlanListStandalone(2000);

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
                        regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJobChain() + "," + dailyPlanDBItem.getDailyPlanDbItem()
                                .getOrderId());
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
                        regExMatcher.reset(dailyPlanDBItem.getDailyPlanDbItem().getJobChain() + "," + dailyPlanDBItem.getDailyPlanDbItem()
                                .getOrderId());
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
                        LOGGER.debug("commandUrl:" + commandUrl);
                        createDailyPlanOptions.commandUrl.setValue(commandUrl);
                        calendar2Db = new Calendar2DB(sosHibernateSession, dbItemInventoryInstance.getSchedulerId());
                        if (dbItemInventoryInstance.getAuth() != null && !dbItemInventoryInstance.getAuth().isEmpty()) {
                            LOGGER.debug("basicAuthorization:" + dbItemInventoryInstance.getAuth());
                            createDailyPlanOptions.basicAuthorization.setValue(dbItemInventoryInstance.getAuth());
                        }
                        calendar2Db.setOptions(createDailyPlanOptions);

                        if (maxDate.before(toDate)) {
                            Date f = calendar2Db.addCalendar(maxDate, 1, java.util.Calendar.SECOND);
                            if (f.before(fromDate)) {
                                f = fromDate;
                            }
                            List<DailyPlanDBItem> listOfCalenderItems = calendar2Db.getStartTimesFromScheduler(f, toDate);

                            Matcher regExMatcherJob = null;
                            if (planFilter.getJob() != null && !planFilter.getJob().isEmpty()) {
                                planFilter.setJob(SearchStringHelper.getRegexValue(planFilter.getJob()));
                                regExMatcherJob = Pattern.compile(planFilter.getJob()).matcher("");
                            }
                            Matcher regExMatcherOrderId = null;
                            if (planFilter.getOrderId() != null && !planFilter.getOrderId().isEmpty()) {
                                planFilter.setOrderId(SearchStringHelper.getRegexValue(planFilter.getOrderId()));
                                regExMatcherOrderId = Pattern.compile(planFilter.getOrderId()).matcher("");
                            }
                            Matcher regExMatcherJobChain = null;
                            if (planFilter.getJobChain() != null && !planFilter.getJobChain().isEmpty()) {
                                planFilter.setJobChain(SearchStringHelper.getRegexValue(planFilter.getJobChain()));
                                regExMatcherJobChain = Pattern.compile(planFilter.getJobChain()).matcher("");
                            }

                            for (DailyPlanDBItem dailyPlanDBItem : listOfCalenderItems) {
                                DailyPlanWithReportTriggerDBItem dailyPlanWithReportTriggerDBItem = new DailyPlanWithReportTriggerDBItem(
                                        dailyPlanDBItem, null);

                                boolean add = true;

                                PlanItem p = createPlanItem(dailyPlanDBItem);

                                p.setStartMode(dailyPlanWithReportTriggerDBItem.getStartMode());

                                if (regExMatcher != null) {
                                    regExMatcher.reset(dailyPlanDBItem.getJobChain() + "," + dailyPlanDBItem.getOrderId());
                                    add = regExMatcher.find();
                                    regExMatcher.reset(dailyPlanDBItem.getJob());
                                    add = add || regExMatcher.find();
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

                                if (regExMatcherJob != null) {
                                    regExMatcherJob.reset(dailyPlanDBItem.getJob());
                                    add = add && regExMatcherJob.find();
                                }
                                if (regExMatcherJobChain != null) {
                                    regExMatcherJobChain.reset(dailyPlanDBItem.getJobChain());
                                    add = add && regExMatcherJobChain.find();
                                }
                                if (regExMatcherOrderId != null) {
                                    regExMatcherOrderId.reset(dailyPlanDBItem.getOrderId());
                                    add = add && regExMatcherOrderId.find();
                                }

                                add = add && (dailyPlanDBLayer.getFilter().containsFolder(path));

                                if (add) {
                                    result.add(p);
                                }
                            }
                        }
                    } catch (Exception e) {
                        String cause = "";
                        if (e.getCause() != null) {
                            cause = e.getCause().toString();
                        }
                        LOGGER.warn("->" + e.toString() + ":" + cause);
                        if (calendar2Db != null) {
                            PlanCreated planCreated = new PlanCreated();
                            planCreated.setUntil(calendar2Db.getMaxPlannedTime(planFilter.getJobschedulerId()));
                            planCreated.setDays(calendar2Db.getDayOffset());
                            entity.setCreated(planCreated);
                        }
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
