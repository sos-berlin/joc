package com.sos.joc.classes.db.calendars;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;

public class TestCalendarUsageDbLayer {

    private static final String HIBERNATE_CONFIG_FILE = "C:/Users/ur/Documents/sos-berlin.com/jobscheduler/scheduler_joc_cockpit/config/hibernate.cfg.xml";
    private SOSHibernateFactory sosHibernateFactory;
    private SOSHibernateSession sosHibernateSession;
    CalendarUsageDBLayer calendarUsageDBLayer;

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
        sosHibernateFactory.setAutoCommit(false);
        sosHibernateFactory.build();
        sosHibernateSession = sosHibernateFactory.openStatelessSession();
        calendarUsageDBLayer = new CalendarUsageDBLayer(sosHibernateSession);
    }

    @Test
    public void testCalendarUsageDBLayerGetList() throws Exception {
        initConnection();

        List<DBItemInventoryCalendarUsage> l = calendarUsageDBLayer.getListOfCalendarUsage(2L);
        DBItemInventoryCalendarUsage calendarUsageDBItem = l.get(0);
        sosHibernateFactory.close();
        assertEquals("testCalendarUsageDBLayerGetList", "/job4", calendarUsageDBItem.getPath());

    }

    @Test
    public void testJocConfigurationWriteRecord() throws Exception {
        initConnection();
        sosHibernateSession.beginTransaction();

        calendarUsageDBLayer.deleteCalendarUsage(2L);

        DBItemInventoryCalendarUsage dbItemCalendarUsage = new DBItemInventoryCalendarUsage();
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job4");
        dbItemCalendarUsage.setInstanceId(164L);
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);

        dbItemCalendarUsage = new DBItemInventoryCalendarUsage();
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job5");
        dbItemCalendarUsage.setInstanceId(164L);
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);
        sosHibernateSession.commit();
        sosHibernateFactory.close();
    }

    @Test
    public void testJocConfigurationDeleteRecord() throws Exception {
        initConnection();
        sosHibernateSession.beginTransaction();
        calendarUsageDBLayer.deleteCalendarUsage(2L);
        sosHibernateSession.commit();
        sosHibernateFactory.close();
    }

}
