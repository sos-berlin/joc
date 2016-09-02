
package com.sos.joc.model.processClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * process classes (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ProcessClassesVSchema {

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
    private List<ProcessClassVSchema> processClasses = new ArrayList<ProcessClassVSchema>();

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
    public List<ProcessClassVSchema> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * (Required)
     * 
     * @param processClasses
     *     The processClasses
     */
    public void setProcessClasses(List<ProcessClassVSchema> processClasses) {
        this.processClasses = processClasses;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(processClasses).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessClassesVSchema) == false) {
            return false;
        }
        ProcessClassesVSchema rhs = ((ProcessClassesVSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(processClasses, rhs.processClasses).isEquals();
    }

}
