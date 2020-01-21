package com.sos.joc.documentations.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.common.DeleteTempFile;
import com.sos.joc.db.documentation.DocumentationContent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsExportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObject;
import com.sos.joc.model.docu.DeployDocumentation;
import com.sos.joc.model.docu.DeployDocumentations;
import com.sos.joc.model.docu.DocumentationsFilter;
import com.sos.joc.model.docu.ExportInfo;
import com.sos.schema.JsonValidator;

@Path("documentations")
public class DocumentationsExportResourceImpl extends JOCResourceImpl implements IDocumentationsExportResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationsExportResourceImpl.class);
    private static final String API_CALL = "./documentations/export";
    public static final String DEPLOY_USAGE_JSON = "/sos-documentation-usages.json";

    @Override
    public JOCDefaultResponse postExportDocumentations(String xAccessToken, byte[] filterBytes) {

        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(filterBytes, DocumentationsFilter.class);
            DocumentationsFilter filter = Globals.objectMapper.readValue(filterBytes, DocumentationsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getDocumentation().isExport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            String targetFilename = "documentation_" + filter.getJobschedulerId() + ".zip";

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            final List<DocumentationContent> contents = mapToDocumentationContents(filter, connection);
            StreamingOutput streamingOutput = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    ZipOutputStream zipOut = null;
                    try {
                        zipOut = new ZipOutputStream(new BufferedOutputStream(output), Charsets.UTF_8);
                        for (DocumentationContent content : contents) {
                            ZipEntry entry = new ZipEntry(content.getPath().substring(1));
                            zipOut.putNextEntry(entry);
                            zipOut.write(content.getContent());
                            zipOut.closeEntry();
                        }
                        zipOut.flush();
                    } finally {
                        if (zipOut != null) {
                            try {
                                zipOut.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            };
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(streamingOutput, targetFilename);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse getExportDocumentations(String xAccessToken, String accessToken, String jobschedulerId, String filename) {
        try {
            xAccessToken = getAccessToken(xAccessToken, accessToken);
            ExportInfo file = new ExportInfo();
            file.setFilename(filename);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, file, xAccessToken, jobschedulerId, getPermissonsJocCockpit(jobschedulerId,
                    xAccessToken).getDocumentation().isExport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", jobschedulerId);
            checkRequiredParameter("filename", filename);

            java.nio.file.Path path = Paths.get(System.getProperty("java.io.tmpdir"), filename);
            if (!Files.isReadable(path)) {
                throw new JobSchedulerObjectNotExistException("Temp. file '" + filename + "' not found.");
            }

            final java.nio.file.Path downPath = Files.move(path, path.getParent().resolve(path.getFileName().toString() + ".zip"),
                    StandardCopyOption.ATOMIC_MOVE);

            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(downPath);
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {
                            }
                        }
                        try {
                            Files.deleteIfExists(downPath);
                        } catch (Exception e) {
                        }
                    }
                }
            };

            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, "sos-documentation-" + jobschedulerId + ".zip");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postExportInfo(String xAccessToken, byte[] filterBytes) {
        SOSHibernateSession connection = null;
        ZipOutputStream zipOut = null;
        try {
            JsonValidator.validateFailFast(filterBytes, DocumentationsFilter.class);
            DocumentationsFilter filter = Globals.objectMapper.readValue(filterBytes, DocumentationsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "/info", filter, xAccessToken, filter.getJobschedulerId(),
                    getPermissonsJocCockpit(filter.getJobschedulerId(), xAccessToken).getDocumentation().isExport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL + "/info");
            List<DocumentationContent> contents = mapToDocumentationContents(filter, connection);

            java.nio.file.Path path = Files.createTempFile("sos-download-", ".zip.tmp");
            zipOut = new ZipOutputStream(Files.newOutputStream(path));
            for (DocumentationContent content : contents) {
                ZipEntry entry = new ZipEntry(content.getPath().substring(1));
                zipOut.putNextEntry(entry);
                zipOut.write(content.getContent());
                zipOut.closeEntry();
            }
            zipOut.flush();

            ExportInfo entity = new ExportInfo();
            entity.setFilename(path.getFileName().toString());
            entity.setDeliveryDate(Date.from(Instant.now()));

            DeleteTempFile runnable = new DeleteTempFile(path);
            new Thread(runnable).start();

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private List<DocumentationContent> mapToDocumentationContents(DocumentationsFilter filter, SOSHibernateSession connection)
            throws DBConnectionRefusedException, DBInvalidDataException, JocMissingRequiredParameterException, JsonProcessingException,
            DBMissingDataException {
        DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
        List<DBItemDocumentation> docs = getDocsFromDb(dbLayer, filter);
        List<DocumentationContent> contents = new ArrayList<DocumentationContent>();
        DocumentationContent usagesJson = getDeployUsageData(filter.getJobschedulerId(), dbLayer, docs.stream().collect(Collectors.mapping(
                DBItemDocumentation::getPath, Collectors.toSet())));
        if (usagesJson != null) {
            contents.add(usagesJson);
        }

        for (DBItemDocumentation doc : docs) {
            DocumentationContent content = null;
            if (doc.getContent() != null) {
                content = new DocumentationContent(doc.getPath(), doc.getContent().getBytes(Charsets.UTF_8));
            } else {
                DBItemDocumentationImage image = dbLayer.getDocumentationImage(doc.getImageId());
                if (image != null) {
                    content = new DocumentationContent(doc.getPath(), image.getImage());
                }
            }
            if (content != null) {
                contents.add(content);
            }
        }
        return contents;
    }
    
    private DocumentationContent getDeployUsageData(String jobschedulerId, DocumentationDBLayer dbLayer, Collection<String> docPaths)
            throws DBConnectionRefusedException, DBInvalidDataException, JsonProcessingException {
        try {
            DeployDocumentations docUsages = new DeployDocumentations();
            docUsages.setJobschedulerId(jobschedulerId);
            List<DeployDocumentation> docUsageList = new ArrayList<DeployDocumentation>();
            Map<String, List<JobSchedulerObject>> docUsageMap = dbLayer.getDocumentationUsages(jobschedulerId, docPaths);
            for (Entry<String, List<JobSchedulerObject>> entry : docUsageMap.entrySet()) {
                DeployDocumentation docUsage = new DeployDocumentation();
                docUsage.setDocumentation(entry.getKey());
                docUsage.setObjects(entry.getValue());
                docUsageList.add(docUsage);
            }
            docUsages.setDocumentations(docUsageList);
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return new DocumentationContent(DEPLOY_USAGE_JSON, objMapper.writeValueAsBytes(docUsages));
        } catch (Exception e) {
            LOGGER.warn("Problem at export documentation usages", e);
            return null;
        }
    }

    private List<DBItemDocumentation> getDocsFromDb(DocumentationDBLayer dbLayer, DocumentationsFilter filter)
            throws JocMissingRequiredParameterException, DBConnectionRefusedException, DBInvalidDataException, DBMissingDataException {
        List<DBItemDocumentation> docs = new ArrayList<DBItemDocumentation>();
        if (filter.getDocumentations() != null && !filter.getDocumentations().isEmpty()) {
            docs = dbLayer.getDocumentations(filter.getJobschedulerId(), filter.getDocumentations());
        } else if (filter.getFolders() != null && !filter.getFolders().isEmpty()) {
            for (Folder folder : filter.getFolders()) {
                docs.addAll(dbLayer.getDocumentations(filter.getJobschedulerId(), null, folder.getFolder(), folder.getRecursive()));
            }
        } else {
            throw new JocMissingRequiredParameterException("Neither 'documentations' nor 'folders' are specified!");
        }
        if (docs == null || docs.isEmpty()) {
            throw new DBMissingDataException("No 'documentations' found!");
        }
        return docs;
    }

}
