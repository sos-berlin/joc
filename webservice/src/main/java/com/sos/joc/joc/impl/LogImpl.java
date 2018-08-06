package com.sos.joc.joc.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joc.resource.ILogResource;
import com.sos.joc.model.JOClog;
import com.sos.joc.model.JOClogs;

@javax.ws.rs.Path("")
public class LogImpl extends JOCResourceImpl implements ILogResource {

    private static final String API_CALL = "./log";

    @Override
    public JOCDefaultResponse postLog(String accessToken, JOClog jocLog) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jocLog, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (Globals.sosShiroProperties == null) {
                Globals.sosShiroProperties = new JocCockpitProperties();
            }
            Path logDir = Paths.get("logs");
            Path latestLogFile = null;
            String pattern = "^\\d{4}_\\d{2}_\\d{2}\\.stderrout\\.log$";
            if (jocLog.getFilename() != null) {
                if (jocLog.getFilename().matches(pattern)) {
                    latestLogFile = logDir.resolve(jocLog.getFilename());
                }
            } else {
                latestLogFile = logDir.resolve(DateTimeFormatter.ofPattern("yyyy_MM_dd").format(ZonedDateTime.of(LocalDateTime.now(),
                        Globals.jocTimeZone.toZoneId())) + ".stderrout.log");
                if (!Files.isReadable(latestLogFile)) {
                    latestLogFile = null;
                    List<Path> filenames = new ArrayList<Path>();
                    for (Path logFile : getFileListStream(logDir, pattern)) {
                        filenames.add(logFile);
                    }
                    filenames.sort(Comparator.reverseOrder());
                    if (!filenames.isEmpty()) {
                        latestLogFile = filenames.get(0);
                    }
                }
                if (latestLogFile == null) {
                    throw new FileNotFoundException("JOC Cockpit log not found.");
                }
            }

            final Path log = latestLogFile;

            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(log);
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
                    }
                }
            };

            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, log.getFileName().toString());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postLogs(String accessToken) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "s", null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Path logDir = Paths.get("logs");
            String pattern = "^\\d{4}_\\d{2}_\\d{2}\\.stderrout\\.log$";
            List<String> filenames = new ArrayList<String>();
            for (Path logFile : getFileListStream(logDir, pattern)) {
                filenames.add(logFile.getFileName().toString());
            }
            filenames.sort(Comparator.reverseOrder());
            JOClogs entity = new JOClogs();
            entity.setFilenames(filenames);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse getLog(String accessToken, String queryAccessToken, String filename) {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        JOClog jocLog = new JOClog();
        jocLog.setFilename(filename);
        return postLog(accessToken, jocLog);
    }

    private static DirectoryStream<Path> getFileListStream(final Path folder, final String regexp) throws IOException {

        if (folder == null) {
            throw new FileNotFoundException("directory not specified!!");
        }
        if (!Files.isDirectory(folder)) {
            throw new FileNotFoundException("directory does not exist: " + folder);
        }
        final Pattern pattern = Pattern.compile(regexp);
        return Files.newDirectoryStream(folder, path -> {
            if (Files.isDirectory(path)) {
                return false;
            }
            if (!Files.isReadable(path)) {
                return false;
            }
            return pattern.matcher(path.getFileName().toString()).find();
        });
    }

}
