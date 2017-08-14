
package com.sos.joc.model.yade;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.LogMime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * yade filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "fileId",
    "compact",
    "mime"
})
public class FileFilter {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("fileId")
    private Long fileId;
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    private Boolean compact = false;
    /**
     * log mime filter
     * <p>
     * The log can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    private LogMime mime = LogMime.fromValue("PLAIN");

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The fileId
     */
    @JsonProperty("fileId")
    public Long getFileId() {
        return fileId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param fileId
     *     The fileId
     */
    @JsonProperty("fileId")
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @return
     *     The compact
     */
    @JsonProperty("compact")
    public Boolean getCompact() {
        return compact;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @param compact
     *     The compact
     */
    @JsonProperty("compact")
    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    /**
     * log mime filter
     * <p>
     * The log can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     * @return
     *     The mime
     */
    @JsonProperty("mime")
    public LogMime getMime() {
        return mime;
    }

    /**
     * log mime filter
     * <p>
     * The log can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     * @param mime
     *     The mime
     */
    @JsonProperty("mime")
    public void setMime(LogMime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(fileId).append(compact).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileFilter) == false) {
            return false;
        }
        FileFilter rhs = ((FileFilter) other);
        return new EqualsBuilder().append(fileId, rhs.fileId).append(compact, rhs.compact).append(mime, rhs.mime).isEquals();
    }

}
