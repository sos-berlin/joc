package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.schedule.SchedulePermanent;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.schedule.resource.IScheduleResourceP;

@Path("schedule")
public class ScheduleResourcePImpl extends JOCResourceImpl implements IScheduleResourceP {

    private static final String API_CALL = "./schedule/p";

    @Override
    public JOCDefaultResponse postScheduleP(String accessToken, ScheduleFilter scheduleFilter) throws Exception {
        try {
            initLogging(API_CALL, scheduleFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, scheduleFilter.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Globals.beginTransaction();
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(Globals.sosHibernateConnection);
            DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(normalizePath(scheduleFilter.getSchedule()),
                    dbItemInventoryInstance.getId());
            ScheduleP schedule = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, dbItemInventoryInstance);

            ScheduleP200 entity = new ScheduleP200();
            entity.setSchedule(schedule);
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