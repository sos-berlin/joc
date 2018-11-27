package com.sos.joc.documentation.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationShowResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.docu.DocumentationShowFilter;
import com.sos.joc.model.docu.DocumentationUrl;

@Path("documentation")
public class DocumentationShowResourceImpl extends JOCResourceImpl implements IDocumentationShowResource {

    private static final String API_CALL_SHOW = "/documentation/show";
    private static final String API_CALL_URL = "/documentation/url";

    @Override
    public JOCDefaultResponse show(String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception {
        try {
            // TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SHOW, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>\n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='./api/%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    getUrl(API_CALL_SHOW, xAccessToken, documentationFilter));

            return JOCDefaultResponse.responseHtmlStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postUrl(String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception {
        try {
            // TODO Permission
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
            DBMissingDataException {
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

            return documentationFilter.getJobschedulerId() + "/" + xAccessToken + path;
        } finally {
            Globals.disconnect(connection);
        }
    }

}
