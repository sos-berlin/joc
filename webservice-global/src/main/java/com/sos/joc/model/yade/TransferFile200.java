
package com.sos.joc.model.yade;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * yade file
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "file"
})
public class TransferFile200 {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * yade file
     * <p>
     * compact=true -> required fields + possibly targetPath
     * (Required)
     * 
     */
    @JsonProperty("file")
    private TransferFile file;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * yade file
     * <p>
     * compact=true -> required fields + possibly targetPath
     * (Required)
     * 
     * @return
     *     The file
     */
    @JsonProperty("file")
    public TransferFile getFile() {
        return file;
    }

    /**
     * yade file
     * <p>
     * compact=true -> required fields + possibly targetPath
     * (Required)
     * 
     * @param file
     *     The file
     */
    @JsonProperty("file")
    public void setFile(TransferFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(file).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransferFile200) == false) {
            return false;
        }
        TransferFile200 rhs = ((TransferFile200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(file, rhs.file).isEquals();
    }

}
