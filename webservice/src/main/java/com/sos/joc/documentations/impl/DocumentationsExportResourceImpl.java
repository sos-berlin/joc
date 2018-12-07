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
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

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
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.docu.DocumentationsFilter;
import com.sos.joc.model.docu.ExportInfo;

@Path("documentations")
public class DocumentationsExportResourceImpl extends JOCResourceImpl implements IDocumentationsExportResource {

    private static final String API_CALL = "./documentations/export";

    @Override
    public JOCDefaultResponse postExportDocumentations(String xAccessToken, DocumentationsFilter filter) throws Exception {

        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getDocumentation().isExport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            String targetFilename = "documentation_" + filter.getJobschedulerId() + ".zip";

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = getDocsFromDb(dbLayer, filter);
            final List<DocumentationContent> contents = mapToDocumentationContents(docs, dbLayer);
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

    private List<DocumentationContent> mapToDocumentationContents(List<DBItemDocumentation> docs, DocumentationDBLayer dbLayer)
            throws DBConnectionRefusedException, DBInvalidDataException {
        List<DocumentationContent> contents = new ArrayList<DocumentationContent>();
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

    @Override
    public JOCDefaultResponse getExportDocumentations(String xAccessToken, String accessToken, String jobschedulerId, String filename)
            throws Exception {
        try {
            xAccessToken = getAccessToken(xAccessToken, accessToken);
            ExportInfo file = new ExportInfo();
            file.setFilename(filename);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, file, xAccessToken, jobschedulerId, getPermissonsJocCockpit(
                    jobschedulerId, xAccessToken).getDocumentation().isExport());
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
    public JOCDefaultResponse postExportInfo(String xAccessToken, DocumentationsFilter filter) throws Exception {
        SOSHibernateSession connection = null;
        ZipOutputStream zipOut = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "/info", filter, xAccessToken, filter.getJobschedulerId(),
                    getPermissonsJocCockpit(filter.getJobschedulerId(), xAccessToken).getDocumentation().isExport());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL + "/info");
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = getDocsFromDb(dbLayer, filter);
            List<DocumentationContent> contents = mapToDocumentationContents(docs, dbLayer);

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

    private List<DBItemDocumentation> getDocsFromDb(DocumentationDBLayer dbLayer, DocumentationsFilter filter)
            throws JocMissingRequiredParameterException, DBConnectionRefusedException, DBInvalidDataException {
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
        return docs;
    }

}
