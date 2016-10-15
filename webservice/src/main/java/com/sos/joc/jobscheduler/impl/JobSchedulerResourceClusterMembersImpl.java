package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.MastersV;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilterSchema) {
        LOGGER.debug("init jobscheduler/cluster/members");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");
            jocXmlCommand.createNodeList("//spooler/answer/state/cluster/cluster_member");

            int count = jocXmlCommand.getNodeList().getLength();

            MastersV entity = new MastersV();
            ArrayList<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();

            for (int i = 0; i < count; i++) {
                jocXmlCommand.getElementFromList(i);

                entity.setDeliveryDate(new Date());
                JobSchedulerV jobscheduler = new JobSchedulerV();
                jobscheduler.setHost(jocXmlCommand.getAttribute("host"));
                jobscheduler.setJobschedulerId(jocXmlCommand.getAttribute("cluster_member_id"));
                jobscheduler.setPort(jocXmlCommand.getAttributeAsIntegerOr0("tcp_port"));
                jobscheduler.setStartedAt(jocXmlCommand.getAttributeAsDate("running_since"));
                JobSchedulerState state = new JobSchedulerState();
                state.setSeverity(0);
                state.set_text(JobSchedulerStateText.RUNNING);
                if ("yes".equals(jocXmlCommand.getAttribute("dead"))) {
                    state.setSeverity(1);
                    state.set_text(JobSchedulerStateText.DEAD);
                }
                jobscheduler.setState(state);
                jobscheduler.setSurveyDate(jocXmlCommand.getSurveyDate());
                masters.add(jobscheduler);

            }

            entity.setMasters(masters);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
