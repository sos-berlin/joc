package com.sos.joc;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.exceptions.JocException;

public class TestEnvWebserviceGlobalsTest {

    public static final String SCHEDULER_ID = "scheduler.1.12";
    public static SOSHibernateSession sosHibernateSession;

    public static SOSHibernateSession getSession() throws JocException {
        Globals.sosShiroProperties = new JocCockpitProperties();
        Globals.setProperties();
        sosHibernateSession = Globals.createSosHibernateStatelessConnection("JUnit: GlobalsTest");
        return sosHibernateSession;
    }

}
