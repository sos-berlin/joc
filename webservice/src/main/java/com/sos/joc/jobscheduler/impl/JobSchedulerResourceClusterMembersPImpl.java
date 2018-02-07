package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
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

    private static final String API_CALL = "./jobscheduler/cluster/members/p";
    private static final String MASTER_VIEW_PERM = "sos:products:joc_cockpit:jobscheduler_master:view:status";
    private static final String CLUSTER_VIEW_PERM = "sos:products:joc_cockpit:jobscheduler_master_cluster:view:status";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String xAccessToken, String accessToken, JobSchedulerId jobSchedulerFilter) {
        return postJobschedulerClusterMembers(getAccessToken(xAccessToken, accessToken), jobSchedulerFilter);
    }

    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        SOSHibernateSession connection = null;
        try {
            if (jobSchedulerFilter.getJobschedulerId() == null) {
                jobSchedulerFilter.setJobschedulerId("");
            }
            
            SOSShiroCurrentUser shiroUser = getJobschedulerUser(accessToken).getSosShiroCurrentUser();
            boolean isPermitted = true;
            String curJobSchedulerId = jobSchedulerFilter.getJobschedulerId();
            
            if (!curJobSchedulerId.isEmpty()) {
                if (curJobSchedulerId.equals(shiroUser.getSelectedJobSchedulerId())) {
                    isPermitted = getPermissonsJocCockpit(accessToken).getJobschedulerMasterCluster().getView().isStatus() || getPermissonsJocCockpit(
                            accessToken).getJobschedulerMaster().getView().isStatus();
                } else {
                    isPermitted = shiroUser.isPermitted(CLUSTER_VIEW_PERM, curJobSchedulerId) || shiroUser.isPermitted(MASTER_VIEW_PERM,
                            curJobSchedulerId);
                }
            }

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerFilter, accessToken, curJobSchedulerId,
                    isPermitted);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection("getClusterMembers");
            MastersP entity = new MastersP();
            List<JobSchedulerP> masters = new ArrayList<JobSchedulerP>();
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(curJobSchedulerId, true);
            if (schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
                String masterId = "";
                for (DBItemInventoryInstance instance : schedulersFromDb) {
                    if (curJobSchedulerId.isEmpty()) {
                        if (instance.getSchedulerId() == null || instance.getSchedulerId().isEmpty()) {
                            continue;
                        }
                        if (!masterId.equals(instance.getSchedulerId())) {
                            masterId = instance.getSchedulerId();
                            isPermitted = shiroUser.isPermitted(CLUSTER_VIEW_PERM, masterId) || shiroUser.isPermitted(MASTER_VIEW_PERM, masterId);
                        }
                        if (!isPermitted) {
                            continue;
                        }
                    }
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
                        DBItemInventoryInstance supervisorFromDb = instanceLayer.getInventoryInstanceByKey(instance.getSupervisorId());
                        if (supervisorFromDb != null) {
                            HostPortParameter supervisor = new HostPortParameter();
                            supervisor.setHost(supervisorFromDb.getHostname());
                            supervisor.setJobschedulerId(supervisorFromDb.getSchedulerId());
                            supervisor.setPort(supervisorFromDb.getPort());
                            jobscheduler.setSupervisor(supervisor);
                        }
                    }
                    masters.add(jobscheduler);
                }
                if (masters.isEmpty() && curJobSchedulerId.isEmpty()) {
                    return accessDeniedResponse();
                }
            }
            entity.setMasters(masters);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}