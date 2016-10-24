package com.sos.joc.schedule.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;
import com.sos.joc.model.schedule.ModifyRunTime;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceSetRunTimeImpl.class);
    private static final String API_CALL = "./schedule/set_run_time";

    @Override
    public JOCDefaultResponse postScheduleSetRuntime(String accessToken, ModifyRunTime modifyRuntime) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            JOCDefaultResponse jocDefaultResponse = init(modifyRuntime.getJobschedulerId(), getPermissons(accessToken).getSchedule().isEdit());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            //TODO 
            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyRuntime));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyRuntime));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}