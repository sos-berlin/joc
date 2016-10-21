package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceIds;
import com.sos.joc.model.jobscheduler.JobSchedulerIds;

@Path("jobscheduler")
public class JobSchedulerResourceIdsImpl extends JOCResourceImpl implements IJobSchedulerResourceIds {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceIdsImpl.class);
    private static final String API_CALL = "API-CALL: ./jobscheduler/ids";

    @Override
    public JOCDefaultResponse postJobschedulerIds(String accessToken) {
        LOGGER.debug(API_CALL);
        try {
            Globals.beginTransaction();
            JOCDefaultResponse jocDefaultResponse = init("", getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            List<DBItemInventoryInstance> listOfInstance = dbLayer.getJobSchedulerIds();
            Set<String> jobSchedulerIds = new HashSet<String>();
            String first = "";
            for (DBItemInventoryInstance inventoryInstance : listOfInstance) {
                jobSchedulerIds.add(inventoryInstance.getSchedulerId());
                if ("".equals(first)) {
                    first = inventoryInstance.getSchedulerId();
                }
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            String selectedInstance = jocPreferences.get("selected_instance", first);

            JobSchedulerIds entity = new JobSchedulerIds();
            entity.getJobschedulerIds().addAll(jobSchedulerIds);
            entity.setSelected(selectedInstance);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            e.addErrorMetaInfo(API_CALL, "USER: "+getJobschedulerUser().getSosShiroCurrentUser().getUsername());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
        finally{
            Globals.rollback();
        }

    }

}
