package com.sos.joc.jobchains.resource;

 
 
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
 

@Path("Ijob_chains")
public interface IJobchainsResource {
    
    /*   @POST
    @Path("job_chains")
    @Consumes("application/json")
    @Produces({ "application/json" })
   public IJobchainsResource.PostJobchainsResponse postJobchains(
            @QueryParam("host") String host, 
            @QueryParam("port") Long port,
            @QueryParam("compact") boolean compact,
            @QueryParam("regex") String regex,
            @QueryParam("job_chains") String[] jobChains,
            @QueryParam("state") String state,
            @QueryParam("access_token") String accessToken) throws Exception;    

    
   */      
    
  /*  public class PostJobchainsResponse extends com.sos.joc.support.ResponseWrapper {
        
        private PostJobchainsResponse(Response delegate) {
            super(delegate);
        }
        
        public static IJobchainsResource.PostJobChainsResponse responseStatus200(JobchainsPermanentPart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new IJobchainsResource.PostJobchainsResponse(responseBuilder.build());
        }
        
        
        public static IJobchainsResource.PostJobchainsResponse responseStatus420(ErrorObjectWithDeliveryDate entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new IJobchainsResource.PostJobchainsResponse(responseBuilder.build());
        }
        
    }
    
    
}
*/
}
