
package com.sos.joc.schedules.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.SchedulesFilter;

 
public interface ISchedulesResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSchedules(            
            @HeaderParam("access_token") String accessToken, SchedulesFilter schedulesFilter) throws Exception;


 
    
}
