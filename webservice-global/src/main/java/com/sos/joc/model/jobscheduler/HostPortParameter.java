
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * hostPortParam
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "host",
    "port",
    "comment"
})
public class HostPortParameter {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("host")
    private String host;
    /**
     * port
     * <p>
     * 
     * 
     */
    @JsonProperty("port")
    private Integer port;
    /**
     * Field to comment this action which can be logged.
     * 
     */
    @JsonProperty("comment")
    private String comment;

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The host
     */
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    /**
     * 
     * @param host
     *     The host
     */
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * port
     * <p>
     * 
     * 
     * @return
     *     The port
     */
    @JsonProperty("port")
    public Integer getPort() {
        return port;
    }

    /**
     * port
     * <p>
     * 
     * 
     * @param port
     *     The port
     */
    @JsonProperty("port")
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @return
     *     The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @param comment
     *     The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(host).append(port).append(comment).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HostPortParameter) == false) {
            return false;
        }
        HostPortParameter rhs = ((HostPortParameter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(host, rhs.host).append(port, rhs.port).append(comment, rhs.comment).isEquals();
    }

}
