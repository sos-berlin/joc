package com.sos.auth.rest;

import java.util.HashMap;
import java.util.Map;

public class SOSShiroCurrentUsersList {

    private Map<String, SOSShiroCurrentUser> currentUsers;

    public SOSShiroCurrentUsersList() {
        this.currentUsers = new HashMap<String, SOSShiroCurrentUser>();
    }

    public void addUser(SOSShiroCurrentUser user) {
        this.currentUsers.put(user.getAccessToken(), user);
    }

    public SOSShiroCurrentUser getUser(String accessToken) {
        return this.currentUsers.get(accessToken);
    }

    public void removeUser(String accessToken) {
        currentUsers.remove(accessToken);
    }
    
    public int size(){
        return currentUsers.size();
    }
   

}
