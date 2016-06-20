package com.sos.joc.classes;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;


public class SOSJobschedulerUser {

    private String accessToken;
    private SOSShiroCurrentUser sosShiroCurrentUser;

    public SOSJobschedulerUser(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public boolean isAuthenticated() {
        if (sosShiroCurrentUser == null) {
            sosShiroCurrentUser = SOSServicePermissionShiro.currentUsersList.getUser(accessToken);
        }
        return (sosShiroCurrentUser != null);
    }

    public SOSShiroCurrentUser getSosShiroCurrentUser() {

        if (!isAuthenticated()) {
            return sosShiroCurrentUser;
        } else {
            return null;
        }
    }

}