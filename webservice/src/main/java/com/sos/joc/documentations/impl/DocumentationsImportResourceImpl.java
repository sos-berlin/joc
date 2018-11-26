package com.sos.joc.documentations.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ws.rs.Path;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.ByteArrayBuffer;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsImportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocUnsupportedFileTypeException;
import com.sos.joc.model.docu.DocumentationImport;

@Path("/documentations/import")
public class DocumentationsImportResourceImpl extends JOCResourceImpl implements IDocumentationsImportResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationsImportResourceImpl.class);
    private static final String API_CALL = "/documentations/import";
    private boolean hasUnsupportedFiles = false;

    @Override
    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String jobschedulerId, String directory, FormDataBodyPart body)
            throws Exception {
        DocumentationImport filter = new DocumentationImport();
        filter.setJobschedulerId(jobschedulerId);
        filter.setFolder(directory);
        filter.setFile(body.getContentDisposition().getFileName());
//        JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken,
//                jobschedulerId, getPermissonsJocCockpit(jobschedulerId, xAccessToken).getDocumentationImport().getView().isStatus());
//        if (jocDefaultResponse != null) {
//            return jocDefaultResponse;
//        }
        SOSHibernateSession connection = null;
        InputStream stream = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection("/documentations/import");
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            Set<DBItemDocumentation> documentations = new HashSet<DBItemDocumentation>();
            stream = body.getEntityAs(InputStream.class);
            if (body.getMediaType().toString().contains("zip")) {
                readZipFileContent(stream, jobschedulerId, directory, documentations, body, dbLayer);
                if (hasUnsupportedFiles) {
                    throw new JocUnsupportedFileTypeException("The zip file to upload contains at least one unsupported file type, upload is rejected!");
                }
            } else {
                readFileContent(stream, jobschedulerId, directory, documentations, body);
                if (hasUnsupportedFiles) {
                    throw new JocUnsupportedFileTypeException("The file to upload is an unsupported file type, upload is rejected!");
                }
            }
            for (DBItemDocumentation doc : documentations) {
                DBItemDocumentation docFromDB = dbLayer.getDocumentation(doc.getSchedulerId(), doc.getDirectory(), doc.getName());
                if (docFromDB != null) {
                   docFromDB.setContent(doc.getContent());
                   docFromDB.setImageId(doc.getImageId());
                   docFromDB.setType(doc.getType());
                   docFromDB.setModified(Date.from(Instant.now()));
                   connection.update(docFromDB);
                } else {
                    connection.save(doc);
                }
            }
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {}
        }
    }

    private String getExtensionFromFilename(String filename) {
        String extension = filename;
        if (extension.contains(".")) {
            extension = extension.replaceFirst(".*\\.([^\\.]+)$", "$1");
        } else {
            extension = "";
        }
        return extension;
    }
    
    private void readZipFileContent(InputStream inputStream, String jobschedulerId, String directory, Set<DBItemDocumentation> documentations,
            FormDataBodyPart body, DocumentationDBLayer dbLayer) {
        ZipInputStream zipStream = null;
        try {
            zipStream = new ZipInputStream(inputStream);
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null) {
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
                java.nio.file.Path complete = targetFolder.resolve(entry.getName());
                documentation.setDirectory(complete.getParent().toString().replace('\\', '/'));
                documentation.setName(complete.getFileName().toString());
                if (!entry.isDirectory()) {
                    String fileExtension = getExtensionFromFilename(documentation.getName());
                    ByteArrayBuffer outBuffer = new ByteArrayBuffer(8192);
                    byte[] binBuffer = new byte[8192];
                    int binRead = 0;
                    while ((binRead = zipStream.read(binBuffer, 0, 8192)) >= 0) {
                        outBuffer.append(binBuffer, 0, binRead);
                    }
                    byte[] bytes = outBuffer.toByteArray();
                    documentation.setType(guessContentTypeFromBytes(bytes, fileExtension));
                    if (documentation.getType() != null) {
                        if (documentation.getType().startsWith("image")) {
                            DBItemDocumentationImage image = new DBItemDocumentationImage();
                            image.setSchedulerId(jobschedulerId);
                            image.setImage(bytes);
                            try {
                                DBItemDocumentationImage imageFromDB = dbLayer.getDocumentationImage(image.getSchedulerId(), image.getImage());
                                if (imageFromDB != null) {
                                    documentation.setImageId(imageFromDB.getId());
                                } else {
                                    dbLayer.getSession().save(image);
                                    documentation.setImageId(image.getId());
                                }
                            } catch (SOSHibernateException | DBConnectionRefusedException | DBInvalidDataException e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                        } else {                                
                            documentation.setContent(new String(bytes));
                        }
                    } else {
                        hasUnsupportedFiles = true;
                        return;
                    }
                }
                documentation.setCreated(Date.from(Instant.now()));
                documentation.setModified(Date.from(Instant.now()));
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
            FormDataBodyPart body) {
        DBItemDocumentation documentation = new DBItemDocumentation();
        documentation.setSchedulerId(jobschedulerId);
        documentation.setDirectory(directory);
        documentation.setName(body.getContentDisposition().getFileName());
        documentation.setCreated(Date.from(Instant.now()));
        documentation.setModified(Date.from(Instant.now()));
        try {
            documentation.setType(guessContentTypeFromBytes(IOUtils.toByteArray(inputStream), getExtensionFromFilename(documentation.getName())));
            if (documentation.getType() == null) {
                hasUnsupportedFiles = true;
            }
            documentation.setContent(IOUtils.toString(inputStream, Charsets.UTF_8.toString()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }           
        documentations.add(documentation);
    }

    private String guessContentTypeFromBytes(byte[] b, String extension) throws IOException {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(b);
            String s = URLConnection.guessContentTypeFromStream(is);
            if (s == null && isTextPlain(b)) {
                switch (extension) {
                case "js":
                    return "text/javascript";
                case "json":
                    return "application/json";
                case "css":
                    return "text/css";
                case "md":
                case "markdown":
                    return "text/markdown";
                case "txt":
                    return "text/plain";
                default:
                    return null;    
                }
            } else if (s != null && s.equals("application/xml")) {
                switch (extension) {
                case "xsl":
                case "xslt":
                    return "application/xsl";
                case "xsd":
                    return "application/xsd";
                default:
                    return s;   
                }
            }
            return s;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private boolean isTextPlain(byte[] b) {
        try {
            Charset.availableCharsets().get("UTF-8").newDecoder().decode(ByteBuffer.wrap(b));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }
}
