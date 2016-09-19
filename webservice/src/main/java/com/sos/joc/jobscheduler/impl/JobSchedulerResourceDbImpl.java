package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.Database;
import com.sos.joc.model.jobscheduler.DbSchema;
import com.sos.joc.model.jobscheduler.State__;

@Path("jobscheduler")
public class JobSchedulerResourceDbImpl extends JOCResourceImpl implements IJobSchedulerResourceDb {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerDb(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) {

        LOGGER.debug("init jobscheduler/db");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice

            DbSchema entity = new DbSchema();
            Database database = new Database();
            database.setDbms("myDbms");
            database.setSurveyDate(dbItemInventoryInstance.getModified());
            database.setVersion(dbItemInventoryInstance.getVersion());
            State__ state = new State__();
            state.setSeverity(0);
            state.setText(State__.Text.RUNNING);
            database.setState(state);
            entity.setDatabase(database);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
