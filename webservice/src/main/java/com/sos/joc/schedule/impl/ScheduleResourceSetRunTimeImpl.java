package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ModifyRunTime;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {

    private static final String API_CALL = "./schedule/set_run_time";

    @Override
    public JOCDefaultResponse postScheduleSetRuntime(String accessToken, ModifyRunTime modifyRuntime) throws Exception {
        try {
            initLogging(API_CALL, modifyRuntime);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyRuntime.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .isEdit());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}