
package com.sos.joc.audit.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.audit.AuditLogFilter;

public interface IAuditLogResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postAuditLog(@HeaderParam("access_token") String accessToken, AuditLogFilter auditLogFilter) throws Exception;

}
