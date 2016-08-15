
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
import com.sos.joc.model.jobscheduler.MastersVSchema;

public interface IJobSchedulerResourceClusterMembers {

    @POST
    @Path("cluster/members")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse postJobschedulerClusterMembers(@HeaderParam("access_token") String accessToken,
            JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;

    public class JobschedulerClusterMembersResponse extends com.sos.joc.support.ResponseWrapper {

        private JobschedulerClusterMembersResponse(Response delegate) {
            super(delegate);
        }

        public static IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse responseStatus200(MastersVSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON);
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse(responseBuilder.build());
        }
    }

}
