
package com.sos.joc.model.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * configuration content
 * <p>
 * A parameter can specify if the content is xml or html. Either 'xml' or 'html' is required
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "xml",
    "html"
})
public class ConfigurationContent {

    @JsonProperty("xml")
    private String xml;
    @JsonProperty("html")
    private String html;

    /**
     * 
     * @return
     *     The xml
     */
    @JsonProperty("xml")
    public String getXml() {
        return xml;
    }

    /**
     * 
     * @param xml
     *     The xml
     */
    @JsonProperty("xml")
    public void setXml(String xml) {
        this.xml = xml;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(xml).append(html).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigurationContent) == false) {
            return false;
        }
        ConfigurationContent rhs = ((ConfigurationContent) other);
        return new EqualsBuilder().append(xml, rhs.xml).append(html, rhs.html).isEquals();
    }

}
