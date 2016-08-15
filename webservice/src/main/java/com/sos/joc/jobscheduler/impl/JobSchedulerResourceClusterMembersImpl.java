package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.MastersVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {

    @Override
    public JobschedulerClusterMembersResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerClusterMembersResponse jobschedulerClusterResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerClusterMembersResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerClusterMembersResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerMaster().getView().isStatus()) {
            return JobschedulerClusterMembersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()) {
            return JobschedulerClusterMembersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerDefaultBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
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
                jobscheduler.setHost(jocXmlCommand.getAttribut("host"));
                jobscheduler.setJobschedulerId(jocXmlCommand.getAttribut("cluster_member_id"));
                jobscheduler.setPort(jocXmlCommand.getAttributAsIntegerOr0("tcp_port"));
                jobscheduler.setStartedAt(jocXmlCommand.getAttributAsDate("running_since"));
                State state = new State();
                state.setSeverity(Severity._0);
                state.setText(Text.RUNNING);
                if ("yes".equals(jocXmlCommand.getAttribut("dead"))){
                    state.setSeverity(Severity._1);
                    state.setText(Text.DEAD);
                }
                jobscheduler.setState(state);
                jobscheduler.setSurveyDate(jocXmlCommand.getSurveyDate());
                masters.add(jobscheduler);

            }

            entity.setMasters(masters);

            jobschedulerClusterResponse = JobschedulerClusterMembersResponse.responseStatus200(entity);

            return jobschedulerClusterResponse;
        } catch (Exception e) {
            return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
