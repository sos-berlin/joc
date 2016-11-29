
package com.sos.joc.model.event;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "path",
    "type",
    "recursive"
})
public class JobSchedulerObject {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("type")
    private JobSchedulerObjectType type;
    @JsonProperty("recursive")
    private Boolean recursive = true;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public JobSchedulerObjectType getType() {
        return type;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(JobSchedulerObjectType type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The recursive
     */
    @JsonProperty("recursive")
    public Boolean getRecursive() {
        return recursive;
    }

    /**
     * 
     * @param recursive
     *     The recursive
     */
    @JsonProperty("recursive")
    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(type).append(recursive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerObject) == false) {
            return false;
        }
        JobSchedulerObject rhs = ((JobSchedulerObject) other);
        return new EqualsBuilder().append(path, rhs.path).append(type, rhs.type).append(recursive, rhs.recursive).isEquals();
    }

}
