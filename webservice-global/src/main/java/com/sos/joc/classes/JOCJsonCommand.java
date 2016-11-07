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

import com.sos.exception.ConnectionRefusedException;
import com.sos.exception.NoResponseException;
import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
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

    public JsonObject getJsonObjectFromPost(String postBody, String csrfToken) throws JocException {
        try {
            return getJsonObjectFromPost(getURI(), postBody, csrfToken);
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public JsonObject getJsonObjectFromPost(URI uri, String postBody, String csrfToken) throws JocException {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        if (postBody == null) {
            postBody = "";
        }
        String[] metaInfo = { "JS-URL: " + uri, "JS-PostBody: " + postBody };
        String response;
        try {
            response = client.postRestService(uri, postBody);
        } catch (ConnectionRefusedException e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerConnectionRefusedException(e);
        } catch (NoResponseException e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerNoResponseException(e);
        } catch (Exception e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerBadRequestException(e);
        }
        return getJsonObjectFromResponse(response, client, metaInfo);
    }

    public JsonObject getJsonObjectFromGet(String csrfToken) throws JocException {
        try {
            return getJsonObjectFromGet(getURI(), csrfToken);
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public JsonObject getJsonObjectFromGet(URI uri, String csrfToken) throws JocException {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        String[] metaInfo = { "JS-URL: " + uri };
        String response;
        try {
            response = client.getRestService(uri);
        } catch (ConnectionRefusedException e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerConnectionRefusedException(e);
        } catch (NoResponseException e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerNoResponseException(e);
        } catch (Exception e) {
            // Throwable t = (e.getCause() != null) ? e.getCause() : e;
            throw new JobSchedulerBadRequestException(e);
        }
        return getJsonObjectFromResponse(response, client, metaInfo);
    }

    private String getCsrfToken(String csrfToken) {
        if (csrfToken == null || csrfToken.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return csrfToken;
    }

    private JsonObject getJsonObjectFromResponse(String response, JobSchedulerRestApiClient client, String[] metaInfo) throws JocException {
        int httpReplyCode = client.statusCode();
        String contentType = client.getResponseHeader("Content-Type");
        if (response == null) {
            response = "";
        }

        try {
            switch (httpReplyCode) {
            case 200:
                if (contentType.contains("application/json")) {
                    if (response.isEmpty()) {
                        throw new JobSchedulerNoResponseException("Unexpected empty response");
                    }
                    JsonReader rdr = Json.createReader(new StringReader(response));
                    JsonObject json = rdr.readObject();
                    LOGGER.debug(json.toString());
                    return json;
                } else {
                    throw new JobSchedulerInvalidResponseDataException(String.format("Unexpected content type '%1$s'. Response: %2$s", contentType,
                            response));
                }
            case 400:
                if ("Unknown Agent".equalsIgnoreCase(response)) {
                    throw new UnknownJobSchedulerAgentException();
                }
                if (contentType.contains("application/json") && !response.isEmpty()) {
                    JsonReader rdr = Json.createReader(new StringReader(response));
                    JsonObject json = rdr.readObject();
                    throw new JobSchedulerBadRequestException(json.getString("message", response));
                } else {
                    throw new JobSchedulerBadRequestException(response);
                }
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + client.getHttpResponse().getStatusLine().getReasonPhrase());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(metaInfo);
            throw e;
        }
    }
}
