package com.sos.joc.db.configuration;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceGlobalsTest;

public class TestJocConfigurationDbLayer {

    private JocConfigurationDbLayer jocConfigurationDBLayer;
    private static SOSHibernateSession sosHibernateSession;
    private JocConfigurationFilter filter;
    
    @Before
    public void setUp() throws Exception {
        sosHibernateSession = TestEnvWebserviceGlobalsTest.getSession();
        jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
        filter = new JocConfigurationFilter(); 
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Globals.disconnect(sosHibernateSession);
        Globals.closeFactory();
    }

    private void initFilter() {
        // jocConfigurationDBLayer.getFilter().setObjectType("customization");
        filter.setConfigurationType("PROFILE");
        filter.setSchedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        // jocConfigurationDBLayer.getFilter().setName("test");
        filter.setAccount("root");
    }

    @Test
    public void testJocConfigurationDBLayerGetList() throws Exception {
        initFilter();

        List<JocConfigurationDbItem> l = jocConfigurationDBLayer.getJocConfigurationList(filter,11);
        JocConfigurationDbItem jocConfigurationDbItem = l.get(0);
        assertEquals("testJocConfigurationDBLayerGetList", "root", jocConfigurationDbItem.getAccount());

    }

    @Test
    public void testJocConfigurationWriteRecord() throws Exception {
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

    }

    @Test
    public void testJocConfigurationDeleteRecord() throws Exception {
        filter.setSchedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        filter.setAccount("root");
        filter.setConfigurationType("profil");
        filter.setName("profil");
        filter.setObjectType("profil");
        jocConfigurationDBLayer.deleteConfiguration(filter);
    }

}
