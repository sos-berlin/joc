package com.sos.joc.classes.schedule;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.ScheduleState;
import com.sos.joc.model.schedule.ScheduleStateText;
import com.sos.joc.model.schedule.Substitute;
import com.sos.joc.model.schedule.UsedByJob;
import com.sos.joc.model.schedule.UsedByOrder;


public class SchedulePermanent {

    public static ScheduleP initSchedule(InventorySchedulesDBLayer dbLayer, DBItemInventorySchedule scheduleFromDb,
            DBItemInventoryInstance instance) throws Exception {
        ScheduleP schedule = new ScheduleP();
        if (scheduleFromDb != null) {
            schedule.setConfigurationDate(dbLayer.getScheduleConfigurationDate(scheduleFromDb.getId()));
            schedule.setName(scheduleFromDb.getBasename());
            schedule.setPath(scheduleFromDb.getName());
            schedule.setSurveyDate(scheduleFromDb.getModified());
            String title = scheduleFromDb.getTitle();
            if (title != null && title.isEmpty()) {
                title = null;
            }
            schedule.setTitle(title);
//            boolean isActive = true;
//            Date now = Date.from(Instant.now());
            if (scheduleFromDb.getSubstituteId() != DBLayer.DEFAULT_ID) {
                Substitute substitute = new Substitute();
                substitute.setValidFrom(scheduleFromDb.getSubstituteValidFrom());
                substitute.setValidTo(scheduleFromDb.getSubstituteValidTo());
                substitute.setPath(scheduleFromDb.getSubstitute());
                schedule.setSubstitute(substitute);
                schedule.setSubstitutedBy(null);
                schedule.setUsedByJobs(null);
                schedule.setUsedByOrders(null);
//                if (scheduleFromDb.getSubstituteValidFrom().after(now) || scheduleFromDb.getSubstituteValidTo().compareTo(now) <= 0) {
//                    isActive = false;
//                }
            } else {
                schedule.setSubstitute(null);
                List<DBItemInventorySchedule> substitutedBys = dbLayer.getSubstitutedBySchedules(scheduleFromDb.getId());
                if (substitutedBys != null && !substitutedBys.isEmpty()) {
                    List<String> s = new ArrayList<String>();
                    for (DBItemInventorySchedule substitutedBy : substitutedBys) {
                        s.add(substitutedBy.getName());
//                        if (isActive && substitutedBy.getSubstituteValidFrom().compareTo(now) <= 0 && 
//                                substitutedBy.getSubstituteValidTo().after(now)) {
//                            isActive = false;
//                        }
                    }
                    schedule.setSubstitutedBy(s);
                } else {
                    schedule.setSubstitutedBy(null);
                }
                
                List<DBItemInventoryJob> jobsFromDb =
                        dbLayer.getUsedIn(scheduleFromDb.getId(), instance.getId(), DBItemInventoryJob.class);
                if (jobsFromDb != null && !jobsFromDb.isEmpty()) {
                    List<UsedByJob> listOfUsesByJob = new ArrayList<UsedByJob>();
                    for (DBItemInventoryJob job : jobsFromDb) {
                        UsedByJob usedByJob = new UsedByJob();
                        usedByJob.setJob(job.getName());
                        listOfUsesByJob.add(usedByJob);
                    }
                    schedule.setUsedByJobs(listOfUsesByJob);
                } else {
                    schedule.setUsedByJobs(null);
                }
                List<DBItemInventoryOrder> ordersFromDb = 
                        dbLayer.getUsedIn(scheduleFromDb.getId(), instance.getId(), DBItemInventoryOrder.class);
                if (ordersFromDb != null && !ordersFromDb.isEmpty()) {
                    List<UsedByOrder> listOfUsesByOrder = new ArrayList<UsedByOrder>();
                    for (DBItemInventoryOrder order : ordersFromDb) {
                        UsedByOrder usedByOrder = new UsedByOrder();
                        usedByOrder.setJobChain(order.getJobChainName());
                        usedByOrder.setOrderId(order.getOrderId());
                        listOfUsesByOrder.add(usedByOrder);
                    }
                    schedule.setUsedByOrders(listOfUsesByOrder);
                } else {
                    schedule.setUsedByOrders(null);
                }
            }
//            schedule.setState(getState(isActive));
        }
        return schedule;
    }

//    private static ScheduleState getState(boolean isActive) {
//        ScheduleState state = new ScheduleState();
//        if (isActive) {
//            state.setSeverity(0);
//            state.set_text(ScheduleStateText.ACTIVE);
//        } else {
//            state.setSeverity(1);
//            state.set_text(ScheduleStateText.INACTIVE);
//        }
//        return state;
//    }

}