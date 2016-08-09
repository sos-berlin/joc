package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb;
import com.sos.joc.model.jobscheduler.Database;
import com.sos.joc.model.jobscheduler.Database.Dbms;
import com.sos.joc.model.jobscheduler.DbSchema;
import com.sos.joc.model.jobscheduler.State__;
import com.sos.joc.model.jobscheduler.State__.Severity;
import com.sos.joc.response.JocCockpitResponse;


@Path("jobscheduler")
public class JobSchedulerResourceDbImpl  extends JOCResourceImpl implements IJobSchedulerResourceDb {

    @Override
    public JobschedulerDbResponse postJobschedulerDb(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerDbResponse jobschedulerDbResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerDbResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerDbResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerDbResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMaster().getView().isStatus()){
            return JobschedulerDbResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobschedulerDbResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerDefaultBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }
 
            //TODO JOC Cockpit Webservice

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
            
            jobschedulerDbResponse = JobschedulerDbResponse.responseStatus200(entity);

            return jobschedulerDbResponse;
        } catch (Exception e) {
            return JobschedulerDbResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }
 

    

}
