package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.MastersVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) {
        LOGGER.debug("init JobschedulerClusterMembers");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");
            jocXmlCommand.createNodeList("//spooler/answer/state/cluster/cluster_member");

            int count = jocXmlCommand.getNodeList().getLength();

            MastersVSchema entity = new MastersVSchema();
            ArrayList<Jobscheduler_> masters = new ArrayList<Jobscheduler_>();

            for (int i = 0; i < count; i++) {
                jocXmlCommand.getElementFromList(i);

                entity.setDeliveryDate(new Date());
                Jobscheduler_ jobscheduler = new Jobscheduler_();
                jobscheduler.setHost(jocXmlCommand.getAttribute("host"));
                jobscheduler.setJobschedulerId(jocXmlCommand.getAttribute("cluster_member_id"));
                jobscheduler.setPort(jocXmlCommand.getAttributeAsIntegerOr0("tcp_port"));
                jobscheduler.setStartedAt(jocXmlCommand.getAttributeAsDate("running_since"));
                State state = new State();
                state.setSeverity(0);
                state.setText(Text.RUNNING);
                if ("yes".equals(jocXmlCommand.getAttribute("dead"))) {
                    state.setSeverity(1);
                    state.setText(Text.DEAD);
                }
                jobscheduler.setState(state);
                jobscheduler.setSurveyDate(jocXmlCommand.getSurveyDate());
                masters.add(jobscheduler);

            }

            entity.setMasters(masters);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
