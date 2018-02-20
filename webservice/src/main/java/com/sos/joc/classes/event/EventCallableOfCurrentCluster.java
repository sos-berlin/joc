package com.sos.joc.classes.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallableOfCurrentCluster extends EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallableOfCurrentCluster.class);
    private final String accessToken;
    private final Boolean isCurrent;
    private List<EventCallable> tasksOfClustermember = null;
    private List<JOCJsonCommand> jocJsonCommands = null;
    private String jobSchedulerId = null;
    private String eventId = null;
    private Session shiroSession = null;
    private SOSHibernateSession session = null;
    private final SOSShiroCurrentUser shiroUser;
    
    public EventCallableOfCurrentCluster(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session,
            Long instanceId, SOSShiroCurrentUser shiroUser, List<EventCallable> tasksOfClustermember, List<JOCJsonCommand> jocJsonCommands, Boolean isCurrentJobScheduler) {
        super();
        this.accessToken = accessToken;
        this.tasksOfClustermember = tasksOfClustermember;
        this.jocJsonCommands = jocJsonCommands;
        this.jobSchedulerId = jobSchedulerEvent.getJobschedulerId();
        this.shiroUser = shiroUser;
        this.eventId = jobSchedulerEvent.getEventId();
        this.shiroSession = session;
        this.isCurrent = isCurrentJobScheduler;
    }

    @Override
    public JobSchedulerEvent call() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(tasksOfClustermember.size());
        try {
            JobSchedulerEvent evt = executorService.invokeAny(tasksOfClustermember);
            LOGGER.debug("EventOfCluster: " + evt.getJobschedulerId() + "," + evt.getEventId() );
            if (evt.getJobschedulerId().startsWith("__instance__")) {
                shiroSession.setAttribute(jobSchedulerId + "#eventIdOfClusterMembers", evt.getEventId());
                evt.setJobschedulerId(jobSchedulerId);
                evt.setEventId(eventId);
                evt.getEventSnapshots().addAll(updateSavedInventoryInstance());
            } else {
                shiroSession.setAttribute(jobSchedulerId + "#eventIdOfClusterMembers", eventId);
            }
            LOGGER.debug("EventIdOfCluster: "+shiroSession.getAttribute(jobSchedulerId + "#eventIdOfClusterMembers"));
            return evt;
        } catch (ExecutionException | InterruptedException e) {
            if (e.getCause() instanceof JocException) {
                throw (JocException) e.getCause();
            } else {
                throw (Exception) e.getCause();
            }
        } finally {
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
    
    private List<EventSnapshot> updateSavedInventoryInstance() {
        List<EventSnapshot> events = new ArrayList<EventSnapshot>();
        DBItemInventoryInstance curInstance = null;
        if (Globals.urlFromJobSchedulerId.containsKey(jobSchedulerId)) {
            curInstance = Globals.urlFromJobSchedulerId.get(jobSchedulerId);
        }
        try {
            if (session == null) {
                session = Globals.createSosHibernateStatelessConnection("eventClusterCallable-" + jobSchedulerId);
            }
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
            Globals.beginTransaction(session);
            DBItemInventoryInstance inst = dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerId, accessToken, curInstance);
            shiroUser.addSchedulerInstanceDBItem(jobSchedulerId, inst);
            Globals.rollback(session);
            Globals.urlFromJobSchedulerId.put(jobSchedulerId, inst);
            if (isCurrent && inst != null && !inst.equals(curInstance)) {
                EventSnapshot masterChangedEventSnapshot = new EventSnapshot();
                masterChangedEventSnapshot.setEventType("CurrentJobSchedulerChanged");
                masterChangedEventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                masterChangedEventSnapshot.setPath(inst.getUrl());
                events.add(masterChangedEventSnapshot);
            }
        } catch (Exception e) {
        } finally {
            Globals.disconnect(session);
            session = null;
        }
        return events;
    }

}
