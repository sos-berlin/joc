package com.sos.joc.classes.event;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.plugin.FactEventHandler.CustomEventType;
import com.sos.jitl.reporting.plugin.FactEventHandler.CustomEventTypeValue;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;
import com.sos.joc.model.event.NodeTransition;
import com.sos.joc.model.event.NodeTransitionType;

public class EventCallableOfCurrentJobScheduler extends EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallableOfCurrentJobScheduler.class);
    private final String accessToken;
    private final JobSchedulerEvent jobSchedulerEvent;
    private final JOCJsonCommand command;
    private final SOSShiroCurrentUser shiroUser;
    private final Map<String, Set<String>> nestedJobChains;
    private final Long instanceId;
    private final Session session;
    private String eventId = null;
    private SOSHibernateSession connection = null;
    private Set<String> removedObjects = new HashSet<String>();
    private Set<String> jobChainEventsOfJob = new HashSet<String>();
    
    public EventCallableOfCurrentJobScheduler(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session,
            Long instanceId, SOSShiroCurrentUser shiroUser, Map<String, Set<String>> nestedJobChains) {
        super(command, jobSchedulerEvent, accessToken, session, instanceId);
//        this.accessToken = accessToken;
        this.accessToken = accessToken;
        this.command = command;
        this.jobSchedulerEvent = jobSchedulerEvent;
        this.eventId = jobSchedulerEvent.getEventId();
        this.shiroUser = shiroUser;
        this.nestedJobChains = nestedJobChains;
        this.instanceId = instanceId;
        this.session = session;
    }

    @Override
    public JobSchedulerEvent call() throws Exception {
        return super.call();
    }

    private Events updateSavedInventoryInstance() {
        Events events = new Events();
        if (Globals.urlFromJobSchedulerId.containsKey(jobSchedulerEvent.getJobschedulerId())) {
            DBItemInventoryInstance instance = Globals.urlFromJobSchedulerId.get(jobSchedulerEvent.getJobschedulerId());
            if (instance != null && !"standalone".equals(instance.getClusterType())) {

                try {
                    if (connection == null) {
                        connection = Globals.createSosHibernateStatelessConnection("eventCallable-" + jobSchedulerEvent.getJobschedulerId());
                    }
                    InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
                    Globals.beginTransaction(connection);
                    DBItemInventoryInstance inst = dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerEvent.getJobschedulerId(), accessToken);
                    shiroUser.addSchedulerInstanceDBItem(jobSchedulerEvent.getJobschedulerId(), inst);
                    Globals.rollback(connection);
                    Globals.urlFromJobSchedulerId.put(jobSchedulerEvent.getJobschedulerId(), inst);
                    if (!instance.equals(inst)) {
                        EventSnapshot masterChangedEventSnapshot = new EventSnapshot();
                        masterChangedEventSnapshot.setEventType("CurrentJobSchedulerChanged");
                        masterChangedEventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                        masterChangedEventSnapshot.setPath(inst.getUrl());
                        events.put(masterChangedEventSnapshot);
                    }
                } catch (Exception e) {
                } finally {
                    Globals.disconnect(connection);
                    connection = null;
                }
            }
        }
        return events;
    }

    private EventSnapshot createJobChainEventOfOrder(EventSnapshot orderEventSnapshot) {
        String[] pathParts = orderEventSnapshot.getPath().split(",", 2);
        String jobChain = pathParts[0];
        EventSnapshot jobChainEventSnapshot = new EventSnapshot();
        jobChainEventSnapshot.setEventType("JobChainStateChanged");
        jobChainEventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
        jobChainEventSnapshot.setPath(jobChain);
        return jobChainEventSnapshot;
    }
    
    private Events createJobChainEventsOfJob(String job) {
        InventoryJobChainsDBLayer jobChainsLayer = null;
        List<String> jobChains = null;
        Events events = new Events();
        if (jobChainEventsOfJob.contains(job)) {
            return events;
        }
        jobChainEventsOfJob.add(job);
        try {
            if (job != null && instanceId != null) {
                if (connection == null) {
                    connection = Globals.createSosHibernateStatelessConnection("eventCallable-"+jobSchedulerEvent.getJobschedulerId());
                }
                jobChainsLayer = new InventoryJobChainsDBLayer(connection);
                if (jobChainsLayer!= null) {
                    jobChains = jobChainsLayer.getJobChainsOfJob(job, instanceId);
                }
                if (jobChains != null) {
                    for (String jobChain : jobChains) {
                        EventSnapshot jobChainEventSnapshot = new EventSnapshot();
                        jobChainEventSnapshot.setEventType("JobChainStateChanged");
                        jobChainEventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                        jobChainEventSnapshot.setPath(jobChain);
                        events.put(jobChainEventSnapshot);
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            Globals.disconnect(connection);
            connection = null;
        }
        return events;
    }
    
    private Events createEventforJobAndOrderWhichUseSchedule(String path) {
//        Events events = new Events();
//        try {
//            if (connection == null) {
//                connection = Globals.createSosHibernateStatelessConnection("eventCallable-" + jobSchedulerEvent.getJobschedulerId());
//            }
//            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
//            Globals.beginTransaction(connection);
//            DBItemInventoryInstance inst = dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerEvent.getJobschedulerId(), accessToken);
//            shiroUser.addSchedulerInstanceDBItem(jobSchedulerEvent.getJobschedulerId(), inst);
//            Globals.rollback(connection);
//            Globals.urlFromJobSchedulerId.put(jobSchedulerEvent.getJobschedulerId(), inst);
//            if (!instance.equals(inst)) {
//                EventSnapshot masterChangedEventSnapshot = new EventSnapshot();
//                masterChangedEventSnapshot.setEventType("CurrentJobSchedulerChanged");
//                masterChangedEventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
//                masterChangedEventSnapshot.setPath(inst.getUrl());
//                events.put(masterChangedEventSnapshot);
//            }
//        } catch (Exception e) {
//        } finally {
//            Globals.disconnect(connection);
//            connection = null;
//        }
        return null;
    }

    @Override
    protected List<EventSnapshot> getEventSnapshots(String eventId) throws JocException {
        return getEventSnapshotsMap(eventId).values(removedObjects);
    }
    
    private Events nonEmptyEvent(Long newEventId, JsonObject json) {
        Events eventSnapshots = new Events();
        jobSchedulerEvent.setEventId(newEventId.toString());
        for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            EventSnapshot eventNotification = new EventSnapshot();
            
            String eventType = event.getString("TYPE", null);
            eventSnapshot.setEventType(eventType);
            eventNotification.setEventType(eventType);
            
            Long eId = event.getJsonNumber("eventId").longValue();
            eventNotification.setEventId(eId.toString());
            
            
            if (eventType.startsWith("Task")) {
                eventNotification = null;
                eventSnapshot.setEventType("JobStateChanged");
                JsonObject eventKeyO = event.getJsonObject("key");
                String jobPath = eventKeyO.getString("jobPath", null);
                if (jobPath == null) { // || "/scheduler_file_order_sink".equals(jobPath)) {
                    continue;
                }
                eventSnapshot.setPath(jobPath);
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
            } else if (eventType.startsWith("Scheduler")) {
                eventNotification = null;
                eventSnapshot.setEventType("SchedulerStateChanged");
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                //String state = event.getString("state", null);
                eventSnapshot.setPath(command.getSchemeAndAuthority());
                //if (state!= null && (state.contains("stop") || state.contains("waiting"))) {
                    eventSnapshots.putAll(updateSavedInventoryInstance());
                //}
            }  else {
                String eventKey = event.getString("key", null);
                if (eventKey == null) {
                    continue;
                }
                if (eventType.startsWith("File")) {
                    if ("FileBasedRemoved".equals(eventType)) {
                        String[] eventKeyParts = eventKey.split(":", 2);
                        removedObjects.add(eventKeyParts[1] + "." + JobSchedulerObjectType.fromValue(eventKeyParts[0].toUpperCase().replaceAll("_", "")).name());
                    }
                    continue;
                } else if (eventType.equals("VariablesCustomEvent")) {
                    eventNotification = null;
                    JsonObject variables = event.getJsonObject("variables");
                    if (variables == null) {
                        continue;
                    }
                    if (variables.containsKey("InventoryEventUpdateFinished")) {
                        String[] eventKeyParts = eventKey.split(":", 2);
                        eventSnapshot.setPath(eventKeyParts[1]);
                        eventSnapshot.setObjectType(JobSchedulerObjectType.fromValue(eventKeyParts[0].toUpperCase().replaceAll("_", "")));
                        String FileBasedEventType = variables.getString("InventoryEventUpdateFinished", "FileBasedUpdated");
                        switch (FileBasedEventType) {
                        case "FileBasedUpdated":
                            eventSnapshot.setEventType("FileBasedActivated");
                            break;
                        case "FileBasedRemoved":
                            eventSnapshot.setEventType(FileBasedEventType);
                            removedObjects.add(eventSnapshot.getPath() + "." + eventSnapshot.getObjectType().name());
                            break;
                        }
                        if (JobSchedulerObjectType.SCHEDULE == eventSnapshot.getObjectType()) {
                            //JOC-242
                            createEventforJobAndOrderWhichUseSchedule(eventSnapshot.getPath());
                        }
                        
                    } else if (variables.containsKey(CustomEventType.DailyPlanChanged.name())) {
                        eventSnapshot.setEventType(CustomEventType.DailyPlanChanged.name());
                        eventSnapshot.setObjectType(JobSchedulerObjectType.OTHER);
                        eventSnapshot.setPath(eventSnapshot.getEventType());
                        
                    } else if (variables.containsKey(CustomEventType.ReportingChanged.name())) {
                        switch (CustomEventTypeValue.valueOf(variables.getString(CustomEventType.ReportingChanged.name(), null))) {
                        case order: 
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name()+"Order");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                            break;
                        case standalone:
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name()+"Job");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                            break;
                        case order_standalone: 
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name()+"Order");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                            EventSnapshot eventSnapshot2 = new EventSnapshot();
                            eventSnapshot2.setEventType(CustomEventType.ReportingChanged.name()+"Job");
                            eventSnapshot2.setObjectType(JobSchedulerObjectType.JOB);
                            eventSnapshot2.setPath(eventSnapshot2.getEventType());
                            eventSnapshots.put(eventSnapshot2);
                            break;
                        }
                        eventSnapshot.setPath(eventSnapshot.getEventType());
                    } else if ("CalendarUsageUpdated".equals(eventKey)) {
                        continue;
                    } else if (eventKey.startsWith("Calendar")) {
                        eventSnapshot.setEventType(eventKey); //CalendarUpdated, CalendarCreated, CalendarDeleted
                        if (JobSchedulerObjectType.NONWORKINGDAYSCALENDAR.name().equals(variables.getString("objectType", JobSchedulerObjectType.WORKINGDAYSCALENDAR.name()))) {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.NONWORKINGDAYSCALENDAR);
                        } else {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.WORKINGDAYSCALENDAR);
                        }
                        eventSnapshot.setPath(variables.getString("path", null));
                    } else if (eventKey.startsWith("CustomEvent")) {
                        eventSnapshot.setEventType(eventKey); //CustomEventAdded, CustomEventDeleted
                        eventSnapshot.setObjectType(JobSchedulerObjectType.OTHER);
                        eventSnapshot.setPath(eventSnapshot.getEventType());
                    } else {
                        continue;
                    }
                    
                } else {
                    eventSnapshot.setPath(eventKey);
                    eventNotification.setPath(eventKey);
//                    if (eventSnapshots.getEvents().containsKey(eventKey)) {
//                        continue;
//                    }
                    if (eventType.startsWith("JobState") || eventType.equals("JobUnstopped")) {
                        eventSnapshot.setEventType("JobStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                        eventSnapshots.putAll(createJobChainEventsOfJob(eventSnapshot.getPath()));
                        eventNotification.setObjectType(JobSchedulerObjectType.JOB);
                        String jobState = event.getString("state", null);
                        eventNotification.setState(jobState);
                        if (jobState != null && "closed,initialized,loaded".contains(jobState)) {
                            continue; 
                        }
                    } else if (eventType.equals("JobTaskQueueChanged")) {
                        eventNotification = null;
                        eventSnapshot.setEventType("JobStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                    } else if (eventType.startsWith("JobChainNode")) {
                        eventNotification = null;
                        eventSnapshot.setEventType("JobChainStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                    } else if (eventType.startsWith("JobChain")) {
                        eventSnapshot.setEventType("JobChainStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                        eventNotification.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                        String jobChainState = event.getString("state", null);
                        eventNotification.setState(jobChainState);
                        if ("closed,initialized,loaded".contains(jobChainState)) {
                            continue; 
                        }
                    } else if (eventType.startsWith("Order")) {
                        if ("OrderAdded".equals(eventType)) {
                            eventSnapshot.setEventType(eventType);
                        } else {
                            eventSnapshot.setEventType("OrderStateChanged");
                        }
                        eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                        eventSnapshots.put(createJobChainEventOfOrder(eventSnapshot));
                        //add event for outerJobChain if exist
                        String[] eventKeyParts = eventKey.split(",",2);
                        if (nestedJobChains != null && nestedJobChains.containsKey(eventKeyParts[0])) {
                            for (String outerJobChain : nestedJobChains.get(eventKeyParts[0])) {
                                EventSnapshot eventSnapshot2 = new EventSnapshot();
                                eventSnapshot2.setEventType("OrderStateChanged");
                                eventSnapshot2.setObjectType(JobSchedulerObjectType.ORDER);
                                eventSnapshot2.setPath(outerJobChain+","+eventKeyParts[1]);
                                eventSnapshots.put(eventSnapshot2);
                                eventSnapshots.put(createJobChainEventOfOrder(eventSnapshot2));
                            }
                        }
                        eventNotification.setObjectType(JobSchedulerObjectType.ORDER);
                        switch (eventType) {
                        case "OrderAdded":
                            eventNotification.setNodeId(event.getString("nodeId", null));
                            break;
                        case "OrderNodeChanged":
                            eventNotification.setNodeId(event.getString("nodeId", null));
                            eventNotification.setFromNodeId(event.getString("fromNodeId", null));
                            break;
                        case "OrderStepStarted":
                            eventNotification.setNodeId(event.getString("nodeId", null));
                            eventNotification.setTaskId(event.getString("taskId", null));
                            break;
                        case "OrderFinished":
                            String node = event.getString("nodeId", null);
                            eventNotification.setNodeId(node);
                            eventNotification.setState("successful");
                            if (isOrderFinishedWithError(eventKeyParts[0], node)) {
                                eventNotification.setState("failed");
                            }
                        case "OrderStepEnded":
                            JsonObject nodeTrans = event.getJsonObject("nodeTransition");
                            if (nodeTrans != null) {
                                NodeTransition nodeTransition = new NodeTransition();
                                try {
                                    nodeTransition.setType(NodeTransitionType.fromValue(nodeTrans.getString("TYPE", "SUCCESS").toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    //LOGGER.warn("unknown event transition type", e);
                                    nodeTransition.setType(NodeTransitionType.SUCCESS);
                                }
                                nodeTransition.setReturnCode(nodeTrans.getInt("returnCode", 0));
                                eventNotification.setNodeTransition(nodeTransition);
                            }
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (eventNotification != null) {
                eventSnapshots.add(eventNotification);
            }
            if (eventSnapshot != null) {
                eventSnapshots.put(eventSnapshot);
            }
        }
        try {
            json.clear();
        } catch (Exception e) {
        } finally {
            json = null;
        }
        return eventSnapshots;
    }
    
    private Events getEventSnapshotsMap(String eventId) throws JocException {
        Events eventSnapshots = new Events();
        checkTimeout();
        try {
            JsonObject json = getJsonObject(eventId);
            Long newEventId = json.getJsonNumber("eventId").longValue();
            String type = json.getString("TYPE", "Empty");
            switch (type) {
            case "Empty":
                eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString()));
                break;
            case "NonEmpty":
                eventSnapshots.putAll(nonEmptyEvent(newEventId, json));
                if (eventSnapshots.isEmpty()) {
                    eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString())); 
                } else {
                    for (int i=0; i < 6; i++) {
                        if (!(Boolean) session.getAttribute(Globals.SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY)) {
                            try { //collect further events after 2sec to minimize the number of responses 
                                int delay = Math.min(250, new Long(getSessionTimeout()).intValue());
                                if (delay > 0) {
                                    Thread.sleep(delay);
                                }
                            } catch (InterruptedException e1) {
                            } 
                        }
                    }
                    eventSnapshots.putAll(getEventSnapshotsMapFromNextResponse(newEventId.toString()));
                    // add auditLogEvent
                    eventSnapshots.putAll(addAuditLogEvent());
                    try { // a small delay because events comes earlier then the JobScheduler has update its objects in some requests
                        int delay = Math.min(500, new Long(getSessionTimeout()).intValue());
                        if (delay > 0) {
                            Thread.sleep(delay);
                        }
                    } catch (InterruptedException e1) {
                    }
                }
                break;
            case "Torn":
                eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString()));
                break;
            }
        } catch (JobSchedulerNoResponseException | JobSchedulerConnectionRefusedException e) {
            // if current Jobscheduler down then retry after 15sec
            eventSnapshots.putAll(updateSavedInventoryInstance());
            try {
                int delay = Math.min(15000, new Long(getSessionTimeout()).intValue());
                LOGGER.debug(command.getSchemeAndAuthority() + ": connection refused; retry after " + delay + "ms");
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
            }
            eventSnapshots.putAll(getEventSnapshotsMap(eventId));
        } catch (JobSchedulerConnectionResetException e) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            eventSnapshot.setEventType("SchedulerStateChanged");
            eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
            eventSnapshot.setPath(command.getSchemeAndAuthority());
            eventSnapshots.put(eventSnapshot);
            eventSnapshots.putAll(updateSavedInventoryInstance());
        }
        return eventSnapshots;
    }

    private Events getEventSnapshotsMapFromNextResponse(String eventId) throws JocException {
        Events eventSnapshots = new Events();
        JsonObject json = getJsonObject(eventId, 0);
        Long newEventId = json.getJsonNumber("eventId").longValue();
        String type = json.getString("TYPE", "Empty");
        switch (type) {
        case "Empty":
            break;
        case "NonEmpty":
            eventSnapshots.putAll(nonEmptyEvent(newEventId, json));
            break;
        case "Torn":
            break;
        }
        return eventSnapshots;
    }
    
    private Events addAuditLogEvent() {
        Events eventSnapshots = new Events();
        try {
            if (connection == null) {
                connection = Globals.createSosHibernateStatelessConnection("eventCallable-" + jobSchedulerEvent.getJobschedulerId());
            }
            Date from = new Date();
            from.setTime(Long.parseLong(eventId) / 1000);
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            Globals.beginTransaction(connection);
            List<DBItemAuditLog> auditLogs = dbLayer.getAllAuditLogs(jobSchedulerEvent.getJobschedulerId(), null, from, null, null, null);
            Globals.rollback(connection);
            if (auditLogs != null && !auditLogs.isEmpty()) {
                for (DBItemAuditLog auditLogItem : auditLogs) {
                    EventSnapshot auditLogEvent = new EventSnapshot();
                    auditLogEvent.setEventType("AuditLogChanged");
                    if (auditLogItem.getJob() != null && !auditLogItem.getJob().isEmpty()) {
                        auditLogEvent.setObjectType(JobSchedulerObjectType.JOB);
                        auditLogEvent.setPath(auditLogItem.getJob());
                    } else if (auditLogItem.getJobChain() != null && !auditLogItem.getJobChain().isEmpty()) {
                        String path = auditLogItem.getJobChain();
                        auditLogEvent.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                        if (auditLogItem.getOrderId() != null && !auditLogItem.getOrderId().isEmpty()) {
                            path += "," + auditLogItem.getOrderId();
                            auditLogEvent.setObjectType(JobSchedulerObjectType.ORDER);
                        }
                        auditLogEvent.setPath(path);
                    } else if (auditLogItem.getCalendar() != null && !auditLogItem.getCalendar().isEmpty() ) {
                        auditLogEvent.setObjectType(JobSchedulerObjectType.WORKINGDAYSCALENDAR);
                        auditLogEvent.setPath(auditLogItem.getCalendar());
                    }else {
                        auditLogEvent.setObjectType(JobSchedulerObjectType.OTHER);
                        auditLogEvent.setPath("AuditLogChanged");
                    }
                    eventSnapshots.put(auditLogEvent);
                }
                
            }
        } catch (Exception e) {
        } finally {
            Globals.disconnect(connection);
            connection = null;
        }
        return eventSnapshots;
    }
}
