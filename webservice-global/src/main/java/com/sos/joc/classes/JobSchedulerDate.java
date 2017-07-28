package com.sos.joc.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JobSchedulerDate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerDate.class);
    
    public static String getNowInISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    
    public static Date getDateFromISO8601String(String dateString) {
        Instant fromString = getInstantFromISO8601String(dateString);
        return (fromString != null) ? Date.from(fromString) : null;
    }

    public static Instant getInstantFromISO8601String(String dateString) {
        Instant fromString = null;
        if (dateString != null && !dateString.isEmpty()) {
            try {
                if (dateString.trim().matches("^\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,3})?Z?$")) {
                    //only time will be extended with today to complete timestamp
                    dateString = Instant.now().toString().replaceFirst("^(\\d{4}-\\d{2}-\\d{2}T).*", "$1" + dateString.trim() + "Z").replaceFirst("ZZ+$", "Z"); 
                }
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
    
    public static Date getDateTo(String date, String timeZone) throws JobSchedulerInvalidResponseDataException {
        return getDate(date, true, timeZone);
    }
    
    public static Date getDateFrom(String date, String timeZone) throws JobSchedulerInvalidResponseDataException {
        return getDate(date, false, timeZone);
    }
    
    public static Date getDate(String date, boolean dateTo, String timeZone) throws JobSchedulerInvalidResponseDataException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return Date.from(getInstantFromDateStr(date, dateTo, timeZone));
        } catch (JobSchedulerInvalidResponseDataException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerInvalidResponseDataException(e);
        }
    }
   
    private static Instant getInstantFromDateStr(String dateStr, boolean dateTo, String timeZone) throws JobSchedulerInvalidResponseDataException {
        Pattern offsetPattern = Pattern.compile("(\\d{2,4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}(?:\\.\\d+)?|(?:\\s*-?\\d+\\s*[smhdwMy])+)([+-][0-9:]+|Z)?$");
        Pattern dateTimePattern = Pattern.compile("(?:(-?\\d+)\\s*([smhdwMy])\\s*)");
        Matcher m = offsetPattern.matcher(dateStr);
        Calendar calendar = null;
        ZonedDateTime zdt = null;
        //ZoneId zoneId = null;
        TimeZone timeZ = null;
        boolean dateTimeIsRelative = false;
        try {
            if (timeZone != null && !timeZone.isEmpty()) {
                //zoneId = ZoneId.of(timeZone);
                timeZ = TimeZone.getTimeZone(timeZone);
            }
            if (m.find()) {
//                if (zoneId == null) {
//                    zoneId = (m.group(2) != null) ? ZoneOffset.of(m.group(2)) : ZoneOffset.UTC;
//                }
                if (timeZ == null) {
                    timeZ = (m.group(2) != null) ? TimeZone.getTimeZone("GMT"+m.group(2)) : TimeZone.getTimeZone(ZoneOffset.UTC);
                }
                dateStr = m.group(1);
            } else if (timeZ == null) {
//                zoneId = ZoneOffset.UTC;
                timeZ = TimeZone.getTimeZone(ZoneOffset.UTC);
            }
            m = dateTimePattern.matcher(dateStr.replaceAll("([smhdwMy])", "$1 "));
            while (m.find()) {
                dateTimeIsRelative = true;
                if (calendar == null) {
                    Instant instant = Instant.now();
                    calendar = Calendar.getInstance();
                    if (Pattern.compile("[dwMy]").matcher(dateStr).find()) {
                        calendar.setTime(Date.from(instant));
                        calendar.setTimeZone(timeZ);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        if (dateTo) {
                            calendar.add(Calendar.DATE, 1);
                        }
                        zdt = ZonedDateTime.ofInstant(calendar.toInstant(), timeZ.toZoneId());
                    } else {
                        zdt = ZonedDateTime.ofInstant(instant, timeZ.toZoneId());
                    }
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
                Instant instant = Instant.parse(dateStr+"Z");
                int offset = timeZ.getOffset(instant.toEpochMilli());
                return instant.plusMillis(-1*offset);
//                zdt = ZonedDateTime.ofInstant(Instant.parse(dateStr+"Z"), timeZ.toZoneId());
//                return Instant.ofEpochMilli(DatatypeConverter.parseDateTime(dateStr+zdt.getOffset().toString()).getTimeInMillis());
            } else {
                return zdt.toInstant();
            }
        } catch (Exception e) {
            throw new JobSchedulerInvalidResponseDataException(e);
        }
    }
}
