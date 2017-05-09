package com.sos.joc.jobscheduler.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.inventory.db.InventoryCleanup;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyJobSchedulerAudit;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerCleanUpInventoryResource;
import com.sos.joc.model.jobscheduler.HostPortParameter;

@Path("jobscheduler")
public class JobSchedulerCleanupInventoryImpl extends JOCResourceImpl implements IJobSchedulerCleanUpInventoryResource {

    private static String API_CALL = "./jobscheduler/cleanup";

    @Override
    public JOCDefaultResponse postJobschedulerCleanupInventory(String accessToken, HostPortParameter hostPortParameter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            // TODO permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, hostPortParameter, accessToken, hostPortParameter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobschedulerMaster().isAbort());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(hostPortParameter.getAuditLog());
            ModifyJobSchedulerAudit jobschedulerAudit = new ModifyJobSchedulerAudit(hostPortParameter);
            logAuditMessage(jobschedulerAudit);
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);
            new InventoryCleanup().cleanup(connection, hostPortParameter.getJobschedulerId(), hostPortParameter.getHost(), hostPortParameter
                    .getPort());
            Globals.commit(connection);
            storeAuditLogEntry(jobschedulerAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
}
