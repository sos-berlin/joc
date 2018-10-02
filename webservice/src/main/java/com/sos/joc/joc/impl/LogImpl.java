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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joc.resource.ILogResource;
import com.sos.joc.model.JOClog;
import com.sos.joc.model.JOClogs;

@javax.ws.rs.Path("")
public class LogImpl extends JOCResourceImpl implements ILogResource {

    private static final String API_CALL = "./log";
    private static Logger LOGGER = LoggerFactory.getLogger(LogImpl.class);
    private String logDirectory = "logs";
    private String logTimezone = "GMT";

    @Override
    public JOCDefaultResponse postLog(String accessToken, JOClog jocLog) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jocLog, accessToken, "", getPermissonsJocCockpit("", accessToken).getJoc()
                    .getView().isLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (!System.getProperties().containsKey("jetty.base")) {
                throw new JocConfigurationException("This function is only available with Jetty.");
            }

            readJettyLoggingProperties();

            Path logDir = Paths.get(logDirectory);
            if (!Files.exists(logDir)) {
                throw new FileNotFoundException("JOC Cockpit logs directory doesn't exist.");
            }
            Path latestLogFile = null;
            String pattern = "^\\d{4}_\\d{2}_\\d{2}\\.stderrout\\.log$";
            if (jocLog.getFilename() != null) {
                if (jocLog.getFilename().matches(pattern)) {
                    latestLogFile = logDir.resolve(jocLog.getFilename());
                }
            } else {
                latestLogFile = logDir.resolve(DateTimeFormatter.ofPattern("yyyy_MM_dd").format(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(
                        logTimezone))) + ".stderrout.log");
                if (!Files.isReadable(latestLogFile) || Files.size(latestLogFile) == 0) {
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
            }

            if (latestLogFile == null) {
                throw new FileNotFoundException("JOC Cockpit log not found.");
            }
            if (!Files.isReadable(latestLogFile)) {
                throw new FileNotFoundException("JOC Cockpit log is not readable.");
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
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "s", null, accessToken, "", getPermissonsJocCockpit("", accessToken).getJoc()
                    .getView().isLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (!System.getProperties().containsKey("jetty.base")) {
                throw new JocConfigurationException("This function is only available with Jetty.");
            }

            readJettyLoggingProperties();

            Path logDir = Paths.get(logDirectory);
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
            throw new FileNotFoundException("JOC Cockpit logs directory not specified!!");
        }
        if (!Files.isDirectory(folder)) {
            throw new FileNotFoundException("JOC Cockpit logs directory does not exist: " + folder);
        }
        final Pattern pattern = Pattern.compile(regexp);
        return Files.newDirectoryStream(folder, path -> {
            if (Files.isDirectory(path)) {
                return false;
            }
            if (Files.size(path) == 0) {
                return false;
            }
            if (!Files.isReadable(path)) {
                return false;
            }
            return pattern.matcher(path.getFileName().toString()).find();
        });
    }

    private void readJettyLoggingProperties() {
        InputStream stream = null;
        Properties properties = new Properties();
        Path jettyLoggingConf = Paths.get("start.d/logging.ini");
        if (!Files.exists(jettyLoggingConf)) {
            jettyLoggingConf = Paths.get("start.ini");
        }
        if (Files.exists(jettyLoggingConf)) {
            try {
                stream = Files.newInputStream(jettyLoggingConf);
                if (stream != null) {
                    properties.load(stream);
                }
                if (properties.containsKey("jetty.logging.dir")) {
                    logDirectory = properties.getProperty("jetty.logging.dir");
                }
                if (properties.containsKey("jetty.logging.timezone")) {
                    logTimezone = properties.getProperty("jetty.logging.timezone");
                }
            } catch (Exception e) {
                LOGGER.warn(String.format("Error while reading %1$s:", jettyLoggingConf.toString()), e);
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

}
