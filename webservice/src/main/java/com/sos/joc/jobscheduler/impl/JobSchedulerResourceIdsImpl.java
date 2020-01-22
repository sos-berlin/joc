package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceIds;
import com.sos.joc.model.jobscheduler.JobSchedulerIds;

@Path("jobscheduler")
public class JobSchedulerResourceIdsImpl extends JOCResourceImpl implements IJobSchedulerResourceIds {

    private static final String API_CALL = "./jobscheduler/ids";

    @Override
    public JOCDefaultResponse postJobschedulerIds(String accessToken) {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", getPermissonsJocCockpit("", accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            SOSShiroCurrentUser shiroUser = jobschedulerUser.getSosShiroCurrentUser();
            JOCPreferences jocPreferences = new JOCPreferences(shiroUser.getUsername());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> listOfInstance = dbLayer.getJobSchedulerIds();
            Set<String> jobSchedulerIds = new HashSet<String>();
            DBItemInventoryInstance first = null;
            if (listOfInstance != null && !listOfInstance.isEmpty()) {
                for (DBItemInventoryInstance inventoryInstance : listOfInstance) {
                    if (inventoryInstance.getSchedulerId() == null || inventoryInstance.getSchedulerId().isEmpty()) {
                        continue;
                    }
                    if (!getPermissonsJocCockpit(inventoryInstance.getSchedulerId(), accessToken).getJobschedulerMasterCluster().getView().isStatus()
                            && !getPermissonsJocCockpit(inventoryInstance.getSchedulerId(), accessToken).getJobschedulerMaster().getView().isStatus()) {
                        continue;
                    }
                    jobSchedulerIds.add(inventoryInstance.getSchedulerId());
                    if (first == null) {
                        first = inventoryInstance;
                    }
                }
                if (jobSchedulerIds.isEmpty()) {
                    return accessDeniedResponse();
                }
            } else {
                throw new DBMissingDataException("No JobSchedulers found in DB!");
            }
            String selectedInstanceSchedulerId = jocPreferences.get(WebserviceConstants.SELECTED_INSTANCE, first.getSchedulerId());

            if (!jobSchedulerIds.contains(selectedInstanceSchedulerId)) {
                if (first != null) {
                    selectedInstanceSchedulerId = first.getSchedulerId();
                    jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, first.getSchedulerId());
                }
            }

            DBItemInventoryInstance inst = jobschedulerUser.getSchedulerInstance(selectedInstanceSchedulerId);

            JobSchedulerIds entity = new JobSchedulerIds();
            entity.getJobschedulerIds().addAll(jobSchedulerIds);
            entity.setSelected(selectedInstanceSchedulerId);
            entity.setPrecedence(inst.getPrecedence());
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);

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
