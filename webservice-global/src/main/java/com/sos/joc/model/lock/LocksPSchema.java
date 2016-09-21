
package com.sos.joc.model.lock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.TreeSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * locks (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class LocksPSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<LockPSchema> locks = new ArrayList<LockPSchema>();
    /**
     * 
     * (Required)
     * 
     */
    private List<TreeSchema> folders = new ArrayList<TreeSchema>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
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
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The locks
     */
    public List<LockPSchema> getLocks() {
        return locks;
    }

    /**
     * 
     * (Required)
     * 
     * @param locks
     *     The locks
     */
    public void setLocks(List<LockPSchema> locks) {
        this.locks = locks;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The folders
     */
    public List<TreeSchema> getFolders() {
        return folders;
    }

    /**
     * 
     * (Required)
     * 
     * @param folders
     *     The folders
     */
    public void setFolders(List<TreeSchema> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(locks).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LocksPSchema) == false) {
            return false;
        }
        LocksPSchema rhs = ((LocksPSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(locks, rhs.locks).append(folders, rhs.folders).isEquals();
    }

}
