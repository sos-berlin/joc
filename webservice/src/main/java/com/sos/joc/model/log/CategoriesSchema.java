
package com.sos.joc.model.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * log categories object with category collection
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "currentCategories",
    "resetAt",
    "categories"
})
public class CategoriesSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("currentCategories")
    private String currentCategories;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("resetAt")
    private Date resetAt;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    private List<Category> categories = new ArrayList<Category>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The currentCategories
     */
    @JsonProperty("currentCategories")
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
    @JsonProperty("currentCategories")
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
    @JsonProperty("resetAt")
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
    @JsonProperty("resetAt")
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
    @JsonProperty("categories")
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
    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(currentCategories).append(resetAt).append(categories).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(currentCategories, rhs.currentCategories).append(resetAt, rhs.resetAt).append(categories, rhs.categories).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
