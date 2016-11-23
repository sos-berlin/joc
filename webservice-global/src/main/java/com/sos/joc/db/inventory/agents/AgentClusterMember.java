package com.sos.joc.db.inventory.agents;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.OperatingSystem;

public class AgentClusterMember extends AgentOfCluster {

    @JsonIgnore
    private Long agentClusterId;
    
    public AgentClusterMember(Long agentClusterId, String url, Date surveyDate, String version, Integer state, Date startedAt, String hostname, String osName,
            String osArchitecture, String osDistribution) {
        this.agentClusterId = agentClusterId;
        JobSchedulerState jsState = new JobSchedulerState();
        if (state != null && state == 0) {
            setStartedAt(startedAt);
            jsState.setSeverity(0);
            jsState.set_text(JobSchedulerStateText.RUNNING);
        } else {
            setStartedAt(null);
            jsState.setSeverity(2);
            jsState.set_text(JobSchedulerStateText.UNREACHABLE);
        }
        setState(jsState);
        if (osName != null) {
            OperatingSystem os = new OperatingSystem();
            os.setArchitecture(osArchitecture);
            os.setDistribution(osDistribution);
            os.setName(osName);
            setOs(os);
        } else {
            setOs(null);
        }
        setHost(hostname);
        setVersion(version);
        setSurveyDate(surveyDate);
        setUrl(url);
    }

    @JsonIgnore
    public Long getAgentClusterId() {
        return agentClusterId;
    }
}
