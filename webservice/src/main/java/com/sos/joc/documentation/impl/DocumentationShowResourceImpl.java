package com.sos.joc.documentation.impl;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationShowResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.docu.DocumentationShowFilter;
import com.sos.joc.model.docu.DocumentationUrl;
import com.sos.schema.JsonValidator;

@Path("documentation")
public class DocumentationShowResourceImpl extends JOCResourceImpl implements IDocumentationShowResource {

    private static final String API_CALL_SHOW = "./documentation/show";
    private static final String API_CALL_URL = "./documentation/url";
    private static final String API_CALL_PREVIEW = "./documentation/preview";

    @Override
    public JOCDefaultResponse show(String xAccessToken, String accessToken, String jobschedulerId, String path, String type) {
        return show(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, type);
    }

    public JOCDefaultResponse show(String xAccessToken, String jobschedulerId, String path, String type) {
        try {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            if(jobschedulerId != null) {
                builder.add("jobschedulerId", jobschedulerId);
            }
            if(path != null) {
                builder.add("path", path);
            }
            if (type != null) {
                builder.add("type", type);
            }
            //String json = String.format("{\"jobschedulerId\": \"%s\", \"path\": \"%s\", \"type\": \"%s\"}", jobschedulerId, path, type);
            return show(xAccessToken, builder.build().toString().getBytes());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse show(String xAccessToken, byte[] documentationFilterBytes) {
        try {
            JsonValidator.validateFailFast(documentationFilterBytes, DocumentationShowFilter.class);
            DocumentationShowFilter documentationFilter = Globals.objectMapper.readValue(documentationFilterBytes, DocumentationShowFilter.class);
            
            boolean perm = false;
            if (documentationFilter.getType() != null) {
                SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(documentationFilter.getJobschedulerId(), xAccessToken);
                switch (documentationFilter.getType()) {
                case JOB: 
                    perm = sosPermission.getJob().getView().isDocumentation();
                    break;
                case JOBCHAIN: 
                    perm = sosPermission.getJobChain().getView().isDocumentation();
                    break;
                case LOCK: 
                    perm = sosPermission.getLock().getView().isDocumentation();
                    break;
                case ORDER: 
                    perm = sosPermission.getOrder().getView().isDocumentation();
                    break;
                case PROCESSCLASS: 
                    perm = sosPermission.getProcessClass().getView().isDocumentation();
                    break;
                case SCHEDULE: 
                    perm = sosPermission.getSchedule().getView().isDocumentation();
                    break;
                case NONWORKINGDAYSCALENDAR:
                case WORKINGDAYSCALENDAR:
                    perm = sosPermission.getCalendar().getView().isDocumentation();
                    break;
                default:
                    break;
                }
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SHOW, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    perm);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>%n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    getUrl(API_CALL_SHOW, xAccessToken, documentationFilter));

            return JOCDefaultResponse.responseHtmlStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse preview(String xAccessToken, String accessToken, String jobschedulerId, String path) {
        return preview(getAccessToken(xAccessToken, accessToken), jobschedulerId, path);
    }

    public JOCDefaultResponse preview(String xAccessToken, String jobschedulerId, String path) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if(jobschedulerId != null) {
            builder.add("jobschedulerId", jobschedulerId);
        }
        if(path != null) {
            builder.add("path", path);
        }
        //String json = String.format("{\"jobschedulerId\": \"%s\", \"path\": \"%s\"}", jobschedulerId, path);
        return preview(xAccessToken, builder.build().toString().getBytes());
    }

    @Override
    public JOCDefaultResponse preview(String xAccessToken, byte[] documentationFilterBytes) {
        try {
            JsonValidator.validateFailFast(documentationFilterBytes, DocumentationShowFilter.class);
            DocumentationShowFilter documentationFilter = Globals.objectMapper.readValue(documentationFilterBytes, DocumentationShowFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_PREVIEW, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(documentationFilter.getJobschedulerId(), xAccessToken).getDocumentation().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("documentation", documentationFilter.getPath());

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>%n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='%s/%s%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    documentationFilter.getJobschedulerId(), xAccessToken, JOCJsonCommand.urlEncodedPath(normalizePath(documentationFilter.getPath())));

            return JOCDefaultResponse.responseHtmlStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postUrl(String xAccessToken, byte[] documentationFilterBytes) {
        try {
            JsonValidator.validateFailFast(documentationFilterBytes, DocumentationShowFilter.class);
            DocumentationShowFilter documentationFilter = Globals.objectMapper.readValue(documentationFilterBytes, DocumentationShowFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_URL, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            DocumentationUrl entity = new DocumentationUrl();
            entity.setUrl(getUrl(API_CALL_URL, xAccessToken, documentationFilter));
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private String getUrl(String apiCall, String xAccessToken, DocumentationShowFilter documentationFilter)
            throws JocMissingRequiredParameterException, JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            DBMissingDataException, UnsupportedEncodingException, DBOpenSessionException {
        SOSHibernateSession connection = null;
        try {
            checkRequiredParameter("jobschedulerId", documentationFilter.getJobschedulerId());
            checkRequiredParameter("path", documentationFilter.getPath());
            checkRequiredParameter("objectType", documentationFilter.getType().name());

            documentationFilter.setPath(normalizePath(documentationFilter.getPath()));
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String path = dbLayer.getDocumentationPath(documentationFilter);
            if (path == null) {
                throw new DBMissingDataException("The documentation couldn't determine");
            }

            return documentationFilter.getJobschedulerId() + "/" + xAccessToken + JOCJsonCommand.urlEncodedPath(documentationFilter.getPath());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
