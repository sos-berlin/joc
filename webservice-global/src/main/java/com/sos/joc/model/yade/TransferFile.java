
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
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Integer id;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("transferId")
    private Integer transferId;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("interventionTransferId")
    private Integer interventionTransferId;
    /**
     * state for each transferred file
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private FileTransferState state;
    @JsonProperty("integrityHash")
    private String integrityHash;
    @JsonProperty("modificationDate")
    private Date modificationDate;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("size")
    private Integer size;
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
     * (Required)
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
     * (Required)
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The transferId
     */
    @JsonProperty("transferId")
    public Integer getTransferId() {
        return transferId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param transferId
     *     The transferId
     */
    @JsonProperty("transferId")
    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The interventionTransferId
     */
    @JsonProperty("interventionTransferId")
    public Integer getInterventionTransferId() {
        return interventionTransferId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param interventionTransferId
     *     The interventionTransferId
     */
    @JsonProperty("interventionTransferId")
    public void setInterventionTransferId(Integer interventionTransferId) {
        this.interventionTransferId = interventionTransferId;
    }

    /**
     * state for each transferred file
     * <p>
     * 
     * (Required)
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
     * (Required)
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
     * 
     * @return
     *     The modificationDate
     */
    @JsonProperty("modificationDate")
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * 
     * @param modificationDate
     *     The modificationDate
     */
    @JsonProperty("modificationDate")
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The size
     */
    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param size
     *     The size
     */
    @JsonProperty("size")
    public void setSize(Integer size) {
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
