package com.sos.joc.yade.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.yade.FilesFilter;


public interface IYadeFilesResource {

    @POST
    @Path("files")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postYadeFiles(@HeaderParam("X-Access-Token") String accessToken, FilesFilter filterBody)
            throws Exception;
}
