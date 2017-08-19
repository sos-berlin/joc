
package com.sos.joc.model.yade;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * yade file
 * <p>
 * compact=true -> required fields + possibly targetPath
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "id",
    "transferId",
    "interventionTransferId",
    "state",
    "integrityHash",
    "modificationDate",
    "size",
    "error",
    "sourcePath",
    "targetPath"
})
public class TransferFile {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("transferId")
    private Long transferId;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("interventionTransferId")
    private Long interventionTransferId;
    /**
     * state for each transferred file
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private FileTransferState state;
    @JsonProperty("integrityHash")
    private String integrityHash;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("modificationDate")
    private Date modificationDate;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("size")
    private Long size;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("sourcePath")
    private String sourcePath;
    @JsonProperty("targetPath")
    private String targetPath;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * non negative long
     * <p>
     * 
     * (Required)
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
     * (Required)
     * 
     * @param transferId
     *     The transferId
     */
    @JsonProperty("transferId")
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The interventionTransferId
     */
    @JsonProperty("interventionTransferId")
    public Long getInterventionTransferId() {
        return interventionTransferId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param interventionTransferId
     *     The interventionTransferId
     */
    @JsonProperty("interventionTransferId")
    public void setInterventionTransferId(Long interventionTransferId) {
        this.interventionTransferId = interventionTransferId;
    }

    /**
     * state for each transferred file
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public FileTransferState getState() {
        return state;
    }

    /**
     * state for each transferred file
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(FileTransferState state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The integrityHash
     */
    @JsonProperty("integrityHash")
    public String getIntegrityHash() {
        return integrityHash;
    }

    /**
     * 
     * @param integrityHash
     *     The integrityHash
     */
    @JsonProperty("integrityHash")
    public void setIntegrityHash(String integrityHash) {
        this.integrityHash = integrityHash;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The modificationDate
     */
    @JsonProperty("modificationDate")
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param modificationDate
     *     The modificationDate
     */
    @JsonProperty("modificationDate")
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The size
     */
    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param size
     *     The size
     */
    @JsonProperty("size")
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public Err getError() {
        return error;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @param error
     *     The error
     */
    @JsonProperty("error")
    public void setError(Err error) {
        this.error = error;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The sourcePath
     */
    @JsonProperty("sourcePath")
    public String getSourcePath() {
        return sourcePath;
    }

    /**
     * 
     * (Required)
     * 
     * @param sourcePath
     *     The sourcePath
     */
    @JsonProperty("sourcePath")
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * 
     * @return
     *     The targetPath
     */
    @JsonProperty("targetPath")
    public String getTargetPath() {
        return targetPath;
    }

    /**
     * 
     * @param targetPath
     *     The targetPath
     */
    @JsonProperty("targetPath")
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(id).append(transferId).append(interventionTransferId).append(state).append(integrityHash).append(modificationDate).append(size).append(error).append(sourcePath).append(targetPath).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransferFile) == false) {
            return false;
        }
        TransferFile rhs = ((TransferFile) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(id, rhs.id).append(transferId, rhs.transferId).append(interventionTransferId, rhs.interventionTransferId).append(state, rhs.state).append(integrityHash, rhs.integrityHash).append(modificationDate, rhs.modificationDate).append(size, rhs.size).append(error, rhs.error).append(sourcePath, rhs.sourcePath).append(targetPath, rhs.targetPath).isEquals();
    }

}
