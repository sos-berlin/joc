
package com.sos.joc.tree.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface ITreeResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTree(@HeaderParam("X-Access-Token") String accessToken, byte[] treeBody);
    
    @POST
    @Path("joe")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJoeTree(@HeaderParam("X-Access-Token") String accessToken, byte[] treeBody);

}
