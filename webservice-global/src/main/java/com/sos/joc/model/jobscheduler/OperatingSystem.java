
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
public class OperatingSystem {

    /**
     * Windows, Linux, AIX, Solaris, other
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("architecture")
    private String architecture;
    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * (Required)
     * 
     */
    @JsonProperty("distribution")
    private String distribution;

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
     * (Required)
     * 
     * @return
     *     The architecture
     */
    @JsonProperty("architecture")
    public String getArchitecture() {
        return architecture;
    }

    /**
     * 
     * (Required)
     * 
     * @param architecture
     *     The architecture
     */
    @JsonProperty("architecture")
    public void setArchitecture(String architecture) {
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(architecture).append(distribution).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OperatingSystem) == false) {
            return false;
        }
        OperatingSystem rhs = ((OperatingSystem) other);
        return new EqualsBuilder().append(name, rhs.name).append(architecture, rhs.architecture).append(distribution, rhs.distribution).isEquals();
    }

}
