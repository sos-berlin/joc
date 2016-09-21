
package com.sos.joc.model.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * plan
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class PlanSchema {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<PlanItem> planItems = new ArrayList<PlanItem>();

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
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
     *     The planItems
     */
    public List<PlanItem> getPlanItems() {
        return planItems;
    }

    /**
     * 
     * (Required)
     * 
     * @param planItems
     *     The planItems
     */
    public void setPlanItems(List<PlanItem> planItems) {
        this.planItems = planItems;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(planItems).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanSchema) == false) {
            return false;
        }
        PlanSchema rhs = ((PlanSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(planItems, rhs.planItems).isEquals();
    }

}
