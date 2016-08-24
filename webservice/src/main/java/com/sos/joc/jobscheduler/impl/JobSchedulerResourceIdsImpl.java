package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.InventoryDBLayer;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceIds;
import com.sos.joc.model.jobscheduler.JobschedulerIdsSchema;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceIdsImpl extends JOCResourceImpl implements IJobSchedulerResourceIds {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResourceIdsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerIds(String accessToken) {
        LOGGER.debug("init JobschedulerIds");
        try {
            JOCDefaultResponse jocDefaultResponse = init("", getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobschedulerIdsSchema entity = new JobschedulerIdsSchema();

            // TODO JOC Cockpit Webservice

            ArrayList<String> jobschedulerIs = new ArrayList<String>();

            entity.setDeliveryDate(new Date());
            String first = "";

            InventoryDBLayer dbLayer = new InventoryDBLayer(jobschedulerUser.getSosShiroCurrentUser().getSosHibernateConnection());
            List<DBItemInventoryInstance> listOfInstance = dbLayer.getInventoryInstances();
            for (DBItemInventoryInstance inventoryInstance : listOfInstance) {
                jobschedulerIs.add(inventoryInstance.getSchedulerId());
                if ("".equals(first)) {
                    first = inventoryInstance.getSchedulerId();
                }
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            String selectedInstance = jocPreferences.get("selected_instance",first);

            entity.setSelected(selectedInstance);
            entity.setJobschedulerIds(jobschedulerIs);

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (Exception e) {

            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
