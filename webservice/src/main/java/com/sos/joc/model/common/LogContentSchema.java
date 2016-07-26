
package com.sos.joc.model.common;

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
 * log content
 * <p>
 * The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "plain",
    "html"
})
public class LogContentSchema {

    @JsonProperty("plain")
    private String plain;
    @JsonProperty("html")
    private String html;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     * 
     * @return
     *     The plain
     */
    @JsonProperty("plain")
    public String getPlain() {
        return plain;
    }

    /**
     * 
     * @param plain
     *     The plain
     */
    @JsonProperty("plain")
    public void setPlain(String plain) {
        this.plain = plain;
    }

    /**
     * 
     * @return
     *     The html
     */
    @JsonProperty("html")
    public String getHtml() {
        return html;
    }

    /**
     * 
     * @param html
     *     The html
     */
    @JsonProperty("html")
    public void setHtml(String html) {
        this.html = html;
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
        return new HashCodeBuilder().append(plain).append(html).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LogContentSchema) == false) {
            return false;
        }
        LogContentSchema rhs = ((LogContentSchema) other);
        return new EqualsBuilder().append(plain, rhs.plain).append(html, rhs.html).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
