package com.sos.joc.classes;

import java.io.StringReader;
import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;

public class JOCHotFolder extends JobSchedulerRestApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOCHotFolder.class);
    private static final String MASTER_API_PATH = "/jobscheduler/master/api/live";
    private String url = null;

    public JOCHotFolder() {
        setProperties();
    }

    public JOCHotFolder(JOCResourceImpl jocResourceImpl) {
        this.url = jocResourceImpl.getUrl();
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setProperties();
    }

    public JOCHotFolder(DBItemInventoryInstance dbItemInventoryInstance) {
        this.url = dbItemInventoryInstance.getUrl();
        setBasicAuthorization(dbItemInventoryInstance.getAuth());
        setProperties();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void put(String path, String body) throws JocException {
        put(getURI(path), body);
    }

    public void put(String path, byte[] body) throws JocException {
        put(getURI(path), body);
    }

    public void delete(String path) throws JocException {
        delete(getURI(path));
    }

    public JsonArray getFolder(String path) throws JocException {
        path = (path + "/").replaceAll("//+", "/");
        return getFolder(getURI(path));
    }

    public byte[] getFile(String path) throws JocException {
        return getFile(getURI(path));
    }
    
    public Instant getLastModifiedInstant() {
        try {
            String lastModified = getResponseHeader("Last-Modified");
            if (lastModified == null || lastModified.isEmpty()) {
                return null;
            }
            return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(lastModified));
        } catch (Exception e) {
            return null;
        }
    }
    
    public Date getLastModifiedDate() {
        try {
            Instant i = getLastModifiedInstant();
            if (i == null) {
                return null;
            }
            return Date.from(i);
        } catch (Exception e) {
            return null;
        }
    }

    private void setProperties() {
        setAllowAllHostnameVerifier(!Globals.withHostnameVerification);
        setConnectionTimeout(Globals.httpConnectionTimeout);
        setSocketTimeout(Globals.httpSocketTimeout);
    }

    private URI getURI(String path) {
        UriBuilder uriBuilder = UriBuilder.fromPath(url);
        uriBuilder.path(MASTER_API_PATH + "{path}");
        return uriBuilder.buildFromEncoded(path);
    }

    private void put(URI uri, String body) throws JocException {
        setAccept("*/*");
        addHeader("Content-Type", "application/octet-stream");
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()), "JS-PutBody: " + body);
        if (body == null || body.isEmpty()) {
            jocError.setMessage("body is missing");
            throw new JobSchedulerBadRequestException(jocError);
        }
        try {
            String response = putRestService(uri, body);
            checkResponse(response, uri, jocError);
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }

    private void put(URI uri, byte[] body) throws JocException {
        setAccept("text/plain");
        addHeader("Content-Type", "application/octet-stream");
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()), "JS-PutBody: " + body);
        if (body == null || body.length == 0) {
            jocError.setMessage("body is missing");
            throw new JobSchedulerBadRequestException(jocError);
        }
        try {
            String response = putByteArrayRestService(uri, body);
            checkResponse(response, uri, jocError);
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }

    private void delete(URI uri) throws JocException {
        setAccept("text/plain");
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            String response = deleteRestService(uri);
            checkResponse(response, uri, jocError);
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }

    private JsonArray getFolder(URI uri) throws JocException {
        setAccept("application/json");
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            String response = getRestService(uri);
            return getFolderResponse(response, uri, jocError);
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }

    private byte[] getFile(URI uri) throws JocException {
        setAccept("application/octet-stream");
        JocError jocError = new JocError();
        jocError.appendMetaInfo("JS-URL: " + (uri == null ? "null" : uri.toString()));
        try {
            byte[] response = getByteArrayByRestService(uri);
            checkFileResponse(response, uri, jocError);
            return response;
        } catch (SOSConnectionResetException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionResetException(jocError, e);
            }
        } catch (SOSConnectionRefusedException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerConnectionRefusedException(jocError, e);
            }
        } catch (SOSNoResponseException e) {
            if (isForcedClosingHttpClient()) {
                throw new ForcedClosingHttpClientException(uri.getScheme() + "://" + uri.getAuthority(), e);
            } else {
                throw new JobSchedulerNoResponseException(jocError, e);
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(jocError, e);
        }
    }

    private void checkResponse(String response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        String status = null;
        if (response == null) {
            response = "";
        }
        
        try {
            switch (httpReplyCode) {
            case 200:
            case 201:
                break;
            case 400:
                status = String.format("%d %s: %s", httpReplyCode, getHttpResponse().getStatusLine().getReasonPhrase(), response);
                // Async call while JobScheduler is terminating
                if (response.contains("com.sos.scheduler.engine.common.async.CallQueue$ClosedException")) {
                    throw new JobSchedulerConnectionResetException(status);
                }
                throw new JobSchedulerBadRequestException(status);
            default:
                status = String.format("%d %s: %s", httpReplyCode, getHttpResponse().getStatusLine().getReasonPhrase(), response);
                throw new JobSchedulerBadRequestException(status);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }

    private JsonArray getFolderResponse(String response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        String contentType = getResponseHeader("Content-Type");
        String status = null;
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
                    JsonArray json = (JsonArray) rdr.read();
                    rdr.close();
                    LOGGER.debug(json.toString());
                    return json;
                } else {
                    throw new JobSchedulerInvalidResponseDataException(String.format("Unexpected content type '%1$s'. Response: %2$s", contentType,
                            response));
                }
            case 400:
                // Async call while JobScheduler is terminating
                if (response.contains("com.sos.scheduler.engine.common.async.CallQueue$ClosedException")) {
                    throw new JobSchedulerConnectionResetException(response);
                }
                status = String.format("%d %s: %s", httpReplyCode, getHttpResponse().getStatusLine().getReasonPhrase(), response);
                throw new JobSchedulerBadRequestException(status);
            default:
                status = String.format("%d %s: %s", httpReplyCode, getHttpResponse().getStatusLine().getReasonPhrase(), response);
                throw new JobSchedulerBadRequestException(status);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }

    private void checkFileResponse(byte[] response, URI uri, JocError jocError) throws JocException {
        int httpReplyCode = statusCode();
        try {
            switch (httpReplyCode) {
            case 200:
                break;
            default:
                String status = String.format("%d %s: %s", httpReplyCode, getHttpResponse().getStatusLine().getReasonPhrase(), new String(response));
                throw new JobSchedulerBadRequestException(status);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(jocError);
            throw e;
        }
    }
}
