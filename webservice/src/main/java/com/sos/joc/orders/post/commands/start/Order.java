package com.sos.joc.orders.post.commands.start;

import java.util.ArrayList;
import java.util.List;

/**
 * NOTE: orderId is required too except for add order
 *
 */
public class Order {

    private String orderId;
    /**
     * absolute path based on live folder of a JobScheduler object. (Required)
     *
     */
    private String jobChain;
    private String state;
    private String endState;
    /**
     * Field to comment this action which can be logged.
     *
     */
    private String comment;
    /**
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now +
     * SECONDS
     *
     */
    private String at = "now";
    /**
     * only useful when changing order state of suspended orders
     *
     */
    private Boolean resume = false;
    private List<Param> params = new ArrayList<Param>();
    /**
     * A run_time xml is expected which is specified in the
     * <xsd:complexType name='run_time'> element of
     * http://www.sos-berlin.com/schema/scheduler.xsd
     *
     */
    private String runTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getJobChain() {
        return jobChain;
    }

    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndState() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState = endState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public Boolean getResume() {
        return resume;
    }

    public void setResume(Boolean resume) {
        this.resume = resume;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

}