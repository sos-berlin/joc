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
        String dateFrom = "6d";
        d = JobSchedulerDate.getDateFromDateFrom(dateFrom, timeZone);
        dateFrom = "2015-12-17T15:02:22.999Z";
        d = JobSchedulerDate.getDateFromDateFrom(dateFrom, timeZone);
        dateFrom = "2015-12-17T15:02:22.999Z";
        
    }

}
