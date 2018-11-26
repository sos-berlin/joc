package com.sos.joc.documentations.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.documentations.resource.IDocumentationsExportResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.docu.DocumentationsFilter;

@Path("/documentations")
public class DocumentationsExportResourceImpl extends JOCResourceImpl implements IDocumentationsExportResource{
    
    private static final String API_CALL = "/documentations/export";

    @Override
    public JOCDefaultResponse export(String xAccessToken, DocumentationsFilter documentationsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            //TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, documentationsFilter, xAccessToken, documentationsFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            //TODO Objekte aus DB holen gemäß Filter, in temporäres Verzeichnis schreiben und zippen
            // ZipFile als StreamingOutput übergeben.
            
            java.nio.file.Path path = null; //Path of zip file
            
            final java.nio.file.Path downPath = path;

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
            
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(path), null);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    private String getFileName(java.nio.file.Path path) {
        return path.getFileName().toString();
    }

}
