
package com.sos.joc.model.common;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * folder
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class TreeSchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    private String name;
    private List<TreeSchema> subfolders = new ArrayList<TreeSchema>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

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
     *     The subfolders
     */
    public List<TreeSchema> getSubfolders() {
        return subfolders;
    }

    /**
     * 
     * @param subfolders
     *     The subfolders
     */
    public void setSubfolders(List<TreeSchema> subfolders) {
        this.subfolders = subfolders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(name).append(subfolders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TreeSchema) == false) {
            return false;
        }
        TreeSchema rhs = ((TreeSchema) other);
        return new EqualsBuilder().append(path, rhs.path).append(name, rhs.name).append(subfolders, rhs.subfolders).isEquals();
    }

}
