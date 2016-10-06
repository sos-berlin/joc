package com.sos.joc.classes;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JobSchedulerDate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerDate.class);

    
    public static Date getDateFromISO8601String(String dateString) {
        Instant fromString = getInstantFromISO8601String(dateString);
        return (fromString != null) ? Date.from(fromString) : null;
    }

    public static Instant getInstantFromISO8601String(String dateString) {
        Instant fromString = null;
        if (dateString != null) {
            try {
                dateString = dateString.trim().replaceFirst("^(\\d{4}-\\d{2}-\\d{2}) ", "$1T");
                fromString = Instant.parse(dateString);
                // JobScheduler responses max or in time but means 'never'
                if (fromString == null || fromString.getEpochSecond() == 0 || fromString.getEpochSecond() == Long.MAX_VALUE) {
                    fromString = null;
                }
            } catch (DateTimeParseException e) {
                LOGGER.info(dateString, e);
                fromString = null;
            }
        }
        return fromString;
    }
    
    public static Date getDateFromEventId(long eventId) {
        Instant fromEpochMilli = Instant.ofEpochMilli(eventId/1000);
        return Date.from(fromEpochMilli);
    }
}
