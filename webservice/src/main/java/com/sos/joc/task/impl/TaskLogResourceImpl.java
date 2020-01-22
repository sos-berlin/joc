package com.sos.joc.task.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.classes.common.DeleteTempFile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogInfo;
import com.sos.joc.model.common.LogInfo200;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.task.resource.ITaskLogResource;
import com.sos.schema.JsonValidator;

@Path("task")
public class TaskLogResourceImpl extends JOCResourceImpl implements ITaskLogResource {

    private static final String API_CALL = "./task/log";

    @Override
    public JOCDefaultResponse postTaskLog(String accessToken, byte[] taskFilter) {
        return execute(API_CALL, accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse getTaskLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String taskId, String filename) {
        byte[] taskFilter = setTaskFilter(jobschedulerId, taskId, filename, LogMime.HTML);
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return execute(API_CALL + "/html", accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse downloadTaskLog(String accessToken, String queryAccessToken, String jobschedulerId, String taskId, String filename) {
        byte[] taskFilter = setTaskFilter(jobschedulerId, taskId, filename, LogMime.PLAIN);
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return downloadTaskLog(accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse downloadTaskLog(String accessToken, byte[] taskFilter) {
        return execute(API_CALL + "/download", accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse getLogInfo(String accessToken, byte[] taskFilterBytes) {
        try {
            JsonValidator.validateFailFast(taskFilterBytes, TaskFilter.class);
            TaskFilter taskFilter = Globals.objectMapper.readValue(taskFilterBytes, TaskFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "/info", taskFilter, accessToken, taskFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(taskFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);

            LogInfo200 logInfo200 = new LogInfo200();
            logInfo200.setSurveyDate(Date.from(Instant.now()));
            java.nio.file.Path path = getLogPath(logTaskContent, taskFilter, false);
            LogInfo logInfo = new LogInfo();
            logInfo.setFilename(path.getFileName().toString());
            logInfo.setSize(null);
            try {
                if (Files.exists(path)) {
                    //logInfo.setSize(Files.size(path));
                    logInfo.setSize(getSize(path));
                    logInfo.setDownload(logInfo.getSize() > Globals.maxSizeOfLogsToDisplay);
                }
            } catch (Exception e) {
            }
            logInfo200.setLog(logInfo);
            logInfo200.setDeliveryDate(Date.from(Instant.now()));
            
            DeleteTempFile runnable = new DeleteTempFile(path);
            new Thread(runnable).start();

            return JOCDefaultResponse.responseStatus200(logInfo200);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    public JOCDefaultResponse execute(String apiCall, String accessToken, byte[] taskFilterBytes) {

        try {
            JsonValidator.validateFailFast(taskFilterBytes, TaskFilter.class);
            TaskFilter taskFilter = Globals.objectMapper.readValue(taskFilterBytes, TaskFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(apiCall, taskFilter, accessToken, taskFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    taskFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);
            java.nio.file.Path path = getLogPath(logTaskContent, taskFilter, true);
//            boolean offerredAsDownload = false;
//            switch (apiCall) {
//            case API_CALL + "/download":
//                offerredAsDownload = true;
//                break;
//            default:
//                try {
//                    if (Files.exists(path) && getSize(path) > Globals.maxSizeOfLogsToDisplay) {
//                        offerredAsDownload = true;
//                    }
//                } catch (Exception e) {
//                }
//                break;
//            }
            boolean offerredAsDownload = (API_CALL + "/download").equals(apiCall);

            if ((API_CALL + "/html").equals(apiCall)) {
                path = logTaskContent.pathOfHtmlPageWithColouredGzipLogContent(path, "Task " + taskFilter.getTaskId());
            } else if ((API_CALL).equals(apiCall) && taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                path = logTaskContent.pathOfHtmlWithColouredGzipLogContent(path);
            }
            
            long unCompressedLength = getSize(path);

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
            if (offerredAsDownload) {
                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(path), unCompressedLength);
            } else {
                if ((API_CALL + "/html").equals(apiCall)) {
                    return JOCDefaultResponse.responseHtmlStatus200(fileStream, unCompressedLength);
                } else {
                    return JOCDefaultResponse.responsePlainStatus200(fileStream, unCompressedLength);
                }
            }

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            if ((API_CALL + "/html").equals(apiCall)) {
                return JOCDefaultResponse.responseHTMLStatusJSError(e);
            } else {
                return JOCDefaultResponse.responseStatusJSError(e);
            }
        } catch (Exception e) {
            if ((API_CALL + "/html").equals(apiCall)) {
                return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
            } else {
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());
            }
        }
    }

    private java.nio.file.Path getLogPath(LogTaskContent logTaskContent, TaskFilter taskFilter, boolean withFilenameCheck) throws Exception {

        if (withFilenameCheck) {
            if (taskFilter.getFilename() != null && !taskFilter.getFilename().isEmpty()) {
                java.nio.file.Path path = Paths.get(System.getProperty("java.io.tmpdir"), taskFilter.getFilename());
                if (Files.exists(path)) {
                    return Files.move(path, path.getParent().resolve(path.getFileName().toString()+".log"), StandardCopyOption.ATOMIC_MOVE);
                }
            }
        }
        checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
        checkRequiredParameter("taskId", taskFilter.getTaskId());
        java.nio.file.Path path = null;
        try {
            path = logTaskContent.writeGzipLogFile();
            return path;
        } catch (Exception e) {
            try {
                if (path != null) {
                    Files.deleteIfExists(path);
                }
            } catch (Exception e1) {
            }
            throw e;
        }
    }

    private byte[] setTaskFilter(String jobschedulerId, String taskId, String filename, LogMime mime) {
        String json = String.format("{\"jobschedulerId\": \"%s\", \"taskId\": \"%s\", \"filename\": \"%s\", \"mime\": \"%s\"}", jobschedulerId,
                taskId, filename, mime.name());
        return json.getBytes();
    }

    private String getFileName(java.nio.file.Path path) {
        return path.getFileName().toString().replaceFirst("^sos-(.*)-download-.+\\.tmp(\\.log)*$", "$1");
    }
    
    private long getSize(java.nio.file.Path path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r");
        raf.seek(raf.length() - 4);
        int b4 = raf.read();
        int b3 = raf.read();
        int b2 = raf.read();
        int b1 = raf.read();
        raf.close();
        return ((long)b1 << 24) | ((long)b2 << 16) | ((long)b3 << 8) | (long)b4;
    }

}
