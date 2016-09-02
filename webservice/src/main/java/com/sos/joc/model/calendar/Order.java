
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * run time in calendar
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Order {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * order id
     * 
     */
    private String orderId;
    private Order.State state;
    private Boolean holiday = false;
    /**
     * estimated time
     * <p>
     * 
     * 
     */
    private StartTime startTime;
    /**
     * estimated time
     * <p>
     * 
     * 
     */
    private StartTime endTime;

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
     * order id
     * 
     * @return
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * order id
     * 
     * @param orderId
     *     The orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 
     * @return
     *     The state
     */
    public Order.State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(Order.State state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The holiday
     */
    public Boolean getHoliday() {
        return holiday;
    }

    /**
     * 
     * @param holiday
     *     The holiday
     */
    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    /**
     * estimated time
     * <p>
     * 
     * 
     * @return
     *     The startTime
     */
    public StartTime getStartTime() {
        return startTime;
    }

    /**
     * estimated time
     * <p>
     * 
     * 
     * @param startTime
     *     The startTime
     */
    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }

    /**
     * estimated time
     * <p>
     * 
     * 
     * @return
     *     The endTime
     */
    public StartTime getEndTime() {
        return endTime;
    }

    /**
     * estimated time
     * <p>
     * 
     * 
     * @param endTime
     *     The endTime
     */
    public void setEndTime(StartTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(orderId).append(state).append(holiday).append(startTime).append(endTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order) == false) {
            return false;
        }
        Order rhs = ((Order) other);
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(state, rhs.state).append(holiday, rhs.holiday).append(startTime, rhs.startTime).append(endTime, rhs.endTime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum State {

        PENDING("PENDING"),
        RUNNING("RUNNING"),
        SUSPENDED("SUSPENDED"),
        SETBACK("SETBACK"),
        WAITINGFORRESOURCE("WAITINGFORRESOURCE"),
        BLACKLIST("BLACKLIST");
        private final String value;
        private final static Map<String, Order.State> CONSTANTS = new HashMap<String, Order.State>();

        static {
            for (Order.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Order.State fromValue(String value) {
            Order.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
