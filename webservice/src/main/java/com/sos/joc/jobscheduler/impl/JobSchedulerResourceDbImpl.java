package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.Database;
import com.sos.joc.model.jobscheduler.DB;
import com.sos.joc.model.jobscheduler.DBState;
import com.sos.joc.model.jobscheduler.DBStateText;

@Path("jobscheduler")
public class JobSchedulerResourceDbImpl extends JOCResourceImpl implements IJobSchedulerResourceDb {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceDbImpl.class);
    private static final String API_CALL = "./jobscheduler/db";
    
    @Override
    public JOCDefaultResponse postJobschedulerDb(String accessToken, JobSchedulerId jobSchedulerFilter) {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilter.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            DB entity = new DB();
            Database database = new Database();
            database.setDbms(dbItemInventoryInstance.getDbmsName());
            database.setSurveyDate(dbItemInventoryInstance.getModified());
            database.setVersion(dbItemInventoryInstance.getDbmsVersion());
            DBState state = new DBState();
            //TODO DB is not always running
            state.setSeverity(0);
            state.set_text(DBStateText.RUNNING);
            database.setState(state);
            entity.setDatabase(database);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobSchedulerFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobSchedulerFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }

}
