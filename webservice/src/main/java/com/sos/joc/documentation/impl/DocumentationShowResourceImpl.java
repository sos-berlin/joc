package com.sos.joc.documentation.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.documentation.resource.IDocumentationShowResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.docu.DocumentationShowFilter;
import com.sos.joc.model.docu.DocumentationUrl;

@Path("documentation")
public class DocumentationShowResourceImpl extends JOCResourceImpl implements IDocumentationShowResource {

    private static final String API_CALL = "/documentation/show";

    @Override
    public JOCDefaultResponse show(String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception {
        try {
            // TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>\n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='./api/%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    getUrl(xAccessToken, documentationFilter));

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
            DocumentationUrl entity = new DocumentationUrl();
            entity.setUrl(getUrl(xAccessToken, documentationFilter));
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private String getUrl(String xAccessToken, DocumentationShowFilter documentationFilter) throws JocMissingRequiredParameterException {

        checkRequiredParameter("jobschedulerId", documentationFilter.getJobschedulerId());
        checkRequiredParameter("path", documentationFilter.getPath());

        return documentationFilter.getJobschedulerId() + "/" + xAccessToken + normalizePath(documentationFilter.getPath());
    }

}
