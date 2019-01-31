package com.sos.auth.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.shiro.ShiroException;

public class SOSShiroCurrentUsersList {

    private static final String VALID = "valid";
    private static final String NOT_VALID = "not-valid";
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

        CopyOnWriteArrayList<String> toBeRemoved = new CopyOnWriteArrayList<String>();

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
        
        Iterator<String> it = toBeRemoved.iterator();
        while (it.hasNext()) {
            currentUsers.remove(it.next()); 
        }
         
    }

    public SOSShiroCurrentUserAnswer getUserByName(String user) {
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer(user);
        sosShiroCurrentUserAnswer.setUser(user);
        sosShiroCurrentUserAnswer.setIsAuthenticated(false);
        sosShiroCurrentUserAnswer.setSessionTimeout(0L);
        boolean found = false;
        for (Map.Entry<String, SOSShiroCurrentUser> entry : currentUsers.entrySet()) {
            found = user.equals(entry.getValue().getUsername());
            if (found) {
                sosShiroCurrentUserAnswer.setAccessToken(VALID);

                if (entry.getValue().getCurrentSubject() != null) {
                    sosShiroCurrentUserAnswer.setIsAuthenticated(entry.getValue().getCurrentSubject().isAuthenticated());
                    if (entry.getValue().getCurrentSubject().getSession() != null) {
                        try {
                            sosShiroCurrentUserAnswer.setSessionTimeout(entry.getValue().getCurrentSubject().getSession().getTimeout());
                        } catch (ShiroException e) {
                            sosShiroCurrentUserAnswer.setMessage("user not valid");
                            sosShiroCurrentUserAnswer.setSessionTimeout(0l);
                            sosShiroCurrentUserAnswer.setIsAuthenticated(false);
                            sosShiroCurrentUserAnswer.setAccessToken(NOT_VALID);
                        }
                    }
                }
                return sosShiroCurrentUserAnswer;
            }
        }

        sosShiroCurrentUserAnswer.setAccessToken(NOT_VALID);
        sosShiroCurrentUserAnswer.setMessage("user " + user + " not found");
        return sosShiroCurrentUserAnswer;
    }

    public int size() {
        return currentUsers.size();
    }

    public SOSShiroCurrentUserAnswer getUserByToken(String token) {
        SOSShiroCurrentUser user = getUser(token);
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = null;
        if (user != null) {
            sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer(user.getUsername());
            sosShiroCurrentUserAnswer.setAccessToken(VALID);
            sosShiroCurrentUserAnswer.setIsAuthenticated(user.isAuthenticated());
            try {
                if (user.getCurrentSubject() != null && user.getCurrentSubject().getSession() != null) {
                    sosShiroCurrentUserAnswer.setSessionTimeout(user.getCurrentSubject().getSession().getTimeout());
                } else {
                    sosShiroCurrentUserAnswer.setSessionTimeout(0l);
                }
            } catch (ShiroException e) {
                sosShiroCurrentUserAnswer.setMessage("token not valid");
                sosShiroCurrentUserAnswer.setSessionTimeout(0l);
                sosShiroCurrentUserAnswer.setIsAuthenticated(false);
                sosShiroCurrentUserAnswer.setAccessToken(NOT_VALID);
            }
        } else {
            sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer("");
            sosShiroCurrentUserAnswer.setAccessToken(NOT_VALID);
            sosShiroCurrentUserAnswer.setMessage("token not valid");
            sosShiroCurrentUserAnswer.setIsAuthenticated(false);
            sosShiroCurrentUserAnswer.setSessionTimeout(0L);
        }

        return sosShiroCurrentUserAnswer;
    }

}
