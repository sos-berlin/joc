package com.sos.joc.classes.jobscheduler;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.UnknownJobSchedulerAgentException;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;

public class AgentVCallable implements Callable<AgentV> {

    public static final String AGENT_API_PATH_FORMAT = "/jobscheduler/master/api/agent/%1$s/jobscheduler/agent/api";
    private final String masterUrl;
    private final String agentUrl;
    private final String accessToken;

    public AgentVCallable(String agentUrl, String masterUrl, String accessToken) {
        this.agentUrl = agentUrl;
        this.masterUrl = masterUrl;
        this.accessToken = accessToken;
    }

    @Override
    public AgentV call() throws Exception {
        // "/jobscheduler/master/api/agent/http://[agent-host]:{agent-port]/jobscheduler/agent/api"
        String jsonPath = String.format(AGENT_API_PATH_FORMAT, agentUrl);
        return getAgentV(agentUrl, masterUrl, accessToken, jsonPath);
    }

    public AgentV getAgentV(String agentUrl, String masterUrl, String accessToken, String jsonPath) throws JocException {
        JOCJsonCommand jocJsonCommand = new JOCJsonCommand(masterUrl, jsonPath);
        AgentV agent = new AgentV();
        agent.setSurveyDate(Date.from(Instant.now()));
        agent.setUrl(agentUrl);
        JobSchedulerState state = new JobSchedulerState();
        try {
            JsonObject json = jocJsonCommand.getJsonObjectFromGet(accessToken);
            agent.setRunningTasks(json.getInt("currentTaskCount", 0));
            agent.setStartedAt(JobSchedulerDate.getDateFromISO8601String(json.getString("startedAt")));
            if (json.getBoolean("isTerminating", false)) {
                state.set_text(JobSchedulerStateText.TERMINATING);
                state.setSeverity(3);
            } else {
                state.set_text(JobSchedulerStateText.RUNNING);
                state.setSeverity(0);
            }
//        } catch (JobSchedulerBadRequestException e) {
//            state.set_text(JobSchedulerStateText.UNREACHABLE);
//            state.setSeverity(2);
//        } catch (UnknownJobSchedulerAgentException e) {
//            e.setErrorMessage(agentUrl);
//            throw e;
        } catch (JocException e) {
            state.set_text(JobSchedulerStateText.UNREACHABLE);
            state.setSeverity(2);
        }
        agent.setState(state);
        return agent;
    }
}
