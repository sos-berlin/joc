package com.sos.joc.classes;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sos.joc.exceptions.JocMissingRequiredParameterException;

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
    
    public boolean checkRequiredParameter(String paramKey, String paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

}
