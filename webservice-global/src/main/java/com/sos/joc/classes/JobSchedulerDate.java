package com.sos.joc.classes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JobSchedulerDate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerDate.class);

    
    public static Date getDateFromISO8601String(String dateString) {
        Instant fromString = getInstantFromISO8601String(dateString);
        return (fromString != null) ? Date.from(fromString) : null;
    }

    public static Instant getInstantFromISO8601String(String dateString) {
        Instant fromString = null;
        if (dateString != null && !dateString.isEmpty()) {
            try {
                dateString = dateString.trim().replaceFirst("^(\\d{4}-\\d{2}-\\d{2}) ", "$1T");
                //dateString must have 'Z' as offset
                fromString = Instant.parse(dateString);
                //better: dateString can have arbitrary offsets
                //fromString = Instant.ofEpochMilli(DatatypeConverter.parseDateTime(dateString).getTimeInMillis());
                // JobScheduler responses max or min time but means 'never'
                if (fromString == null || fromString.getEpochSecond() == 0 || fromString.getEpochSecond() == Long.MAX_VALUE) {
                    fromString = null;
                }
            } catch (DateTimeParseException e) {
                LOGGER.warn(dateString, e);
                fromString = null;
            }
        }
        return fromString;
    }
    
    public static Date getDateFromEventId(Long eventId) {
        if (eventId == null) {
            return null;
        }
        Instant fromEpochMilli = Instant.ofEpochMilli(eventId/1000);
        return Date.from(fromEpochMilli);
    }
    
    public static Date getDate(String date, String timeZone) throws JobSchedulerInvalidResponseDataException {
        try {
            return Date.from(getInstantFromDateStr(date, timeZone));
        } catch (JobSchedulerInvalidResponseDataException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerInvalidResponseDataException(e);
        }
    }
   
    private static Instant getInstantFromDateStr(String dateStr, String timeZone) throws JobSchedulerInvalidResponseDataException {
        if (dateStr == null){
            return  Instant.now();
        }
        Pattern offsetPattern = Pattern.compile("(\\d{2,4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}(?:\\.\\d+)?|(?:\\s*-?\\d+\\s*[smhdwMy])+)([+-][0-9:]+|Z)?$");
        Pattern dateTimePattern = Pattern.compile("(?:(-?\\d+)\\s*([smhdwMy])\\s*)");
        Matcher m = offsetPattern.matcher(dateStr);
        ZonedDateTime zdt = null;
        ZoneId zoneId = null;
        boolean dateTimeIsRelative = false;
        try {
            if (timeZone != null && !timeZone.isEmpty()) {
                zoneId = ZoneId.of(timeZone);
            }
            if (m.find()) {
                if (zoneId == null) {
                    zoneId = (m.group(2) != null) ? ZoneOffset.of(m.group(2)) : ZoneOffset.UTC;
                }
                dateStr = m.group(1);
            } else if (zoneId == null) {
                zoneId = ZoneOffset.UTC;
            }
            m = dateTimePattern.matcher(dateStr.replaceAll("([smhdwMy])", "$1 "));
            while (m.find()) {
                dateTimeIsRelative = true;
                if (zdt == null) {
                    zdt = ZonedDateTime.ofInstant(Instant.now(), zoneId);
                }
                Long number = Long.valueOf(m.group(1));
                
                switch (m.group(2)) {
                case "s":
                    zdt = zdt.plusSeconds(number);
                    break;
                case "m":
                    zdt = zdt.plusMinutes(number);
                    break;
                case "h":
                    zdt = zdt.plusHours(number);
                    break;
                case "d":
                    zdt = zdt.plusDays(number);
                    break;
                case "w":
                    zdt = zdt.plusWeeks(number);
                    break;
                case "M":
                    zdt = zdt.plusMonths(number);
                    break;
                case "y":
                    zdt = zdt.plusYears(number);
                    break;
                }
            }
            if (!dateTimeIsRelative) {
                zdt = ZonedDateTime.ofInstant(Instant.parse(dateStr+"Z"), zoneId);
                return Instant.ofEpochMilli(DatatypeConverter.parseDateTime(dateStr+zdt.getOffset().toString()).getTimeInMillis());
            } else {
                return zdt.toInstant();
            }
        } catch (Exception e) {
            throw new JobSchedulerInvalidResponseDataException(e);
        }
    }
}
