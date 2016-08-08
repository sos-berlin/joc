package com.sos.joc.jobscheduler.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSchedulerAgentClustersBody {

    private String jobschedulerId;
    private List<JobSchedulerAgentCluster> agentClusters = new ArrayList<JobSchedulerAgentCluster>();

    private String regex;
    private JobSchedulerAgentClustersBody.State state;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public List<JobSchedulerAgentCluster> getAgentClusters() {
        return agentClusters;
    }

    public void setAgentClusters(List<JobSchedulerAgentCluster> agentClusters) {
        this.agentClusters = agentClusters;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public JobSchedulerAgentClustersBody.State getState() {
        return state;
    }

    public void setState(JobSchedulerAgentClustersBody.State state) {
        this.state = state;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public enum State {

        _0("0"), _1("1"), _2("2");
        private final String value;
        private final static Map<String, JobSchedulerAgentClustersBody.State> CONSTANTS = new HashMap<String, JobSchedulerAgentClustersBody.State>();

        static {
            for (JobSchedulerAgentClustersBody.State c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static JobSchedulerAgentClustersBody.State fromValue(String value) {
            JobSchedulerAgentClustersBody.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
