package com.sos.joc.order.post;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class OrderConfigurationBody {

    private String jobschedulerId;
    private String jobChain;
    private String orderId;
    private OrderConfigurationBody.Mime mime = OrderConfigurationBody.Mime.fromValue("xml");

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getJobChain() {
        return jobChain;
    }

    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderConfigurationBody.Mime getMime() {
        return mime;
    }

    public void setMime(OrderConfigurationBody.Mime mime) {
        this.mime = mime;
    }

    public enum Mime {

        html("html"), xml("xml");
        private final String value;
        private final static Map<String, OrderConfigurationBody.Mime> CONSTANTS = new HashMap<String, OrderConfigurationBody.Mime>();

        static {
            for (OrderConfigurationBody.Mime c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mime(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static OrderConfigurationBody.Mime fromValue(String value) {
            OrderConfigurationBody.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
