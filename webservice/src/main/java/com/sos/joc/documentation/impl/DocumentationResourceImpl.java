package com.sos.joc.documentation.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import com.google.common.base.Charsets;
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
    private static final java.nio.file.Path CSS = Paths.get("/sos/css/default-markdown.css");

    @Override
    public JOCDefaultResponse postDocumentation(String accessToken, String jobschedulerId, String path) {
        SOSHibernateSession connection = null;
        try {
            String request = String.format("%s/%s/%s/%s", API_CALL, jobschedulerId, accessToken, path);
            boolean perm = getPermissonsJocCockpit(jobschedulerId, accessToken).getDocumentation().isView();
            JOCDefaultResponse jocDefaultResponse = init(request, null, accessToken, jobschedulerId, perm);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobschedulerId);
            checkRequiredParameter("path", path);

            path = normalizePath(path);
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            DBItemDocumentation dbItem = dbLayer.getDocumentation(jobschedulerId, path);
            String errMessage = "No database entry (" + path + ") as documentation resource found";

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
                jocDefaultResponse = JOCDefaultResponse.responseStatus200(dbItemImage.getImage(), getType(type));

            } else if (dbItem.getContent() != null && !dbItem.getContent().isEmpty()) {
                if ("markdown".equals(type)) {
                    jocDefaultResponse = JOCDefaultResponse.responseStatus200(createHTMLfromMarkdown(dbItem), MediaType.TEXT_HTML);
                } else {
                    jocDefaultResponse = JOCDefaultResponse.responseStatus200(dbItem.getContent(), getType(type));
                }

            } else {
                throw new DBMissingDataException(errMessage);
            }

            try { //simulates a touch
                jobschedulerUser.resetTimeOut();
            } catch (org.apache.shiro.session.InvalidSessionException e) {
            }
            return jocDefaultResponse;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
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
        case "pdf":
            type = "application/" + type;
            break;
        case "javascript":
        case "css":
            type = "text/" + type;
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
            type = "image/" + type;
            break;
        }
        if (type.startsWith("text/")) {
            type += "; charset=UTF-8";
        }
        return type;
    }

    private String createHTMLfromMarkdown(DBItemDocumentation dbItem) {
        java.nio.file.Path path = Paths.get(dbItem.getDirectory());
        String title = dbItem.getName().replaceFirst("\\.[^\\.]*$", ""); //filename without extension
        String cssFile = path.relativize(CSS).toString().replace('\\', '/');
        
        /*
         * Markdown can start with reference-style links which are invisible
         * these links can be used as header information for title and css
         * [css] : my.css
         * [title] : myTitle
         */
        Matcher markdownHeader = Pattern.compile("^((?:\\[[^\\]]+\\]\\p{Blank}*:.*\\r?\\n)+)").matcher(dbItem.getContent());
        while (markdownHeader.find()) {
            String[] lines = markdownHeader.group(1).split("\\r?\\n");
            Pattern p = Pattern.compile("\\[([^\\]]+)\\]\\p{Blank}*:(.*)");
            Matcher m;
            for (int i=0; i<lines.length; i++) {
                m = p.matcher(lines[i]);
                if (m.find()) {
                    if ("title".equals(m.group(1).toLowerCase())) {
                        title = m.group(2).trim();
                    }
                    if ("css".equals(m.group(1).toLowerCase())) {
                        cssFile = m.group(2).trim();
                    }
                }
            }
        }
        final Configuration conf = Configuration.builder().forceExtentedProfile().build();
        final String html = Processor.process(dbItem.getContent(), conf);

        boolean isCompleteHTML = false;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(html.getBytes(Charsets.UTF_8));
            String media = URLConnection.guessContentTypeFromStream(is);
            if (media != null && media.contains("html")) {
                isCompleteHTML = true; 
            }
        } catch (IOException e) {
        } finally {
            if (is != null) {
              try {
                is.close();
            } catch (IOException e) {
            }  
            }
        }

        if (isCompleteHTML) {
            return html;
        } else {
            StringBuilder s = new StringBuilder();
            s.append("<!DOCTYPE html>");
            s.append("<html>\n<head>\n");
            s.append("  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge, chrome=1\"/>\n");
            s.append("  <meta charset=\"utf-8\"/>\n");
            s.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, minimal-ui\"/>\n");
            s.append("  <link rel=\"stylesheet\" href=\"").append(cssFile).append("\"/>\n");
            s.append("  <title>").append(title).append("</title>\n");
            s.append("</head>\n<body>\n  <article class=\"markdown-body\">\n");
            s.append(html);
            s.append("  </article>\n</body>\n</html>");
            return s.toString();
        }
    }

}
