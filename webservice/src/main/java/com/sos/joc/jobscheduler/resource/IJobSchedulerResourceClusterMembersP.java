
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.MastersPSchema;

@Path("Ijobscheduler")
public interface IJobSchedulerResourceClusterMembersP {

    @POST
    @Path("cluster/members/p")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse postJobschedulerClusterMembers(@HeaderParam("access_token") String accessToken,
            JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;

    public class JobschedulerClusterMembersPResponse extends com.sos.joc.support.ResponseWrapper {

        private JobschedulerClusterMembersPResponse(Response delegate) {
            super(delegate);
        }

        public static IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse responseStatus200(MastersPSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse(responseBuilder.build());
        }
    }

}
