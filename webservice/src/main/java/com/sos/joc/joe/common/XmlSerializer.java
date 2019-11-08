package com.sos.joc.joe.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.job.Commands;
import com.sos.joc.model.joe.job.EnviromentVariables;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.job.StartJob;
import com.sos.joc.model.joe.jobchain.FileOrderSource;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.jobchain.OnReturnCodes;
import com.sos.joc.model.joe.nodeparams.Config;
import com.sos.joc.model.joe.nodeparams.ConfigNode;
import com.sos.joc.model.joe.nodeparams.ConfigOrder;
import com.sos.joc.model.joe.order.AddOrder;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.processclass.ProcessClass;
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

    private static final List<String> objectsWithSpecialSerialization = Arrays.asList("JOB", "JOBCHAIN", "ORDER", "PROCESSCLASS", "AGENTCLUSTER",
            "SCHEDULE", "RUNTIME", "MONITOR", "NODEPARAMS");
    private static final List<String> trueValues = Arrays.asList("true", "1", "yes");
    private static final List<String> falseValues = Arrays.asList("false", "0", "no");
    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + System.lineSeparator() + System.lineSeparator();

    public static String serializeToStringWithHeader(Object jsonPojo) throws JsonProcessingException {
        return xmlHeader + Globals.xmlMapper.writeValueAsString(jsonPojo);
    }

    public static String serializeToStringWithHeader(String json, String objType) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        return xmlHeader + serializeToString(json, objType);
    }

    public static String serializeToString(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsString(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(byte[] json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType)));
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

    public static <T> T serialize(IJSObject obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return serialize(clazz.cast(obj), clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T serialize(T obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        switch (clazz.getSimpleName()) {
        case "Job":
            return (T) serializeJob((Job) obj);

        case "JobChain":
            return (T) serializeJobChain((JobChain) obj);

        case "Order":
            return (T) serializeOrder((Order) obj);

        case "ProcessClass":
            return (T) serializeProcessClass((ProcessClass) obj);

        case "Schedule":
        case "RunTime":
            return (T) serializeAbstractSchedule((AbstractSchedule) obj);

        case "Monitor":
            return (T) serializeMonitor((Monitor) obj, false);

        case "Config":
            return (T) serializeNodeParams((Config) obj);
        }
        return obj;
    }

    public static Job serializeJob(Job job) {
        if (job.getEnabled() != null && trueValues.contains(job.getEnabled())) {
            job.setEnabled(null);
        }
        if (job.getIsOrderJob() != null && falseValues.contains(job.getIsOrderJob())) {
            job.setIsOrderJob(null);
        }
        if (job.getMaxTasks() != null && job.getMaxTasks() == 1) {
            job.setMaxTasks(null);
        }
        if (job.getMinTasks() != null && job.getMinTasks() == 0) {
            job.setMinTasks(null);
        }
        if (job.getForceIdleTimeout() != null && falseValues.contains(job.getForceIdleTimeout())) {
            job.setForceIdleTimeout(null);
        }
        if (job.getStopOnError() != null && trueValues.contains(job.getStopOnError())) {
            job.setStopOnError(null);
        }
        if (job.getStderrLogLevel() != null && !"error".equals(job.getStderrLogLevel())) {
            job.setStderrLogLevel(null);
        }
        if (job.getLoadUserProfile() != null && falseValues.contains(job.getLoadUserProfile())) {
            job.setLoadUserProfile(null);
        }
        if (job.getRunTime() == null) {
            job.setRunTime(new RunTime());
        } else {
            RunTime runtime = serializeAbstractSchedule(job.getRunTime());
            job.setRunTime(runtime);
        }
        if (job.getIsOrderJob() != null && trueValues.contains(job.getIsOrderJob())) {
            job.setDelayAfterErrors(null);
            job.setStartWhenDirectoriesChanged(null);
        } else {
            job.setDelayOrderAfterSetbacks(null);
        }
        job.setParams(serializeParams(job.getParams()));
        job.setEnvironment(serializeEnvironment(job.getEnvironment()));
        if (job.getMonitorUses() != null) {
            job.setMonitorUses(job.getMonitorUses().stream().filter(i -> i.getMonitor() != null && !i.getMonitor().isEmpty()).collect(Collectors
                    .toList()));
            if (job.getMonitorUses().isEmpty()) {
                job.setMonitorUses(null);
            }
        }
        if (job.getLockUses() != null) {
            job.setLockUses(job.getLockUses().stream().filter(i -> i.getLock() != null && !i.getLock().isEmpty()).collect(Collectors.toList()));
            if (job.getLockUses().isEmpty()) {
                job.setLockUses(null);
            }
        }
        if (job.getDelayOrderAfterSetbacks() != null) {
            job.setDelayOrderAfterSetbacks(job.getDelayOrderAfterSetbacks().stream().filter(i -> i.getSetbackCount() != null && ((i.getDelay() != null
                    && !i.getDelay().isEmpty()) || (i.getIsMaximum() != null && !i.getIsMaximum().isEmpty()))).collect(Collectors.toList()));
            if (job.getDelayOrderAfterSetbacks().isEmpty()) {
                job.setDelayOrderAfterSetbacks(null);
            }
        }
        if (job.getStartWhenDirectoriesChanged() != null) {
            job.setStartWhenDirectoriesChanged(job.getStartWhenDirectoriesChanged().stream().filter(i -> i.getDirectory() != null && !i.getDirectory()
                    .isEmpty()).collect(Collectors.toList()));
            if (job.getStartWhenDirectoriesChanged().isEmpty()) {
                job.setStartWhenDirectoriesChanged(null);
            }
        }
        if (job.getCommands() != null) {
            job.setCommands(job.getCommands().stream().filter(i -> (i.getAddOrders() != null && !i.getAddOrders().isEmpty()) || (i
                    .getStartJobs() != null && !i.getStartJobs().isEmpty())).collect(Collectors.toList()));
            for (Commands commands : job.getCommands()) {
                if (commands.getAddOrders() != null) {
                    for (AddOrder addOrder : commands.getAddOrders()) {
                        addOrder.setParams(serializeParams(addOrder.getParams(), true));
                    }
                }
                if (commands.getStartJobs() != null) {
                    for (StartJob startJob : commands.getStartJobs()) {
                        startJob.setParams(serializeParams(startJob.getParams(), true));
                        startJob.setEnvironment(serializeEnvironment(startJob.getEnvironment()));
                    }
                }
            }
        }
        job.setScript(serializeScript(job.getScript()));
        if (job.getMonitors() != null) {
            job.setMonitors(job.getMonitors().stream().map(i -> serializeMonitor(i, true)).collect(Collectors.toList()));
        }

        return job;
    }

    public static JobChain serializeJobChain(JobChain jobChain) {
        if (jobChain.getOrdersRecoverable() != null && trueValues.contains(jobChain.getOrdersRecoverable())) {
            jobChain.setOrdersRecoverable(null);
        }
        if (jobChain.getDistributed() != null && falseValues.contains(jobChain.getDistributed())) {
            jobChain.setDistributed(null);
        }
        if (jobChain.getFileOrderSources() != null) {
            for (FileOrderSource orderSource : jobChain.getFileOrderSources()) {
                if (orderSource.getAlertWhenDirectoryMissing() != null && trueValues.contains(orderSource.getAlertWhenDirectoryMissing())) {
                    orderSource.setAlertWhenDirectoryMissing(null);
                }
            }
        }
        if (jobChain.getJobChainNodes() != null) {
            jobChain.getJobChainNodes().stream().map(item -> {
                item.setOnReturnCodes(serializeOnReturnCodes(item.getOnReturnCodes()));
                return item;
            }).collect(Collectors.toList());
        }
        return jobChain;
    }

    public static Order serializeOrder(Order order) {
        if (order.getRunTime() == null) {
            order.setRunTime(new RunTime());
        } else {
            RunTime runtime = serializeAbstractSchedule(order.getRunTime());
            order.setRunTime(runtime);
        }
        order.setParams(serializeParams(order.getParams()));
        return order;
    }

    public static ProcessClass serializeProcessClass(ProcessClass processClass) {
        RemoteSchedulers agents = processClass.getRemoteSchedulers();
        if (agents != null) {
            if (agents.getRemoteSchedulerList() != null) {
                agents.setRemoteSchedulerList(agents.getRemoteSchedulerList().stream().filter(i -> i.getRemoteScheduler() != null && !i
                        .getRemoteScheduler().isEmpty()).collect(Collectors.toList()));
                if (agents.getRemoteSchedulerList().isEmpty()) {
                    agents = null;
                }
            }
            if (agents != null && agents.getSelect() != null && "first".equals(agents.getSelect())) {
                agents.setSelect(null);
            }
            processClass.setRemoteSchedulers(agents);
        }
        if (processClass.getTimeout() != null && processClass.getTimeout() == 0) {
            processClass.setTimeout(null);
        }
        return processClass;
    }

    public static Monitor serializeMonitor(Monitor monitor, boolean internal) {
        if (!internal) {
            monitor.setName(null);
        }
        monitor.setScript(serializeScript(monitor.getScript()));
        return monitor;
    }

    public static Config serializeNodeParams(Config nodeParams) {
        if (nodeParams.getJobChain() != null) {
            ConfigOrder orderElem = nodeParams.getJobChain().getOrder();
            if (orderElem != null) {
                orderElem.setParams(serializeNodeParams(orderElem.getParams()));
                if (orderElem.getJobChainNodes() != null) {
                    for (ConfigNode node : orderElem.getJobChainNodes()) {
                        node.setParams(serializeNodeParams(node.getParams()));
                    }
                    orderElem.setJobChainNodes(orderElem.getJobChainNodes().stream().filter(i -> i.getParams() != null && i.getParams()
                            .getParamList() != null && !i.getParams().getParamList().isEmpty()).collect(Collectors.toList()));
                    if (orderElem.getJobChainNodes().isEmpty()) {
                        orderElem.setJobChainNodes(null);
                    }
                }
                if (orderElem.getParams() == null && orderElem.getJobChainNodes() == null) {
                    nodeParams.setJobChain(null);
                } else {
                    nodeParams.getJobChain().setOrder(orderElem);
                }
            }
        }
        return nodeParams;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractSchedule> T serializeAbstractSchedule(AbstractSchedule runtime) {
        if (runtime.getLetRun() != null && falseValues.contains(runtime.getLetRun())) {
            runtime.setLetRun(null);
        }
        if (runtime.getRunOnce() != null && falseValues.contains(runtime.getRunOnce())) {
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
            if (period.getLetRun() != null && falseValues.contains(period.getLetRun())) {
                period.setLetRun(null);
            }
            if (period.getRunOnce() != null && falseValues.contains(period.getRunOnce())) {
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
    
    private static Params serializeParams(Params params) {
        return serializeParams(params, false);
    }

    private static Params serializeParams(Params params, boolean withCopyParams) {
        if (params != null) {
            if (params.getIncludes() != null) {
                params.setIncludes(params.getIncludes().stream().filter(i -> (i.getFile() != null && !i.getFile().isEmpty()) || (i
                        .getLiveFile() != null && !i.getLiveFile().isEmpty())).collect(Collectors.toList()));
                if (params.getIncludes().isEmpty()) {
                    params.setIncludes(null);
                }
            }
            if (params.getParamList() != null) {
                params.setParamList(params.getParamList().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors
                        .toList()));
                if (params.getParamList().isEmpty()) {
                    params.setParamList(null);
                }
            }
            if (withCopyParams) {
                if (params.getCopyParams() != null) {
                    params.setCopyParams(params.getCopyParams().stream().filter(i -> i.getFrom() != null && ("order".equals(i.getFrom()) || "task"
                            .equals(i.getFrom()))).collect(Collectors.toSet()));
                    if (params.getCopyParams().isEmpty()) {
                        params.setCopyParams(null);
                    }
                }
            } else {
                params.setCopyParams(null);
            }
            if (params.getIncludes() == null && params.getParamList() == null && params.getCopyParams() == null) {
                params = null;
            }
        }
        return params;
    }

    private static com.sos.joc.model.joe.nodeparams.Params serializeNodeParams(com.sos.joc.model.joe.nodeparams.Params params) {
        if (params != null) {
            if (params.getParamList() != null) {
                params.setParamList(params.getParamList().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors
                        .toList()));
                if (params.getParamList().isEmpty()) {
                    params.setParamList(null);
                }
            }
            if (params.getParamList() == null) {
                params = null;
            }
        }
        return params;
    }

    private static EnviromentVariables serializeEnvironment(EnviromentVariables variables) {
        if (variables != null) {
            if (variables.getVariables() != null) {
                variables.setVariables(variables.getVariables().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors
                        .toList()));
                if (variables.getVariables().isEmpty()) {
                    variables = null;
                }
            }
        }
        return variables;
    }

    private static Script serializeScript(Script script) {
        if (script != null) {
            if (script.getLanguage() == null) {
                script.setLanguage("shell");
            }
            if (script.getLanguage().contains("java")) {
                script.setComClass(null);
                script.setDll(null);
                script.setDotnetClass(null);
                if (!script.getLanguage().equals("java")) {
                    script.setJavaClass(null);
                }
            } else if (script.getLanguage().equals("dotnet")) {
                script.setJavaClass(null);
                script.setJavaClassPath(null);
                script.setComClass(null);
            } else {
                script.setJavaClass(null);
                script.setJavaClassPath(null);
                // script.setComClass(null);
                script.setDll(null);
                script.setDotnetClass(null);
            }
            if (script.getContent() != null) {
                script.setContent("\n" + script.getContent().trim() + "\n");
            }
        }
        return script;
    }

    private static OnReturnCodes serializeOnReturnCodes(OnReturnCodes onReturnCodes) {
        if (onReturnCodes != null) {
            if (onReturnCodes.getOnReturnCodeList() != null) {
                onReturnCodes.setOnReturnCodeList(onReturnCodes.getOnReturnCodeList().stream().filter(i -> i.getReturnCode() != null && ((i
                        .getToState() != null && i.getToState().getState() != null && !i.getToState().getState().isEmpty()) || (i
                                .getAddOrder() != null && i.getAddOrder().getJobChain() != null && !i.getAddOrder().getJobChain().isEmpty())))
                        .collect(Collectors.toList()));
                if (onReturnCodes.getOnReturnCodeList().isEmpty()) {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return onReturnCodes;
    }

}
