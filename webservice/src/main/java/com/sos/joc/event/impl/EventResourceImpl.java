package com.sos.joc.event.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Path;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.event.EventCallable;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.IEventResource;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.event.JobSchedulerEvent;
import com.sos.joc.model.event.JobSchedulerEvents;
import com.sos.joc.model.event.JobSchedulerObjects;
import com.sos.joc.model.event.RegisterEvent;

@Path("events")
public class EventResourceImpl extends JOCResourceImpl implements IEventResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOCDefaultResponse.class);
    private static final String API_CALL = "./events";
    private static final Integer EVENT_TIMEOUT = 90;
    private static final String SESSION_KEY = "EventsStarted";
    private String threadName = "";
    private String urlOfCurrentJs = null;

    @Override
    public JOCDefaultResponse postEvent(String accessToken, RegisterEvent eventBody) throws Exception {

        JobSchedulerEvents entity = new JobSchedulerEvents();
        Map<String, JobSchedulerEvent> eventList = new HashMap<String, JobSchedulerEvent>();
        Session session = null;
        threadName = Thread.currentThread().getName();
        try {
            initLogging(API_CALL, eventBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, "", getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                session = getJobschedulerUser().getSosShiroCurrentUser().getCurrentSubject().getSession(false);
                session.setAttribute(SESSION_KEY, threadName);
            } catch (InvalidSessionException e1) {
            }
            Globals.forceClosingHttpClients(session);

            if (eventBody.getClose() != null && eventBody.getClose()) {
                entity.setEvents(null);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);
            }

            if (eventBody.getJobscheduler() == null && eventBody.getJobscheduler().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobscheduler'");
            }

            Long defaultEventId = Instant.now().toEpochMilli() * 1000;
            List<EventCallable> tasks = new ArrayList<EventCallable>();
            List<JOCJsonCommand> jocJsonCommands = new ArrayList<JOCJsonCommand>();
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            Globals.beginTransaction();

            for (JobSchedulerObjects jsObject : eventBody.getJobscheduler()) {
                if (jsObject.getEventId() == null || jsObject.getEventId().isEmpty()) {
                    jsObject.setEventId(defaultEventId.toString());
                }
                JobSchedulerEvent jsEvent = new JobSchedulerEvent();
                jsEvent.setEventId(jsObject.getEventId());
                jsEvent.setJobschedulerId(jsObject.getJobschedulerId());
                eventList.put(jsObject.getJobschedulerId(), jsEvent);
                DBItemInventoryInstance instance = Globals.UrlFromJobSchedulerId.get(jsObject.getJobschedulerId());
                if (instance == null) {
                    instance = instanceLayer.getInventoryInstanceBySchedulerId(jsObject.getJobschedulerId(), accessToken);
                    Globals.UrlFromJobSchedulerId.put(jsObject.getJobschedulerId(), instance);
                }
                JOCJsonCommand command = new JOCJsonCommand();
                command.setUriBuilderForEvents(instance.getUrl());
                command.setSocketTimeout((EVENT_TIMEOUT + 5) * 1000);
                command.createHttpClient();
                command.setAutoCloseHttpClient(false);
                command.addEventQuery(jsObject.getEventId(), EVENT_TIMEOUT);
                jocJsonCommands.add(command);
                tasks.add(new EventCallable(command, jsEvent, instance.getId(), accessToken, session, EVENT_TIMEOUT));
                if (urlOfCurrentJs == null) {
                    urlOfCurrentJs = instance.getUrl();
                }
            }
            try {
                session.setAttribute(Globals.SESSION_KEY_FOR_USED_HTTP_CLIENTS_BY_EVENTS, jocJsonCommands);
            } catch (Exception e1) {
            }

            ExecutorService executorService = Executors.newFixedThreadPool(eventBody.getJobscheduler().size());
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
                Globals.forceClosingHttpClients(session);
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
        } catch (InvalidSessionException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            LOGGER.debug("./events ended");
            Globals.rollback();
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
}
