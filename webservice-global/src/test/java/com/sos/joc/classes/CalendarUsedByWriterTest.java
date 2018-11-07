package com.sos.joc.classes;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.GlobalsTest;
import com.sos.joc.db.calendars.CalendarUsedByWriter;

public class CalendarUsedByWriterTest {

    static SOSHibernateSession sosHibernateSession;

    @Before
    public void setUp() throws Exception {
        sosHibernateSession = GlobalsTest.getSession();
    }

    @Test
    public void updateUsedByTest() throws Exception {
        String command =
                "<modify_order><run_time><weekdays><day day='1 2 3 4 5 6 7'><period single_start='00:01'/></day></weekdays><date date='2017-10-10' calendar='2'><period single_start='19:00:00'/></date><date date='2017-10-13' calendar='1'><period single_start='12:12:00'/></date><date date='2017-11-03' calendar='2'><period single_start='14:02:00'/></date> <holidays ><holiday  date='2017-10-09' calendar='3'/></holidays></run_time></modify_order>";
        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(sosHibernateSession, GlobalsTest.SCHEDULER_ID, CalendarObjectType.ORDER,
                "test,1", command);
        calendarUsedByWriter.updateUsedBy();
        Globals.disconnect(sosHibernateSession);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Globals.disconnect(sosHibernateSession);
        Globals.sosHibernateFactory.close();
    }
}
