package com.sos.joc.classes;

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
        System.out.println(JobSchedulerDate.getDate("-0dZ", false, null));
        System.out.println(JobSchedulerDate.getDate("-0dZ", true, null));
        System.out.println(JobSchedulerDate.getDate("-0wZ", false, null));
        System.out.println(JobSchedulerDate.getDate("-0wZ", true, null));
        System.out.println(JobSchedulerDate.getDate("-0MZ", false, null));
        System.out.println(JobSchedulerDate.getDate("-0MZ", true, null));
        System.out.println(JobSchedulerDate.getDate("-0yZ", false, null));
        System.out.println(JobSchedulerDate.getDate("-0yZ", true, null));
        
    }

}
