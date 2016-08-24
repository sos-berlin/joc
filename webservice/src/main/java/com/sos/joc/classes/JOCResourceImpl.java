package com.sos.joc.classes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.response.JOCDefaultResponse;

public class JOCResourceImpl {
    private static final Logger LOGGER = Logger.getLogger(JOCResourceImpl.class);
    protected static final String JOBSCHEDULER_DATE_FORMAT = "yyyy-mm-dd hh:mm:ss.SSS'Z'";
    protected static final String JOBSCHEDULER_DATE_FORMAT2 = "yyyy-mm-dd'T'hh:mm:ss.SSS'Z'";
    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected static final String NO = "no";
    protected static final String YES = "yes";
    private String accessToken;
    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;

    protected SOSPermissionJocCockpit getPermissons(String accessToken) {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    public String getAccessToken() {
        return accessToken;
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

    public Date getDateFromTimestamp(Long timeStamp) {
        Long time = new Long(timeStamp);
        Timestamp stamp = new Timestamp(time);
        Date date = new Date(stamp.getTime());
        return date;
    }

    public JOCDefaultResponse init(String schedulerId, boolean permission) throws Exception {
        JOCDefaultResponse jocDefaultResponse = null;

        try {
            
            if (!jobschedulerUser.isAuthenticated()) {
                jocDefaultResponse = JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser,""));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            jocDefaultResponse = JOCDefaultResponse.responseStatus440(JOCDefaultResponse.getError401Schema(jobschedulerUser, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            jocDefaultResponse = JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

        if (!permission) {
            jocDefaultResponse = JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser,""));
        }

        if (schedulerId == null) {
            jocDefaultResponse = JOCDefaultResponse.responseStatusJSError("schedulerId is null");
        }
        dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));

        if (dbItemInventoryInstance == null) {
            jocDefaultResponse = JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s", schedulerId, DBLayer.TABLE_INVENTORY_INSTANCES));
        }

        return jocDefaultResponse;
    }

}
