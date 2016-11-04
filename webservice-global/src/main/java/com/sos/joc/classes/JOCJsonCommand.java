package com.sos.joc.classes;

import java.io.StringReader;
import java.net.URI;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.UnknownJobSchedulerAgentException;


public class JOCJsonCommand {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
    private static final String MASTER_API_PATH = "/jobscheduler/master/api/";
    private UriBuilder uriBuilder;
    
    public JOCJsonCommand() {
    }
    
    public JOCJsonCommand(String url, String path) {
        setUriBuilder(url, path);
    }
    
    public void setUriBuilderForOrders(String url) {
        setUriBuilder(url, MASTER_API_PATH + "order");
    }
    
    public void setUriBuilderForProcessClasses(String url) {
        setUriBuilder(url, MASTER_API_PATH + "processClass");
    }
    
    public void setUriBuilderForJobs(String url) {
        setUriBuilder(url, MASTER_API_PATH + "job");
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
        String returnQuery = (compact) ? "OrdersComplemented/OrderOverview" : "OrdersComplemented/OrderDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addOrderStatisticsQuery() {
        uriBuilder.queryParam("return", "OrderStatistics");
    }
    
    public void addProcessClassCompactQuery(boolean compact) {
        String returnQuery = (compact) ? "ProcessClassOverview" : "ProcessClassDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addJobDescriptionQuery() {
        uriBuilder.queryParam("return", "JobDescription");
    }
    
    public JsonObject getJsonObjectFromPost(String postBody, String csrfToken) throws Exception {
        return getJsonObjectFromPost(uriBuilder.build(), postBody, csrfToken);
    }
    
    public JsonObject getJsonObjectFromPost(URI uri, String postBody, String csrfToken) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        if (postBody == null) {
            postBody = "";
        }
        String[] metaInfo = {"JS-URL: " + uri, "JS-PostBody: " + postBody};
        String response;
        try {
            response = client.executeRestServiceCommand("post", uri.toURL(), postBody);
        } catch (Exception e) {
            throw new JobSchedulerConnectionRefusedException(e);
        }
        return getJsonObjectFromResponse(response, client, metaInfo);
    }
    
    public JsonObject getJsonObjectFromGet(String csrfToken) throws Exception {
        return getJsonObjectFromGet(uriBuilder.build(), csrfToken);
    }
    
    public JsonObject getJsonObjectFromGet(URI uri, String csrfToken) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        String[] metaInfo = {"JS-URL: " + uri};
        String response;
        try {
            response = client.executeRestServiceCommand("get", uri.toURL());
        } catch (Exception e) {
            throw new JobSchedulerConnectionRefusedException(e);
        }
        return getJsonObjectFromResponse(response, client, metaInfo);
    }
    
    private String getCsrfToken(String csrfToken) {
        if (csrfToken == null || csrfToken.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return csrfToken;
    }
    
    private JsonObject getJsonObjectFromResponse(String response, JobSchedulerRestApiClient client, String[] metaInfo) throws Exception {
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
                JobSchedulerBadRequestException e = new JobSchedulerBadRequestException(json.getString("message", response));
                e.addErrorMetaInfo(metaInfo);
                throw e;
            } else {
                if ("Unknown Agent".equalsIgnoreCase(response)) {
                    UnknownJobSchedulerAgentException e = new UnknownJobSchedulerAgentException();
                    e.addErrorMetaInfo(metaInfo);
                    throw e; 
                }
                JobSchedulerBadRequestException e = new JobSchedulerBadRequestException(response);
                e.addErrorMetaInfo(metaInfo);
                throw e;
            }
        default:
            JobSchedulerBadRequestException e = new JobSchedulerBadRequestException(httpReplyCode + " " + client.getHttpResponse().getStatusLine().getReasonPhrase());
            e.addErrorMetaInfo(metaInfo);
            throw e;
        }
    }
}
