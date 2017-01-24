package com.sos.joc.configuration.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.configuration.resource.IJocConfiguration;
import com.sos.joc.db.configuration.JocConfigurationDbItem;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;

@Path("save_configuration")
public class JocConfigurationImpl extends JOCResourceImpl implements IJocConfiguration {

    private static final String API_CALL = "./configuration/save_configuration";

    @Override
    public JOCDefaultResponse saveConfiguration(String accessToken, JobFilter jobFilter) throws Exception {

        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection();
        
            initLogging(API_CALL, jobFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobscheduler_id", jobFilter.getJob());
            checkRequiredParameter("user_id", jobFilter.getJob());
            checkRequiredParameter("object_type", jobFilter.getJob());
            checkRequiredParameter("object_source", jobFilter.getJob());
            checkRequiredParameter("name", jobFilter.getJob());

            JocConfigurationDbLayer dbLayer = new JocConfigurationDbLayer(connection);
            // Long instanceId = jocConfigurationDbItem.getId();
            // dbLayer.saveConfiguration(instanceId);

            return null;// JOCDefaultResponse("");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }finally{
            Globals.disconnect(connection);
        }
    }

}