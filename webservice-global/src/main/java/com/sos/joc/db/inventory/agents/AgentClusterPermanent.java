package com.sos.joc.db.inventory.agents;

import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.AgentCluster;
import com.sos.joc.model.jobscheduler.AgentClusterP;
import com.sos.joc.model.jobscheduler.AgentClusterType;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;

public class AgentClusterPermanent extends AgentCluster {

    @JsonIgnore
    private Long agentClusterId;

    public AgentClusterPermanent(Long agentClusterId, String schedulingType, Integer numberOfAgents, Date surveyDate, String path,
            Integer maxProcesses) {
        this.agentClusterId = agentClusterId;
        NumOfAgentsInCluster numOfAgents = new NumOfAgentsInCluster();
        numOfAgents.setAny(numberOfAgents);
        setNumOfAgents(numOfAgents);
        setSurveyDate(surveyDate);
        setPath(path);
        setName(Paths.get(path).getFileName().toString());
        setMaxProcesses(maxProcesses);
        set_type(getType(schedulingType));
    }

    @JsonIgnore
    public Long getAgentClusterId() {
        return agentClusterId;
    }

    private AgentClusterType getType(String schedulingType) {
        switch (schedulingType) {
        case "single":
            return AgentClusterType.SINGLE_AGENT;
        case "first":
            return AgentClusterType.FIX_PRIORITY;
        default:
            return AgentClusterType.ROUND_ROBIN;
        }
    }
}
