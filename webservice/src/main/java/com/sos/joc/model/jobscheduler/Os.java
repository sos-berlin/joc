
package com.sos.joc.model.jobscheduler;

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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler platform
 * <p>
 * 
 * 
 */
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
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("architecture")
    private Os.Architecture architecture;
    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * (Required)
     * 
     */
    @JsonProperty("distribution")
    private String distribution;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Windows, Linux, AIX, Solaris, other
     * (Required)
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
     * (Required)
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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

    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * (Required)
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
     * (Required)
     * 
     * @param distribution
     *     The distribution
     */
    @JsonProperty("distribution")
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(architecture).append(distribution).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Os) == false) {
            return false;
        }
        Os rhs = ((Os) other);
        return new EqualsBuilder().append(name, rhs.name).append(architecture, rhs.architecture).append(distribution, rhs.distribution).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Architecture {

        _32("32"),
        _64("64");
        private final String value;
        private final static Map<String, Os.Architecture> CONSTANTS = new HashMap<String, Os.Architecture>();

        static {
            for (Os.Architecture c: values()) {
                CONSTANTS.put(c.value, c);
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
            Os.Architecture constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
