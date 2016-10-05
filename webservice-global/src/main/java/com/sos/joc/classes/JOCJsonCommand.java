package com.sos.joc.classes;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;


public class JOCJsonCommand {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
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
    
    public void addOrderStatisticsQuery() {
        uriBuilder.queryParam("return", "OrderStatistics");
    }
    
    public JsonObject getJsonObjectFromResponse(URI uri, String postBody) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        LOGGER.info("call " + uri.toString());
        if (postBody != null) {
            LOGGER.info("with POST body: " + postBody); 
        }
        String response = client.executeRestServiceCommand("post", uri.toURL(), postBody);
        int httpReplyCode = client.statusCode();
        String contentType = client.getResponseHeader("Content-Type");
        
        switch (httpReplyCode) {
        case 200:
            if (contentType.contains("application/json")) {
                JsonReader rdr = Json.createReader(new StringReader(response));
                JsonObject json = rdr.readObject();
                LOGGER.debug(json.toString());
                return json;
            } else {
                throw new JobSchedulerInvalidResponseDataException("Unexpected content type '" + contentType + "'. Response: " + response);
            }
        case 400:
            if (contentType.contains("application/json")) {
                JsonReader rdr = Json.createReader(new StringReader(response));
                JsonObject json = rdr.readObject();
                throw new JobSchedulerBadRequestException(json.getString("message", response));
            } else {
                throw new JobSchedulerBadRequestException(response);
            }
        default:
            throw new JobSchedulerBadRequestException(httpReplyCode + " " + client.getHttpResponse().getStatusLine().getReasonPhrase());
        }
    }
}
