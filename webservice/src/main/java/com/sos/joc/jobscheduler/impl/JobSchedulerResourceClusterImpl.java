package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.Clusters;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceCluster {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerCluster(String accessToken, JobSchedulerId jobSchedulerFilterSchema) {
        LOGGER.debug("init jobscheduler/cluster");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");

            Clusters entity = new Clusters();

            ClusterType cluster = new ClusterType();
            cluster.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());

            // TODO JOC Cockpit Webservice
            cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
            cluster.setType("myType");

            entity.setDeliveryDate(new Date());
            entity.setCluster(cluster);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
