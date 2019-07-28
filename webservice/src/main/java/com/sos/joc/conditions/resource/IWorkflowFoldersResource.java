package com.sos.joc.conditions.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.conditions.WorkflowFolders;

public interface IWorkflowFoldersResource {

    @POST
    @Path("workflow_folders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse workflowFolders(            
            @HeaderParam("X-Access-Token") String accessToken, WorkflowFolders workflowFolders) throws Exception;
}
