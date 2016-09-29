package com.sos.joc;

import java.util.HashMap;

import com.sos.auth.rest.SOSShiroCurrentUsersList;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.scheduler.model.SchedulerObjectFactory;

public class Globals {
    private static final String HIBERNATE_CONFIGURATION_FILE = "hibernate_configuration_file";
    public static SOSShiroCurrentUsersList currentUsersList;
    public static SOSHibernateConnection sosHibernateConnection;
    public static SchedulerObjectFactory schedulerObjectFactory;
    public static HashMap<String, SOSHibernateConnection> sosSchedulerHibernateConnections;

    public static SOSHibernateConnection getConnection(String schedulerId) throws Exception {
        if (sosSchedulerHibernateConnections == null) {
            sosSchedulerHibernateConnections = new HashMap<String, SOSHibernateConnection>();
        }

        SOSHibernateConnection sosHibernateConnection = sosSchedulerHibernateConnections.get(schedulerId);

        if (sosHibernateConnection == null) {

            String confFile = getConfFile(schedulerId);
            sosHibernateConnection = new SOSHibernateConnection(confFile);
            sosHibernateConnection.addClassMapping(DBLayer.getSchedulerClassMapping());
            sosHibernateConnection.connect();
            sosSchedulerHibernateConnections.put(schedulerId, sosHibernateConnection);
        }

        return sosHibernateConnection;
    }

    private static String getConfFile(String schedulerId) throws JocException {
        String propertieKey = HIBERNATE_CONFIGURATION_FILE + "_" + schedulerId;
        JocCockpitProperties sosShiroProperties = new JocCockpitProperties();
        String confFile = sosShiroProperties.getProperty(propertieKey);
        if (confFile == null) {
            confFile = sosShiroProperties.getProperty(HIBERNATE_CONFIGURATION_FILE);
            if (confFile == null) {
                JocError error = new JocError();
                error.setCode("JOC-310");
                error.setMessage(String.format("Could find value for %s in joc_properties file", propertieKey));
                throw new JocException(error);
            }
        }

        return confFile;

    }

}