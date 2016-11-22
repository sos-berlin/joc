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
        String date = "6d";
        d = JobSchedulerDate.getDate(date, timeZone);
        date = "-6d";
        d = JobSchedulerDate.getDate(date, timeZone);
        date = "2015-12-17T15:02:22.999-02";
        d = JobSchedulerDate.getDate(date, timeZone);
        date = "2015-12-17T15:02:22.999Z";
        
    }

}
