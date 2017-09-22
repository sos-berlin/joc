package com.sos.joc.classes;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarDatesFilter;

public class FrequencyResolverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyResolverTest.class);
    private FrequencyResolver fr = new FrequencyResolver();

    @Before
    public void setUp() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String json = "{\"includes\":{";
        json += "\"dates\":[\"2016-01-01\", \"2017-05-02\", \"2017-04-15\"],";
        json += "\"holidays\":[{\"dates\":[\"2016-01-01\", \"2017-05-01\", \"2017-12-25\"]}],";
        json += "\"weekdays\":[{\"from\":\"\", \"to\":\"\", \"days\":[1, 2]}],";
        json += "\"monthdays\":[{\"from\":\"\", \"to\":\"2017-11-01\", \"days\":[1, 2]}],";
        json += "\"ultimos\":[{\"from\":\"\", \"to\":\"2017-11-01\", \"days\":[1, 2]}],";
        json += "\"repetitions\":[";
        json += "    {\"from\":\"2017-05-01\", \"to\":\"2017-11-01\", \"repetition\":\"WEEKLY\", \"step\":2},";
        json += "    {\"from\":\"2017-01-31\", \"to\":\"2017-12-31\", \"repetition\":\"MONTHLY\", \"step\":1}";
        json += "],";
        json += "\"months\":[{\"months\":[6, 7], \"from\":\"2017-06-15\", \"to\":\"2018-11-01\",";
        json += "    \"weekdays\":[{\"from\":\"\", \"to\":\"\", \"days\":[3]}],";
        json += "    \"monthdays\":[{\"from\":\"\", \"to\":\"2017-07-01\", \"days\":[10, 20]}],";
        json += "    \"ultimos\":[{\"from\":\"\", \"to\":\"2017-07-01\", \"days\":[8, 11]}]";
        json += "}]";
        json += "}}";
        //LOGGER.info(json);
        CalendarDatesFilter calendarFilter = new CalendarDatesFilter();
        calendarFilter.setDateFrom("2017-01-01");
        calendarFilter.setDateTo("2017-12-31");
        calendarFilter.setCalendar(new ObjectMapper().readValue(json, Calendar.class));
        fr.init(calendarFilter);
    }
    
//    @Test
//    public void getWorkingDayOfMonthTest() throws Exception {
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        cal.setTime(Date.from(Instant.parse("2017-06-22T00:00:00Z")));
//        int workingDay = fr.getWorkingDay(cal, fr.getFirstDayOfMonth(java.util.Calendar.getInstance(), cal));
//        int expected = 16;
//        LOGGER.info(workingDay+"");
//        assertEquals("getWorkingDayOfMonthTest", expected, workingDay);
//    }
//    
//    @Test
//    public void getUltimoWorkingDayOfMonthTest() throws Exception {
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        cal.setTime(Date.from(Instant.parse("2017-06-22T00:00:00Z")));
//        int workingDay = fr.getUltimoWorkingDay(cal, fr.getLastDayOfMonth(java.util.Calendar.getInstance(), cal), fr.getFirstDayOfMonth(java.util.Calendar.getInstance(), cal));
//        int expected = 7;
//        LOGGER.info(workingDay+"");
//        assertEquals("getUltimoWorkingDayOfMonthTest", expected, workingDay);
//    }

    @Test
    public void addDatesTest() throws Exception {
        fr.addDates();
        String expected = "[2017-04-15, 2017-05-02]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addDatesTest", expected, s.toString());
    }
    
    @Test
    public void addHolidaysTest() throws Exception {
        fr.addHolidays();
        String expected = "[2017-05-01, 2017-12-25]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addHolidaysTest", expected, s.toString());
    }

    @Test
    public void addWeekDaysTest() throws Exception {
        fr.addWeekDays();
        String expected =
                "[2017-01-02, 2017-01-03, 2017-01-09, 2017-01-10, 2017-01-16, 2017-01-17, 2017-01-23, 2017-01-24, 2017-01-30, 2017-01-31, 2017-02-06, 2017-02-07, 2017-02-13, 2017-02-14, 2017-02-20, 2017-02-21, 2017-02-27, 2017-02-28, 2017-03-06, 2017-03-07, 2017-03-13, 2017-03-14, 2017-03-20, 2017-03-21, 2017-03-27, 2017-03-28, 2017-04-03, 2017-04-04, 2017-04-10, 2017-04-11, 2017-04-17, 2017-04-18, 2017-04-24, 2017-04-25, 2017-05-01, 2017-05-02, 2017-05-08, 2017-05-09, 2017-05-15, 2017-05-16, 2017-05-22, 2017-05-23, 2017-05-29, 2017-05-30, 2017-06-05, 2017-06-06, 2017-06-12, 2017-06-13, 2017-06-19, 2017-06-20, 2017-06-26, 2017-06-27, 2017-07-03, 2017-07-04, 2017-07-10, 2017-07-11, 2017-07-17, 2017-07-18, 2017-07-24, 2017-07-25, 2017-07-31, 2017-08-01, 2017-08-07, 2017-08-08, 2017-08-14, 2017-08-15, 2017-08-21, 2017-08-22, 2017-08-28, 2017-08-29, 2017-09-04, 2017-09-05, 2017-09-11, 2017-09-12, 2017-09-18, 2017-09-19, 2017-09-25, 2017-09-26, 2017-10-02, 2017-10-03, 2017-10-09, 2017-10-10, 2017-10-16, 2017-10-17, 2017-10-23, 2017-10-24, 2017-10-30, 2017-10-31, 2017-11-06, 2017-11-07, 2017-11-13, 2017-11-14, 2017-11-20, 2017-11-21, 2017-11-27, 2017-11-28, 2017-12-04, 2017-12-05, 2017-12-11, 2017-12-12, 2017-12-18, 2017-12-19, 2017-12-25, 2017-12-26]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addWeekDaysTest", expected, s.toString());
    }

    @Test
    public void addMonthDaysTest() throws Exception {
        fr.addMonthDays();
        String expected =
                "[2017-01-01, 2017-01-02, 2017-02-01, 2017-02-02, 2017-03-01, 2017-03-02, 2017-04-01, 2017-04-02, 2017-05-01, 2017-05-02, 2017-06-01, 2017-06-02, 2017-07-01, 2017-07-02, 2017-08-01, 2017-08-02, 2017-09-01, 2017-09-02, 2017-10-01, 2017-10-02, 2017-11-01]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addMonthDaysTest", expected, s.toString());
    }

    @Test
    public void addUltimosTest() throws Exception {
        fr.addUltimos();
        String expected =
                "[2017-01-30, 2017-01-31, 2017-02-27, 2017-02-28, 2017-03-30, 2017-03-31, 2017-04-29, 2017-04-30, 2017-05-30, 2017-05-31, 2017-06-29, 2017-06-30, 2017-07-30, 2017-07-31, 2017-08-30, 2017-08-31, 2017-09-29, 2017-09-30, 2017-10-30, 2017-10-31]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addUltimosTest", expected, s.toString());
    }

    @Test
    public void addRepetitionsTest() throws Exception {
        fr.addRepetitions();
        String expected =
                "[2017-01-31, 2017-02-28, 2017-03-31, 2017-04-30, 2017-05-01, 2017-05-15, 2017-05-29, 2017-05-31, 2017-06-12, 2017-06-26, 2017-06-30, 2017-07-10, 2017-07-24, 2017-07-31, 2017-08-07, 2017-08-21, 2017-08-31, 2017-09-04, 2017-09-18, 2017-09-30, 2017-10-02, 2017-10-16, 2017-10-30, 2017-10-31, 2017-11-30, 2017-12-31]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addRepetitionsTest", expected, s.toString());
    }

    @Test
    public void addMonthsTest() throws Exception {
        fr.addMonths();
        String expected = "[2017-06-20, 2017-06-21, 2017-06-23, 2017-06-28, 2017-07-05, 2017-07-12, 2017-07-19, 2017-07-26]";
        Set<String> s = fr.getDates();
        LOGGER.info(s.toString());
        assertEquals("addMonthsTest", expected, s.toString());
    }

}
