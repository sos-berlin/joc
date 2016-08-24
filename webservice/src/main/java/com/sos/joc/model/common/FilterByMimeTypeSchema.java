
package com.sos.joc.model.common;

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
 * mimeTypeParameter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "mime"
})
public class FilterByMimeTypeSchema {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("mime")
    private FilterByMimeTypeSchema.Mime mime = FilterByMimeTypeSchema.Mime.fromValue("plain");
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

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
     *     The mime
     */
    @JsonProperty("mime")
    public FilterByMimeTypeSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    @JsonProperty("mime")
    public void setMime(FilterByMimeTypeSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, java.lang.Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(mime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FilterByMimeTypeSchema) == false) {
            return false;
        }
        FilterByMimeTypeSchema rhs = ((FilterByMimeTypeSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(mime, rhs.mime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        plain("plain"),
        html("html");
        private final String value;
        private final static Map<String, FilterByMimeTypeSchema.Mime> CONSTANTS = new HashMap<String, FilterByMimeTypeSchema.Mime>();

        static {
            for (FilterByMimeTypeSchema.Mime c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mime(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static FilterByMimeTypeSchema.Mime fromValue(String value) {
            FilterByMimeTypeSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
