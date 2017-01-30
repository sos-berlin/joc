package com.sos.joc.db.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.jitl.reporting.db.DBLayer;

public class TestJocConfigurationDbLayer {
    private static final String HIBERNATE_CONFIG_FILE = "C:/Users/ur/Documents/sos-berlin.com/jobscheduler/scheduler_joc_cockpit/config/hibernate.cfg.xml";
    private SOSHibernateFactory sosHibernateFactory;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        initConnection();
    }

    public void initConnection() throws Exception {
        sosHibernateFactory = new SOSHibernateFactory(HIBERNATE_CONFIG_FILE);
        sosHibernateFactory.addClassMapping(DBLayer.getInventoryClassMapping());
        sosHibernateFactory.addClassMapping(DBLayer.getReportingClassMapping());
        sosHibernateFactory.setAutoCommit(true);
        sosHibernateFactory.build();
        sosHibernateFactory.close();
    }

    

}
