package com.sos.joc.event.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Path;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.dailyplan.db.DailyPlanDBLayer;
import com.sos.jitl.dailyplan.filter.DailyPlanFilter;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.calendar.SendEventScheduled;
import com.sos.joc.classes.event.EventCallable;
import com.sos.joc.classes.event.EventCallableActiveJobSchedulerStateChanged;
import com.sos.joc.classes.event.EventCallableOfCurrentCluster;
import com.sos.joc.classes.event.EventCallableOfCurrentJobScheduler;
import com.sos.joc.classes.event.EventCallablePassiveJobSchedulerStateChanged;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.event.resource.IEventResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;
import com.sos.joc.model.event.JobSchedulerEvents;
import com.sos.joc.model.event.JobSchedulerObjects;
import com.sos.joc.model.event.RegisterEvent;
import com.sos.schema.JsonValidator;

@Path("events")
public class EventResourceImpl extends JOCResourceImpl implements IEventResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventResourceImpl.class);
    private static final String API_CALL = "./events";
    private static final String SESSION_KEY = "EventsStarted";
    private String threadName = "";
    private String urlOfCurrentJs = null;
    private ScheduledThreadPoolExecutor executor = null;
    public static final Integer EVENT_TIMEOUT = 90;
    
    @Override
    public JOCDefaultResponse postEvent(String accessToken, byte[] eventBodyBytes) {

        JobSchedulerEvents entity = new JobSchedulerEvents();
        Map<String, JobSchedulerEvent> eventList = new HashMap<String, JobSchedulerEvent>();
        Session session = null;
        threadName = Thread.currentThread().getName();

        SOSHibernateSession connection = null;

        try {
            JsonValidator.validateFailFast(eventBodyBytes, RegisterEvent.class);
            RegisterEvent eventBody = Globals.objectMapper.readValue(eventBodyBytes, RegisterEvent.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, eventBody, accessToken, "", getPermissonsJocCockpit("", accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            SOSShiroCurrentUser shiroUser = null;
            connection = Globals.createSosHibernateStatelessConnection("postEvent");
            try {
                shiroUser = getJobschedulerUser().getSosShiroCurrentUser();
                session = shiroUser.getCurrentSubject().getSession(false);
                if (session != null) {
                    session.setAttribute(SESSION_KEY, threadName);
                    session.setAttribute(Globals.SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY, false);
                }
            } catch (InvalidSessionException e1) {
                throw new SessionNotExistException(e1);
            }
            // Not a good idea: Same session in multiple tabs closed http
            // clients vice versa
            // Globals.forceClosingHttpClients(session);

            if (eventBody.getClose() != null && eventBody.getClose()) {
                Globals.forceClosingHttpClients(shiroUser, accessToken);
                entity.setEvents(null);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);
            }

            if (eventBody.getJobscheduler() == null && eventBody.getJobscheduler().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobscheduler'");
            }
            
            Long defaultEventId = Instant.now().toEpochMilli() * 1000;
            List<EventCallable> tasks = new ArrayList<EventCallable>();
            Set<JOCJsonCommand> jocJsonCommands = new HashSet<JOCJsonCommand>();
            // List<JOCJsonCommand> jocJsonCommandsOfClusterMember = new ArrayList<JOCJsonCommand>();

            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            InventoryJobChainsDBLayer jobChainLayer = new InventoryJobChainsDBLayer(connection);

            Boolean isCurrentJobScheduler = true;
            for (JobSchedulerObjects jsObject : eventBody.getJobscheduler()) {
                JobSchedulerEvent jsEvent = initEvent(jsObject, defaultEventId);
                eventList.put(jsObject.getJobschedulerId(), jsEvent);
                DBItemInventoryInstance instance = getJobSchedulerInstance(jsObject, instanceLayer, accessToken);
                JOCJsonCommand command = initJocJsonCommand(jsEvent, instance);
                jocJsonCommands.add(command);
                List<JOCJsonCommand> jocJsonCommandsOfClusterMember = new ArrayList<JOCJsonCommand>();
                List<EventCallable> tasksOfClusterMember = new ArrayList<EventCallable>();

                if (isCurrentJobScheduler) {
                    sendNexStartsFromDailyPlan(connection, jsObject.getJobschedulerId(), jsEvent.getEventId(), new JOCXmlCommand(instance), accessToken);
                    tasksOfClusterMember.add(new EventCallableOfCurrentJobScheduler(command, jsEvent, accessToken, session, instance.getId(),
                            shiroUser, getNestedJobChains(jobChainLayer, instance)));
                } else {
                    tasksOfClusterMember.add(new EventCallable(command, jsEvent, accessToken, session, instance.getId()));
                }
                
                if (instance.getSupervisorId() > 0) {
                    jocJsonCommandsOfClusterMember.add(command);
                    DBItemInventoryInstance supervisor = instanceLayer.getInventoryInstanceByKey(instance.getSupervisorId());
                    if (supervisor != null) {
                        JobSchedulerEvent jsEventOfMember = initEvent(jsObject, session, supervisor.getId(), defaultEventId);
                        JOCJsonCommand commandOfMember = initJocJsonCommand(jsEventOfMember, setMappedUrl(supervisor), "SchedulerEvent");
                        jocJsonCommandsOfClusterMember.add(commandOfMember);
                        tasksOfClusterMember.add(new EventCallablePassiveJobSchedulerStateChanged(commandOfMember, jsEventOfMember, accessToken, session,
                                supervisor.getId()));
                    } else {
                        Boolean supervisorWarningIsLogged = (Boolean) session.getAttribute("supervisorWarningIsLogged");
                        if (supervisorWarningIsLogged == null) {
                            LOGGER.warn(String.format("Supervisor with ID %d expected but not found in INVENTORY_INSTANCES", instance
                                    .getSupervisorId()));
                            session.setAttribute("supervisorWarningIsLogged", true);
                        }
                    }
                }
                List<DBItemInventoryInstance> jobSchedulerMembers = null;
                
                switch (instance.getClusterType()) {
                case "standalone":
                    break;
                case "passive":
                    jobSchedulerMembers = instanceLayer.getInventoryInstancesBySchedulerId(jsObject.getJobschedulerId());
                    if (jobSchedulerMembers != null) {
                        for (DBItemInventoryInstance jobSchedulerMember : jobSchedulerMembers) {
                            if (jobSchedulerMember == null || jobSchedulerMember.equals(instance)) {
                                continue;
                            }
                            JobSchedulerEvent jsEventOfMember = initEvent(jsObject, session, jobSchedulerMember.getId(), defaultEventId);
                            JOCJsonCommand commandOfMember = initJocJsonCommand(jsEventOfMember, setMappedUrl(jobSchedulerMember), "SchedulerEvent");
                            jocJsonCommandsOfClusterMember.add(commandOfMember);
                            tasksOfClusterMember.add(new EventCallablePassiveJobSchedulerStateChanged(commandOfMember, jsEventOfMember, accessToken, session,
                                    jobSchedulerMember.getId()));
                        }
                    }
                    break;
                case "active":
                    List<String> distributedJobChains = jobChainLayer.getDistributedJobChains(instance.getId());
                    List<String> distributedJobs = jobChainLayer.getDistributedJobs(instance.getId());
                    jobSchedulerMembers = instanceLayer.getInventoryInstancesBySchedulerId(jsObject.getJobschedulerId());
                    if (jobSchedulerMembers != null) {
                        for (DBItemInventoryInstance jobSchedulerMember : jobSchedulerMembers) {
                            if (jobSchedulerMember == null || jobSchedulerMember.equals(instance)) {
                                continue;
                            }
                            JobSchedulerEvent jsEventOfMember = initEvent(jsObject, session, jobSchedulerMember.getId(), defaultEventId);
                            JOCJsonCommand commandOfMember = initJocJsonCommand(jsEventOfMember, setMappedUrl(jobSchedulerMember), "SchedulerEvent,TaskEvent,OrderEvent,VariablesCustomEvent");
                            jocJsonCommandsOfClusterMember.add(commandOfMember);
                            tasksOfClusterMember.add(new EventCallableActiveJobSchedulerStateChanged(commandOfMember, jsEventOfMember, accessToken, session,
                                    jobSchedulerMember.getId(), distributedJobChains, distributedJobs));
                        }
                    }
                    break;
                }
                
                if (tasksOfClusterMember.size() == 1) {
                    tasks.add(tasksOfClusterMember.get(0));
                } else {
                    tasks.add(new EventCallableOfCurrentCluster(command, jsEvent, accessToken, session, instance.getId(), shiroUser,
                            tasksOfClusterMember, jocJsonCommandsOfClusterMember, isCurrentJobScheduler));
                }
                isCurrentJobScheduler = false;
                if (urlOfCurrentJs == null) {
                    urlOfCurrentJs = instance.getUrl();
                }
                jocJsonCommands.addAll(jocJsonCommandsOfClusterMember);
            }
            Globals.disconnect(connection);
            try {
                jobschedulerUser.setJocJsonCommands(jocJsonCommands);
            } catch (Exception e1) {
            }
            if (!tasks.isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
                try {
                    JobSchedulerEvent evt = executorService.invokeAny(tasks);
                    eventList.put(evt.getJobschedulerId(), evt);
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof JocException) {
                        throw (JocException) e.getCause();
                    } else {
                        throw e.getCause();
                    }
                } finally {
                    // Globals.forceClosingHttpClients(session);
                    for (JOCJsonCommand command : jocJsonCommands) {
                        command.forcedClosingHttpClient();
                    }
                    executorService.shutdown();
                    if (!executorService.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                        for (JOCJsonCommand command : jocJsonCommands) {
                            command.forcedClosingHttpClient();
                        }
                    }
                }
            }

            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
            
        } catch (DBConnectionRefusedException e) {
        	LOGGER.info(e.getMessage());
        } catch (ForcedClosingHttpClientException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
        } catch (JobSchedulerConnectionRefusedException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            if (!concurrentEventsCallIsStarted(session)) {
                try {
                    e = new JobSchedulerConnectionRefusedException(urlOfCurrentJs);
                } catch (Exception e1) {
                }
                e.addErrorMetaInfo(getJocError());
                return JOCDefaultResponse.responseStatus434JSError(e);
            } else {
                LOGGER.warn("./events concurrent call was started");
                entity.setDeliveryDate(Date.from(Instant.now()));
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (InvalidSessionException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
        } catch (Throwable e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            LOGGER.debug("./events ended");
            Globals.disconnect(connection);
            if (executor != null) {
                executor.shutdown();
            }
        }
        return JOCDefaultResponse.responseStatus200(entity);
    }

    private boolean concurrentEventsCallIsStarted(Session session) {
        boolean concurrentEventsCallIsStarted = false;
        String sessionUuid = null;
        if (session != null) {
            try {
                sessionUuid = (String) session.getAttribute(SESSION_KEY);
            } catch (Exception ee) {
            }
        }
        if (sessionUuid != null && !sessionUuid.equals(threadName)) {
            concurrentEventsCallIsStarted = true;
        }
        return concurrentEventsCallIsStarted;
    }

    private JobSchedulerEvent initEvent(JobSchedulerObjects jsObject, Session session, Long instanceId, Long defaultEventId) {
        String eventId = defaultEventId.toString();
        String jsId = jsObject.getJobschedulerId();

        if (jsObject.getEventId() != null && !jsObject.getEventId().isEmpty()) {
            eventId = jsObject.getEventId();
        }
        if (session != null) {
            String eventIdOfClusterMembers = (String) session.getAttribute(jsId + "#eventIdOfClusterMembers");
            if (eventIdOfClusterMembers != null) {
                eventId = eventIdOfClusterMembers;
            }
        }
        if (instanceId != null) {
            jsId = "__instance__" + instanceId;
        }
        LOGGER.debug("EventId of " + jsId + ": " + eventId);
        JobSchedulerEvent jsEvent = new JobSchedulerEvent();
        jsEvent.setEventId(eventId);
        jsEvent.setJobschedulerId(jsId);
        jsEvent.setEventSnapshots(new ArrayList<EventSnapshot>());
        return jsEvent;
    }

    private JobSchedulerEvent initEvent(JobSchedulerObjects jsObject, Long defaultEventId) {
        return initEvent(jsObject, null, null, defaultEventId);
    }

    private JOCJsonCommand initJocJsonCommand(JobSchedulerEvent jsEvent, DBItemInventoryInstance instance) {
        return initJocJsonCommand(jsEvent, instance, null);
    }

    private JOCJsonCommand initJocJsonCommand(JobSchedulerEvent jsEvent, DBItemInventoryInstance instance, String event) {
        JOCJsonCommand command = new JOCJsonCommand();
        command.setUriBuilderForEvents(instance.getUrl());
        command.setBasicAuthorization(instance.getAuth());
        command.setSocketTimeout((EVENT_TIMEOUT + 5) * 1000);
        command.createHttpClient();
        command.setAutoCloseHttpClient(false);
        command.addEventQuery(jsEvent.getEventId(), EVENT_TIMEOUT, event);
        return command;
    }

    private Map<String, Set<String>> getNestedJobChains(InventoryJobChainsDBLayer jobChainLayer, DBItemInventoryInstance instance) {
        Map<String, Set<String>> nestedJobChains = null;
        try {
            nestedJobChains = jobChainLayer.getMapOfOuterJobChains(instance.getId());
        } catch (JocException e) {
            LOGGER.warn("cannot determine nested job chains: " + e.getCause().getMessage());
        }
        return nestedJobChains;
    }

    private DBItemInventoryInstance getJobSchedulerInstance(JobSchedulerObjects jsObject, InventoryInstancesDBLayer instanceLayer, String accessToken)
            throws DBInvalidDataException, DBMissingDataException, DBConnectionRefusedException {
        DBItemInventoryInstance instance = setMappedUrl(Globals.urlFromJobSchedulerId.get(jsObject.getJobschedulerId()));
        if (instance == null) {
            instance = instanceLayer.getInventoryInstanceBySchedulerId(jsObject.getJobschedulerId(), accessToken, true);
            Globals.urlFromJobSchedulerId.put(jsObject.getJobschedulerId(), instance);
        }
        return instance;
    }

    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance, true);
        }
        return instance;
    }
    
    private void sendNexStartsFromDailyPlan(SOSHibernateSession connection, String jobschedulerId, String eventId, JOCXmlCommand jocXmlCommand,
            String accessToken) {
        try {
            Instant from = Instant.ofEpochMilli(Long.valueOf(eventId) / 1000); // Instant.now();
            Instant to = from.plusSeconds(60 * 6);
            DailyPlanFilter dailyPlanFilter = new DailyPlanFilter();
            dailyPlanFilter.setPlannedStartFrom(Date.from(from));
            dailyPlanFilter.setPlannedStartTo(Date.from(to));
            dailyPlanFilter.setSchedulerId(jobschedulerId);
            DailyPlanDBLayer dailyPlanDbLayer = new DailyPlanDBLayer(connection);
            dailyPlanDbLayer.setFilter(dailyPlanFilter);
            List<DailyPlanDBItem> dailyPlanDbItems = dailyPlanDbLayer.getDailyPlanListOfOrderSingleStarts();
            if (dailyPlanDbItems != null && !dailyPlanDbItems.isEmpty()) {
                List<SendEventScheduled> sendEventsLater = new ArrayList<SendEventScheduled>();
                for (DailyPlanDBItem dailyPlanDbItem : dailyPlanDbItems) {
                    long seconds = 0L;
                    String orderId = dailyPlanDbItem.getOrderIdNotNull();
                    String jobChain = dailyPlanDbItem.getJobChainNotNull();
                    if (dailyPlanDbItem.getPlannedStart() != null && !jobChain.isEmpty() && !orderId.isEmpty()) {
                        seconds = dailyPlanDbItem.getPlannedStart().getTime() - from.getEpochSecond();
                        if (seconds < 0) {
                            seconds = 0L;
                        }
                        SendEventScheduled evt = new SendEventScheduled(jobChain + "," + orderId, jocXmlCommand, accessToken, seconds);
                        if (seconds == 0L) {
                            evt.run();
                        } else {
                            sendEventsLater.add(evt);
                        }
                    }
                }
                if (!sendEventsLater.isEmpty()) {
                    executor = new ScheduledThreadPoolExecutor(sendEventsLater.size());
                    executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
                    for (SendEventScheduled evt : sendEventsLater) {
                        executor.schedule(evt, evt.getSeconds(), TimeUnit.SECONDS);
                    }
                }
            }

        } catch (SOSHibernateException e) {
            LOGGER.warn("", e);
        }
    }

}
