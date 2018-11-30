package com.sos.joc.classes.schedule;

import java.util.ArrayList;
import java.util.List;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.Substitute;
import com.sos.joc.model.schedule.UsedByJob;
import com.sos.joc.model.schedule.UsedByOrder;


public class SchedulePermanent {

    public static ScheduleP initSchedule(InventorySchedulesDBLayer dbLayer, DBItemInventorySchedule scheduleFromDb,
            String documentation, DBItemInventoryInstance instance) throws Exception {
        ScheduleP schedule = new ScheduleP();
        if (scheduleFromDb != null) {
            schedule.setConfigurationDate(dbLayer.getScheduleConfigurationDate(scheduleFromDb.getId()));
            schedule.setName(scheduleFromDb.getBasename());
            schedule.setPath(scheduleFromDb.getName());
            schedule.setDocumentation(documentation);
            schedule.setSurveyDate(scheduleFromDb.getModified());
            String title = scheduleFromDb.getTitle();
            if (title != null && title.isEmpty()) {
                title = null;
            }
            schedule.setTitle(title);
            if (scheduleFromDb.getSubstituteId() != DBLayer.DEFAULT_ID) {
                Substitute substitute = new Substitute();
                substitute.setValidFrom(scheduleFromDb.getSubstituteValidFrom());
                substitute.setValidTo(scheduleFromDb.getSubstituteValidTo());
                substitute.setPath(scheduleFromDb.getSubstitute());
                schedule.setSubstitute(substitute);
                schedule.setSubstitutedBy(null);
                schedule.setUsedByJobs(null);
                schedule.setUsedByOrders(null);
            } else {
                schedule.setSubstitute(null);
                List<DBItemInventorySchedule> substitutedBys = dbLayer.getSubstitutedBySchedules(scheduleFromDb.getId());
                if (substitutedBys != null && !substitutedBys.isEmpty()) {
                    List<Substitute> s = new ArrayList<Substitute>();
                    for (DBItemInventorySchedule substitutedBy : substitutedBys) {
                        Substitute substitute = new Substitute();
                        substitute.setPath(substitutedBy.getName());
                        substitute.setValidFrom(substitutedBy.getSubstituteValidFrom());
                        substitute.setValidTo(substitutedBy.getSubstituteValidTo());
                        s.add(substitute);
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
        }
        return schedule;
    }

}