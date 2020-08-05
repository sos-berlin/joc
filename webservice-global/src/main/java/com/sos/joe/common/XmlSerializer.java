package com.sos.joe.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JoeConfigurationException;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.job.Commands;
import com.sos.joc.model.joe.job.EnviromentVariables;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.job.StartJob;
import com.sos.joc.model.joe.jobchain.FileOrderSink;
import com.sos.joc.model.joe.jobchain.FileOrderSource;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.jobchain.JobChainEndNode;
import com.sos.joc.model.joe.jobchain.JobChainNode;
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
    //public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + System.lineSeparator() + System.lineSeparator();
    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + System.lineSeparator() + System.lineSeparator();

    public static String serializeToStringWithHeader(Object jsonPojo) throws JsonProcessingException {
        return xmlHeader + Globals.xmlMapper.writeValueAsString(jsonPojo);
    }

    public static String serializeToStringWithHeader(String json, String objType) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        return serializeToStringWithHeader(json, objType, false);
    }

    public static String serializeToStringWithHeader(String json, String objType, boolean withCheck) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        return xmlHeader + serializeToString(json, objType, withCheck);
    }

    public static String serializeToString(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        return serializeToString(json, objType, false);
    }

    public static String serializeToString(String json, String objType, boolean withCheck) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsString(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType), withCheck));
    }

    public static byte[] serializeToBytes(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        return serializeToBytes(json, objType, false);
    }

    public static byte[] serializeToBytes(String json, String objType, boolean withCheck) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType), withCheck));
    }

    public static byte[] serializeToBytes(byte[] json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        return serializeToBytes(json, objType, false);
    }

    public static byte[] serializeToBytes(byte[] json, String objType, boolean withCheck) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, JOEHelper.CLASS_MAPPING.get(objType), withCheck));
    }

    private static <T> T serialize(byte[] json, String objType, Class<T> clazz, boolean withCheck) throws JsonParseException, JsonMappingException,
            IOException, JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz, withCheck);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    private static <T> T serialize(String json, String objType, Class<T> clazz, boolean withCheck) throws JsonParseException, JsonMappingException,
            IOException, JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz, withCheck);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    public static <T> T serialize(IJSObject obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return serialize(clazz.cast(obj), clazz, false);
    }

    public static <T> T serialize(IJSObject obj, Class<T> clazz, boolean withCheck) throws JsonParseException, JsonMappingException, IOException {
        return serialize(clazz.cast(obj), clazz, withCheck);
    }

    public static <T> T serialize(T obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return serialize(obj, clazz, false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T serialize(T obj, Class<T> clazz, boolean withCheck) throws JsonParseException, JsonMappingException, IOException {
        switch (clazz.getSimpleName()) {
        case "Job":
            return (T) serializeJob((Job) obj, withCheck);

        case "JobChain":
            return (T) serializeJobChain((JobChain) obj, withCheck);

        case "Order":
            return (T) serializeOrder((Order) obj);

        case "ProcessClass":
            return (T) serializeProcessClass((ProcessClass) obj, withCheck);

        case "Schedule":
        case "RunTime":
            return (T) serializeAbstractSchedule((AbstractSchedule) obj);

        case "Monitor":
            return (T) serializeMonitor((Monitor) obj, false, withCheck);

        case "Config":
            return (T) serializeNodeParams((Config) obj);
        }
        return obj;
    }

    public static Job serializeJob(Job job) {
        return serializeJob(job, false);
    }

    public static Job serializeJob(Job job, boolean withCheck) {
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
        if (job.getCriticality() != null && "normal".equals(job.getCriticality())) {
            job.setCriticality(null);
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
        job.setScript(serializeScript(job.getScript(), withCheck));
        if (job.getMonitors() != null) {
            job.setMonitors(job.getMonitors().stream().map(i -> serializeMonitor(i, true, withCheck)).collect(Collectors.toList()));
        }

        return job;
    }

    public static JobChain serializeJobChain(JobChain jobChain) {
        return serializeJobChain(jobChain, false);
    }

    public static JobChain serializeJobChain(JobChain jobChain, boolean withCheck) {
        List<String> statesOfAllNodesList = new ArrayList<String>();
        Set<String> nextStatesOfAllNodes = new HashSet<String>();
        Set<String> errorStatesOfAllNodes = new HashSet<String>();

        if (jobChain.getOrdersRecoverable() != null && trueValues.contains(jobChain.getOrdersRecoverable())) {
            jobChain.setOrdersRecoverable(null);
        }
        if (jobChain.getDistributed() != null && falseValues.contains(jobChain.getDistributed())) {
            jobChain.setDistributed(null);
        }
        if (jobChain.getFileOrderSources() != null) {
            if (withCheck && !jobChain.getFileOrderSources().isEmpty()) {
                if (jobChain.getFileOrderSources().stream().filter(i -> i.getDirectory() == null || i.getDirectory().isEmpty()).findFirst()
                        .isPresent()) {
                    throw new JoeConfigurationException("The directory attribute is required in a file order source");
                }
                nextStatesOfAllNodes.addAll(jobChain.getFileOrderSources().stream().filter(i -> i.getNextState() != null && !i.getNextState()
                        .isEmpty()).map(FileOrderSource::getNextState).collect(Collectors.toSet()));
            }
            for (FileOrderSource orderSource : jobChain.getFileOrderSources()) {
                if (orderSource.getAlertWhenDirectoryMissing() != null && trueValues.contains(orderSource.getAlertWhenDirectoryMissing())) {
                    orderSource.setAlertWhenDirectoryMissing(null);
                }
            }
        }
        if (jobChain.getFileOrderSinks() != null) {
            if (withCheck && !jobChain.getFileOrderSinks().isEmpty()) {
                List<String> fileOrderSinkStates = jobChain.getFileOrderSinks().stream().filter(i -> i.getState() != null && !i.getState().isEmpty())
                        .map(FileOrderSink::getState).collect(Collectors.toList());
                if (fileOrderSinkStates.size() != jobChain.getFileOrderSinks().size()) {
                    throw new JoeConfigurationException("A state is required in a file order sink");
                }
                statesOfAllNodesList.addAll(fileOrderSinkStates);
            }
        }
        if (jobChain.getJobChainEndNodes() != null) {
            if (withCheck && !jobChain.getJobChainEndNodes().isEmpty()) {
                List<String> endNodeStates = jobChain.getJobChainEndNodes().stream().filter(i -> i.getState() != null && !i.getState().isEmpty()).map(
                        JobChainEndNode::getState).collect(Collectors.toList());
                if (endNodeStates.size() != jobChain.getJobChainEndNodes().size()) {
                    throw new JoeConfigurationException("The state attribute is required in a job chain end node");
                }
                statesOfAllNodesList.addAll(endNodeStates);
            }
        }
        if (jobChain.getJobChainNodes() != null) {
            if (withCheck && !jobChain.getJobChainNodes().isEmpty()) {
                List<String> nodeStates = jobChain.getJobChainNodes().stream().filter(i -> i.getState() != null && !i.getState().isEmpty()).map(
                        JobChainNode::getState).collect(Collectors.toList());
                if (nodeStates.size() != jobChain.getJobChainNodes().size()) {
                    throw new JoeConfigurationException("The state attribute is required in a job chain node");
                }
                nextStatesOfAllNodes.addAll(jobChain.getJobChainNodes().stream().filter(i -> i.getNextState() != null && !i.getNextState().isEmpty())
                        .map(JobChainNode::getNextState).collect(Collectors.toSet()));
                errorStatesOfAllNodes.addAll(jobChain.getJobChainNodes().stream().filter(i -> i.getErrorState() != null && !i.getErrorState()
                        .isEmpty()).map(JobChainNode::getErrorState).collect(Collectors.toSet()));
                statesOfAllNodesList.addAll(nodeStates);
            }
            jobChain.getJobChainNodes().stream().map(item -> {
                item.setOnReturnCodes(serializeOnReturnCodes(item.getOnReturnCodes()));
                return item;
            }).collect(Collectors.toList());
        }
        if (withCheck) {
            if ((jobChain.getJobChainNodes() == null || jobChain.getJobChainNodes().isEmpty()) && (jobChain.getFileOrderSinks() == null || jobChain
                    .getFileOrderSinks().isEmpty())) {
                throw new JoeConfigurationException("At least one job chain node is required");
            }
            Set<String> statesOfAllNodes = new HashSet<String>(statesOfAllNodesList);
            if (statesOfAllNodesList.size() != statesOfAllNodes.size()) {
                throw new JoeConfigurationException("The states of all job chain nodes have to be unique");
            }
            Optional<String> nextState = nextStatesOfAllNodes.stream().filter(i -> !statesOfAllNodes.contains(i)).findFirst();
            if (nextState.isPresent()) {
                throw new JoeConfigurationException("A job chain node with a state '" + nextState.get() + "' is missing");
            }
            Optional<String> errorState = errorStatesOfAllNodes.stream().filter(i -> !statesOfAllNodes.contains(i)).findFirst();
            if (errorState.isPresent()) {
                throw new JoeConfigurationException("A job chain node with a state '" + errorState.get() + "' is missing");
            }
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

    public static ProcessClass serializeProcessClass(ProcessClass processClass, boolean withCheck) {
        if (withCheck && processClass.getMaxProcesses() == null) {
            throw new JoeConfigurationException("The max processes attribute is required");
        }
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
        return serializeMonitor(monitor, internal, false);
    }

    public static Monitor serializeMonitor(Monitor monitor, boolean internal, boolean withCheck) {
        if (!internal) {
            monitor.setName(null);
        }
        monitor.setScript(serializeScript(monitor.getScript(), withCheck));
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
        if (runtime.getCalendars() != null && runtime.getCalendars().trim().equals("{}")) {
            runtime.setCalendars(null);
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

    private static Script serializeScript(Script script, boolean withCheck) {
        if (script != null) {
            String leadingCharInScript = withCheck ? "\n" : "";

            if (script.getLanguage() == null || script.getLanguage().isEmpty()) {
                if (withCheck) {
                    throw new JoeConfigurationException("The script language attribute is required");
                }
            } else {

                switch (script.getLanguage()) {
                case "java":
                    script.setDll(null);
                    script.setDotnetClass(null);
                    script.setIncludes(null);
                    if (withCheck && (script.getJavaClass() == null || script.getJavaClass().isEmpty())) {
                        throw new JoeConfigurationException("The class name attribute is required for language java");
                    }
                    if (script.getContent() != null) {
                        if (script.getContent().trim().isEmpty()) {
                            script.setContent(null);
                        } else {
                            script.setContent(leadingCharInScript + script.getContent().trim() + "\n");
                        }
                    }
                    break;
                case "dotnet":
                    script.setJavaClass(null);
                    script.setJavaClassPath(null);
                    script.setIncludes(null);
                    if (withCheck) {
                        if (script.getDll() == null || script.getDll().isEmpty()) {
                            throw new JoeConfigurationException("The dll attribute is required for language dotnet");
                        }
                        if (script.getDotnetClass() == null || script.getDotnetClass().isEmpty()) {
                            throw new JoeConfigurationException("The class attribute is required for language dotnet");
                        }
                    }
                    if (script.getContent() != null) {
                        if (script.getContent().trim().isEmpty()) {
                            script.setContent(null);
                        } else {
                            script.setContent(leadingCharInScript + script.getContent().trim() + "\n");
                        }
                    }
                    break;
                default:
                    script.setDll(null);
                    script.setDotnetClass(null);
                    if (!script.getLanguage().contains("java")) { // not javascript
                        script.setJavaClass(null);
                        script.setJavaClassPath(null);
                    }
                    if (withCheck && (script.getContent() == null || script.getContent().trim().isEmpty()) && (script.getIncludes() == null || script
                            .getIncludes().isEmpty())) {
                        throw new JoeConfigurationException("A script content or includes are required for language " + script.getLanguage());
                    }
                    if (script.getContent() != null) {
                        if (script.getIncludes() != null && script.getIncludes().isEmpty()) {
                            if (script.getContent().trim().isEmpty()) {
                                script.setContent(null);
                            } else {
                                script.setContent(leadingCharInScript + script.getContent().trim() + "\n");
                            }
                        } else {
                            script.setContent(leadingCharInScript + script.getContent().trim() + "\n");
                        }
                    }
                    break;
                }
            }

        } else if (withCheck) {
            throw new JoeConfigurationException("A script is required");
        }
        return script;
    }

    private static OnReturnCodes serializeOnReturnCodes(OnReturnCodes onReturnCodes) {
        if (onReturnCodes != null) {
            if (onReturnCodes.getOnReturnCodeList() != null) {
                onReturnCodes.setOnReturnCodeList(onReturnCodes.getOnReturnCodeList().stream().filter(i -> i.getReturnCode() != null && ((i
                        .getToState() != null && i.getToState().getState() != null && !i.getToState().getState().isEmpty()) || (i
                                .getAddOrders() != null && !i.getAddOrders().isEmpty())))
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
