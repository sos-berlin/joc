package com.sos.joc.classes.db.calendars;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.GlobalsTest;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;

public class TestCalendarUsageDbLayer {

    CalendarUsageDBLayer calendarUsageDBLayer;
    static SOSHibernateSession sosHibernateSession;

    @Before
    public void setUp() throws Exception {
        sosHibernateSession = GlobalsTest.getSession();
        sosHibernateSession.setAutoCommit(false);
        calendarUsageDBLayer = new CalendarUsageDBLayer(sosHibernateSession);
    }

    @Test
    public void testJocConfigurationWriteRecord() throws Exception {
        sosHibernateSession.beginTransaction();

        calendarUsageDBLayer.deleteCalendarUsage(2L);

        DBItemInventoryClusterCalendarUsage dbItemCalendarUsage = new DBItemInventoryClusterCalendarUsage();
        dbItemCalendarUsage.setEdited(false);
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job4");
        dbItemCalendarUsage.setSchedulerId(GlobalsTest.SCHEDULER_ID);
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);

        dbItemCalendarUsage = new DBItemInventoryClusterCalendarUsage();
        dbItemCalendarUsage.setEdited(false);
        dbItemCalendarUsage.setCalendarId(2L);
        dbItemCalendarUsage.setObjectType("JOB");
        dbItemCalendarUsage.setPath("/job5");
        dbItemCalendarUsage.setSchedulerId("scheduler");
        calendarUsageDBLayer.saveCalendarUsage(dbItemCalendarUsage);
        sosHibernateSession.commit();
    }

    @Test
    public void testJocConfigurationDeleteRecordById() throws Exception {
        sosHibernateSession.beginTransaction();
        calendarUsageDBLayer.deleteCalendarUsage(2L);
        sosHibernateSession.commit();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Globals.disconnect(sosHibernateSession);
        Globals.sosHibernateFactory.close();
    }

}
