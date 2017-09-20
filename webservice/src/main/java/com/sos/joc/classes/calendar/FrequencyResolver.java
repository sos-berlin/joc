package com.sos.joc.classes.calendar;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Frequencies;
import com.sos.joc.model.calendar.Holidays;
import com.sos.joc.model.calendar.MonthDays;
import com.sos.joc.model.calendar.Months;
import com.sos.joc.model.calendar.Repetition;
import com.sos.joc.model.calendar.WeekDays;

public class FrequencyResolver {

    private SortedSet<String> dates = new TreeSet<String>(new Comparator<String>() {

        public int compare(String a, String b) {
            return b.compareTo(a);
        }
    });
    private Instant dateFrom = null;
    private Instant dateTo = null;
    private Frequencies includes = null;
    private Frequencies excludes = null;

    public FrequencyResolver() {
        //
    }

    public SortedSet<String> getDates() {
        return dates;
    }

    public List<String> resolve(CalendarDatesFilter calendarFilter) throws JocMissingRequiredParameterException,
            JobSchedulerInvalidResponseDataException {
        init(calendarFilter);
        addDates();
        addHolidays();
        addWeekDays();
        addMonthDays();
        addUltimos();
        addMonths();
        addRepetitions();
        removeDates();
        removeWeekDays();
        removeMonthDays();
        removeUltimos();
        removeMonths();
        removeHolidays();
        removeRepetitions();
        return new ArrayList<String>(dates);
    }

    public void init(CalendarDatesFilter calendarFilter) throws JocMissingRequiredParameterException, JobSchedulerInvalidResponseDataException {
        if (calendarFilter != null) {
            setDateFrom(calendarFilter.getDateFrom());
            setDateTo(calendarFilter.getDateTo());
            if (this.dateFrom.compareTo(this.dateTo) == 1) {
                throw new JobSchedulerInvalidResponseDataException("'dateFrom' must be an older date than 'dateTo'.");
            }
            if (calendarFilter.getCalendar() != null) {
                this.includes = calendarFilter.getCalendar().getIncludes();
                this.excludes = calendarFilter.getCalendar().getExcludes();
            }
        }
    }

    public void setDateFrom(String dateFrom) throws JocMissingRequiredParameterException, JobSchedulerInvalidResponseDataException {
        if (dateFrom == null || dateFrom.isEmpty()) {
            throw new JocMissingRequiredParameterException("'dateFrom' parameter is undefined.");
        }
        if (!dateFrom.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            throw new JobSchedulerInvalidResponseDataException("'dateFrom' parameter must have the format YYYY-MM-DD.");
        }
        this.dateFrom = Instant.parse(dateFrom + "T00:00:00Z");
    }

    public void setDateTo(String dateTo) throws JocMissingRequiredParameterException, JobSchedulerInvalidResponseDataException {
        if (dateTo == null || dateTo.isEmpty()) {
            throw new JocMissingRequiredParameterException("'dateTo' parameter is undefined.");
        }
        if (!dateTo.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            throw new JobSchedulerInvalidResponseDataException("'dateTo' parameter must have the format YYYY-MM-DD.");
        }
        this.dateTo = Instant.parse(dateTo + "T00:00:00Z");
    }

    public void addDates() {
        if (includes != null) {
            addDates(includes.getDates());
        }
    }

    public void removeDates() {
        if (excludes != null && dates.size() > 0) {
            addDates(excludes.getDates());
        }
    }

    public void addHolidays() {
        if (includes != null) {
            addHolidays(includes.getHolidays());
        }
    }

    public void removeHolidays() {
        if (excludes != null && dates.size() > 0) {
            addHolidays(excludes.getHolidays());
        }
    }

    public void addWeekDays() throws JobSchedulerInvalidResponseDataException {
        if (includes != null) {
            addWeekDays(includes.getWeekdays());
        }
    }

    public void removeWeekDays() throws JobSchedulerInvalidResponseDataException {
        if (excludes != null && dates.size() > 0) {
            removeWeekDays(excludes.getWeekdays());
        }
    }

    public void addMonthDays() throws JobSchedulerInvalidResponseDataException {
        if (includes != null) {
            addMonthDays(includes.getMonthdays());
        }
    }

    public void removeMonthDays() throws JobSchedulerInvalidResponseDataException {
        if (excludes != null && dates.size() > 0) {
            removeMonthDays(excludes.getMonthdays());
        }
    }

    public void addUltimos() throws JobSchedulerInvalidResponseDataException {
        if (includes != null) {
            addUltimos(includes.getUltimos());
        }
    }

    public void removeUltimos() throws JobSchedulerInvalidResponseDataException {
        if (excludes != null && dates.size() > 0) {
            removeUltimos(excludes.getUltimos());
        }
    }

    public void addRepetitions() throws JobSchedulerInvalidResponseDataException {
        if (includes != null) {
            addRepetitions(includes.getRepetitions());
        }
    }

    public void removeRepetitions() throws JobSchedulerInvalidResponseDataException {
        if (excludes != null && dates.size() > 0) {
            addRepetitions(excludes.getRepetitions());
        }
    }

    public void addMonths() throws JobSchedulerInvalidResponseDataException {
        if (includes != null) {
            addMonths(includes.getMonths());
        }
    }

    public void removeMonths() throws JobSchedulerInvalidResponseDataException {
        if (excludes != null && dates.size() > 0) {
            removeMonths(excludes.getMonths());
        }
    }

    public void addDates(List<String> list) {
        addAll(list);
    }

    public void removeDates(List<String> list) {
        removeAll(list);
    }

    public void addHolidays(List<Holidays> holidays) {
        if (holidays != null) {
            for (Holidays holiday : holidays) {
                addDates(holiday.getDates());
            }
        }
    }

    public void removeHolidays(List<Holidays> holidays) {
        if (holidays != null) {
            for (Holidays holiday : holidays) {
                removeDates(holiday.getDates());
            }
        }
    }

    public void addWeekDays(List<WeekDays> weekDays) throws JobSchedulerInvalidResponseDataException {
        if (weekDays != null) {
            for (WeekDays weekDay : weekDays) {
                addDates(FrequencyWeekDaysResolver.resolve(weekDay.getDays(), getFrom(weekDay.getFrom()), getTo(weekDay.getTo())));
            }
        }
    }

    public void removeWeekDays(List<WeekDays> weekDays) throws JobSchedulerInvalidResponseDataException {
        if (weekDays != null) {
            for (WeekDays weekDay : weekDays) {
                removeDates(FrequencyWeekDaysResolver.resolve(weekDay.getDays(), getFrom(weekDay.getFrom()), getTo(weekDay.getTo())));
            }
        }
    }

    public void addMonthDays(List<MonthDays> monthDays) throws JobSchedulerInvalidResponseDataException {
        if (monthDays != null) {
            for (MonthDays monthDay : monthDays) {
                addDates(FrequencyMonthDaysResolver.resolve(monthDay.getDays(), monthDay.getWorkingDays(), monthDay.getWeeklyDays(), getFrom(monthDay
                        .getFrom()), getTo(monthDay.getTo())));
            }
        }
    }

    public void removeMonthDays(List<MonthDays> monthDays) throws JobSchedulerInvalidResponseDataException {
        if (monthDays != null) {
            for (MonthDays monthDay : monthDays) {
                removeDates(FrequencyMonthDaysResolver.resolve(monthDay.getDays(), monthDay.getWorkingDays(), monthDay.getWeeklyDays(), getFrom(
                        monthDay.getFrom()), getTo(monthDay.getTo())));
            }
        }
    }

    public void addUltimos(List<MonthDays> ultimos) throws JobSchedulerInvalidResponseDataException {
        if (ultimos != null) {
            for (MonthDays ultimo : ultimos) {
                addDates(FrequencyUltimosResolver.resolve(ultimo.getDays(), ultimo.getWorkingDays(), ultimo.getWeeklyDays(), getFrom(ultimo
                        .getFrom()), getTo(ultimo.getTo())));
            }
        }
    }

    public void removeUltimos(List<MonthDays> ultimos) throws JobSchedulerInvalidResponseDataException {
        if (ultimos != null) {
            for (MonthDays ultimo : ultimos) {
                removeDates(FrequencyUltimosResolver.resolve(ultimo.getDays(), ultimo.getWorkingDays(), ultimo.getWeeklyDays(), getFrom(ultimo
                        .getFrom()), getTo(ultimo.getTo())));
            }
        }
    }

    public void addRepetitions(List<Repetition> repetitions) throws JobSchedulerInvalidResponseDataException {
        if (repetitions != null) {
            for (Repetition repetition : repetitions) {
                addDates(FrequencyRepetitionResolver.resolve(repetition.getRepetition(), repetition.getStep(), getFrom(repetition.getFrom()), getTo(
                        repetition.getTo())));
            }
        }
    }

    public void removeRepetitions(List<Repetition> repetitions) throws JobSchedulerInvalidResponseDataException {
        if (repetitions != null) {
            for (Repetition repetition : repetitions) {
                removeDates(FrequencyRepetitionResolver.resolve(repetition.getRepetition(), repetition.getStep(), getFrom(repetition.getFrom()),
                        getTo(repetition.getTo())));
            }
        }
    }

    public void addMonths(List<Months> months) throws JobSchedulerInvalidResponseDataException {
        if (months != null) {
            for (Months month : months) {
                addDates(FrequencyMonthsResolver.resolve(month.getMonths(), month.getWeekdays(), month.getMonthdays(), month.getUltimos(), getFrom(
                        month.getFrom()), getTo(month.getTo())));
            }
        }
    }

    public void removeMonths(List<Months> months) throws JobSchedulerInvalidResponseDataException {
        if (months != null) {
            for (Months month : months) {
                removeDates(FrequencyMonthsResolver.resolve(month.getMonths(), month.getWeekdays(), month.getMonthdays(), month.getUltimos(), getFrom(
                        month.getFrom()), getTo(month.getTo())));
            }
        }
    }

    private boolean addAll(Collection<String> c) {
        if (c == null) {
            return false;
        }
        return dates.addAll(c);
    }

    private boolean removeAll(Collection<String> c) {
        if (c == null) {
            return false;
        }
        return dates.removeAll(c);
    }

    private Instant getFrom(String from) throws JobSchedulerInvalidResponseDataException {
        if (from == null || from.isEmpty()) {
            return dateFrom;
        }
        if (!from.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            throw new JobSchedulerInvalidResponseDataException("json field 'from' must have the format YYYY-MM-DD.");
        }
        Instant instant = Instant.parse(from + "T00:00:00Z");
        if (instant.compareTo(dateFrom) > 0) {
            return instant;
        }
        return dateFrom;
    }

    private Instant getTo(String to) throws JobSchedulerInvalidResponseDataException {
        if (to == null || to.isEmpty()) {
            return dateTo;
        }
        if (!to.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            throw new JobSchedulerInvalidResponseDataException("json field 'to' must have the format YYYY-MM-DD.");
        }
        Instant instant = Instant.parse(to + "T00:00:00Z");
        if (instant.compareTo(dateTo) < 0) {
            return instant;
        }
        return dateFrom;
    }

}
