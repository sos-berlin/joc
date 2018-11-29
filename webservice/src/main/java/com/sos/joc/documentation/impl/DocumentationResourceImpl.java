package com.sos.joc.documentation.impl;

import java.nio.file.Paths;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationResource;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;

@Path("documentation")
public class DocumentationResourceImpl extends JOCResourceImpl implements IDocumentationResource {

    private static final String API_CALL = "./documentation";

    @Override
    public JOCDefaultResponse postDocumentation(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return postDocumentation(getAccessToken(xAccessToken, accessToken), jobschedulerId, path);
    }

    private JOCDefaultResponse postDocumentation(String accessToken, String jobschedulerId, String path) {
        SOSHibernateSession connection = null;
        try {
            String request = String.format("%s/%s/%s/%s", API_CALL, jobschedulerId, accessToken, path);
            // TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(request, null, accessToken, jobschedulerId, true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            checkRequiredParameter("jobschedulerId", jobschedulerId);
            checkRequiredParameter("path", path);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            DBItemDocumentation dbItem = dbLayer.getDocumentation(jobschedulerId, normalizePath(path));
            String errMessage = "No database entry (" + jobschedulerId + "/" + path + ") as documentation resource found";

            if (dbItem == null) {
                throw new DBMissingDataException(errMessage);
            }
            String type = dbItem.getType();
            if (dbItem.getImageId() != null && dbItem.getImageId() != 0L) {
                DBItemDocumentationImage dbItemImage = dbLayer.getDocumentationImage(dbItem.getImageId());
                if (dbItemImage == null) {
                    throw new DBMissingDataException(errMessage);
                }
                if (type.isEmpty()) {
                    type = MediaType.APPLICATION_OCTET_STREAM;
                }
                return JOCDefaultResponse.responseStatus200(dbItemImage.getImage(), getType(type));

            } else if (dbItem.getContent() != null && !dbItem.getContent().isEmpty()) {
                return JOCDefaultResponse.responseStatus200(dbItem.getContent(), getType(type));

            } else {
                throw new DBMissingDataException(errMessage);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
    
    private String getType(String type) {
        switch (type) {
        case "xml":
        case "xsl":
        case "xsd":
            type = MediaType.APPLICATION_XML;
            break;
        // TODO convert markdown -> html
        case "javascript":
        case "css":
            type = "text/"+type;
            break;
        case "json":
            type = MediaType.APPLICATION_JSON;
            break;
        case "markdown":
        case "":
            type = MediaType.TEXT_PLAIN;
            break;
        case "html":
            type = MediaType.TEXT_HTML;
            break;
        case "gif":
        case "jpeg":
        case "png":
            type = "image/"+type;
            break;
        }
        if (type.startsWith("text/")) {
            type += "; charset=UTF-8";
        }
        return type;
    }

}
