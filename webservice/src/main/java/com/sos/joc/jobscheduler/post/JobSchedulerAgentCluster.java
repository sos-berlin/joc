package com.sos.joc.jobscheduler.post;
 
import java.util.HashMap;
import java.util.Map;

public class JobSchedulerAgentCluster {
 
private String agentCluster;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public String getAgentCluster() {
return agentCluster;
}

public void setAgentCluster(String agentCluster) {
this.agentCluster = agentCluster;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
/*
{
"jobschedulerId":"scheduler_joc_cockpit",
"agents":[{"agent":" http://galadriel:4445"
}]
}
*/