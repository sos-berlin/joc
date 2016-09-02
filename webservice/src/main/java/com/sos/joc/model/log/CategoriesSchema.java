
package com.sos.joc.model.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * log categories object with category collection
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class CategoriesSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String currentCategories;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date resetAt;
    /**
     * 
     * (Required)
     * 
     */
    private List<Category> categories = new ArrayList<Category>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The currentCategories
     */
    public String getCurrentCategories() {
        return currentCategories;
    }

    /**
     * 
     * (Required)
     * 
     * @param currentCategories
     *     The currentCategories
     */
    public void setCurrentCategories(String currentCategories) {
        this.currentCategories = currentCategories;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The resetAt
     */
    public Date getResetAt() {
        return resetAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param resetAt
     *     The resetAt
     */
    public void setResetAt(Date resetAt) {
        this.resetAt = resetAt;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * (Required)
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(currentCategories).append(resetAt).append(categories).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CategoriesSchema) == false) {
            return false;
        }
        CategoriesSchema rhs = ((CategoriesSchema) other);
        return new EqualsBuilder().append(currentCategories, rhs.currentCategories).append(resetAt, rhs.resetAt).append(categories, rhs.categories).isEquals();
    }

}
