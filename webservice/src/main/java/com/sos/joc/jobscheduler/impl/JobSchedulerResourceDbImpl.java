package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.Database;
import com.sos.joc.model.jobscheduler.DB;
import com.sos.joc.model.jobscheduler.DBState;
import com.sos.joc.model.jobscheduler.DBStateText;

@Path("jobscheduler")
public class JobSchedulerResourceDbImpl extends JOCResourceImpl implements IJobSchedulerResourceDb {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerDb(String accessToken, JobSchedulerId jobSchedulerFilterSchema) {

        LOGGER.debug("init jobscheduler/db");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice

            DB entity = new DB();
            Database database = new Database();
            database.setDbms("myDbms");
            database.setSurveyDate(dbItemInventoryInstance.getModified());
            database.setVersion(dbItemInventoryInstance.getVersion());
            DBState state = new DBState();
            state.setSeverity(0);
            state.set_text(DBStateText.RUNNING);
            database.setState(state);
            entity.setDatabase(database);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
