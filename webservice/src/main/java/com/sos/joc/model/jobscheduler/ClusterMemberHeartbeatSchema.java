
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * cluster member heartbeat
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ClusterMemberHeartbeatSchema {

    /**
     * 
     * (Required)
     * 
     */
    private Integer count;
    /**
     * 
     * (Required)
     * 
     */
    private ClusterMemberHeartbeatSchema.Quality quality;
    private Date lastDetected;
    private Integer lastDetectedAge;
    private Boolean dead;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * (Required)
     * 
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The quality
     */
    public ClusterMemberHeartbeatSchema.Quality getQuality() {
        return quality;
    }

    /**
     * 
     * (Required)
     * 
     * @param quality
     *     The _quality
     */
    public void setQuality(ClusterMemberHeartbeatSchema.Quality quality) {
        this.quality = quality;
    }

    /**
     * 
     * @return
     *     The lastDetected
     */
    public Date getLastDetected() {
        return lastDetected;
    }

    /**
     * 
     * @param lastDetected
     *     The lastDetected
     */
    public void setLastDetected(Date lastDetected) {
        this.lastDetected = lastDetected;
    }

    /**
     * 
     * @return
     *     The lastDetectedAge
     */
    public Integer getLastDetectedAge() {
        return lastDetectedAge;
    }

    /**
     * 
     * @param lastDetectedAge
     *     The lastDetectedAge
     */
    public void setLastDetectedAge(Integer lastDetectedAge) {
        this.lastDetectedAge = lastDetectedAge;
    }

    /**
     * 
     * @return
     *     The dead
     */
    public Boolean getDead() {
        return dead;
    }

    /**
     * 
     * @param dead
     *     The dead
     */
    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(count).append(quality).append(lastDetected).append(lastDetectedAge).append(dead).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMemberHeartbeatSchema) == false) {
            return false;
        }
        ClusterMemberHeartbeatSchema rhs = ((ClusterMemberHeartbeatSchema) other);
        return new EqualsBuilder().append(count, rhs.count).append(quality, rhs.quality).append(lastDetected, rhs.lastDetected).append(lastDetectedAge, rhs.lastDetectedAge).append(dead, rhs.dead).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Quality {

        GOOD("GOOD"),
        LATE("LATE"),
        STILL_CHECKING("STILL_CHECKING");
        private final String value;
        private final static Map<String, ClusterMemberHeartbeatSchema.Quality> CONSTANTS = new HashMap<String, ClusterMemberHeartbeatSchema.Quality>();

        static {
            for (ClusterMemberHeartbeatSchema.Quality c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Quality(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static ClusterMemberHeartbeatSchema.Quality fromValue(String value) {
            ClusterMemberHeartbeatSchema.Quality constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
