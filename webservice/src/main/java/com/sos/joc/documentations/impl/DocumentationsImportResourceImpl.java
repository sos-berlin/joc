package com.sos.joc.documentations.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
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

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ImportDocumentationAudit;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsImportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.JocUnsupportedFileTypeException;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.JobSchedulerObject;
import com.sos.joc.model.docu.DeployDocumentation;
import com.sos.joc.model.docu.DeployDocumentations;
import com.sos.joc.model.docu.DocumentationImport;
import com.sos.schema.JsonValidator;

@Path("documentations")
public class DocumentationsImportResourceImpl extends JOCResourceImpl implements IDocumentationsImportResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationsImportResourceImpl.class);
    private static final String API_CALL = "./documentations/import";
    private static final List<String> SUPPORTED_SUBTYPES = new ArrayList<String>(Arrays.asList("html", "xml", "pdf", "xsl", "xsd", "javascript",
            "json", "css", "markdown", "gif", "jpeg", "png"));
    private static final List<String> SUPPORTED_IMAGETYPES = new ArrayList<String>(Arrays.asList("pdf", "gif", "jpeg", "png"));
    private SOSHibernateSession connection = null;
    private DeployDocumentations deployDocumentations = null;

    @Override
    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String accessToken, String jobschedulerId, String directory,
            FormDataBodyPart body, String timeSpent, String ticketLink, String comment) throws Exception {
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (comment != null) {
            builder.add("comment", comment);
        }
        if (ticketLink != null) {
            builder.add("ticketLink", ticketLink);
        }
        if (timeSpent != null) {
            try {
                builder.add("timeSpent", Integer.valueOf(timeSpent));
            } catch (Exception e) {
            }
        }
        String json = builder.build().toString();
        JsonValidator.validateFailFast(json.getBytes(), AuditParams.class);
        AuditParams auditLog = Globals.objectMapper.readValue(json, AuditParams.class);
        return postImportDocumentations(getAccessToken(xAccessToken, accessToken), jobschedulerId, directory, body, auditLog);
    }

    public JOCDefaultResponse postImportDocumentations(String xAccessToken, String jobschedulerId, String directory, FormDataBodyPart body,
            AuditParams auditLog) throws Exception {

        InputStream stream = null;
        try {
            if (directory == null || directory.isEmpty()) {
                directory = "/";
            } else {
                directory = normalizeFolder(directory.replace('\\', '/'));
            }
            
            String file = "";
            if (body != null) {
                file = URLDecoder.decode(body.getContentDisposition().getFileName(), "UTF-8");
            }
            //String json = String.format("{\"jobschedulerId\": \"%s\", \"folder\": \"%s\", \"file\": \"%s\"}", jobschedulerId, directory, file);
            JsonObjectBuilder builder = Json.createObjectBuilder();
            if(jobschedulerId != null) {
                builder.add("jobschedulerId", jobschedulerId);
            }
            if (directory != null) {
                builder.add("folder", directory);
            }
            if (file != null) {
                builder.add("file", file);
            }
            String json = builder.build().toString();
            JsonValidator.validateFailFast(json.getBytes(), DocumentationImport.class);
            DocumentationImport filter = Globals.objectMapper.readValue(json, DocumentationImport.class);
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
                if (("/"+filter.getFile()).equals(DocumentationsExportResourceImpl.DEPLOY_USAGE_JSON)) {
                    setDeployDocumentations(IOUtils.toByteArray(stream));
                } else {
                    saveOrUpdate(setDBItemDocumentation(IOUtils.toByteArray(stream), filter, supportedSubType.get()));
                }
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
            
            deployDocumentations();

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

    private void deployDocumentations() throws JocException {
        if (deployDocumentations != null && deployDocumentations.getDocumentations() != null && !deployDocumentations.getDocumentations().isEmpty()) {
            try {
                if (connection == null) {
                    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                }
                DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
                for (DeployDocumentation deployDocumentation : deployDocumentations.getDocumentations()) {
                    if (deployDocumentation.getObjects() == null || deployDocumentation.getObjects().isEmpty()) {
                       continue; 
                    }
                    Long documentationId = dbLayer.getDocumentationId(dbItemInventoryInstance.getSchedulerId(), deployDocumentation.getDocumentation());
                    if (documentationId != null) {
                        List<DBItemDocumentationUsage> oldUsages = dbLayer.getDocumentationUsages(dbItemInventoryInstance.getSchedulerId(), documentationId);
                        for (JobSchedulerObject jsObj : deployDocumentation.getObjects()) {
                            DBItemDocumentationUsage newUsage = new DBItemDocumentationUsage();
                            newUsage.setDocumentationId(documentationId);
                            newUsage.setObjectType(jsObj.getType().name());
                            newUsage.setPath(jsObj.getPath());
                            newUsage.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
                            if (oldUsages.contains(newUsage)) {
                               continue; 
                            }
                            newUsage.setCreated(Date.from(Instant.now()));
                            newUsage.setModified(newUsage.getCreated());
                            dbLayer.getSession().save(newUsage);
                        }
                    }
                }
            } catch (JocConfigurationException | DBOpenSessionException | DBConnectionRefusedException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.warn("Problem at import documentation usages", e);
            }
        }
    }
    
    private void setDeployDocumentations(byte[] b) {
        try {
            deployDocumentations = new ObjectMapper().readValue(b, DeployDocumentations.class);
        } catch (Exception e) {
            LOGGER.warn("Problem at import documentation usages", e);
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
            JocConfigurationException, DBOpenSessionException {
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
        if (filename == null) {
            return "";
        }
        if (extension.contains(".")) {
            extension = extension.replaceFirst(".*\\.([^\\.]+)$", "$1");
        } else {
            extension = "";
        }
        return extension.toLowerCase();
    }

    private void readZipFileContent(InputStream inputStream, DocumentationImport filter) throws DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, IOException, JocUnsupportedFileTypeException, JocConfigurationException, DBOpenSessionException {
        ZipInputStream zipStream = null;
        Set<DBItemDocumentation> documentations = new HashSet<DBItemDocumentation>();
        try {
            zipStream = new ZipInputStream(inputStream);
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                String entryName = entry.getName().replace('\\', '/');
                if (entryName.endsWith(DocumentationsExportResourceImpl.DEPLOY_USAGE_JSON) && !("/"+entryName).equals(
                        DocumentationsExportResourceImpl.DEPLOY_USAGE_JSON)) {
                    continue;
                }
                ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
                byte[] binBuffer = new byte[8192];
                int binRead = 0;
                while ((binRead = zipStream.read(binBuffer, 0, 8192)) >= 0) {
                    outBuffer.write(binBuffer, 0, binRead);
                }
                byte[] bytes = outBuffer.toByteArray();

                if (("/"+entryName).equals(DocumentationsExportResourceImpl.DEPLOY_USAGE_JSON)) {
                    setDeployDocumentations(bytes);
                    continue;
                }
                DBItemDocumentation documentation = new DBItemDocumentation();
                documentation.setSchedulerId(filter.getJobschedulerId());
                java.nio.file.Path targetFolder = Paths.get(filter.getFolder());
                java.nio.file.Path complete = targetFolder.resolve(entryName.replaceFirst("^/", ""));
                documentation.setPath(complete.toString().replace('\\', '/'));
                documentation.setDirectory(complete.getParent().toString().replace('\\', '/'));
                documentation.setName(complete.getFileName().toString());
                String fileExtension = getExtensionFromFilename(documentation.getName());
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
        documentation.setPath((filter.getFolder() + "/" + filter.getFile()).replaceAll("//+", "/"));
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
        documentation.setPath((filter.getFolder() + "/" + filter.getFile()).replaceAll("//+", "/"));
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
                case "html":
                case "xhtml":
                case "htm":
                    media = "html";
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
            } else if (media == null && !isPlainText) {
                if ("pdf".equals(extension)) {
                    media = "pdf";
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
