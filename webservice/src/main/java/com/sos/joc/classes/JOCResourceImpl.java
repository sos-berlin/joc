package com.sos.joc.classes;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;

public class JOCResourceImpl {
    protected static final String NO = "no";
    protected static final String YES = "yes";
    
    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;

    protected SOSPermissionJocCockpit getPermissons(){
       return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    public JobSchedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

}
