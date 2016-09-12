
package com.sos.joc.model.processClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.TreeSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * process classes (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ProcessClassesPSchema {

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
    private List<ProcessClassPSchema> processClasses = new ArrayList<ProcessClassPSchema>();
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
     *     The processClasses
     */
    public List<ProcessClassPSchema> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * (Required)
     * 
     * @param processClasses
     *     The processClasses
     */
    public void setProcessClasses(List<ProcessClassPSchema> processClasses) {
        this.processClasses = processClasses;
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
        return new HashCodeBuilder().append(deliveryDate).append(processClasses).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessClassesPSchema) == false) {
            return false;
        }
        ProcessClassesPSchema rhs = ((ProcessClassesPSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(processClasses, rhs.processClasses).append(folders, rhs.folders).isEquals();
    }

}
