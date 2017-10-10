package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsedByWriter;

public class CalendarUsedByWriterTest {
    private static final String HIBERNATE_CONFIG_FILE = "C:/ProgramData/sos-berlin.com/joc/jetty_base/resources/joc/reporting.hibernate.cfg.xml";
    private SOSHibernateFactory sosHibernateFactory;
    private SOSHibernateSession sosHibernateSession;
 

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
    }

    @Test
    public void updateUsedByTest() throws Exception {
        String command = "<modify_order><run_time><weekdays><day day='1 2 3 4 5 6 7'><period single_start='00:01'/></day></weekdays><date date='2017-10-10' calendar='/sos/calendar/test2'><period single_start='19:00:00'/></date><date date='2017-10-13' calendar='/sos/calendar/test1'><period single_start='12:12:00'/></date><date date='2017-11-03' calendar='/sos/calendar/test2'><period single_start='14:02:00'/></date> <holidays ><holiday  date='2017-10-09' calendar='/sos/calendar/test3'/></holidays></run_time></modify_order>";
        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(sosHibernateSession,1L,"ORDER", "test,1", command);
        calendarUsedByWriter.updateUsedBy();
    }

    @Test
    public void deleteUsedByTest() throws Exception {
        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(sosHibernateSession,1L,"ORDER", "test,1", "");
        calendarUsedByWriter.deleteUsedBy();
    }

}
