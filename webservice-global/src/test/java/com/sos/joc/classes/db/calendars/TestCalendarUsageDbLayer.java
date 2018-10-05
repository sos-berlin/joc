package com.sos.joc.classes.db.calendars;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;

public class TestCalendarUsageDbLayer {

    private static final String HIBERNATE_CONFIG_FILE = "C:/Program Files/sos-berlin.com/joc/jetty_base/resources/joc/reporting.hibernate.cfg.xml";
    private SOSHibernateFactory sosHibernateFactory;
    private SOSHibernateSession sosHibernateSession;
    CalendarUsageDBLayer calendarUsageDBLayer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
//        initConnection();
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
    public void testJocConfigurationWriteRecord() throws Exception {
        initConnection();
        sosHibernateSession.beginTransaction();

        calendarUsageDBLayer.deleteCalendarUsage(2L);

        DBItemInventoryClusterCalendarUsage dbItemCalendarUsage = new DBItemInventoryClusterCalendarUsage();
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job4");
        dbItemCalendarUsage.setSchedulerId("scheduler");
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);

        dbItemCalendarUsage = new DBItemInventoryClusterCalendarUsage();
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job5");
        dbItemCalendarUsage.setSchedulerId("scheduler");
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);
        sosHibernateSession.commit();
        sosHibernateFactory.close();
    }

    @Test
    public void testJocConfigurationDeleteRecordById() throws Exception {
        initConnection();
        sosHibernateSession.beginTransaction();
        calendarUsageDBLayer.deleteCalendarUsage(2L);
        sosHibernateSession.commit();
        sosHibernateFactory.close();
    }

}
