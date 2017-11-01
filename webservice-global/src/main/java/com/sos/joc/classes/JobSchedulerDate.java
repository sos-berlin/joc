package com.sos.joc.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.exceptions.JobSchedulerBadRequestException;
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
    
    public static String getAtInJobSchedulerTimezone(String at, String userTimezone, String jobSchedulerTimezone) throws JobSchedulerBadRequestException {
        at = at.trim();
        if (at.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            at += ":00";
        }
        if (!at.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            throw new JobSchedulerBadRequestException(String.format("date format yyyy-MM-dd HH:mm:ss expected for \"Start time\": %1$s", at));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(ZonedDateTime.of(LocalDateTime.parse(at, formatter), ZoneId.of(userTimezone)).withZoneSameInstant(ZoneId.of(jobSchedulerTimezone)));
    }
   
    private static Instant getInstantFromDateStr(String dateStr, boolean dateTo, String timeZone) throws JobSchedulerInvalidResponseDataException {
        Pattern offsetPattern = Pattern.compile("(\\d{2,4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}(?:\\.\\d+)?|(?:\\s*-?\\d+\\s*[smhdwMy])+)([+-][0-9:]+|Z)?$");
        Pattern dateTimePattern = Pattern.compile("(?:(-?\\d+)\\s*([smhdwMy])\\s*)");
        Matcher m = offsetPattern.matcher(dateStr);
        Calendar calendar = null;
        TimeZone timeZ = null;
        boolean dateTimeIsRelative = false;
        try {
            if (timeZone != null && !timeZone.isEmpty()) {
                timeZ = TimeZone.getTimeZone(timeZone);
            }
            if (m.find()) {
                if (timeZ == null) {
                    timeZ = (m.group(2) != null) ? TimeZone.getTimeZone("GMT"+m.group(2)) : TimeZone.getTimeZone(ZoneOffset.UTC);
                }
                dateStr = m.group(1);
            } else if (timeZ == null) {
                timeZ = TimeZone.getTimeZone(ZoneOffset.UTC);
            }
            m = dateTimePattern.matcher(dateStr.replaceAll("([smhdwMy])", "$1 "));
            while (m.find()) {
                dateTimeIsRelative = true;
                if (calendar == null) {
                    Instant instant = Instant.now();
                    calendar = Calendar.getInstance();
                    calendar.setTime(Date.from(instant));
                    calendar.setTimeZone(timeZ);
                    if (Pattern.compile("[dwMy]").matcher(dateStr).find()) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    }
                }
                Integer number = Integer.valueOf(m.group(1));
                
                switch (m.group(2)) {
                case "s":
                    calendar.add(Calendar.SECOND, number);
                    break;
                case "m":
                    calendar.add(Calendar.MINUTE, number);
                    break;
                case "h":
                    calendar.add(Calendar.HOUR_OF_DAY, number);
                    break;
                case "d":
                    if (dateTo) {
                        calendar.add(Calendar.DATE, 1 + number);
                    } else {
                        calendar.add(Calendar.DATE, number);
                    }
                    break;
                case "w":
                    if (dateTo) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1 + number);
                    } else {
                        calendar.add(Calendar.WEEK_OF_YEAR, number);
                    }
                    calendar.add(Calendar.DATE, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
                    break;
                case "M":
                    if (dateTo) {
                        calendar.add(Calendar.MONTH, 1 + number);
                    } else {
                        calendar.add(Calendar.MONTH, number);
                    }
                    calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DATE));
                    break;
                case "y":
                    if (dateTo) {
                        calendar.add(Calendar.YEAR, 1 + number);
                    } else {
                        calendar.add(Calendar.YEAR, number);
                    }
                    calendar.add(Calendar.DAY_OF_YEAR, 1 - calendar.get(Calendar.DAY_OF_YEAR));
                    break;
                }
            }
            if (!dateTimeIsRelative) {
                Instant instant = Instant.parse(dateStr+"Z");
                int offset = timeZ.getOffset(instant.toEpochMilli());
                return instant.plusMillis(-1*offset);
            } else {
                return calendar.toInstant();
            }
        } catch (Exception e) {
            throw new JobSchedulerInvalidResponseDataException(e);
        }
    }
}
