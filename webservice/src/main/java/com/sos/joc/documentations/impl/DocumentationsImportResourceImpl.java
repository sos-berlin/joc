package com.sos.joc.documentations.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.ByteArrayBuffer;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.documentations.resource.IDocumentationsImportResource;

@Path("/documentations/import")
public class DocumentationsImportResourceImpl extends JOCResourceImpl implements IDocumentationsImportResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationsImportResourceImpl.class);

    @Override
    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String jobschedulerId, String directory, FormDataBodyPart body)
            throws Exception {
        StringBuilder strb = new StringBuilder();
        Set<DBItemDocumentation> documentations = new HashSet<DBItemDocumentation>();
        strb.append("JobSchedulerId:   ").append(jobschedulerId).append("\n");
        strb.append("target directory: ").append(directory).append("\n");
        InputStream stream = body.getEntityAs(InputStream.class);
        if (body.getMediaType().toString().contains("zip")) {
            readZipFileContent(stream, jobschedulerId, directory, documentations, body, strb);
        } else {
            readFileContent(stream, jobschedulerId, directory, documentations, body, strb);
        }
        stream.close();

        return JOCDefaultResponse.responsePlainStatus200(strb.toString());
    }

    private String getTypeFromMediaTypeLowerCase (String mediaType) {
        if (mediaType.contains("html")) {
            return "html";
        } else if (mediaType.contains("xml")) {
            return "xml";
        } else if (mediaType.contains("text")) {
            return "text";
        } else if (mediaType.contains("json")) {
            return "json";
        }
        return null;
    }

    private String getTypeFromFileExtension(String filename) {
        if (filename.contains(".")) {
            String[] split = filename.split("\\.");
            return split[split.length -1];
        }
        return null;
    }
    
    private void readZipFileContent(InputStream inputStream, String jobschedulerId, String directory, Set<DBItemDocumentation> documentations,
            FormDataBodyPart body, StringBuilder strb) {
        ZipInputStream zipStream = null;
        try {
            strb.append("Media Type:       ").append(body.getMediaType()).append("\n");
            strb.append("FileName:         ").append(body.getContentDisposition().getFileName()).append("\n");
            strb.append("Size:             ").append(body.getContentDisposition().getSize()).append("\n");
            zipStream = new ZipInputStream(inputStream);
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry())!= null) {
                if (entry.isDirectory()) {
                    continue;
                }
                DBItemDocumentation documentation = new DBItemDocumentation();
                documentation.setSchedulerId(jobschedulerId);
                java.nio.file.Path targetFolder = null;
                if (directory != null) {
                    targetFolder = Paths.get(normalizeFolder(directory));    
                } else {
                    targetFolder = Paths.get("/");
                }
                strb.append("\n");
                strb.append("     Zip Entry Name:              ").append(entry.getName()).append("\n");
                java.nio.file.Path complete = targetFolder.resolve(entry.getName());
                documentation.setDirectory(complete.getParent().toString().replace('\\', '/'));
                documentation.setName(complete.getFileName().toString());
                documentation.setType(getTypeFromFileExtension(documentation.getName()));
                strb.append("     Zip Entry is Dir:            ").append(entry.isDirectory()).append("\n");
                strb.append("     Zip Entry last modified:     ").append(entry.getLastModifiedTime()).append("\n");
                strb.append("     Zip Entry size:              ").append(entry.getSize()).append(" bytes").append("\n");
               if (!entry.isDirectory()) {
                   String fileExtension = getTypeFromFileExtension(documentation.getName());
                   switch (fileExtension) {
                   case "css":
                   case "js":
                   case "json":
                   case "html":
                   case "xhtml":
                   case "htm":
                   case "xml":
                   case "xslt":
                   case "xsl":
                   case "txt":
                   case "md":
                   case "markdown":
                       StringBuilder s = new StringBuilder();
                       byte[] buffer = new byte[8192];
                       int read = 0;
                       while ((read = zipStream.read(buffer, 0, 8192)) >= 0) {
                            s.append(new String(buffer, 0, read));
                       }
                       documentation.setContent(s.toString());
                        strb.append("Content:").append("\n").append(documentation.getContent());
                        break;
                   default:
                       ByteArrayBuffer outBuffer = new ByteArrayBuffer(8192);
                       byte[] binBuffer = new byte[8192];
                       int binRead = 0;
                       while ((binRead = zipStream.read(binBuffer, 0, 8192)) >= 0) {
                           outBuffer.append(binBuffer, 0, binRead);
                       }
                       documentation.setImage(outBuffer.toByteArray());
                       break;
                   }
                }
                documentations.add(documentation);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zipStream != null) {
                try {
                    zipStream.close();
                } catch (IOException e) {}
            }
        }
    }
    
    private void readFileContent(InputStream inputStream, String jobschedulerId, String directory, Set<DBItemDocumentation> documentations,
            FormDataBodyPart body, StringBuilder strb) {
        DBItemDocumentation documentation = new DBItemDocumentation();
        documentation.setSchedulerId(jobschedulerId);
        documentation.setDirectory(directory);
        documentation.setType(getTypeFromMediaTypeLowerCase(body.getMediaType().toString().toLowerCase()));
        strb.append("Media Type: ").append(body.getMediaType()).append("\n");
        strb.append("FileName:   ").append(body.getContentDisposition().getFileName()).append("\n");
        documentation.setName(body.getContentDisposition().getFileName());
        strb.append("Size:       ").append(body.getContentDisposition().getSize()).append("\n");
        MultivaluedMap<String, String> headers = body.getHeaders();
        strb.append("\n").append("Headers:    ").append("\n");
        headers.keySet().forEach(key -> strb.append("    ").append(key).append(":    ").append(headers.get(key)).append("\n"));
        try {
            documentation.setContent(IOUtils.toString(inputStream, Charsets.UTF_8.toString()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }           
        strb.append("Content:").append("\n").append(documentation.getContent());
        documentations.add(documentation);
    }

}
