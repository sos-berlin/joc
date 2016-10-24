package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.schedule.SchedulePermanent;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.schedule.resource.IScheduleResourceP;

@Path("schedule")
public class ScheduleResourcePImpl extends JOCResourceImpl implements IScheduleResourceP {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourcePImpl.class);

    @Override
    public JOCDefaultResponse postScheduleP(String accessToken, ScheduleFilter scheduleFilter) throws Exception {
        LOGGER.debug("init schedule/p");
        try {
            JOCDefaultResponse jocDefaultResponse =
                    init(scheduleFilter.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            InventoryInstancesDBLayer instanceDBLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceDBLayer.getInventoryInstanceBySchedulerId(scheduleFilter.getJobschedulerId());
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(Globals.sosHibernateConnection);
            DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(scheduleFilter.getSchedule(), instance.getId());
            ScheduleP schedule = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, instance);

            ScheduleP200 entity = new ScheduleP200();
            entity.setSchedule(schedule);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}