
package com.sos.joc.model.log;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * log category object
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Category {

    /**
     * 
     * (Required)
     * 
     */
    private String name;
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    private Boolean active;
    private Category.Mode mode;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * 
     * @return
     *     The mode
     */
    public Category.Mode getMode() {
        return mode;
    }

    /**
     * 
     * @param mode
     *     The mode
     */
    public void setMode(Category.Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(description).append(active).append(mode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Category) == false) {
            return false;
        }
        Category rhs = ((Category) other);
        return new EqualsBuilder().append(name, rhs.name).append(description, rhs.description).append(active, rhs.active).append(mode, rhs.mode).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mode {

        __EMPTY__(""),
        IMPLICIT("IMPLICIT"),
        EXPLICIT("EXPLICIT");
        private final String value;
        private final static Map<String, Category.Mode> CONSTANTS = new HashMap<String, Category.Mode>();

        static {
            for (Category.Mode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Category.Mode fromValue(String value) {
            Category.Mode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
