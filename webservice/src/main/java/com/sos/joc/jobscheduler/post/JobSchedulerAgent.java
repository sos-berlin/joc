package com.sos.joc.jobscheduler.post;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "agent"
})

public class JobSchedulerAgent {

    @JsonProperty("agent")
    private String agent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    @JsonProperty("agent")
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
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