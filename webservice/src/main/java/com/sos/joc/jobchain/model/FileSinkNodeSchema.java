
package com.sos.joc.jobchain.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fileSink
 * <p>
 * job chain node object of a file sink, 'remove' or 'move' are exclusivly
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "remove",
    "move"
})
public class FileSinkNodeSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("remove")
    private Boolean remove = false;
    /**
     * a directory path is expected
     * 
     */
    @JsonProperty("move")
    private String move;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
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
     * 
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
     *     The remove
     */
    @JsonProperty("remove")
    public Boolean getRemove() {
        return remove;
    }

    /**
     * 
     * @param remove
     *     The remove
     */
    @JsonProperty("remove")
    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    /**
     * a directory path is expected
     * 
     * @return
     *     The move
     */
    @JsonProperty("move")
    public String getMove() {
        return move;
    }

    /**
     * a directory path is expected
     * 
     * @param move
     *     The move
     */
    @JsonProperty("move")
    public void setMove(String move) {
        this.move = move;
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
        return new HashCodeBuilder().append(name).append(remove).append(move).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileSinkNodeSchema) == false) {
            return false;
        }
        FileSinkNodeSchema rhs = ((FileSinkNodeSchema) other);
        return new EqualsBuilder().append(name, rhs.name).append(remove, rhs.remove).append(move, rhs.move).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
