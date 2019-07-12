package com.sos.joc.classes;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JobSchedulerDateTest {
    
    @Before
    public void setUp() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testGetDateFromDateFrom() throws JobSchedulerInvalidResponseDataException {
        Date d = new Date();
        String timeZone = "Europe/Berlin";
        String date = "6d-02";
        d = JobSchedulerDate.getDateFrom(date, timeZone);
        date = "-6d 2h";
        d = JobSchedulerDate.getDateFrom(date, timeZone);
        date = "2015-12-17T15:02:22.999-02";
        d = JobSchedulerDate.getDateFrom(date, timeZone);
        d = JobSchedulerDate.getDateFrom(date, null);
        date = "2015-12-17T15:02:22.999Z";
        d = JobSchedulerDate.getDateFrom(date, timeZone);
        d = JobSchedulerDate.getDateFrom(date, null);
    }
    
    @Test
    public void testGetDateFromDateTo() throws JobSchedulerInvalidResponseDataException {
        Date d = new Date();
        String timeZone = "UTC";
        String date = "6d-02";
        d = JobSchedulerDate.getDateTo(date, timeZone);
        date = "-6d";
        d = JobSchedulerDate.getDateTo(date, timeZone);
        date = "2015-12-17T15:02:22.999-02";
        d = JobSchedulerDate.getDateTo(date, timeZone);
        d = JobSchedulerDate.getDateTo(date, null);
        date = "2015-12-17T15:02:22.999Z";
        d = JobSchedulerDate.getDateTo(date, timeZone);
        d = JobSchedulerDate.getDateTo(date, null);
    }
    
    @Test
    public void testInstantFromISO8601StringWithOnlyTime() throws JobSchedulerInvalidResponseDataException {
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34.567Z"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34.567"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34Z"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34"));
    }
    
    @Test
    public void testGetInstantFromDateStr() throws Exception {
        System.out.println(JobSchedulerDate.getDate("-0d", false, null));
        System.out.println(JobSchedulerDate.getDate("-0d", true, null));
        System.out.println(JobSchedulerDate.getDate("-0w", false, null));
        System.out.println(JobSchedulerDate.getDate("-0w", true, null));
        System.out.println(JobSchedulerDate.getDate("-0M", false, null));
        System.out.println(JobSchedulerDate.getDate("-0M", true, null));
        System.out.println(JobSchedulerDate.getDate("-0y", false, null));
        System.out.println(JobSchedulerDate.getDate("-0y", true, null));
        
        System.out.println(JobSchedulerDate.getDateFrom("0y+3M", null));
        System.out.println(JobSchedulerDate.getDateTo("0y-6M", null));
        System.out.println(JobSchedulerDate.getDateFrom("-1M", null));
        System.out.println(JobSchedulerDate.getDateTo("-1M", null));
        System.out.println(JobSchedulerDate.getDateFrom("-1w", null));
        System.out.println(JobSchedulerDate.getDateTo("-1w", null));
        System.out.println(JobSchedulerDate.getDateFrom("-1d", null));
        System.out.println(JobSchedulerDate.getDateTo("-1d", null));
        System.out.println(JobSchedulerDate.getDateFrom("-1h", null));
        System.out.println(JobSchedulerDate.getDateTo("-1h", null));
        System.out.println(JobSchedulerDate.getDateFrom("-10s", null));
        System.out.println(JobSchedulerDate.getDateTo("-10s", null));
    }
    
    @Test
    public void testGetAtInUTCISO8601() throws Exception {
        String atTimeZone = "Europe/Berlin";
        String atDate = "2019-07-12 11:00";
        String atDate2 = "now + 01:00";
        String result = JobSchedulerDate.getAtInUTCISO8601(atDate, atTimeZone);
        //System.out.println(result);
        assertEquals("testGetAtInUTCISO8601-1", "2019-07-12T09:00:00Z", result);
        result = JobSchedulerDate.getAtInUTCISO8601(atDate2, atTimeZone);
        //System.out.println(result);
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MINUTE, 0);
        now.add(Calendar.HOUR_OF_DAY, 1);
        Calendar resultCal = Calendar.getInstance();
        resultCal.setTime(Date.from(Instant.parse(result)));
        resultCal.set(Calendar.MILLISECOND, 0);
        resultCal.set(Calendar.SECOND, 0);
        resultCal.set(Calendar.MINUTE, 0);
        assertEquals("testGetAtInUTCISO8601-2", now, resultCal);
    }
    

}
