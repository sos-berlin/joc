package com.sos.joc.db.configuration;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.GlobalsTest;

public class TestJocConfigurationDbLayer {

    private JocConfigurationDbLayer jocConfigurationDBLayer;
    private static SOSHibernateSession sosHibernateSession;

    @Before
    public void setUp() throws Exception {
        sosHibernateSession = GlobalsTest.getSession();
        jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Globals.disconnect(sosHibernateSession);
        Globals.sosHibernateFactory.close();
    }

    private void initFilter() {
//        jocConfigurationDBLayer.getFilter().setObjectType("customization");
        jocConfigurationDBLayer.getFilter().setConfigurationType("PROFILE");
        jocConfigurationDBLayer.getFilter().setSchedulerId(GlobalsTest.SCHEDULER_ID);
//        jocConfigurationDBLayer.getFilter().setName("test");
        jocConfigurationDBLayer.getFilter().setAccount("root");
    }

    @Test
    public void testJocConfigurationDBLayerGetList() throws Exception {
        initFilter();

        List<JocConfigurationDbItem> l = jocConfigurationDBLayer.getJocConfigurationList(11);
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
        jocConfigurationDBLayer.getFilter().setSchedulerId(GlobalsTest.SCHEDULER_ID);
        jocConfigurationDBLayer.getFilter().setAccount("root");
        jocConfigurationDBLayer.getFilter().setConfigurationType("profil");
        jocConfigurationDBLayer.getFilter().setName("profil");
        jocConfigurationDBLayer.getFilter().setObjectType("profil");
        jocConfigurationDBLayer.deleteConfiguration();
    }

}
