
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler platform
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Os {

    /**
     * Windows, Linux, AIX, Solaris, other
     * (Required)
     * 
     */
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    private String architecture;
    /**
     * e.g. Windows 2012, CentOS Linux release 7.2.1511 (Core)
     * (Required)
     * 
     */
    private String distribution;

    /**
     * Windows, Linux, AIX, Solaris, other
     * (Required)
     * 
     * @return
     *     The name
     */
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
        if ((other instanceof Os) == false) {
            return false;
        }
        Os rhs = ((Os) other);
        return new EqualsBuilder().append(name, rhs.name).append(architecture, rhs.architecture).append(distribution, rhs.distribution).isEquals();
    }

}
