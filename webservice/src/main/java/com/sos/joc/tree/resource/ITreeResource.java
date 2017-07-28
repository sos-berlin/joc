
package com.sos.joc.tree.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.tree.TreeFilter;

public interface ITreeResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTree(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, TreeFilter treeBody) throws Exception;

}
