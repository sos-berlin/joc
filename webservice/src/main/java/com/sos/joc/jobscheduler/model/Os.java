
package com.sos.joc.jobscheduler.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "architecture",
    "distribution"
})
public class Os {

    /**
     * Windows, Linux, AIX, Solaris, other
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("architecture")
    private Os.Architecture architecture;
    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * 
     */
    @JsonProperty("distribution")
    private String distribution;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Windows, Linux, AIX, Solaris, other
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Windows, Linux, AIX, Solaris, other
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Os withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The architecture
     */
    @JsonProperty("architecture")
    public Os.Architecture getArchitecture() {
        return architecture;
    }

    /**
     * 
     * @param architecture
     *     The architecture
     */
    @JsonProperty("architecture")
    public void setArchitecture(Os.Architecture architecture) {
        this.architecture = architecture;
    }

    public Os withArchitecture(Os.Architecture architecture) {
        this.architecture = architecture;
        return this;
    }

    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * 
     * @return
     *     The distribution
     */
    @JsonProperty("distribution")
    public String getDistribution() {
        return distribution;
    }

    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * 
     * @param distribution
     *     The distribution
     */
    @JsonProperty("distribution")
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public Os withDistribution(String distribution) {
        this.distribution = distribution;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Os withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Architecture {

        _32("32"),
        _64("64");
        private final String value;
        private static Map<String, Os.Architecture> constants = new HashMap<String, Os.Architecture>();

        static {
            for (Os.Architecture c: values()) {
                constants.put(c.value, c);
            }
        }

        private Architecture(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Os.Architecture fromValue(String value) {
            Os.Architecture constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
