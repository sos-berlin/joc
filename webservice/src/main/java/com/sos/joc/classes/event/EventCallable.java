package com.sos.joc.classes.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;
import com.sos.joc.model.event.NodeTransition;
import com.sos.joc.model.event.NodeTransitionType;

public class EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallable.class);
    private final String accessToken;
    private final JobSchedulerEvent jobSchedulerEvent;
    private final JOCJsonCommand command;
    private final Session session;
    private final Integer eventTimeout;
    private final Long instanceId;

    public EventCallable(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, Long instanceId, String accessToken, Session session, Integer eventTimeout) {
        this.accessToken = accessToken;
        this.command = command;
        this.jobSchedulerEvent = jobSchedulerEvent;
        this.session = session;
        this.eventTimeout = eventTimeout;
        this.instanceId = instanceId;
    }

    @Override
    public JobSchedulerEvent call() throws JocException {
        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection("EventCallable.call");
            try {
                connection.connect();
            } catch (Exception e) {
                throw new DBConnectionRefusedException(e);
            }

            Globals.beginTransaction(connection);
            
            InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(connection);
            jobSchedulerEvent.setEventSnapshots(getEventSnapshots(jobSchedulerEvent.getEventId(), dbLayer));
            Globals.jobSchedulerIsRunning.put(command.getSchemeAndAuthority(), true);
        } catch (ForcedClosingHttpClientException e) {
            LOGGER.debug("Connection force close: " + command.getSchemeAndAuthority());
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            throw e;
        } catch (JobSchedulerNoResponseException | JobSchedulerConnectionRefusedException e) {
            if (Globals.urlFromJobSchedulerId.containsKey(jobSchedulerEvent.getJobschedulerId())) {
                DBItemInventoryInstance instance = Globals.urlFromJobSchedulerId.get(jobSchedulerEvent.getJobschedulerId());
                if (instance != null && !"standalone".equals(instance.getClusterType())) {
                    Globals.urlFromJobSchedulerId.remove(jobSchedulerEvent.getJobschedulerId());
                }
            }
            //TODO create JobScheduler unreachable event?
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            Boolean jobSchedulerIsRunning = Globals.jobSchedulerIsRunning.get(command.getSchemeAndAuthority());
            if (jobSchedulerIsRunning == null || jobSchedulerIsRunning) {
                LOGGER.warn(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            Globals.jobSchedulerIsRunning.put(command.getSchemeAndAuthority(), false);
            throw e;
        } catch (SessionNotExistException e) {
            jobSchedulerEvent.setEventSnapshots(null);
            handleError("JOC-440", e.getClass().getSimpleName());
            throw e;
        } catch (JocException e) {
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            LOGGER.error(e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            jobSchedulerEvent.setEventSnapshots(null);
            handleError("JOC-420", e.getClass().getSimpleName());
            LOGGER.error("", e);
            throw e;
        } finally {
            if (command.getHttpClient() != null) {
                LOGGER.debug("Connection close: " + command.getSchemeAndAuthority());
            }
            command.closeHttpClient();
            Globals.disconnect(connection);
        }
        return jobSchedulerEvent;
    }
    
    private void handleError(String code, String simpleName) {
        Err err = new Err();
        err.setCode(code);
        err.setMessage(simpleName + ": " + command.getSchemeAndAuthority());
        jobSchedulerEvent.setError(err);
    }
    
    private String getServiceBody() throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", "/");
        return builder.build().toString();
    }
    
    private JsonObject getJsonObject(String eventId) throws JocException {
        int timeout = Math.min(eventTimeout, new Long(getSessionTimeout()/1000).intValue());
        command.replaceEventQuery(eventId, timeout);
        return command.getJsonObjectFromPost(getServiceBody(), accessToken);
    }
    
    private List<EventSnapshot> getEventSnapshots (String eventId, InventoryJobChainsDBLayer dbLayer) throws JocException {
        List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();
        JsonObject json = getJsonObject(eventId);
        Long newEventId = json.getJsonNumber("eventId").longValue();
        String type = json.getString("TYPE", "Empty");
        switch (type) {
        case "Empty":
            eventSnapshots.addAll(getEventSnapshots(newEventId.toString(), dbLayer));
            break;
        case "NonEmpty":
//            boolean isOrderStarted = false;
            jobSchedulerEvent.setEventId(newEventId.toString());
            for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
//                if ("OrderStarted".equals(event.getString("TYPE", ""))) {
//                    isOrderStarted = true;
//                    break;
//                }
                EventSnapshot eventSnapshot = new EventSnapshot();
                Long eId = event.getJsonNumber("eventId").longValue();
                eventSnapshot.setEventId(eId.toString());
                String eventType = event.getString("TYPE", null);
                eventSnapshot.setEventType(eventType);
                if (eventType.startsWith("Task")) {
                    JsonObject eventKeyO = event.getJsonObject("key");
                    String jobPath = eventKeyO.getString("jobPath", null);
                    if (jobPath == null || "/scheduler_file_order_sink".equals(jobPath)) {
                        continue;
                    }
                    eventSnapshot.setPath(jobPath);
                    eventSnapshot.setTaskId(eventKeyO.getString("taskId", null));
                    eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                } else {
                    String eventKey = event.getString("key", null);
                    if (eventType.startsWith("File")) {
                        String[] eventKeyParts = eventKey.split(":", 2);
                        eventSnapshot.setPath(eventKeyParts[1]);
                        eventSnapshot.setObjectType(JobSchedulerObjectType.fromValue(eventKeyParts[0].toUpperCase().replaceAll("_", "")));
                    } else {
                        eventSnapshot.setPath(eventKey); 
                        if (eventType.startsWith("JobState")) {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                            eventSnapshot.setState(event.getString("state", null));
                        } else if (eventType.startsWith("JobChainState")) {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                            eventSnapshot.setState(event.getString("state", null));
                        } else if (eventType.startsWith("JobChainNode")) {
                            eventSnapshot.setNodeId(event.getString("nodeId", null));
                            String action = event.getString("action", null);
                            if (action != null) {
                                switch (action) {
                                case "stop":
                                    eventSnapshot.setState("stopped");
                                    break;
                                case "next_state":
                                    eventSnapshot.setState("skipped");
                                    break;
                                case "process":
                                    eventSnapshot.setState("active");
                                    break;
                                }
                            }
                        } else if (eventType.startsWith("Order")) {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                            if ("OrderNodeChanged".equals(eventType)) {
                                eventSnapshot.setNodeId(event.getString("nodeId", null));
                                eventSnapshot.setFromNodeId(event.getString("fromNodeId", null));
                            } else if ("OrderStepStarted".equals(eventType)) {
                                eventSnapshot.setNodeId(event.getString("nodeId", null));
                                eventSnapshot.setTaskId(event.getString("taskId", null));
                            } else if ("OrderFinished".equals(eventType)) {
                                String node = event.getString("nodeId", null);
                                eventSnapshot.setNodeId(node);
                                eventSnapshot.setState("successful");
                                String[] pathParts = eventSnapshot.getPath().split(",", 2);
                                String jobChain = pathParts[0];
                                if (node != null && dbLayer.isErrorNode(jobChain, node, instanceId)) {
                                    eventSnapshot.setState("failed");
                                }
                            } else if ("OrderStepEnded".equals(eventType)) {
                                JsonObject nodeTrans = event.getJsonObject("nodeTransition");
                                if (nodeTrans != null) {
                                    NodeTransition nodeTransition = new NodeTransition();
                                    try {
                                        nodeTransition.setType(NodeTransitionType.fromValue(nodeTrans.getString("TYPE", "SUCCESS").toUpperCase()));
                                    } catch (IllegalArgumentException e) {
                                        LOGGER.warn("unknown event transition type", e);
                                        nodeTransition.setType(NodeTransitionType.SUCCESS);
                                    }
                                    nodeTransition.setReturnCode(nodeTrans.getInt("returnCode", 0));
                                    eventSnapshot.setNodeTransition(nodeTransition); 
                                }
                            }
                        } else if (eventType.startsWith("Scheduler")) {
                            eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                            eventSnapshot.setState(event.getString("state", null));
                            eventSnapshot.setPath(command.getSchemeAndAuthority());
                        }
                    }
                }
                
                eventSnapshots.add(eventSnapshot);
            }
//            if (isOrderStarted) {
//                eventSnapshots.addAll(getEventSnapshots(eventId));
//            }
            break;
        case "Torn":
            eventSnapshots.addAll(getEventSnapshots(newEventId.toString(), dbLayer));
            break;
        }
        return eventSnapshots;
    }
    
    private long getSessionTimeout() throws SessionNotExistException {
        try {
            if (session == null) {
                throw new SessionNotExistException("session is invalid");
            }
            return session.getTimeout();
        } catch (SessionNotExistException e) {
            throw e;
        } catch (InvalidSessionException e) {
            throw new SessionNotExistException(e);
        }
    }
}
