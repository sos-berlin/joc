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
import com.sos.joc.exceptions.UnknownJobSchedulerAgentException;


public class JOCJsonCommand {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
    private UriBuilder uriBuilder;
    
    public JOCJsonCommand() {
    }
    
    public JOCJsonCommand(String url) {
        setUriBuilder(url);
    }
    
    public JOCJsonCommand(String url, String path) {
        setUriBuilder(url, path);
    }
    
    public void setUriBuilder(String url) {
        setUriBuilder(url, WebserviceConstants.ORDER_API_PATH);
    }
    
    public void setUriBuilder(String url, String path) {
        StringBuilder s = new StringBuilder();
        s.append(url).append(path);
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
    
    public void addOrderCompactQuery(boolean compact) {
        String returnQuery = (compact) ? WebserviceConstants.ORDER_OVERVIEW : WebserviceConstants.ORDER_DETAILED;
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addOrderStatisticsQuery() {
        uriBuilder.queryParam("return", "OrderStatistics");
    }
    
    public JsonObject getJsonObjectFromPost() throws Exception {
        return getJsonObjectFromPost(uriBuilder.build(), null);
    }
    
    public JsonObject getJsonObjectFromPost(String postBody) throws Exception {
        return getJsonObjectFromPost(uriBuilder.build(), postBody);
    }
    
    public JsonObject getJsonObjectFromPost(URI uri, String postBody) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        LOGGER.info("call " + uri.toString());
        if (postBody != null) {
            LOGGER.info("with POST body: " + postBody); 
        }
        String response = client.executeRestServiceCommand("post", uri.toURL(), postBody);
        return getJsonObjectFromResponse(response, client);
    }
    
    public JsonObject getJsonObjectFromGet() throws Exception {
        return getJsonObjectFromGet(uriBuilder.build());
    }
    
    public JsonObject getJsonObjectFromGet(URI uri) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Accept", "application/json");
        LOGGER.info("call " + uri.toString());
        String response = client.executeRestServiceCommand("get", uri.toURL());
        return getJsonObjectFromResponse(response, client);
    }
    
    private JsonObject getJsonObjectFromResponse(String response, JobSchedulerRestApiClient client) throws Exception {
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
                if ("Unknown Agent".equalsIgnoreCase(response)) {
                    throw new UnknownJobSchedulerAgentException(); 
                }
                throw new JobSchedulerBadRequestException(response);
            }
        default:
            throw new JobSchedulerBadRequestException(httpReplyCode + " " + client.getHttpResponse().getStatusLine().getReasonPhrase());
        }
    }
}
