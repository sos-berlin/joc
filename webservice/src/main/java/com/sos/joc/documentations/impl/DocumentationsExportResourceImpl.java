package com.sos.joc.documentations.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsExportResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.docu.DocumentationsFilter;

@Path("/documentations/export")
public class DocumentationsExportResourceImpl extends JOCResourceImpl implements IDocumentationsExportResource{

    private static final String API_CALL = "/documentations/export";
    private static final String DEFAULT_TARGET_FILENAME = "documentation.zip";
    @Override
    public JOCDefaultResponse postImportDocumentations(String xAccessToken, DocumentationsFilter filter) throws Exception {
        
        SOSHibernateSession connection = null;
        // TODO: permissions
        JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), true);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        List<String> documentations = filter.getDocumentations();
        List<Folder> folders = filter.getFolders();
        String targetFilename = null;
//        filter.getTargetFilename();
        ZipOutputStream out = null;
        try {
            targetFilename = DEFAULT_TARGET_FILENAME;
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = new ArrayList<DBItemDocumentation>();
            if (documentations != null && !documentations.isEmpty()) {
                for (String path : documentations) {
                    docs.addAll(dbLayer.getDocumentation(filter.getJobschedulerId(), path));
                }
            } else if (folders != null) {
                for (Folder folder : folders) {
                    docs.addAll(dbLayer.getDocumentation(filter.getJobschedulerId(), folder.getFolder(), folder.getRecursive()));
                }
            }
            out = createZipOutputStreamForDownload(docs, dbLayer);
            if (targetFilename != null && !targetFilename.isEmpty()) {
                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(out, targetFilename);
            } else {
                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(out, DEFAULT_TARGET_FILENAME);
            }
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {}
        }
    }

    private ZipOutputStream createZipOutputStreamForDownload(List<DBItemDocumentation> docs, DocumentationDBLayer dbLayer)
            throws IOException, DBConnectionRefusedException, DBInvalidDataException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(out);
        for (DBItemDocumentation documentation : docs) {
            java.nio.file.Path path = Paths.get(documentation.getDirectory(), documentation.getName());
            ZipEntry entry = new ZipEntry(path.toString());
            zipOut.putNextEntry(entry);
            if (documentation.getContent() != null) {
                zipOut.write(documentation.getContent().getBytes());
            } else {
                DBItemDocumentationImage image = dbLayer.getDocumentationImage(documentation.getImageId());
                if (image != null) {
                    zipOut.write(image.getImage());
                }
            }
            zipOut.closeEntry();
        }
        return zipOut;
    }
}
