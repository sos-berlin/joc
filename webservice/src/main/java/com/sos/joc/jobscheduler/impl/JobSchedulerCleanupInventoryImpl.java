package com.sos.joc.jobscheduler.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.shiro.session.InvalidSessionException;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.inventory.db.InventoryCleanup;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobSchedulerAudit;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
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
                    getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getAdministration().isRemoveOldInstances());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            checkRequiredParameter("host", hostPortParameter.getHost());
            checkRequiredParameter("port", hostPortParameter.getPort());
            checkRequiredComment(hostPortParameter.getAuditLog());
            ModifyJobSchedulerAudit jobschedulerAudit = new ModifyJobSchedulerAudit(hostPortParameter);
            logAuditMessage(jobschedulerAudit);
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            Globals.beginTransaction(connection);
            DBItemInventoryInstance schedulerInstanceFromDb = instanceLayer.getInventoryInstanceByHostPort(hostPortParameter.getHost(), hostPortParameter.getPort(), hostPortParameter.getJobschedulerId());
            boolean jobSchedulerIsRunning = true;
            try {
                JOCXmlCommand jocCommand = new JOCXmlCommand(schedulerInstanceFromDb);
                jocCommand.executePost("<param.get name=\"\" />", accessToken);
            }
            catch (JobSchedulerConnectionRefusedException e) {
                jobSchedulerIsRunning = false;
            }
            catch (JocException e) {
                //
            }
            if (jobSchedulerIsRunning) {
                throw new JobSchedulerBadRequestException("Cleanup function is not available when JobScheduler is still running.");
            }
            new InventoryCleanup().cleanup(schedulerInstanceFromDb);
            Globals.commit(connection);
            if (hostPortParameter.getJobschedulerId().equals(dbItemInventoryInstance.getSchedulerId()) && hostPortParameter.getHost().equals(
                    dbItemInventoryInstance.getHostname()) && hostPortParameter.getPort() == dbItemInventoryInstance.getPort()) {
                try {
                    SOSShiroCurrentUser shiroUser = getJobschedulerUser().getSosShiroCurrentUser();
                    shiroUser.removeSchedulerInstanceDBItem(dbItemInventoryInstance.getSchedulerId());
                } catch (InvalidSessionException e1) {
                    throw new SessionNotExistException(e1);
                }
            }
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
