package com.sos.joc.classes.event;

import java.net.URI;
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

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
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

    public EventCallable(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session) {
        this.accessToken = accessToken;
        this.command = command;
        this.jobSchedulerEvent = jobSchedulerEvent;
        this.session = session;
    }

    @Override
    public JobSchedulerEvent call() throws JocException {
        try {
            jobSchedulerEvent.setEventSnapshots(getEventSnapshots(jobSchedulerEvent.getEventId()));
            command.closeHttpClient();
        } catch (ForcedClosingHttpClientException e) {
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            throw e;
        } catch (JobSchedulerNoResponseException | JobSchedulerConnectionRefusedException e) {
            if (Globals.UrlFromJobSchedulerId.containsKey(jobSchedulerEvent.getJobschedulerId())) {
                Globals.UrlFromJobSchedulerId.remove(jobSchedulerEvent.getJobschedulerId());
            }
            //TODO create JobScheduler unreachable event?
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            throw e;
        } catch (JocException e) {
            LOGGER.error("", e);
            jobSchedulerEvent.setEventSnapshots(null);
            handleError(e.getError().getCode(), e.getClass().getSimpleName());
            throw e;
        } catch (InvalidSessionException e) {
            command.closeHttpClient();
            jobSchedulerEvent.setEventSnapshots(null);
            LOGGER.error("", e);
            throw new JocException(e);
        } catch (Exception e) {
            jobSchedulerEvent.setEventSnapshots(null);
            LOGGER.error("", e);
            throw new JocException(e);
        }
        return jobSchedulerEvent;
    }
    
    private void handleError(String code, String simpleName) {
        URI uri = command.getURI();
        Err err = new Err();
        err.setCode(code);
        err.setMessage(simpleName + ": " + uri.getScheme()+"://"+uri.getAuthority());
        jobSchedulerEvent.setError(err);
    }
    
    private String getServiceBody() throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", "/");
        return builder.build().toString();
    }
    
    private JsonObject getJsonObject(String eventId) throws JocException {
        command.replaceEventId(eventId);
        return command.getJsonObjectFromPost(getServiceBody(), accessToken);
    }
    
    private List<EventSnapshot> getEventSnapshots (String eventId) throws JocException {
        getSessionTimeout();
        List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();
        JsonObject json = getJsonObject(eventId);
        Long newEventId = json.getJsonNumber("eventId").longValue();
        String type = json.getString("TYPE", "Empty");
        switch (type) {
        case "Empty":
            eventSnapshots.addAll(getEventSnapshots(newEventId.toString()));
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
                String eventKey = event.getString("key", null);
                eventSnapshot.setEventType(eventType);
                if (eventType.startsWith("File")) {
                    String[] eventKeyParts = eventKey.split(":", 2);
                    eventSnapshot.setPath(eventKeyParts[1]);
                    eventSnapshot.setObjectType(JobSchedulerObjectType.fromValue(eventKeyParts[0].toUpperCase().replaceAll("_", "")));
                } else {
                    eventSnapshot.setPath(eventKey); 
                }
                if (eventType.startsWith("Order")) {
                    eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                    if ("OrderNodeChanged".equals(eventType)) {
                        eventSnapshot.setNodeId(event.getString("nodeId", null));
                        eventSnapshot.setFromNodeId(event.getString("fromNodeId", null));
                    } else if ("OrderStepStarted".equals(eventType)) {
                        eventSnapshot.setNodeId(event.getString("nodeId", null));
                        eventSnapshot.setTaskId(event.getString("taskId", null));
                    } else if ("OrderFinished".equals(eventType)) {
                        //TODO OrderFailed, OrderSuccess
                        eventSnapshot.setNodeId(event.getString("nodeId", null));
                        
                    } else if ("OrderStepEnded".equals(eventType)) {
                        JsonObject nodeTrans = event.getJsonObject("nodeTransition");
                        if (nodeTrans != null) {
                            NodeTransition nodeTransition = new NodeTransition();
                            nodeTransition.setType(NodeTransitionType.fromValue(nodeTrans.getString("TYPE", "SUCCESS").toUpperCase()));
                            nodeTransition.setReturnCode(nodeTrans.getInt("returnCode", 0));
                            eventSnapshot.setNodeTransition(nodeTransition); 
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
            eventSnapshots.addAll(getEventSnapshots(newEventId.toString()));
            break;
        }
        return eventSnapshots;
    }
    
    private long getSessionTimeout() throws InvalidSessionException {
        return session.getTimeout();
    }
}
