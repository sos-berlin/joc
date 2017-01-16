package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.jobscheduler.JobSchedulerVCallable;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResource;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

@Path("jobscheduler")
public class JobSchedulerResourceImpl extends JOCResourceImpl implements IJobSchedulerResource {

    private static final String API_CALL = "./jobscheduler";

    @Override
    public JOCDefaultResponse postJobscheduler(String accessToken, HostPortParameter jobSchedulerBody) throws Exception {

        try {
            initLogging(API_CALL, jobSchedulerBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerBody.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            getJobSchedulerInstanceByHostPort(jobSchedulerBody);
            JobSchedulerV200 entity = new JobSchedulerV200();
            entity.setJobscheduler(new JobSchedulerVCallable(dbItemInventoryInstance, accessToken).call());
            entity.setDeliveryDate(new Date());
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void getJobSchedulerInstanceByHostPort(HostPortParameter jobSchedulerBody) throws DBMissingDataException, DBInvalidDataException,
            DBConnectionRefusedException {
        if (jobSchedulerBody.getHost() != null && !jobSchedulerBody.getHost().isEmpty() && jobSchedulerBody.getPort() != null && jobSchedulerBody
                .getPort() > 0) {

            JobSchedulerIdentifier jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerBody.getJobschedulerId());
            jobSchedulerIdentifier.setHost(jobSchedulerBody.getHost());
            jobSchedulerIdentifier.setPort(jobSchedulerBody.getPort());

            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            dbItemInventoryInstance = dbLayer.getInventoryInstanceByHostPort(jobSchedulerIdentifier);
        }
    }
}
