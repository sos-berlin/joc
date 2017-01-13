package com.sos.joc.classes;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JobSchedulerDateTest {

    @Test
    public void testGetDateFromDateFrom() throws JobSchedulerInvalidResponseDataException {
        Date d = new Date();
        String timeZone = "UTC";
        String date = "6d-02";
        d = JobSchedulerDate.getDate(date, timeZone);
        date = "-6d";
        d = JobSchedulerDate.getDate(date, timeZone);
        date = "2015-12-17T15:02:22.999-02";
        d = JobSchedulerDate.getDate(date, timeZone);
        d = JobSchedulerDate.getDate(date, null);
        date = "2015-12-17T15:02:22.999Z";
        d = JobSchedulerDate.getDate(date, timeZone);
        d = JobSchedulerDate.getDate(date, null);
        
    }
    
    @Test
    public void testInstantFromISO8601StringWithOnlyTime() throws JobSchedulerInvalidResponseDataException {
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34.567Z"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34.567"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34Z"));
        System.out.println(JobSchedulerDate.getInstantFromISO8601String("01:12:34"));
    }

}
