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
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.schedule.resource.IScheduleResourceP;

@Path("schedule")
public class ScheduleResourcePImpl extends JOCResourceImpl implements IScheduleResourceP {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourcePImpl.class);
    private static final String API_CALL = "./schedule/p";

    @Override
    public JOCDefaultResponse postScheduleP(String accessToken, ScheduleFilter scheduleFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, scheduleFilter.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(Globals.sosHibernateConnection);
            DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(scheduleFilter.getSchedule(), dbItemInventoryInstance.getId());
            ScheduleP schedule = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, dbItemInventoryInstance);

            ScheduleP200 entity = new ScheduleP200();
            entity.setSchedule(schedule);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, scheduleFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, scheduleFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}