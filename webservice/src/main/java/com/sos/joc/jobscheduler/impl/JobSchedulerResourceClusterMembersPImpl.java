package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.ClusterType;
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
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),
                    getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            MastersP entity = new MastersP();
            entity.setDeliveryDate(new Date());
            ArrayList<JobSchedulerP> masters = new ArrayList<JobSchedulerP>();

            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobSchedulerFilterSchema.getJobschedulerId());
            if(schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
                for (DBItemInventoryInstance instance : schedulersFromDb) {
                    JobSchedulerP jobscheduler = new JobSchedulerP();
                    jobscheduler.setHost(instance.getHostname());
                    jobscheduler.setJobschedulerId(instance.getSchedulerId());
                    jobscheduler.setPort(instance.getPort());
                    jobscheduler.setStartedAt(instance.getStartedAt());
                    ClusterMemberType clusterMemberType = new ClusterMemberType();
                    clusterMemberType.setPrecedence(instance.getPrecedence());
                    switch (instance.getClusterType().toUpperCase()) {
                        case "STANDALONE":
                            clusterMemberType.set_type(ClusterType.STANDALONE);
                            break;
                        case "ACTIVE":
                            clusterMemberType.set_type(ClusterType.ACTIVE);
                            break;
                        case "PASSIVE":
                            clusterMemberType.set_type(ClusterType.PASSIVE);
                            break;
                    }
                    jobscheduler.setClusterType(clusterMemberType);
                    jobscheduler.setTimeZone(instance.getTimeZone());
                    jobscheduler.setVersion(instance.getVersion());
                    jobscheduler.setSurveyDate(instance.getModified());
                    InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
                    DBItemInventoryOperatingSystem osFromDb = osLayer.getInventoryOperatingSystem(instance.getOsId());
                    if (osFromDb != null) {
                        OperatingSystem os = new OperatingSystem();
                        os.setArchitecture(osFromDb.getArchitecture());
                        os.setDistribution(osFromDb.getDistribution());
                        os.setName(osFromDb.getName());
                        jobscheduler.setOs(os);
                    }
                    if(instance.getSupervisorId() != DBLayer.DEFAULT_ID) {
                        DBItemInventoryInstance supervisorFromDb = instanceLayer.getInventoryInstancesByKey(instance.getSupervisorId());
                        HostPortParameter supervisor = new HostPortParameter();
                        supervisor.setHost(supervisorFromDb.getHostname());
                        supervisor.setJobschedulerId(supervisorFromDb.getSchedulerId());
                        supervisor.setPort(supervisorFromDb.getPort());
                        jobscheduler.setSupervisor(supervisor);
                    }
                    masters.add(jobscheduler);
                }               
            }
            entity.setMasters(masters);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}