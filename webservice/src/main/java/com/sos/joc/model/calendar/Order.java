
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * run time in calendar
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "path",
    "orderId",
    "state",
    "holiday",
    "startTime",
    "endTime"
})
public class Order {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private Pattern path;
    /**
     * order id
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("state")
    private Order.State state;
    @JsonProperty("holiday")
    private Boolean holiday = false;
    /**
     * estimated time
     * <p>
     * 
     * 
     */
    @JsonProperty("startTime")
    private StartTime startTime;
    /**
     * estimated time
     * <p>
     * 
     * 
     */
    @JsonProperty("endTime")
    private StartTime endTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public Pattern getPath() {
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
    @JsonProperty("path")
    public void setPath(Pattern path) {
        this.path = path;
    }

    /**
     * order id
     * 
     * @return
     *     The orderId
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * order id
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public Order.State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(Order.State state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The holiday
     */
    @JsonProperty("holiday")
    public Boolean getHoliday() {
        return holiday;
    }

    /**
     * 
     * @param holiday
     *     The holiday
     */
    @JsonProperty("holiday")
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
    @JsonProperty("startTime")
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
    @JsonProperty("startTime")
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
    @JsonProperty("endTime")
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
    @JsonProperty("endTime")
    public void setEndTime(StartTime endTime) {
        this.endTime = endTime;
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
        return new HashCodeBuilder().append(path).append(orderId).append(state).append(holiday).append(startTime).append(endTime).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(state, rhs.state).append(holiday, rhs.holiday).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum State {

        PENDING("pending"),
        RUNNING("running"),
        SUSPENDED("suspended"),
        SETBACK("setback"),
        WAITING_FOR_RESSOURCE("waitingForRessource"),
        BLACKLIST("blacklist");
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

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
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
