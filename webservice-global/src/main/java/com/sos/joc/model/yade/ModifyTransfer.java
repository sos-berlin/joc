
package com.sos.joc.model.yade;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify transfer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "transferId",
    "fileIds"
})
public class ModifyTransfer {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("transferId")
    private Long transferId;
    @JsonProperty("fileIds")
    private List<Long> fileIds = new ArrayList<Long>();

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The transferId
     */
    @JsonProperty("transferId")
    public Long getTransferId() {
        return transferId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param transferId
     *     The transferId
     */
    @JsonProperty("transferId")
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    /**
     * 
     * @return
     *     The fileIds
     */
    @JsonProperty("fileIds")
    public List<Long> getFileIds() {
        return fileIds;
    }

    /**
     * 
     * @param fileIds
     *     The fileIds
     */
    @JsonProperty("fileIds")
    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(transferId).append(fileIds).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyTransfer) == false) {
            return false;
        }
        ModifyTransfer rhs = ((ModifyTransfer) other);
        return new EqualsBuilder().append(transferId, rhs.transferId).append(fileIds, rhs.fileIds).isEquals();
    }

}
