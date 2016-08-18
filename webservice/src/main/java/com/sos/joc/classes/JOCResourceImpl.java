package com.sos.joc.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.joc.response.JOCCockpitResponse;

public class JOCResourceImpl {
    private static final Logger LOGGER = Logger.getLogger(JOCResourceImpl.class);
    protected static final String JOBSCHEDULER_DATE_FORMAT = "yyyy-mm-dd hh:mm:ss.SSS'Z'";
    protected static final String JOBSCHEDULER_DATE_FORMAT2 = "yyyy-mm-dd'T'hh:mm:ss.SSS'Z'";
    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected static final String NO = "no";
    protected static final String YES = "yes";

    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;

    protected SOSPermissionJocCockpit getPermissons() {
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    public JobSchedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

    public Date getDateFromString(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
            date = formatter.parse(dateString);
        } catch (Exception e) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);
                date = formatter.parse(dateString);
            } catch (Exception ee) {
            }
        }

        return date;
    }

    public JOCDefaultResponse init(String accessToken, String schedulerId) throws Exception {
        JOCDefaultResponse jocDefaultResponse = null;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return JOCDefaultResponse.responseStatus401(JOCCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JOCDefaultResponse.responseStatus440(JOCCockpitResponse.getError401Schema(jobschedulerUser, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema(e.getMessage()));
        }

        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()) {
            return JOCDefaultResponse.responseStatus403(JOCCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (schedulerId == null) {
            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema("schedulerId is null"));
        }

        dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));

        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s", schedulerId,
                    DBLayer.TABLE_INVENTORY_INSTANCES)));
        }

        return jocDefaultResponse;
    }

}
