package com.sos.joc.classes.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.order.OrdersSummary;


public class OrdersSnapshotEvent extends OrdersSummary {
    
    Long eventId;

    @JsonIgnore
    public Long getEventId() {
        return eventId;
    }

    @JsonIgnore
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

}
