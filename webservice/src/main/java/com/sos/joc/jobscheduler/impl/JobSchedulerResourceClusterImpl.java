package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterSchema;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceCluster {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerCluster(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) {
        LOGGER.debug("init jobscheduler/cluster");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");

            ClusterSchema entity = new ClusterSchema();

            Cluster cluster = new Cluster();
            cluster.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());

            // TODO JOC Cockpit Webservice
            cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
            cluster.setType("myType");

            entity.setDeliveryDate(new Date());
            entity.setCluster(cluster);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
