
package com.sos.joc.model.log;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class SetCategoriesSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String category;
    /**
     * 
     * (Required)
     * 
     */
    private Boolean active;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * 
     * (Required)
     * 
     * @param category
     *     The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * (Required)
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(category).append(active).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SetCategoriesSchema) == false) {
            return false;
        }
        SetCategoriesSchema rhs = ((SetCategoriesSchema) other);
        return new EqualsBuilder().append(category, rhs.category).append(active, rhs.active).isEquals();
    }

}
