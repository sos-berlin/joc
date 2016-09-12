package com.sos.joc.schedules.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.schedules.resource.ISchedulesResourceP;
import com.sos.joc.model.schedule.Schedule;
import com.sos.joc.model.schedule.SchedulesFilterSchema;
import com.sos.joc.model.schedule.SchedulesPSchema;
import com.sos.joc.model.schedule.Substitute;
import com.sos.joc.model.schedule.UsedByJob;
import com.sos.joc.model.schedule.UsedByOrder;
 

@Path("schedules")
public class SchedulesResourcePImpl extends JOCResourceImpl implements ISchedulesResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResourcePImpl.class);

    @Override
    public JOCDefaultResponse postSchedulesP(String accessToken, SchedulesFilterSchema schedulesFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(schedulesFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init schedules/p");

            SchedulesPSchema entity = new SchedulesPSchema();
            entity.setDeliveryDate(new Date());
            List<Schedule> listOfSchedules = new ArrayList<Schedule>();
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
            
            listOfSchedules.add(schedule);
            
            entity.setSchedules(listOfSchedules);
             
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

     

}