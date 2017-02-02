package com.sos.joc.classes;
 

import com.sos.auth.rest.SOSShiroCurrentUsersList;
 

public final class JocWebserviceDataContainer {
    private static JocWebserviceDataContainer instance;
    
    public static SOSShiroCurrentUsersList currentUsersList;
    

    private JocWebserviceDataContainer() {

    }

    public static synchronized JocWebserviceDataContainer getInstance() {
        if (instance == null) {
            instance = new JocWebserviceDataContainer();
        }
        return instance;
    }

    public  SOSShiroCurrentUsersList getCurrentUsersList() {
        return currentUsersList;
    }

    public  void setCurrentUsersList(SOSShiroCurrentUsersList currentUsersList) {
        JocWebserviceDataContainer.currentUsersList = currentUsersList;
    }

   
}

   
