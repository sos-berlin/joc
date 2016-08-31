
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;

public interface IJobSchedulerResourceClusterMembersP {

    @POST
    @Path("cluster/members/p")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerClusterMembers(@HeaderParam("access_token") String accessToken,
            JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;

 

}