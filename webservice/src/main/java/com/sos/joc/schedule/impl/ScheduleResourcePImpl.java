package com.sos.joc.schedule.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.schedule.resource.IScheduleResourceP;
import com.sos.joc.model.schedule.Schedule;
import com.sos.joc.model.schedule.Schedule200PSchema;
import com.sos.joc.model.schedule.ScheduleFilterSchema;
import com.sos.joc.model.schedule.Substitute;
import com.sos.joc.model.schedule.UsedByJob;
import com.sos.joc.model.schedule.UsedByOrder;

@Path("schedule")
public class ScheduleResourcePImpl extends JOCResourceImpl implements IScheduleResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourcePImpl.class);

    @Override
    public JOCDefaultResponse postScheduleP(String accessToken, ScheduleFilterSchema scheduleFilterSchema) throws Exception {
        LOGGER.debug("init schedule/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(scheduleFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Schedule200PSchema entity = new Schedule200PSchema();
            entity.setDeliveryDate(new Date());

            Schedule schedule = new Schedule();
            schedule.setConfigurationDate(new Date());
            schedule.setName("myName");
            schedule.setPath("myPath");
            Substitute substitute = new Substitute();
            substitute.setPath("mypath");
            substitute.setValidFrom(new Date());
            substitute.setValidTo(new Date());
            schedule.setSubstitute(substitute);
            schedule.setSurveyDate(new Date());
            schedule.setTitle("myTitle");
            List<UsedByJob> listOfUsesByJob = new ArrayList<UsedByJob>();
            UsedByJob usedByJob = new UsedByJob();
            usedByJob.setJob("myUsedByJob");
            listOfUsesByJob.add(usedByJob);

            schedule.setUsedByJobs(listOfUsesByJob);

            List<UsedByOrder> listOfUsesByOrder = new ArrayList<UsedByOrder>();
            UsedByOrder usedByOrder = new UsedByOrder();
            usedByOrder.setJobChain("myJobChain");
            usedByOrder.setOrderId("myOrderId");
            listOfUsesByOrder.add(usedByOrder);

            schedule.setUsedByOrders(listOfUsesByOrder);

            entity.setSchedule(schedule);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}