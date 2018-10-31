package com.sos.joc.classes.event;

import java.util.List;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import org.apache.shiro.session.Session;

import com.sos.jitl.reporting.plugin.FactEventHandler.CustomEventType;
import com.sos.jitl.reporting.plugin.FactEventHandler.CustomEventTypeValue;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallableActiveJobSchedulerStateChanged extends EventCallablePassiveJobSchedulerStateChanged implements Callable<JobSchedulerEvent> {

    private final List<String> distributedJobChains;
    private final List<String> distributedJobs;

    public EventCallableActiveJobSchedulerStateChanged(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken,
            Session session, Long instanceId, List<String> distributedJobChains, List<String> distributedJobs) {
        super(command, jobSchedulerEvent, accessToken, session, instanceId);
        this.distributedJobChains = distributedJobChains;
        this.distributedJobs = distributedJobs;
    }

    @Override
    public JobSchedulerEvent call() throws Exception {
        return super.call();
    }

    @Override
    protected List<EventSnapshot> getEventSnapshots(String eventId) throws JocException {
        return super.getEventSnapshotsMap(eventId).values();
    }

    @Override
    protected Events nonEmptyEvent(Long newEventId, JsonObject json) {
        Events eventSnapshots = new Events();
        jobSchedulerEvent.setEventId(newEventId.toString());
        for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
            EventSnapshot eventSnapshot = new EventSnapshot();

            String eventType = event.getString("TYPE", null);
            eventSnapshot.setEventType(eventType);

            if (eventType.startsWith("Task")) {
                eventSnapshot.setEventType("JobStateChanged");
                JsonObject eventKeyO = event.getJsonObject("key");
                String jobPath = eventKeyO.getString("jobPath", null);
                if (jobPath == null || !distributedJobs.contains(jobPath)) {
                    continue;
                }
                eventSnapshot.setPath(jobPath);
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
            } else if (eventType.startsWith("Scheduler")) {
                eventSnapshot.setEventType("SchedulerStateChanged");
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                eventSnapshot.setPath(command.getSchemeAndAuthority());
            } else {
                String eventKey = event.getString("key", null);
                if (eventKey == null) {
                    continue;
                }
                if (eventType.equals("VariablesCustomEvent")) {
                    JsonObject variables = event.getJsonObject("variables");
                    if (variables == null) {
                        continue;
                    }
                    if (variables.containsKey(CustomEventType.ReportingChanged.name())) {
                        switch (CustomEventTypeValue.valueOf(variables.getString(CustomEventType.ReportingChanged.name(), null))) {
                        case order:
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name() + "Order");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                            break;
                        case standalone:
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name() + "Job");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                            break;
                        case order_standalone:
                            eventSnapshot.setEventType(CustomEventType.ReportingChanged.name() + "Order");
                            eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                            EventSnapshot eventSnapshot2 = new EventSnapshot();
                            eventSnapshot2.setEventType(CustomEventType.ReportingChanged.name() + "Job");
                            eventSnapshot2.setObjectType(JobSchedulerObjectType.JOB);
                            eventSnapshot2.setPath(eventSnapshot2.getEventType());
                            eventSnapshots.put(eventSnapshot2);
                            break;
                        }
                        eventSnapshot.setPath(eventSnapshot.getEventType());
                    } else if (eventKey.startsWith("YADE")) {
                        if (eventKey.equals("YADETransferFinished")) {
                            eventSnapshot.setEventType("YADETransferUpdated");
                        } else {
                            eventSnapshot.setEventType(eventKey); // YADETransferStarted, YADETransferUpdated, YADEFileStateChanged
                        }
                        eventSnapshot.setObjectType(JobSchedulerObjectType.OTHER);
                        eventSnapshot.setPath(variables.getString("transferId", null));
                    } else {
                        continue;
                    }
                } else {
                    if (eventType.startsWith("Order")) {
                        eventSnapshot.setPath(eventKey);
                        String[] pathParts = eventSnapshot.getPath().split(",", 2);
                        String jobChain = pathParts[0];
                        if (!distributedJobChains.contains(jobChain)) {
                           continue; 
                        }
                        if ("OrderAdded".equals(eventType)) {
                            eventSnapshot.setEventType(eventType);
                        } else {
                            eventSnapshot.setEventType("OrderStateChanged");
                        }
                        eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                        eventSnapshots.put(createJobChainEventOfOrder(jobChain));
                    } else {
                        continue;
                    }
                }
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

    private EventSnapshot createJobChainEventOfOrder(String jobChain) {
        EventSnapshot jobChainEventSnapshot = new EventSnapshot();
        jobChainEventSnapshot.setEventType("JobChainStateChanged");
        jobChainEventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
        jobChainEventSnapshot.setPath(jobChain);
        return jobChainEventSnapshot;
    }
}
