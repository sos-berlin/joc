package com.sos.joc.joc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.joc.resource.ILogResource;

@Path("log")
public class LogImpl extends JOCResourceImpl implements ILogResource {

    private static final String API_CALL = "./log";

    @Override
    public JOCDefaultResponse postLog(String accessToken) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (Globals.sosShiroProperties == null) {
                Globals.sosShiroProperties = new JocCockpitProperties();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
            final java.nio.file.Path jocLog = Globals.sosShiroProperties.resolvePath("../../logs/" + format.format(ZonedDateTime.of(LocalDateTime
                    .now(), Globals.jocTimeZone.toZoneId())) + ".stderrout.log");

            if (!Files.isReadable(jocLog)) {
                throw new IOException("JOC Cockpit log " + jocLog.toString() + " is not readable.");
            }

            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(jocLog);
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
                            Files.delete(jocLog);
                        } catch (Exception e) {
                        }
                    }
                }
            };

            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, jocLog.getFileName().toString());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse getLog(String accessToken, String queryAccessToken) {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return postLog(accessToken);
    }

}
