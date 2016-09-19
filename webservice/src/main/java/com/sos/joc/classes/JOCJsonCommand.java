package com.sos.joc.classes;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.ProcessingState;
import com.sos.joc.model.order.Type;

public class JOCJsonCommand {
    
    private UriBuilder uriBuilder;
    
    public JOCJsonCommand() {
    }
    
    public JOCJsonCommand(String url) {
        setUriBuilder(url);
    }
    
    public void setUriBuilder(String url) {
        StringBuilder s = new StringBuilder();
        s.append(url).append(WebserviceConstants.ORDER_API_PATH);
        uriBuilder = UriBuilder.fromPath(s.toString());
    }
    
    public void setUriBuilder(UriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }
    
    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }
    
    public URI getURI() {
        return uriBuilder.build();
    }
    
    public void addCompactQuery(boolean compact) {
        String returnQuery = (compact) ? WebserviceConstants.ORDER_OVERVIEW : WebserviceConstants.ORDER_DETAILED;
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addOrderProcessingStateAndTypeQuery(OrdersFilterSchema ordersBody) {
        List<ProcessingState> states = ordersBody.getProcessingState();
        Map<String, Boolean> filterValues = new HashMap<String, Boolean>();
        if (states != null && !states.isEmpty()) {
            for (ProcessingState state : states) {
                switch (state) {
                case PENDING:
                    filterValues.put("Planned", true);
                    filterValues.put("NotPlanned", true);
                    break;
                case RUNNING:
                    filterValues.put("InTaskProcess", true);
                    break;
                case SUSPENDED:
                    // TODO not yet implemented!
                    // is now "Pending,WaitingForOther," with obstacle, so I
                    // need a filter after response
                    // filterValues.put("Suspended", true);
                    filterValues.put("Pending", true);
                    filterValues.put("WaitingForOther", true);
                    break;
                case BLACKLIST:
                    filterValues.put("Blacklisted", true);
                    break;
                case SETBACK:
                    filterValues.put("Setback", true);
                    break;
                case WAITINGFORRESOURCE:
                    filterValues.put("Pending", true);
                    filterValues.put("WaitingInTask", true);
                    filterValues.put("WaitingForOther", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            StringBuilder filterValue = new StringBuilder();
            for (String key : filterValues.keySet()) {
                filterValue.append(",").append(key);
            }
            uriBuilder.queryParam("isOrderProcessingState", filterValue.toString().replaceFirst("^,", ""));
        }
        List<Type> types = ordersBody.getType();
        filterValues.clear();
        if (types != null && !types.isEmpty()) {
            for (Type type : types) {
                switch (type) {
                case AD_HOC:
                    filterValues.put("AdHoc", true);
                    break;
                case PERMANENT:
                    filterValues.put("Permanent", true);
                    break;
                case FILE_ORDER:
                    filterValues.put("FileOrder", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            StringBuilder filterValue = new StringBuilder();
            for (String key : filterValues.keySet()) {
                filterValue.append(",").append(key);
            }
            uriBuilder.queryParam("isOrderSourceType", filterValue.toString().replaceFirst("^,", ""));
        }
    }
    

}
