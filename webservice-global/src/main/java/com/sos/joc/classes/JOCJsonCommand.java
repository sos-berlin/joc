package com.sos.joc.classes;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.sos.exception.SOSConnectionRefusedException;
import com.sos.exception.SOSConnectionResetException;
import com.sos.exception.SOSNoResponseException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.UnknownJobSchedulerAgentException;

public class JOCJsonCommand extends JobSchedulerRestApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
    private static final String MASTER_API_PATH = "/jobscheduler/master/api/";
    private UriBuilder uriBuilder;
    private JOCResourceImpl jocResourceImpl;
    private String url = null;
    
    public JOCJsonCommand() {
        setProperties();
    }

    public JOCJsonCommand(JOCResourceImpl jocResourceImpl) {
        this.jocResourceImpl = jocResourceImpl;
        this.url = jocResourceImpl.getUrl();
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
    }
    
    public JOCJsonCommand(JOCResourceImpl jocResourceImpl, String path) {
        this.jocResourceImpl = jocResourceImpl;
        this.url = jocResourceImpl.getUrl();
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
        setUriBuilder(jocResourceImpl.getUrl(), path);
    }
    
    public JOCJsonCommand(JOCJsonCommand jocJsonCommand) {
        this.jocResourceImpl = jocJsonCommand.getJOCResourceImpl();
        this.url = jocResourceImpl.getUrl();
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
        this.uriBuilder = jocJsonCommand.getUriBuilder();
    }
    
    public JOCJsonCommand(DBItemInventoryInstance dbItemInventoryInstance) {
        setBasicAuthorization(dbItemInventoryInstance.getAuth());
        this.url = dbItemInventoryInstance.getUrl();
        setProperties();
    }

    public void setJOCResourceImpl(JOCResourceImpl jocResourceImpl) {
        this.jocResourceImpl = jocResourceImpl;
        this.url = jocResourceImpl.getUrl();
    }
    
    public JOCResourceImpl getJOCResourceImpl() {
        return jocResourceImpl;
    }
    
    public String getClusterMemberId() {
        if (jocResourceImpl != null) {
            return jocResourceImpl.getClusterMemberId();
        }
        return null;
    }

    public void setUriBuilderForOrders() {
        setUriBuilderForOrders(url);
    }
    
    public void setUriBuilderForOrders(String url) {
        setUriBuilder(url, MASTER_API_PATH + "order");
    }
    
    public void setUriBuilderForEvents() {
        setUriBuilderForEvents(url);
    }
    
    public void setUriBuilderForEvents(String url) {
        setUriBuilder(url, MASTER_API_PATH + "event");
    }

    public void setUriBuilderForProcessClasses() {
        setUriBuilderForProcessClasses(url);
    }
    
    public void setUriBuilderForProcessClasses(String url) {
        setUriBuilder(url, MASTER_API_PATH + "processClass");
    }

    public void setUriBuilderForJobs() {
        setUriBuilderForJobs(url);
    }
    
    public void setUriBuilderForJobs(String url) {
        setUriBuilder(url, MASTER_API_PATH + "job");
    }
    
    public void setUriBuilderForJobChains() {
        setUriBuilderForJobChains(url);
    }
    
    public void setUriBuilderForJobChains(String url) {
        setUriBuilder(url, MASTER_API_PATH + "jobChain");
    }
    
    public URI getUriForJobPathAsUrlParam(String jobPath, Integer limit) {
        uriBuilder = UriBuilder.fromPath(url);
        uriBuilder.path(MASTER_API_PATH + "job/{path}");
        uriBuilder.queryParam("return", "History");
        uriBuilder.queryParam("limit", limit);
        return uriBuilder.buildFromEncoded(jobPath.replaceFirst("^/+", ""));
    }
    
    public void setUriBuilderForMainLog() {
        setUriBuilder(url, "/jobscheduler/engine-cpp/show_log");
        uriBuilder.queryParam("main", "");
    }
    
    public void setUriBuilderForMainLog(String logFileBaseName) {
        //Don't work on Linux, why?
        //setUriBuilder(jurl, "/jobscheduler/joc/scheduler_data/logs/" + logFileBaseName);
        setUriBuilder(url, "/jobscheduler/engine-cpp/scheduler_data/logs/" + logFileBaseName);
    }
    
    public void setUriBuilder(String path) {
        uriBuilder = UriBuilder.fromPath(url);
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
    
    public static String urlEncodedPath(String value) {
        return UriBuilder.fromPath("{path}").buildFromEncoded(value).toString();
    }
    
    public void addJobHistoryQuery(Integer limit) {
        uriBuilder.queryParam("return", "History");
        uriBuilder.queryParam("limit", limit);
    }

    public void addOrderCompactQuery(boolean compact) {
        //String returnQuery = (compact) ? "OrdersComplemented/OrderOverview" : "OrdersComplemented/OrderDetailed";
        //Workaround: JOC-84 
        String returnQuery = "OrdersComplemented/OrderDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addJobCompactQuery(boolean compact) {
        String returnQuery = (compact) ? "JobOverview" : "JobDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }
    
    public void addJobChainCompactQuery(boolean compact) {
        String returnQuery = (compact) ? "JobChainOverview" : "JobChainDetailed";
        uriBuilder.queryParam("return", returnQuery);
    }

    public void addOrderStatisticsQuery() {
        addOrderStatisticsQuery(null);
    }
    
    public void addOrderStatisticsQuery(Boolean isDistributed) {
        uriBuilder.queryParam("return", "JocOrderStatistics");
        if (isDistributed != null) {
            uriBuilder.queryParam("isDistributed", isDistributed);
        }
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
        if (event != null && !event.isEmpty()) {
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

    public <T extends JsonStructure> T getJsonObjectFromPost(String postBody, String csrfToken) throws JocException {
        try {
            return getJsonObjectFromPost(getURI(), postBody, csrfToken);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public <T extends JsonStructure> T getJsonObjectFromPost(URI uri, String postBody, String csrfToken) throws JocException {
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
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
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
    
    public <T extends JsonStructure> T getJsonObjectFromPostWithRetry(String postBody, String csrfToken) throws JocException {
        return getJsonObjectFromPostWithRetry(getURI(), postBody, csrfToken);
    }
    
    public <T extends JsonStructure> T getJsonObjectFromPostWithRetry(URI uri, String postBody, String csrfToken) throws JocException {
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
    
    public String getHtmlFromGet(String csrfToken) throws JocException {
        return getAcceptTypeFromGet(csrfToken, "text/html");
    }
    
    
    
    public String getHtmlFromGet(URI uri, String csrfToken) throws JocException {
        return getAcceptTypeFromGet(uri, csrfToken, "text/html");
    }
    
    public String getPlainFromGet(String csrfToken) throws JocException {
        return getAcceptTypeFromGet(csrfToken, "text/plain");
    }
    
    public String getPlainFromGet(URI uri, String csrfToken) throws JocException {
        return getAcceptTypeFromGet(uri, csrfToken, "text/plain");
    }
    
    public byte[] getByteArrayFromGet(String csrfToken, String acceptHeader) throws JocException {
        try {
            return getByteArrayFromGet(getURI(), csrfToken, acceptHeader);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
    public byte[] getByteArrayFromGet(URI uri, String csrfToken, String acceptHeader) throws JocException {
        if (acceptHeader != null && !acceptHeader.isEmpty()) {
            addHeader("Accept", acceptHeader);
        }
        addHeader("Accept-Encoding", "gzip");
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            return getByteArrayFromResponse(getByteArrayByRestService(uri), uri, jocError);
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
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
    
    public Path getFilePathFromGet(URI uri, String csrfToken, String prefix, String acceptHeader, boolean withGzipEncoding) throws JocException {
        if (acceptHeader != null && !acceptHeader.isEmpty()) {
            addHeader("Accept", acceptHeader);
        }
        addHeader("Accept-Encoding", "gzip");
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            return getFilePathFromResponse(getFilePathByRestService(uri, prefix, withGzipEncoding), uri, jocError);
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
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

    public <T extends JsonStructure> T getJsonObjectFromGet(String csrfToken) throws JocException {
        try {
            return getJsonObjectFromGet(getURI(), csrfToken);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public <T extends JsonStructure> T getJsonObjectFromGet(URI uri, String csrfToken) throws JocException {
        addHeader("Accept", "application/json");
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            String response = getRestService(uri);
            return getJsonObjectFromResponse(response, uri, jocError);
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
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
    
    public String getAcceptTypeFromGet(String csrfToken, String acceptHeader) throws JocException {
        return getAcceptTypeFromGet(getURI(), csrfToken, acceptHeader);
    }
    
    public String getAcceptTypeFromGet(URI uri, String csrfToken, String acceptHeader) throws JocException {
        addHeader("Accept", acceptHeader);
        addHeader("X-CSRF-Token", getCsrfToken(csrfToken));
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            String response = getRestService(uri);
            return getStringFromResponse(response, uri, jocError, acceptHeader);
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme()+"://"+uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
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
    
    public <T extends JsonStructure> T getJsonObjectFromGetWithRetry(String csrfToken) throws JocException {
        return getJsonObjectFromGetWithRetry(getURI(), csrfToken);
    }
    
    public <T extends JsonStructure> T getJsonObjectFromGetWithRetry(URI uri, String csrfToken) throws JocException {
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
    
    public <T extends JsonStructure> T getJsonObjectFromGetWithHTTPConnection(String csrfToken) throws JocException {
        HttpURLConnection httpConn = null;
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (getURI() == null ? "null" : getURI().toString()));
        try {
            httpConn = getHttpURLConnection(getURI().toURL(), Globals.httpSocketTimeout, Globals.httpConnectionTimeout);
            httpConn.setRequestProperty("X-CSRF-Token", getCsrfToken(csrfToken));
            return getJsonObjectFromResponse2(httpConn, getURI(), jocError);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        } finally {
            if (httpConn != null) {
                httpConn.disconnect(); 
            }
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

    private <T extends JsonStructure> T getJsonObjectFromResponse(String response, URI uri, JocError jocError) throws JocException {
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
                    @SuppressWarnings("unchecked")
                    T json = (T) rdr.read();
                    rdr.close();
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
                    rdr.close();
                    String msg = json.getString("message", response);
                    if (msg.contains("SCHEDULER-161") || msg.contains("SCHEDULER-162")) {
                        throw new JobSchedulerObjectNotExistException(msg);
                    }
                    throw new JobSchedulerBadRequestException(msg);
                } else {
                    if (response.contains("SCHEDULER-161") || response.contains("SCHEDULER-162")) {
                        throw new JobSchedulerObjectNotExistException(response);
                    }
                    // Async call while JobScheduler is terminating 
                    if (response.contains("com.sos.scheduler.engine.common.async.CallQueue$ClosedException")) {
                        throw new JobSchedulerConnectionResetException(response);
                    }
                    throw new JobSchedulerBadRequestException(response);
                }
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + getHttpResponse().getStatusLine().getReasonPhrase() + ": " + uri.toString());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }
    
    private <T extends JsonStructure> T getJsonObjectFromResponse2(HttpURLConnection connection, URI uri, JocError jocError) throws JocException, IOException {
        int httpReplyCode = connection.getResponseCode();
        try {
            switch (httpReplyCode) {
            case 200:
                String response = IOUtils.toString(connection.getInputStream(), Charsets.UTF_8);

                if (response.isEmpty()) {
                    throw new JobSchedulerNoResponseException("Unexpected empty response");
                }
                JsonReader rdr = Json.createReader(new StringReader(response));
                @SuppressWarnings("unchecked")
                T json = (T) rdr.read();
                rdr.close();
                LOGGER.debug(json.toString());
                return json;

            case 400:
                String errResponse = IOUtils.toString(connection.getErrorStream(), Charsets.UTF_8);
                if ("Unknown Agent".equalsIgnoreCase(errResponse)) {
                    throw new UnknownJobSchedulerAgentException(uri.toString().replaceFirst(".*/(https?://.*)$", "$1"));
                }
                if (errResponse.contains("SCHEDULER-161") || errResponse.contains("SCHEDULER-162")) {
                    throw new JobSchedulerObjectNotExistException(errResponse);
                }
                // Async call while JobScheduler is terminating 
                if (errResponse.contains("com.sos.scheduler.engine.common.async.CallQueue$ClosedException")) {
                    throw new JobSchedulerConnectionResetException(errResponse);
                }
                throw new JobSchedulerBadRequestException(errResponse);
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + connection.getResponseMessage() + ": " + uri.toString());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }
    
    private byte[] getByteArrayFromResponse(byte[] response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        try {
            switch (httpReplyCode) {
            case 200:
                if (response == null || response.length <= 0) {
                    throw new JobSchedulerNoResponseException("Unexpected empty response");
                }
                return response;
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + getHttpResponse().getStatusLine().getReasonPhrase());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }
    
    private Path getFilePathFromResponse(Path response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        try {
            switch (httpReplyCode) {
            case 200:
                try {
                    if (response == null || Files.size(response) <= 0) {
                        throw new JobSchedulerNoResponseException("Unexpected empty response");
                    }
                } catch (IOException e) {
                    throw new JobSchedulerNoResponseException("Unexpected empty response");
                }
                return response;
            default:
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + getHttpResponse().getStatusLine().getReasonPhrase());
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            try {
                Files.deleteIfExists(response);
            } catch (IOException e1) {}
            throw e;
        }
    }
    
    private String getStringFromResponse(String response, URI uri, JocError jocError, String acceptHeader) throws JocException {
        int httpReplyCode = statusCode();
        try {
            switch (httpReplyCode) {
            case 200:
                if (response == null || response.isEmpty()) {
                    throw new JobSchedulerNoResponseException("Unexpected empty response");
                }
                return response;
            default:
                if (response == null) {
                    response = "";
                }
                throw new JobSchedulerBadRequestException(httpReplyCode + " " + getHttpResponse().getStatusLine().getReasonPhrase() + " " + response);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }

    
    public void setUrl(String url) {
        this.url = url;
    }
}
