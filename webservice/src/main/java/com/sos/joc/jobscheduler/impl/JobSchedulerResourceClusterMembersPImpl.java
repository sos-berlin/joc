package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.ClusterMembersPermanent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.MastersP;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersPImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembersP {

    private static final String API_CALL = "./jobscheduler/cluster/members/p";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            MastersP entity = new MastersP();
            entity.setMasters(ClusterMembersPermanent.getClusterMembers(jobSchedulerFilter.getJobschedulerId()));
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