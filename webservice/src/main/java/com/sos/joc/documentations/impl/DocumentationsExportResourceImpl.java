package com.sos.joc.documentations.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationContent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsExportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.docu.DocumentationsFilter;

@Path("documentations")
public class DocumentationsExportResourceImpl extends JOCResourceImpl implements IDocumentationsExportResource {

    private static final String API_CALL = "/documentations/export";

    @Override
    public JOCDefaultResponse postExportDocumentations(String xAccessToken, DocumentationsFilter filter) throws Exception {

        // TODO: permissions
        JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), true);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
        SOSHibernateSession connection = null;
        List<String> documentations = filter.getDocumentations();
        List<Folder> folders = filter.getFolders();
        String targetFilename = "documentation_" + filter.getJobschedulerId() + ".zip";
        StreamingOutput out = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = new ArrayList<DBItemDocumentation>();
            if (documentations != null && !documentations.isEmpty()) {
                for (String documentation : documentations) {
                    String folder = Paths.get(documentation).getParent().toString().replace('\\', '/');
                    docs.addAll(dbLayer.getDocumentations(filter.getJobschedulerId(), folder));
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    docs.addAll(dbLayer.getDocumentations(filter.getJobschedulerId(), folder.getFolder(), folder.getRecursive()));
                }
            } else {
                throw new JocMissingRequiredParameterException("Neither 'documents' nor 'folders' are specified!");
            }
            List<DocumentationContent> contents = mapToDocumentationContents(docs, dbLayer);
            out = createZipOutputStreamForDownload(contents);
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(out, targetFilename);
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
            java.nio.file.Path path = Paths.get(doc.getDirectory(), doc.getName());
            if (doc.getContent() != null) {
                content = new DocumentationContent(path.toString(), doc.getContent().getBytes());
            } else {
                DBItemDocumentationImage image = dbLayer.getDocumentationImage(doc.getImageId());
                if (image != null) {
                    content = new DocumentationContent(path.toString(), image.getImage());
                }
            }
            contents.add(content);
        }
        return contents;
    }

    private StreamingOutput createZipOutputStreamForDownload(List<DocumentationContent> contents) throws IOException {
        StreamingOutput streamingOutput = new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException {
                ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(output));
                for (DocumentationContent content : contents) {
                    ZipEntry entry = new ZipEntry(content.getPath());
                    zipOut.putNextEntry(entry);
                    if (content.getContent() != null) {
                        zipOut.write(content.getContent());
                    }
                    zipOut.closeEntry();
                }
                zipOut.close();
                output.flush();
                output.close();
            }
        };
        return streamingOutput;
    }
}
