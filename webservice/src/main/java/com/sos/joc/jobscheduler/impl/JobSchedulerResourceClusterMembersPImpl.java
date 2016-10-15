package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.MastersP;
import com.sos.joc.model.jobscheduler.OperatingSystem;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersPImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembersP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilterSchema) {

        LOGGER.debug("init jobscheduler/cluster/members/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            MastersP entity = new MastersP();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());
            ArrayList<JobSchedulerP> masters = new ArrayList<JobSchedulerP>();
            JobSchedulerP jobscheduler = new JobSchedulerP();
            jobscheduler.setHost("myHost");
            jobscheduler.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());
            jobscheduler.setPort(-1);
            jobscheduler.setStartedAt(new Date());

            ClusterMemberType clusterMemberTypeSchema = new ClusterMemberType();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.set_type(ClusterMemberType._type.STANDALONE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            OperatingSystem os = new OperatingSystem();
            os.setArchitecture("64");
            os.setDistribution("myDistribution");
            os.setName("myName");
            jobscheduler.setOs(os);

            HostPortParameter supervisor = new HostPortParameter();
            supervisor.setHost("myHost");
            supervisor.setJobschedulerId("myJobScheduelerId");
            supervisor.setPort(-1);
            jobscheduler.setSupervisor(supervisor);

            jobscheduler.setTimeZone("UTC");
            jobscheduler.setVersion("1.11");
            jobscheduler.setSurveyDate(new Date());
            masters.add(jobscheduler);

            JobSchedulerP jobscheduler2 = new JobSchedulerP();
            jobscheduler2.setHost("myHost2");
            jobscheduler2.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());
            jobscheduler2.setPort(-1);
            jobscheduler2.setStartedAt(new Date());

            ClusterMemberType clusterMemberTypeSchema2 = new ClusterMemberType();
            clusterMemberTypeSchema2.setPrecedence(-1);
            clusterMemberTypeSchema2.set_type(ClusterMemberType._type.STANDALONE);
            jobscheduler.setClusterType(clusterMemberTypeSchema2);

            OperatingSystem os2 = new OperatingSystem();
            os2.setArchitecture("32");
            os2.setDistribution("myDistribution2");
            os2.setName("myName2");
            jobscheduler.setOs(os2);

            HostPortParameter supervisor2 = new HostPortParameter();
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
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
