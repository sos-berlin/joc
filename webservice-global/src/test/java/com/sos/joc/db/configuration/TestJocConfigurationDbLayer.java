package com.sos.joc.db.configuration;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateStatelessSession;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;

public class TestJocConfigurationDbLayer {
    private static final String HIBERNATE_CONFIG_FILE = "C:/Users/ur/Documents/sos-berlin.com/jobscheduler/scheduler_joc_cockpit/config/hibernate.cfg.xml";
    private SOSHibernateFactory sosHibernateFactory;
    private SOSHibernateSession sosHibernateSession;
    JocConfigurationDbLayer jocConfigurationDBLayer;

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
        sosHibernateSession = sosHibernateFactory.openStatelessSession();
        jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
    }

    private void initFilter() {
        jocConfigurationDBLayer.getFilter().setObjectType("customization");
        jocConfigurationDBLayer.getFilter().setConfigurationType("job_chain");
        jocConfigurationDBLayer.getFilter().setInstanceId(61L);
        jocConfigurationDBLayer.getFilter().setName("test");
        jocConfigurationDBLayer.getFilter().setAccount("root");
    }

    @Test
    public void testJocConfigurationDBLayerGetList() throws Exception {
        initConnection();
        initFilter();

        List<JocConfigurationDbItem> l = jocConfigurationDBLayer.getJocConfigurationList(11);
        JocConfigurationDbItem jocConfigurationDbItem = l.get(0);
        sosHibernateFactory.close();
        assertEquals("testJocConfigurationDBLayerGetList", "test", jocConfigurationDbItem.getName());

    }

    @Test
    public void testJocConfigurationWriteRecord() throws Exception {
        initConnection();
        JocConfigurationDbItem jocConfigurationDbItem = new JocConfigurationDbItem();
        jocConfigurationDbItem.setAccount("root");
        jocConfigurationDbItem.setConfigurationItem("my configuration item");
        jocConfigurationDbItem.setConfigurationType("profil");
        jocConfigurationDbItem.setName("profil");
        jocConfigurationDbItem.setObjectType("profil");
        jocConfigurationDbItem.setInstanceId(61L);
        jocConfigurationDbItem.setShared(true);
        jocConfigurationDbItem.setConfigurationItem("myNewConfiguration");
        initFilter();
        jocConfigurationDBLayer.saveOrUpdateConfiguration(jocConfigurationDbItem); 
        sosHibernateFactory.close();

    }

    @Test
    public void testJocConfigurationDeleteRecord() throws Exception {
        initConnection();
        jocConfigurationDBLayer.getFilter().setInstanceId(61L);
        jocConfigurationDBLayer.getFilter().setAccount("root");
        jocConfigurationDBLayer.getFilter().setConfigurationType("profil");
        jocConfigurationDBLayer.getFilter().setName("profil");
        jocConfigurationDBLayer.getFilter().setObjectType("profil");
      jocConfigurationDBLayer.deleteConfiguration();
        
        sosHibernateFactory.close();

    }

}
