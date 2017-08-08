
package com.sos.joc.event.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.ModifyOrders;

 
public interface IModifyCustomEventResource {

    @POST
    @Path("custom/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse addEvent(            
            @HeaderParam("X-Access-Token") String accessToken, ModifyOrders modifyEvent) throws Exception;
    
    @POST
    @Path("custom/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse removeEvent(            
            @HeaderParam("X-Access-Token") String accessToken, ModifyOrders modifyEvent) throws Exception;
    
}
