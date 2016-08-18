package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb;
import com.sos.joc.model.jobscheduler.Database;
import com.sos.joc.model.jobscheduler.Database.Dbms;
import com.sos.joc.model.jobscheduler.DbSchema;
import com.sos.joc.model.jobscheduler.State__;
import com.sos.joc.model.jobscheduler.State__.Severity;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.joc.response.JOCCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceDbImpl extends JOCResourceImpl implements IJobSchedulerResourceDb {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerDb(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {

        LOGGER.debug("init JobschedulerClusterMembers");
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerDefaultBody.getJobschedulerId());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice

            DbSchema entity = new DbSchema();
            Database database = new Database();
            database.setDbms(Dbms.DB_2);
            database.setSurveyDate(dbItemInventoryInstance.getModified());
            database.setVersion(dbItemInventoryInstance.getJobSchedulerVersion());
            State__ state = new State__();
            state.setSeverity(Severity._0);
            state.setText(State__.Text.RUNNING);
            database.setState(state);
            entity.setDatabase(database);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
