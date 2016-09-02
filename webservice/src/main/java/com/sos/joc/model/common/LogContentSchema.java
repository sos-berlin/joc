
package com.sos.joc.model.common;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * log content
 * <p>
 * The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.
 * 
 */
@Generated("org.jsonschema2pojo")
public class LogContentSchema {

    private String plain;
    private String html;

    /**
     * 
     * @return
     *     The plain
     */
    public String getPlain() {
        return plain;
    }

    /**
     * 
     * @param plain
     *     The plain
     */
    public void setPlain(String plain) {
        this.plain = plain;
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
        return new HashCodeBuilder().append(plain).append(html).toHashCode();
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
        return new EqualsBuilder().append(plain, rhs.plain).append(html, rhs.html).isEquals();
    }

}
