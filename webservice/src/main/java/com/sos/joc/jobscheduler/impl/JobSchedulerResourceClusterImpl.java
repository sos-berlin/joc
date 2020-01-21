package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.Clusters;
import com.sos.schema.JsonValidator;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceCluster {

	private static final String API_CALL = "./jobscheduler/cluster";

	@Override
	public JOCDefaultResponse postJobschedulerCluster(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, JobSchedulerId.class);
		    JobSchedulerId jobSchedulerFilter = Globals.objectMapper.readValue(filterBytes, JobSchedulerId.class);
            
            boolean permitted = getPermissonsJocCockpit(jobSchedulerFilter.getJobschedulerId(), accessToken)
					.getJobschedulerMasterCluster().getView().isStatus()
					|| getPermissonsJocCockpit(jobSchedulerFilter.getJobschedulerId(), accessToken)
							.getJobschedulerMaster().getView().isStatus();
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerFilter, accessToken,
					jobSchedulerFilter.getJobschedulerId(), permitted);
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
			String command = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders cluster",
					"/does/not/exist");
			jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

			Cluster cluster = new Cluster();
			cluster.setJobschedulerId(jobSchedulerFilter.getJobschedulerId());
			cluster.setSurveyDate(jocXmlCommand.getSurveyDate());
			Element clusterElem = (Element) jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/state/cluster");
			if (clusterElem == null) {
				cluster.set_type(ClusterType.STANDALONE);
			} else {
				if (!getPermissonsJocCockpit(jobSchedulerFilter.getJobschedulerId(), accessToken)
						.getJobschedulerMasterCluster().getView().isStatus()) {
					return accessDeniedResponse();
				}
				NodeList clusterMembers = jocXmlCommand.getSosxml().selectNodeList(clusterElem,
						"cluster_member[@distributed_orders='yes']");
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
