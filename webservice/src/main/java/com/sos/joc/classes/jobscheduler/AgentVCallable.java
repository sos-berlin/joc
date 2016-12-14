package com.sos.joc.classes.jobscheduler;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.OperatingSystem;

public class AgentVCallable implements Callable<AgentOfCluster> {

    public static final String AGENT_API_PATH_FORMAT = "/jobscheduler/master/api/agent/%1$s/jobscheduler/agent/api";
    private final JOCJsonCommand jocJsonCommand;;
    private final String agentUrl;
    private final String accessToken;

    public AgentVCallable(String agentUrl, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.agentUrl = agentUrl;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    @Override
    public AgentOfCluster call() throws Exception {
        // "/jobscheduler/master/api/agent/http://[agent-host]:{agent-port]/jobscheduler/agent/api"
        String jsonPath = String.format(AGENT_API_PATH_FORMAT, agentUrl);
        return getAgentV(agentUrl, jocJsonCommand, accessToken, jsonPath);
    }

    public AgentOfCluster getAgentV(String agentUrl, JOCJsonCommand jocJsonCommand, String accessToken, String jsonPath) throws JocException {
        jocJsonCommand.setUriBuilder(jsonPath);
        jocJsonCommand.setSocketTimeout(1000);
        AgentOfCluster agent = new AgentOfCluster();
        agent.setSurveyDate(Date.from(Instant.now()));
        agent.setUrl(agentUrl);
        JobSchedulerState state = new JobSchedulerState();
        try {
            JsonObject json = jocJsonCommand.getJsonObjectFromGetWithRetry(accessToken);
            agent.setRunningTasks(json.getInt("currentTaskCount", 0));
            agent.setStartedAt(JobSchedulerDate.getDateFromISO8601String(json.getString("startedAt")));
            JsonObject system = json.getJsonObject("system");
            JsonObject java = json.getJsonObject("java");
            OperatingSystem o = new OperatingSystem();
            if (system != null) {
                agent.setHost(system.getString("hostname", null));
                o.setDistribution(system.getString("distribution", null));
            }
            if (java != null) {
                JsonObject os = java.getJsonObject("systemProperties"); 
                if (os != null) {
                    o.setArchitecture(os.getString("os.arch", null));
                    String osName = os.getString("os.name", null);
                    if (osName != null) {
                        o.setName(osName.replaceFirst("(?i).*(windows|linux|aix|solaris).*", "$1"));
                        if (o.getDistribution() == null) {
                            o.setDistribution(osName);
                        }
                    }
                }
            }    
            agent.setOs(o);    
            agent.setVersion(json.getString("version", null));
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
