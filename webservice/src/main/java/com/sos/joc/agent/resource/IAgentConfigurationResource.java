package com.sos.joc.agent.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.agent.AgentConfiguration200;
import com.sos.joc.model.agent.AgentConfigurationFilter;

public interface IAgentConfigurationResource {
    
    @POST
    @Path("read")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse readAgentConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, AgentConfigurationFilter agentFilter) throws Exception;

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse saveAgentConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, AgentConfiguration200 configuration) throws Exception;
    
    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse deleteAgentConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, AgentConfigurationFilter agentFilter) throws Exception;

}
