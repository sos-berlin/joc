package com.sos.joc.classes;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JobSchedulerDateTest {

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

}
