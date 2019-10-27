package com.sos.joc.joe.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.jobchain.FileOrderSource;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.processclass.RemoteScheduler;
import com.sos.joc.model.joe.processclass.RemoteSchedulers;
import com.sos.joc.model.joe.schedule.AbstractSchedule;
import com.sos.joc.model.joe.schedule.Day;
import com.sos.joc.model.joe.schedule.Monthdays;
import com.sos.joc.model.joe.schedule.Period;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.joe.schedule.Ultimos;
import com.sos.joc.model.joe.schedule.WeekdayOfMonth;
import com.sos.joc.model.joe.schedule.Weekdays;

public class XmlSerializer {

    private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + System.lineSeparator() + System.lineSeparator();
    private static final List<String> objectsWithSpecialSerialization = Arrays.asList("JOB", "JOBCHAIN", "ORDER", "PROCESSCLASS", "AGENTCLUSTER",
            "SCHEDULE", "RUNTIME");

    public static String serializeToStringWithHeader(Object jsonPojo) throws JsonProcessingException {
        return xmlHeader + Globals.xmlMapper.writeValueAsString(jsonPojo);
    }

    public static String serializeToStringWithHeader(String json, String objType) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        return xmlHeader + serializeToString(json, objType);
    }

    public static String serializeToString(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsString(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(byte[] json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    private static <T> T serialize(byte[] json, String objType, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException,
            JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    private static <T> T serialize(String json, String objType, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException,
            JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T serialize(T obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        switch (clazz.getSimpleName()) {
        case "Job":
            Job job = (Job) obj;
            if (job.getEnabled() != null && "true,1,yes".contains(job.getEnabled())) {
                job.setEnabled(null);
            }
            if (job.getIsOrderJob() != null && "false,0,no".contains(job.getIsOrderJob())) {
                job.setIsOrderJob(null);
            }
            if (job.getMaxTasks() != null && job.getMaxTasks() == 1) {
                job.setMaxTasks(null);
            }
            if (job.getMinTasks() != null && job.getMinTasks() == 0) {
                job.setMinTasks(null);
            }
            if (job.getForceIdleTimeout() != null && "false,0,no".contains(job.getForceIdleTimeout())) {
                job.setForceIdleTimeout(null);
            }
            if (job.getStopOnError() != null && "true,1,yes".contains(job.getStopOnError())) {
                job.setStopOnError(null);
            }
            if (job.getStderrLogLevel() != null && "info".equals(job.getStderrLogLevel())) {
                job.setStderrLogLevel(null);
            }
            if (job.getLoadUserProfile() != null && "false,0,no".contains(job.getLoadUserProfile())) {
                job.setLoadUserProfile(null);
            }
            if (job.getRunTime() == null) {
                job.setRunTime(new RunTime());
            } else {
                RunTime runtime = serializeAbstractSchedule(job.getRunTime());
                job.setRunTime(runtime);
            }
            return (T) job;

        case "JobChain":
            JobChain jobChain = (JobChain) obj;
            if (jobChain.getOrdersRecoverable() != null && "true,1,yes".contains(jobChain.getOrdersRecoverable())) {
                jobChain.setOrdersRecoverable(null);
            }
            if (jobChain.getDistributed() != null && "false,0,no".contains(jobChain.getDistributed())) {
                jobChain.setDistributed(null);
            }
            if (jobChain.getFileOrderSources() != null) {
                for (FileOrderSource orderSource : jobChain.getFileOrderSources()) {
                    if (orderSource.getAlertWhenDirectoryMissing() != null && "true,1,yes".contains(orderSource.getAlertWhenDirectoryMissing())) {
                        orderSource.setAlertWhenDirectoryMissing(null);
                    }
                }
            }
            return (T) jobChain;

        case "Order":
            Order order = (Order) obj;
            if (order.getRunTime() == null) {
                order.setRunTime(new RunTime());
            } else {
                RunTime runtime = serializeAbstractSchedule(order.getRunTime());
                order.setRunTime(runtime);
            }
            return (T) order;

        case "ProcessClass":
            ProcessClass processClass = (ProcessClass) obj;
            RemoteSchedulers agents = processClass.getRemoteSchedulers();
            if (agents != null) {
                List<RemoteScheduler> agentList = agents.getRemoteSchedulerList();
                if (agentList == null || agentList.isEmpty()) {
                    processClass.setRemoteSchedulers(null);
                } else if (agents.getSelect() != null && "first".equals(agents.getSelect())) {
                    processClass.getRemoteSchedulers().setSelect(null);
                }
            }
            return (T) processClass;

        case "Schedule":
        case "RunTime":
            return (T) serializeAbstractSchedule((AbstractSchedule) obj);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T extends AbstractSchedule> T serializeAbstractSchedule(AbstractSchedule runtime) {
        if (runtime.getLetRun() != null && "false,0,no".contains(runtime.getLetRun())) {
            runtime.setLetRun(null);
        }
        if (runtime.getRunOnce() != null && "false,0,no".contains(runtime.getRunOnce())) {
            runtime.setRunOnce(null);
        }
        runtime.setPeriods(serializePeriod(runtime.getPeriods()));
        if (runtime.getDates() != null && !runtime.getDates().isEmpty()) {
            runtime.setDates(runtime.getDates().stream().map(item -> {
                item.setPeriods(serializePeriod(item.getPeriods()));
                return item;
            }).collect(Collectors.toList()));
        } else {
            runtime.setDates(null);
        }
        runtime.setWeekdays(serializeWeekdays(runtime.getWeekdays()));
        runtime.setUltimos(serializeUltimos(runtime.getUltimos()));
        runtime.setMonthdays(serializeMonthdays(runtime.getMonthdays()));
        if (runtime.getMonths() != null) {
            runtime.getMonths().stream().map(month -> {
                month.setPeriods(serializePeriod(month.getPeriods()));
                month.setWeekdays(serializeWeekdays(month.getWeekdays()));
                month.setUltimos(serializeUltimos(month.getUltimos()));
                month.setMonthdays(serializeMonthdays(month.getMonthdays()));
                return month;
            }).collect(Collectors.toList());
        }
        if (runtime.getHolidays() != null) {
            if (runtime.getHolidays().getDays() != null && !runtime.getHolidays().getDays().isEmpty()) {
                runtime.getHolidays().setDays(runtime.getHolidays().getDays().stream().map(item -> {
                    item.setPeriods(serializePeriod(item.getPeriods()));
                    return item;
                }).collect(Collectors.toList()));
            } else {
                runtime.getHolidays().setDays(null);
            }
            runtime.getHolidays().setWeekdays(serializeWeekdays(runtime.getHolidays().getWeekdays()));
        }
        return (T) runtime;
    }

    private static List<Period> serializePeriod(List<Period> periods) {
        if (periods == null || periods.isEmpty()) {
            return null;
        }
        return periods.stream().map(period -> {
            if (period.getLetRun() != null && "false,0,no".contains(period.getLetRun())) {
                period.setLetRun(null);
            }
            if (period.getRunOnce() != null && "false,0,no".contains(period.getRunOnce())) {
                period.setRunOnce(null);
            }
            if (period.getWhenHoliday() != null && "suppress".equals(period.getWhenHoliday())) {
                period.setWhenHoliday(null);
            }
            return period;
        }).collect(Collectors.toList());
    }

    private static List<Day> serializeDays(List<Day> days) {
        if (days == null || days.isEmpty()) {
            return null;
        }
        return days.stream().map(item -> {
            item.setPeriods(serializePeriod(item.getPeriods()));
            return item;
        }).collect(Collectors.toList());
    }

    private static Weekdays serializeWeekdays(Weekdays weekdays) {
        if (weekdays == null) {
            return null;
        }
        weekdays.setDays(serializeDays(weekdays.getDays()));
        return weekdays;
    }

    private static Ultimos serializeUltimos(Ultimos ultimos) {
        if (ultimos == null) {
            return null;
        }
        ultimos.setDays(serializeDays(ultimos.getDays()));
        return ultimos;
    }

    private static List<WeekdayOfMonth> serializeWeekdaysOfMonth(List<WeekdayOfMonth> weekdays) {
        if (weekdays == null || weekdays.isEmpty()) {
            return null;
        }
        return weekdays.stream().map(item -> {
            item.setPeriods(serializePeriod(item.getPeriods()));
            return item;
        }).collect(Collectors.toList());
    }

    private static Monthdays serializeMonthdays(Monthdays monthdays) {
        if (monthdays == null) {
            return null;
        }
        monthdays.setDays(serializeDays(monthdays.getDays()));
        monthdays.setWeekdays(serializeWeekdaysOfMonth(monthdays.getWeekdays()));
        return monthdays;
    }

}
