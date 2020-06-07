package com.sos.joc.classes.jobstreams;

import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.exceptions.JocException;


public class TestJobStreamMigrator {

    @Test
    public void testMigrateJobstreams() throws Exception {
        Globals.sosShiroProperties = new JocCockpitProperties();
        Globals.setProperties();
        SOSHibernateSession sosHibernateSession = null;
        sosHibernateSession = Globals.createSosHibernateStatelessConnection("testMigrator");
        JobStreamMigrator jobStreamMigrator = new JobStreamMigrator();
        jobStreamMigrator.migrate(sosHibernateSession);
    }

}
