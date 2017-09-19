package com.sos.auth.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.ShiroException;

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

    public void removeTimedOutUser(String user) {
       
        ArrayList<String> toBeRemoved = new ArrayList<String>();
        
        for (Map.Entry<String, SOSShiroCurrentUser> entry : currentUsers.entrySet()) {
            boolean found = user.equals(entry.getValue().getUsername());
            if (found) {

                if (entry.getValue().getCurrentSubject().getSession() != null) {
                    try {
                        entry.getValue().getCurrentSubject().getSession().getTimeout();
                    } catch (ShiroException e) {
                        toBeRemoved.add(entry.getValue().getAccessToken());
                    }
                }
            }
        }
        
        for (String entry : toBeRemoved) {
            currentUsers.remove(entry);
        }

    }

    public SOSShiroCurrentUserAnswer getUserByName(String user) {
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer(user);
        for (Map.Entry<String, SOSShiroCurrentUser> entry : currentUsers.entrySet()) {
            boolean found = user.equals(entry.getValue().getUsername());
            if (found) {
                sosShiroCurrentUserAnswer.setUser(entry.getValue().getUsername());
                sosShiroCurrentUserAnswer.setAccessToken(entry.getValue().getAccessToken());

                if (entry.getValue().getCurrentSubject() != null) {
                    sosShiroCurrentUserAnswer.setIsAuthenticated(entry.getValue().getCurrentSubject().isAuthenticated());
                    if (entry.getValue().getCurrentSubject().getSession() != null) {
                        try {
                            sosShiroCurrentUserAnswer.setSessionTimeout(entry.getValue().getCurrentSubject().getSession().getTimeout());
                        } catch (ShiroException e) {
                            sosShiroCurrentUserAnswer.setSessionTimeout(0l);
                            sosShiroCurrentUserAnswer.setIsAuthenticated(false);
                            sosShiroCurrentUserAnswer.setAccessToken("");
                        }
                    }
                }
                return sosShiroCurrentUserAnswer;
            }
        }
        return null;
    }

    public int size() {
        return currentUsers.size();
    }

}
