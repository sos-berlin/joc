package com.sos.joc.configuration.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.configuration.Configuration;

 
public interface IJocConfigurationResource{

    @POST
    @Path("save")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSaveConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, Configuration configuration) throws Exception;

    @POST
    @Path("delete")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postDeleteConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, Configuration configuration) throws Exception;

    @POST
    @Path("share")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postShareConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken,  Configuration configuration) throws Exception;
    
    @POST
    @Path("make_private")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postMakePrivate(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken,  Configuration configuration) throws Exception;

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postReadConfiguration(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken,  Configuration configuration) throws Exception;

}
