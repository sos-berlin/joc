
package com.sos.joc.model.lock;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Holders {

    /**
     * 
     * (Required)
     * 
     */
    private Boolean exclusive;
    /**
     * Collection of tasks which are now using the lock
     * (Required)
     * 
     */
    private List<Task> tasks = new ArrayList<Task>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The exclusive
     */
    public Boolean getExclusive() {
        return exclusive;
    }

    /**
     * 
     * (Required)
     * 
     * @param exclusive
     *     The exclusive
     */
    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    /**
     * Collection of tasks which are now using the lock
     * (Required)
     * 
     * @return
     *     The tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Collection of tasks which are now using the lock
     * (Required)
     * 
     * @param tasks
     *     The tasks
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(exclusive).append(tasks).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Holders) == false) {
            return false;
        }
        Holders rhs = ((Holders) other);
        return new EqualsBuilder().append(exclusive, rhs.exclusive).append(tasks, rhs.tasks).isEquals();
    }

}
