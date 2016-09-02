
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * mimeTypeParameter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class FilterByMimeTypeSchema {

    private String jobschedulerId;
    private FilterByMimeTypeSchema.Mime mime = FilterByMimeTypeSchema.Mime.fromValue("PLAIN");

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The mime
     */
    public FilterByMimeTypeSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    public void setMime(FilterByMimeTypeSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(mime).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        PLAIN("PLAIN"),
        HTML("HTML");
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

        @Override
        public String toString() {
            return this.value;
        }

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
