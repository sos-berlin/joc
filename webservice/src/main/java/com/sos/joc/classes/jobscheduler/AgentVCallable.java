package com.sos.joc.classes.jobscheduler;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;

public class AgentVCallable implements Callable<AgentV> {
    private final String masterUrl;
    private final String agentUrl;
    
    public AgentVCallable(String agentUrl, String masterUrl) {
        this.agentUrl = agentUrl;
        this.masterUrl = masterUrl;
    }
    
    @Override
    public AgentV call() throws Exception {
        return getAgentV(agentUrl, masterUrl);
    }
    
    public AgentV getAgentV(String agentUrl, String masterUrl) throws Exception {
        JOCJsonCommand jocJsonCommand = new JOCJsonCommand(masterUrl, WebserviceConstants.AGENTS_API_LIST_PATH + agentUrl + WebserviceConstants.AGENT_API_PATH);
        AgentV agent = new AgentV();
        agent.setSurveyDate(Date.from(Instant.now()));
        agent.setUrl(agentUrl);
        JobSchedulerState state = new JobSchedulerState();
        try {
            JsonObject json = jocJsonCommand.getJsonObjectFromGet();
            agent.setRunningTasks(json.getInt("currentTaskCount", 0));
            agent.setStartedAt(JobSchedulerDate.getDateFromISO8601String(json.getString("startedAt")));
            if (json.getBoolean("isTerminating", false)) {
                state.set_text(JobSchedulerStateText.TERMINATING); 
                state.setSeverity(3);
            } else {
                state.set_text(JobSchedulerStateText.RUNNING); 
                state.setSeverity(0);
            }
        } catch(JobSchedulerBadRequestException e) {
            state.set_text(JobSchedulerStateText.UNREACHABLE); 
            state.setSeverity(2);
        }
        agent.setState(state);
        return agent;
    }
}
