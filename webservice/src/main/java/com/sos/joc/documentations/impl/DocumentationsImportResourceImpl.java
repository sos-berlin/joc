package com.sos.joc.documentations.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ws.rs.Path;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import com.google.common.base.Charsets;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ImportDocumentationAudit;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsImportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.JocUnsupportedFileTypeException;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.docu.DocumentationImport;

@Path("documentations")
public class DocumentationsImportResourceImpl extends JOCResourceImpl implements IDocumentationsImportResource {

    private static final String API_CALL = "/documentations/import";
    private static final List<String> SUPPORTED_SUBTYPES = new ArrayList<String>(Arrays.asList("html", "xml", "xsl", "xsd", "javascript", "json",
            "css", "markdown", "gif", "jpeg", "png"));
    private static final List<String> SUPPORTED_IMAGETYPES = new ArrayList<String>(Arrays.asList("gif", "jpeg", "png"));
    private SOSHibernateSession connection = null;

    @Override
    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String accessToken, String jobschedulerId, String directory,
            FormDataBodyPart body, String timeSpent, String ticketLink, String comment) throws Exception {
        AuditParams auditLog = new AuditParams();
        auditLog.setComment(comment);
        auditLog.setTicketLink(ticketLink);
        try {
            auditLog.setTimeSpent(Integer.valueOf(timeSpent));
        } catch (Exception e) {
        }
        return postImportDocumentations(getAccessToken(xAccessToken, accessToken), jobschedulerId, directory, body, auditLog);
    }

    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String jobschedulerId, String directory, FormDataBodyPart body, AuditParams auditLog)
            throws Exception {

        InputStream stream = null;
        try {
            DocumentationImport filter = new DocumentationImport();
            filter.setJobschedulerId(jobschedulerId);
            if (directory == null || directory.isEmpty()) {
                directory = "/";
            }
            filter.setFolder(normalizeFolder(directory.replace('\\', '/')));
            if (body != null) {
                filter.setFile(body.getContentDisposition().getFileName());
            }
            filter.setAuditLog(auditLog);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, jobschedulerId, getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getDocumentation().isImport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            if (body == null) {
                throw new JocMissingRequiredParameterException("undefined 'file'");
            }

            stream = body.getEntityAs(InputStream.class);
            String extention = getExtensionFromFilename(filter.getFile());

            final String mediaSubType = body.getMediaType().getSubtype().replaceFirst("^x-", "");
            Optional<String> supportedSubType = SUPPORTED_SUBTYPES.stream().filter(s -> mediaSubType.contains(s)).findFirst();
            Optional<String> supportedImageType = SUPPORTED_IMAGETYPES.stream().filter(s -> mediaSubType.contains(s)).findFirst();

            ImportDocumentationAudit importAudit = new ImportDocumentationAudit(filter);
            logAuditMessage(importAudit);

            if (mediaSubType.contains("zip") && !mediaSubType.contains("gzip")) {
                readZipFileContent(stream, filter);
            } else if (supportedImageType.isPresent()) {
                saveOrUpdate(setDBItemDocumentationImage(IOUtils.toByteArray(stream), filter, supportedImageType.get()));
            } else if (supportedSubType.isPresent()) {
                if ("xml".equals(supportedSubType.get())) {
                    switch (extention) {
                    case "xsl":
                    case "xslt":
                        supportedSubType = Optional.of("xsl");
                        break;
                    case "xsd":
                        supportedSubType = Optional.of("xsd");
                        break;
                    }
                }
                saveOrUpdate(setDBItemDocumentation(IOUtils.toByteArray(stream), filter, supportedSubType.get()));
            } else if ("md".equals(extention) || "markdown".equals(extention)) {
                byte[] b = IOUtils.toByteArray(stream);
                if (isPlainText(b)) {
                    saveOrUpdate(setDBItemDocumentation(b, filter, "markdown"));
                } else {
                    throw new JocUnsupportedFileTypeException("Unsupported file type (" + mediaSubType + "), supported types are "
                            + SUPPORTED_SUBTYPES.toString());
                }
            } else {
                throw new JocUnsupportedFileTypeException("Unsupported file type (" + mediaSubType + "), supported types are " + SUPPORTED_SUBTYPES
                        .toString());
            }

            storeAuditLogEntry(importAudit);
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
            } catch (Exception e) {
            }
        }
    }

    private void saveOrUpdate(DocumentationDBLayer dbLayer, DBItemDocumentation doc) throws DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException {
        DBItemDocumentation docFromDB = dbLayer.getDocumentation(doc.getSchedulerId(), doc.getPath());
        if (docFromDB != null) {
            if (doc.hasImage()) {
                DBItemDocumentationImage imageFromDB = dbLayer.getDocumentationImage(docFromDB.getImageId());
                if (imageFromDB == null) {
                    // insert image if not exist
                    docFromDB.setImageId(saveImage(dbLayer, doc));
                } else {
                    // update image if hash unequal
                    String md5Hash = DigestUtils.md5Hex(doc.image());
                    if (!imageFromDB.getMd5Hash().equals(md5Hash)) {
                        imageFromDB.setMd5Hash(md5Hash);
                        imageFromDB.setImage(doc.image());
                        dbLayer.getSession().update(imageFromDB);
                    }
                }
            }
            docFromDB.setContent(doc.getContent());
            docFromDB.setType(doc.getType());
            docFromDB.setModified(Date.from(Instant.now()));
            dbLayer.getSession().update(docFromDB);
        } else {
            if (doc.hasImage()) {
                // insert image
                doc.setImageId(saveImage(dbLayer, doc));
            }
            dbLayer.getSession().save(doc);
        }
    }

    private void saveOrUpdate(DBItemDocumentation doc) throws DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException,
            JocConfigurationException {
        if (connection == null) {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
        saveOrUpdate(dbLayer, doc);
    }

    private Long saveImage(DocumentationDBLayer dbLayer, DBItemDocumentation doc) throws SOSHibernateException {
        DBItemDocumentationImage image = new DBItemDocumentationImage();
        image.setSchedulerId(doc.getSchedulerId());
        image.setImage(doc.image());
        image.setMd5Hash(DigestUtils.md5Hex(doc.image()));
        dbLayer.getSession().save(image);
        return image.getId();
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

    private void readZipFileContent(InputStream inputStream, DocumentationImport filter) throws DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, IOException, JocUnsupportedFileTypeException, JocConfigurationException {
        ZipInputStream zipStream = null;
        Set<DBItemDocumentation> documentations = new HashSet<DBItemDocumentation>();
        try {
            zipStream = new ZipInputStream(inputStream);
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                DBItemDocumentation documentation = new DBItemDocumentation();
                documentation.setSchedulerId(filter.getJobschedulerId());
                java.nio.file.Path targetFolder = Paths.get(filter.getFolder());
                java.nio.file.Path complete = targetFolder.resolve(entry.getName().replace('\\', '/').replaceFirst("^/", ""));
                documentation.setPath(complete.toString().replace('\\', '/'));
                documentation.setDirectory(complete.getParent().toString().replace('\\', '/'));
                documentation.setName(complete.getFileName().toString());
                String fileExtension = getExtensionFromFilename(documentation.getName());
                ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
                byte[] binBuffer = new byte[8192];
                int binRead = 0;
                while ((binRead = zipStream.read(binBuffer, 0, 8192)) >= 0) {
                    outBuffer.write(binBuffer, 0, binRead);
                }
                byte[] bytes = outBuffer.toByteArray();
                boolean isPlainText = isPlainText(bytes);
                final String guessedMediaType = guessContentTypeFromBytes(bytes, fileExtension, isPlainText);
                if (guessedMediaType != null) {
                    Optional<String> supportedSubType = SUPPORTED_SUBTYPES.stream().filter(s -> guessedMediaType.contains(s)).findFirst();
                    Optional<String> supportedImageType = SUPPORTED_IMAGETYPES.stream().filter(s -> guessedMediaType.contains(s)).findFirst();
                    if (supportedImageType.isPresent()) {
                        documentation.setType(supportedImageType.get());
                        documentation.setImage(bytes);
                        documentation.setHasImage(true);
                    } else if (supportedSubType.isPresent()) {
                        documentation.setType(supportedSubType.get());
                        documentation.setContent(new String(bytes, Charsets.UTF_8));
                        documentation.setHasImage(false);
                    } else {
                        throw new JocUnsupportedFileTypeException(String.format("%1$s unsupported, supported types are %2$s", complete.toString()
                                .replace('\\', '/'), SUPPORTED_SUBTYPES.toString()));
                    }
                } else {
                    throw new JocUnsupportedFileTypeException(String.format("%1$s unsupported, supported types are %2$s", complete.toString().replace(
                            '\\', '/'), SUPPORTED_SUBTYPES.toString()));
                }
                documentation.setCreated(Date.from(Instant.now()));
                documentation.setModified(documentation.getCreated());
                documentations.add(documentation);
            }
            if (!documentations.isEmpty()) {
                if (connection == null) {
                    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                }
                DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
                for (DBItemDocumentation itemDocumentation : documentations) {
                    saveOrUpdate(dbLayer, itemDocumentation);
                }
            } else {
                throw new JocUnsupportedFileTypeException("The zip file to upload doesn't contain any supported file, supported types are "
                        + SUPPORTED_SUBTYPES.toString());
            }
        } finally {
            if (zipStream != null) {
                try {
                    zipStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private DBItemDocumentation setDBItemDocumentation(byte[] b, DocumentationImport filter, String mediaSubType) throws IOException,
            JocUnsupportedFileTypeException {
        DBItemDocumentation documentation = new DBItemDocumentation();
        documentation.setSchedulerId(filter.getJobschedulerId());
        documentation.setDirectory(filter.getFolder());
        documentation.setName(filter.getFile());
        documentation.setPath(filter.getFolder() + "/" +filter.getFile());
        documentation.setCreated(Date.from(Instant.now()));
        documentation.setModified(documentation.getCreated());
        documentation.setType(mediaSubType);
        documentation.setContent(new String(b, Charsets.UTF_8));
        documentation.setHasImage(false);
        return documentation;
    }

    private DBItemDocumentation setDBItemDocumentationImage(byte[] b, DocumentationImport filter, String mediaSubType) throws IOException,
            JocUnsupportedFileTypeException {
        DBItemDocumentation documentation = new DBItemDocumentation();
        documentation.setSchedulerId(filter.getJobschedulerId());
        documentation.setDirectory(filter.getFolder());
        documentation.setName(filter.getFile());
        documentation.setPath(filter.getFolder() + "/" +filter.getFile());
        documentation.setCreated(Date.from(Instant.now()));
        documentation.setModified(documentation.getCreated());
        documentation.setType(mediaSubType);
        documentation.setImage(b);
        documentation.setHasImage(true);
        return documentation;
    }

    private String guessContentTypeFromBytes(byte[] b, String extension, boolean isPlainText) throws IOException {
        InputStream is = null;
        String media = null;
        try {
            is = new ByteArrayInputStream(b);
            media = URLConnection.guessContentTypeFromStream(is);
            if (media != null) {
                media = media.replaceFirst("^.*\\/(x-)?", "");
            }
            if (media == null && isPlainText) {
                switch (extension) {
                case "js":
                    media = "javascript";
                    break;
                case "json":
                    media = "json";
                    break;
                case "css":
                    media = "css";
                    break;
                case "md":
                case "markdown":
                    media = "markdown";
                    break;
                case "txt":
                    media = "plain";
                    break;
                }
            } else if (media != null && media.contains("xml")) {
                switch (extension) {
                case "xsl":
                case "xslt":
                    media = "xsl";
                    break;
                case "xsd":
                    media = "xsd";
                    break;
                default:
                    media = "xml";
                }
            }
            return media;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private boolean isPlainText(byte[] b) {
        try {
            Charset.availableCharsets().get("UTF-8").newDecoder().decode(ByteBuffer.wrap(b));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }

}
