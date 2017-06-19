
package com.sos.joc.model.yade;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.audit.AuditParams;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify tranfers
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "transfers",
    "auditLog"
})
public class ModifyTransfers {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("transfers")
    private List<ModifyTransfer> transfers = new ArrayList<ModifyTransfer>();
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The transfers
     */
    @JsonProperty("transfers")
    public List<ModifyTransfer> getTransfers() {
        return transfers;
    }

    /**
     * 
     * (Required)
     * 
     * @param transfers
     *     The transfers
     */
    @JsonProperty("transfers")
    public void setTransfers(List<ModifyTransfer> transfers) {
        this.transfers = transfers;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @return
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(transfers).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyTransfers) == false) {
            return false;
        }
        ModifyTransfers rhs = ((ModifyTransfers) other);
        return new EqualsBuilder().append(transfers, rhs.transfers).append(auditLog, rhs.auditLog).isEquals();
    }

}
