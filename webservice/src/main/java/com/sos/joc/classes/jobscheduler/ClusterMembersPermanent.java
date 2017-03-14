package com.sos.joc.classes.jobscheduler;

import java.util.ArrayList;
import java.util.List;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.OperatingSystem;

public class ClusterMembersPermanent {

    public static List<JobSchedulerP> getClusterMembers(String jobschedulerId) throws Exception {

        SOSHibernateSession connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection("getClusterMembers");

            Globals.beginTransaction(connection);

            List<JobSchedulerP> masters = new ArrayList<JobSchedulerP>();
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobschedulerId);
            if (schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
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
                    InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(connection);
                    DBItemInventoryOperatingSystem osFromDb = osLayer.getInventoryOperatingSystem(instance.getOsId());
                    if (osFromDb != null) {
                        OperatingSystem os = new OperatingSystem();
                        os.setArchitecture(osFromDb.getArchitecture());
                        os.setDistribution(osFromDb.getDistribution());
                        os.setName(osFromDb.getName());
                        jobscheduler.setOs(os);
                    }
                    if (instance.getSupervisorId() != DBLayer.DEFAULT_ID) {
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
            return masters;
        } catch (Exception e) {
            throw e;
        } finally {
            Globals.disconnect(connection);
        }
    }

}
