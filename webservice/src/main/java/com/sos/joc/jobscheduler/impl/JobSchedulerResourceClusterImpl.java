package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterSchema;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceCluster {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerCluster(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {
        LOGGER.debug("init JobschedulerCluster");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerDefaultBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");

            ClusterSchema entity = new ClusterSchema();

            Cluster cluster = new Cluster();
            cluster.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());

            // TODO JOC Cockpit Webservice
            cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
            cluster.setType(Cluster.Type.active);

            entity.setDeliveryDate(new Date());
            entity.setCluster(cluster);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
