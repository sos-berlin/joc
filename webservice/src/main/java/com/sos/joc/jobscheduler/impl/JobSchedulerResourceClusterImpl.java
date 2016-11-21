package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.Clusters;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceCluster {

    private static final String API_CALL = "./jobscheduler/cluster";

    @Override
    public JOCDefaultResponse postJobschedulerCluster(String accessToken, JobSchedulerId jobSchedulerFilter) {
        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String command = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders cluster", "/does/not/exist");
            jocXmlCommand.executePostWithThrowBadRequest(command, accessToken);

            Cluster cluster = new Cluster();
            cluster.setJobschedulerId(jobSchedulerFilter.getJobschedulerId());
            cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
            Element clusterElem = (Element) jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/state/cluster");
            if (clusterElem == null) {
                cluster.set_type(ClusterType.STANDALONE);
            } else {
                NodeList clusterMembers = jocXmlCommand.getSosxml().selectNodeList(clusterElem, "cluster_member[@distributed_orders='yes']");
                if (clusterMembers != null && clusterMembers.getLength() > 0) {
                    cluster.set_type(ClusterType.ACTIVE);
                } else {
                    cluster.set_type(ClusterType.PASSIVE);
                }
            }
            Clusters entity = new Clusters();
            entity.setCluster(cluster);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }
}
