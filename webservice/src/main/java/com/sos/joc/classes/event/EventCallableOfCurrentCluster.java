package com.sos.joc.classes.event;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallableOfCurrentCluster extends EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallableOfCurrentCluster.class);
    private List<EventCallable> tasksOfClustermember = null;
    private List<JOCJsonCommand> jocJsonCommands = null;
    private String jobSchedulerId = null;
    private String eventId = null;
    private Session session = null;
    
    public EventCallableOfCurrentCluster(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session,
            Long instanceId, SOSShiroCurrentUser shiroUser, List<EventCallable> tasksOfClustermember, List<JOCJsonCommand> jocJsonCommands, Map<String, Set<String>> nestedJobChains) {
        super();
        tasksOfClustermember.add(0, new EventCallableOfCurrentJobScheduler(command, jobSchedulerEvent, accessToken, session, instanceId, shiroUser, nestedJobChains));
        this.tasksOfClustermember = tasksOfClustermember;
        this.jocJsonCommands = jocJsonCommands;
        this.jobSchedulerId = jobSchedulerEvent.getJobschedulerId();
        this.eventId = jobSchedulerEvent.getEventId();
        this.session = session;
    }

    @Override
    public JobSchedulerEvent call() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(tasksOfClustermember.size());
        try {
            JobSchedulerEvent evt = executorService.invokeAny(tasksOfClustermember);
            LOGGER.debug("EventOfCluster: " + evt.getJobschedulerId() + "," + evt.getEventId() );
            if (evt.getJobschedulerId().startsWith("__instance__")) {
                session.setAttribute("eventIdOfClusterMembers", evt.getEventId());
                evt.setJobschedulerId(jobSchedulerId);
                evt.setEventId(eventId);
            } else {
                session.setAttribute("eventIdOfClusterMembers", eventId);
            }
            LOGGER.debug("EventIdOfCluster: "+session.getAttribute("eventIdOfClusterMembers"));
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
        }
    }

}
