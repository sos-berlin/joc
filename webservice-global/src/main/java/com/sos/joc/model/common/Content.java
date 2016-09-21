
package com.sos.joc.model.common;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * configuration content
 * <p>
 * A parameter can specify if the content is xml or html. Either 'xml' or 'html' is required
 * 
 */
@Generated("org.jsonschema2pojo")
public class Content {

    private String xml;
    private String html;

    /**
     * 
     * @return
     *     The xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * 
     * @param xml
     *     The xml
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * 
     * @return
     *     The html
     */
    public String getHtml() {
        return html;
    }

    /**
     * 
     * @param html
     *     The html
     */
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
        if ((other instanceof Content) == false) {
            return false;
        }
        Content rhs = ((Content) other);
        return new EqualsBuilder().append(xml, rhs.xml).append(html, rhs.html).isEquals();
    }

}
