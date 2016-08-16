package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterSchema;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl  extends JOCResourceImpl implements IJobSchedulerResourceCluster {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JobschedulerClusterResponse postJobschedulerCluster(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerClusterResponse jobschedulerClusterResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return JobschedulerClusterResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JobschedulerClusterResponse.responseStatus440(JocCockpitResponse.getError401Schema(jobschedulerUser,e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }


        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerDefaultBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");
 
            ClusterSchema entity = new ClusterSchema();
            
            Cluster cluster = new Cluster();
            cluster.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());

            //TODO JOC Cockpit Webservice
            cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
            cluster.setType(Cluster.Type.ACTIVE);
 
            entity.setDeliveryDate(new Date());
            entity.setCluster(cluster);

            jobschedulerClusterResponse = JobschedulerClusterResponse.responseStatus200(entity);

            return jobschedulerClusterResponse;
        } catch (Exception e) {
            return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }
 

    

}
