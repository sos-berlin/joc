package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.ClusterMembersPermanent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.MastersP;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersPImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembersP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceClusterMembersPImpl.class);
    private static final String API_CALL = "API-CALL: ./jobscheduler/cluster/members/p";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilterSchema) {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),
                    getPermissons(accessToken).getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            MastersP entity = new MastersP();
            entity.setDeliveryDate(new Date());
            entity.setMasters(ClusterMembersPermanent.getClusterMembers(jobSchedulerFilterSchema.getJobschedulerId()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(API_CALL, "USER: "+getJobschedulerUser().getSosShiroCurrentUser().getUsername());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}