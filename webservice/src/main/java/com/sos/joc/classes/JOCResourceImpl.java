package com.sos.joc.classes;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;

public class JOCResourceImpl {
    protected static final String NO = "no";
    protected static final String YES = "yes";
    
    protected JobschedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;
    protected JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody;

    protected SOSPermissionJocCockpit getPermissons(){
       return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    public JobschedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

}
