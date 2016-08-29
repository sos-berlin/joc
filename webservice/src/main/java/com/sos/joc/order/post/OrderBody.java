package com.sos.joc.order.post;

import java.util.HashMap;
import java.util.Map;

public class OrderBody {

    private String jobschedulerId;
    private String jobChain;
    private String orderId;
    private Integer historyId;
    private OrderBody.Mime mime = OrderBody.Mime.fromValue("plain");
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public OrderBody.Mime getMime() {
        return mime;
    }

    public void setMime(OrderBody.Mime mime) {
        this.mime = mime;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public enum Mime {

        plain("plain"), html("html");
        private final String value;
        private final static Map<String, OrderBody.Mime> CONSTANTS = new HashMap<String, OrderBody.Mime>();

        static {
            for (OrderBody.Mime c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mime(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static OrderBody.Mime fromValue(String value) {
            OrderBody.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
