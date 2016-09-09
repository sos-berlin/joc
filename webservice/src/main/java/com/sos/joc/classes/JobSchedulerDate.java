package com.sos.joc.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JobSchedulerDate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerDate.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    
    public static Date getDate(String dateString) throws ParseException{
        if (dateString != null) {
            if (!dateString.contains("T")) {
                return SDF.parse(dateString);
            } else {
                return SDF2.parse(dateString);
            }
        } else {
            return null;
        }
    }
    
    public static Date getDateFromISO8601String(String dateString) {
        Instant fromString = getInstantFromISO8601String(dateString);
        return (fromString != null) ? Date.from(getInstantFromISO8601String(dateString)) : null;
    }

    public static Instant getInstantFromISO8601String(String dateString) {
        Instant fromString = null;
        if (dateString != null) {
            try {
                fromString = Instant.parse(dateString);
                // JobScheduler responses max or in time but means 'never'
                if (fromString.getEpochSecond() == 0 || fromString.getEpochSecond() == Long.MAX_VALUE) {
                    fromString = null;
                }
            } catch (NullPointerException e) {
                fromString = null;
            } catch (DateTimeParseException e) {
                LOGGER.info(dateString, e);
                fromString = null;
            }
        }
        return fromString;
    }            
}
