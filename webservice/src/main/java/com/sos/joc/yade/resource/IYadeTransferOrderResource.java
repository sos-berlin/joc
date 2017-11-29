package com.sos.joc.yade.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.yade.ModifyTransfer;


public interface IYadeTransferOrderResource {

    @POST
    @Path("transfer/order")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postYadeTransferOrder(@HeaderParam("X-Access-Token") String accessToken, ModifyTransfer filterBody)
            throws Exception;
}
