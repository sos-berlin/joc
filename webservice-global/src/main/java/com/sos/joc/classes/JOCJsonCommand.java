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
import com.sos.joc.Globals;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.UnknownJobSchedulerAgentException;

public class JOCJsonCommand extends JobSchedulerRestApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
    private static final String MASTER_API_PATH = "/jobscheduler/master/api/";
    private UriBuilder uriBuilder;
    private JOCResourceImpl jocResourceImpl;
    
    public JOCJsonCommand() {
        setProperties();
    }

    public JOCJsonCommand(JOCResourceImpl jocResourceImpl) {
        this.jocResourceImpl = jocResourceImpl;
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
    }
    
    public JOCJsonCommand(JOCResourceImpl jocResourceImpl, String path) {
        this.jocResourceImpl = jocResourceImpl;
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
        setUriBuilder(jocResourceImpl.getUrl(), path);
    }

    public void setJOCResourceImpl(JOCResourceImpl jocResourceImpl) {
        this.jocResourceImpl = jocResourceImpl;
    }
    
    public JOCResourceImpl getJOCResourceImpl() {
        return jocResourceImpl;
    }

    public void setUriBuilderForOrders() {
        setUriBuilderForOrders(jocResourceImpl.getUrl());
    }
    
    public void setUriBuilderForOrders(String url) {
        setUriBuilder(url, MASTER_API_PATH + "order");
    }
    
    public void setUriBuilderForEvents() {
        setUriBuilderForEvents(jocResourceImpl.getUrl());
    }
    
    public void setUriBuilderForEvents(String url) {
        setUriBuilder(url, MASTER_API_PATH + "event");
    }

    public void setUriBuilderForProcessClasses() {
        setUriBuilderForProcessClasses(jocResourceImpl.getUrl());
    }
    
    public void setUriBuilderForProcessClasses(String url) {
        setUriBuilder(url, MASTER_API_PATH + "processClass");
    }

    public void setUriBuilderForJobs() {
        setUriBuilderForJobs(jocResourceImpl.getUrl());
    }
    
    public void setUriBuilderForJobs(String url) {
        setUriBuilder(url, MASTER_API_PATH + "job");
    }
    
    public void setUriBuilder(String path) {
        uriBuilder = UriBuilder.fromPath(jocResourceImpl.getUrl());
        uriBuilder.path(path);
    }
    
    public void setUriBuilder(String url, String path) {
        uriBuilder = UriBuilder.fromPath(url);
        uriBuilder.path(path);
    }

    public void setUriBuilder(UriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }
    
    public UriBuilder replaceUriBuilder(String url, URI uri) {
        uriBuilder = UriBuilder.fromPath(url);
        uriBuilder.path(uri.getPath());
        uriBuilder.replaceQuery(uri.getQuery());
        return uriBuilder;
    }

    public URI getURI() {
        return uriBuilder.build();
    }
    
    public String getSchemeAndAuthority() {
        URI uri = uriBuilder.build();
        return uri.getScheme()+"://"+uri.getAuthority();
    }

    public void addOrderCompactQuery(boolean compact) {
        String returnQuery = (compact) ? "OrdersComplemented/OrderOverview" : "OrdersComplemented/OrderDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }

    public void addOrderStatisticsQuery() {
        uriBuilder.queryParam("return", "JocOrderStatistics");
    }
    
    public void addEventTimeout(Integer timeout) {
        if (timeout == null) {
            timeout = 0;
        }
        uriBuilder.queryParam("timeout", timeout);
    }
    
    public void replaceEventQuery(String eventId, Integer timeout) {
        uriBuilder.replaceQueryParam("after", eventId);
        uriBuilder.replaceQueryParam("timeout", timeout);
    }
    
    public void addEventQuery(String eventId, Integer timeout) {
        addEventQuery(eventId, timeout, null);
    }
    
    public void addEventQuery(String eventId, Integer timeout, String event) {
        if (event != null && event.isEmpty()) {
            uriBuilder.queryParam("return", event);
        }
        if (timeout == null) {
            timeout = 0;
        }
        uriBuilder.queryParam("timeout", timeout);
        uriBuilder.queryParam("after", eventId);
    }
    
    public void addOrderEventQuery(String eventId, Integer timeout) {
        addEventQuery(eventId, timeout, "OrderEvent");
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
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public JsonObject getJsonObjectFromPost(URI uri, String postBody, String csrfToken) throws JocException {
        addHeader("Content-Type", "application/json");
        addHeader("Accept", "application/json");
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        if (postBody == null) {
            postBody = "";
        }
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()), "JS-PostBody: " + postBody);
        try {
            String response = postRestService(uri, postBody);
            return getJsonObjectFromResponse(response, uri, jocError);
        } catch (ConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (NoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }
    
    public JsonObject getJsonObjectFromPostWithRetry(String postBody, String csrfToken) throws JocException {
        return getJsonObjectFromPostWithRetry(getURI(), postBody, csrfToken);
    }
    
    public JsonObject getJsonObjectFromPostWithRetry(URI uri, String postBody, String csrfToken) throws JocException {
        try {
            return getJsonObjectFromPost(uri, postBody, csrfToken);
        } catch (JobSchedulerConnectionRefusedException e) {
            String url = null;
            if (jocResourceImpl != null) {
                url = jocResourceImpl.retrySchedulerInstance(); 
            }
            if (url != null) {
                uri = replaceUriBuilder(url, uri).build();
                return getJsonObjectFromPost(uri, postBody, csrfToken);
            } else {
                throw e;
            }
        } catch (JocException e) {
            throw e;
        }
    }

    public JsonObject getJsonObjectFromGet(String csrfToken) throws JocException {
        try {
            return getJsonObjectFromGet(getURI(), csrfToken);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public JsonObject getJsonObjectFromGet(URI uri, String csrfToken) throws JocException {
        addHeader("Accept", "application/json");
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            String response = getRestService(uri);
            return getJsonObjectFromResponse(response, uri, jocError);
        } catch (ConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (NoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }
    
    public JsonObject getJsonObjectFromGetWithRetry(String csrfToken) throws JocException {
        return getJsonObjectFromGetWithRetry(getURI(), csrfToken);
    }
    
    public JsonObject getJsonObjectFromGetWithRetry(URI uri, String csrfToken) throws JocException {
        try {
            return getJsonObjectFromGet(uri, csrfToken);
        } catch (JobSchedulerConnectionRefusedException e) {
            String url = null;
            if (jocResourceImpl != null) {
                url = jocResourceImpl.retrySchedulerInstance(); 
            }
            if (url != null) {
                uri = replaceUriBuilder(url, uri).build();
                return getJsonObjectFromGet(uri, csrfToken);
            } else {
                throw e;
            }
        } catch (JocException e) {
            throw e;
        }
    }
    
    private void setProperties() {
        setAllowAllHostnameVerifier(!Globals.withHostnameVerification);
        setConnectionTimeout(Globals.httpConnectionTimeout);
        setSocketTimeout(Globals.httpSocketTimeout);
    }
    
    private String getCsrfToken(String csrfToken) {
        if (csrfToken == null || csrfToken.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return csrfToken;
    }

    private JsonObject getJsonObjectFromResponse(String response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        String contentType = getResponseHeader("Content-Type");
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
                    throw new UnknownJobSchedulerAgentException(uri.toString().replaceFirst(".*/(https?://.*)$", "$1"));
                }
                if (contentType.contains("application/json") && !response.isEmpty()) {
                    JsonReader rdr = Json.createReader(new StringReader(response));
                    JsonObject json = rdr.readObject();
                    throw new JobSchedulerBadRequestException(json.getString("message", response));
                } else {
                    throw new JobSchedulerBadRequestException(response);
                }
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + getHttpResponse().getStatusLine().getReasonPhrase());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }
}
