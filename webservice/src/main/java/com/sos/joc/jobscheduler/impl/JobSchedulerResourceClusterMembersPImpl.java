package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.MastersPSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Supervisor;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersPImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembersP {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {

        LOGGER.debug("init JobschedulerClusterMembers");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerDefaultBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            MastersPSchema entity = new MastersPSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());
            ArrayList<Jobscheduler> masters = new ArrayList<Jobscheduler>();
            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost("myHost");
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(-1);
            jobscheduler.setStartedAt(new Date());

            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            Os os = new Os();
            os.setArchitecture("64");
            os.setDistribution("myDistribution");
            os.setName("myName");
            jobscheduler.setOs(os);

            Supervisor supervisor = new Supervisor();
            supervisor.setHost("myHost");
            supervisor.setJobschedulerId("myJobScheduelerId");
            supervisor.setPort(-1);
            jobscheduler.setSupervisor(supervisor);

            jobscheduler.setTimeZone("UTC");
            jobscheduler.setVersion("1.11");
            jobscheduler.setSurveyDate(new Date());
            masters.add(jobscheduler);

            Jobscheduler jobscheduler2 = new Jobscheduler();
            jobscheduler2.setHost("myHost2");
            jobscheduler2.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler2.setPort(-1);
            jobscheduler2.setStartedAt(new Date());

            ClusterMemberTypeSchema clusterMemberTypeSchema2 = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema2.setPrecedence(-1);
            clusterMemberTypeSchema2.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema2);

            Os os2 = new Os();
            os2.setArchitecture("32");
            os2.setDistribution("myDistribution2");
            os2.setName("myName2");
            jobscheduler.setOs(os2);

            Supervisor supervisor2 = new Supervisor();
            supervisor2.setHost("myHost2");
            supervisor2.setJobschedulerId("myJobScheduelerId2");
            supervisor2.setPort(-1);
            jobscheduler.setSupervisor(supervisor2);

            jobscheduler2.setTimeZone("UTC");
            jobscheduler2.setVersion("1.11");
            jobscheduler2.setSurveyDate(new Date());
            masters.add(jobscheduler2);

            entity.setMasters(masters);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
