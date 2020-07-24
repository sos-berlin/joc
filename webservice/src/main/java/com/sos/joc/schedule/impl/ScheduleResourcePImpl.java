package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.schedule.SchedulePermanent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.schedule.resource.IScheduleResourceP;
import com.sos.schema.JsonValidator;

@Path("schedule")
public class ScheduleResourcePImpl extends JOCResourceImpl implements IScheduleResourceP {

    private static final String API_CALL = "./schedule/p";

    @Override
    public JOCDefaultResponse postScheduleP(String accessToken, byte[] scheduleFilterBytes) {
        SOSHibernateSession connection = null;

        try {
            JsonValidator.validateFailFast(scheduleFilterBytes, ScheduleFilter.class);
            ScheduleFilter scheduleFilter = Globals.objectMapper.readValue(scheduleFilterBytes, ScheduleFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, scheduleFilter, accessToken, scheduleFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(scheduleFilter.getJobschedulerId(), accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("schedule", scheduleFilter.getSchedule());
            String schedulePath = normalizePath(scheduleFilter.getSchedule());
            checkFolderPermissions(schedulePath);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(connection);
            DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(schedulePath, dbItemInventoryInstance.getId());
            if (scheduleFromDb == null) {
                throw new DBMissingDataException(String.format("no schedule found in DB: %s", schedulePath));
            }
            DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
            ScheduleP schedule = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, dbDocLayer.getDocumentationPath(scheduleFilter
                    .getJobschedulerId(), JobSchedulerObjectType.SCHEDULE, scheduleFromDb.getName()), dbItemInventoryInstance);

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
            Globals.disconnect(connection);
        }
    }

}