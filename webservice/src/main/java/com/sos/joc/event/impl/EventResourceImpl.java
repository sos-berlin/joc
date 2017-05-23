package com.sos.joc.event.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Path;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.StoppedSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.event.EventCallable;
import com.sos.joc.classes.event.EventCallableJobSchedulerStateChanged;
import com.sos.joc.classes.event.EventCallableOfCurrentCluster;
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

@Path("events")
public class EventResourceImpl extends JOCResourceImpl implements IEventResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventResourceImpl.class);
    private static final String API_CALL = "./events";
    private static final String SESSION_KEY = "EventsStarted";
    private String threadName = "";
    private String urlOfCurrentJs = null;
    public static final Integer EVENT_TIMEOUT = 90;

    @Override
    public JOCDefaultResponse postEvent(String accessToken, RegisterEvent eventBody) {

        JobSchedulerEvents entity = new JobSchedulerEvents();
        Map<String, JobSchedulerEvent> eventList = new HashMap<String, JobSchedulerEvent>();
        Session session = null;
        threadName = Thread.currentThread().getName();

        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, eventBody, accessToken, "", getPermissonsJocCockpit(accessToken)
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
                Globals.forceClosingHttpClients(session);
                entity.setEvents(null);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);
            }

            if (eventBody.getJobscheduler() == null && eventBody.getJobscheduler().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobscheduler'");
            }

            Long defaultEventId = Instant.now().toEpochMilli() * 1000;
            List<EventCallable> tasks = new ArrayList<EventCallable>();
            List<EventCallable> tasksOfClusterMember = new ArrayList<EventCallable>();
            List<JOCJsonCommand> jocJsonCommands = new ArrayList<JOCJsonCommand>();
            List<JOCJsonCommand> jocJsonCommandsOfClusterMember = new ArrayList<JOCJsonCommand>();

            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            InventoryJobChainsDBLayer jobChainLayer = new InventoryJobChainsDBLayer(connection);

            Boolean isCurrentJobScheduler = true;
            for (JobSchedulerObjects jsObject : eventBody.getJobscheduler()) {
                JobSchedulerEvent jsEvent = initEvent(jsObject, defaultEventId);
                eventList.put(jsObject.getJobschedulerId(), jsEvent);
                DBItemInventoryInstance instance = getJobSchedulerInstance(jsObject, instanceLayer, accessToken);
                JOCJsonCommand command = initJocJsonCommand(jsObject, instance);
                jocJsonCommands.add(command);

                if (isCurrentJobScheduler) {

                    jocJsonCommandsOfClusterMember.add(command);
                    List<DBItemInventoryInstance> jobSchedulerMembers = instanceLayer.getInventoryInstancesBySchedulerId(jsObject
                            .getJobschedulerId());
                    if (instance.getSupervisorId() > 0) {
                        DBItemInventoryInstance supervisor = instanceLayer.getInventoryInstanceByKey(instance.getSupervisorId());
                        JobSchedulerEvent jsEventOfMember = initEvent(jsObject, session, supervisor.getId(), defaultEventId);
                        JOCJsonCommand commandOfMember = initJocJsonCommand(jsObject, supervisor, "SchedulerEvent");
                        jocJsonCommandsOfClusterMember.add(commandOfMember);
                        tasksOfClusterMember.add(new EventCallableJobSchedulerStateChanged(commandOfMember, jsEventOfMember, accessToken, session,
                                supervisor.getId()));
                    }

                    for (DBItemInventoryInstance jobSchedulerMember : jobSchedulerMembers) {
                        if (jobSchedulerMember.equals(instance)) {
                            continue;
                        }
                        JobSchedulerEvent jsEventOfMember = initEvent(jsObject, session, jobSchedulerMember.getId(), defaultEventId);
                        JOCJsonCommand commandOfMember = initJocJsonCommand(jsObject, jobSchedulerMember, "SchedulerEvent");
                        jocJsonCommandsOfClusterMember.add(commandOfMember);
                        tasksOfClusterMember.add(new EventCallableJobSchedulerStateChanged(commandOfMember, jsEventOfMember, accessToken, session,
                                jobSchedulerMember.getId()));
                    }
                    tasks.add(new EventCallableOfCurrentCluster(command, jsEvent, accessToken, session, instance.getId(), shiroUser,
                            tasksOfClusterMember, jocJsonCommandsOfClusterMember, getNestedJobChains(jobChainLayer, instance)));

                } else {
                    tasks.add(new EventCallable(command, jsEvent, accessToken, session, instance.getId()));
                }
                isCurrentJobScheduler = false;
                if (urlOfCurrentJs == null) {
                    urlOfCurrentJs = instance.getUrl();
                }
            }
            jocJsonCommands.addAll(jocJsonCommandsOfClusterMember);
            Globals.disconnect(connection);
            try {
                session.setAttribute(Globals.SESSION_KEY_FOR_USED_HTTP_CLIENTS_BY_EVENTS, jocJsonCommands);
            } catch (Exception e1) {
            }

            ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
            try {
                JobSchedulerEvent evt = executorService.invokeAny(tasks);
                eventList.put(evt.getJobschedulerId(), evt);
            } catch (ExecutionException | InterruptedException e) {
                if (e.getCause() instanceof JocException) {
                    throw (JocException) e.getCause();
                } else {
                    throw (Exception) e.getCause();
                }
            } finally {
                // Globals.forceClosingHttpClients(session);
                for (JOCJsonCommand command : jocJsonCommands) {
                    command.forcedClosingHttpClient();
                }
                executorService.shutdown();
            }

            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));

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
        } catch (StoppedSessionException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            LOGGER.debug("./events ended");
            Globals.disconnect(connection);
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
            String eventIdOfClusterMembers = (String) session.getAttribute("eventIdOfClusterMembers");
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

    private JOCJsonCommand initJocJsonCommand(JobSchedulerObjects jsObject, DBItemInventoryInstance instance) {
        return initJocJsonCommand(jsObject, instance, null);
    }

    private JOCJsonCommand initJocJsonCommand(JobSchedulerObjects jsObject, DBItemInventoryInstance instance, String event) {
        JOCJsonCommand command = new JOCJsonCommand();
        command.setUriBuilderForEvents(instance.getUrl());
        command.setBasicAuthorization(instance.getAuth());
        command.setSocketTimeout((EVENT_TIMEOUT + 5) * 1000);
        command.createHttpClient();
        command.setAutoCloseHttpClient(false);
        command.addEventQuery(jsObject.getEventId(), EVENT_TIMEOUT, event);
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
        DBItemInventoryInstance instance = Globals.urlFromJobSchedulerId.get(jsObject.getJobschedulerId());
        if (instance == null) {
            instance = instanceLayer.getInventoryInstanceBySchedulerId(jsObject.getJobschedulerId(), accessToken);
            Globals.urlFromJobSchedulerId.put(jsObject.getJobschedulerId(), instance);
        }
        return instance;
    }

}
