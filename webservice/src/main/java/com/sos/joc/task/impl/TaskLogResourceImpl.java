package com.sos.joc.task.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.common.LogInfo;
import com.sos.joc.model.common.LogInfo200;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.task.resource.ITaskLogResource;

@Path("task")
public class TaskLogResourceImpl extends JOCResourceImpl implements ITaskLogResource {

    private static final String API_CALL = "./task/log";

    @Override
    public JOCDefaultResponse postTaskLog(String xAccessToken, String accessToken, TaskFilter taskFilter) throws Exception {
        return postTaskLog(getAccessToken(xAccessToken, accessToken), taskFilter);
    }

    public JOCDefaultResponse postTaskLog(String accessToken, TaskFilter taskFilter) throws Exception {
        return execute(API_CALL, accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse getTaskLogHtml(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String taskId,
            String filename) throws Exception {
        return getTaskLogHtml(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, taskId, filename);
    }

    public JOCDefaultResponse getTaskLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String taskId, String filename)
            throws Exception {
        TaskFilter taskFilter = setTaskFilter(jobschedulerId, taskId, filename, LogMime.HTML);
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return execute(API_CALL + "/html", accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse downloadTaskLog(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String taskId,
            String filename) throws Exception {
        return downloadTaskLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, taskId, filename);
    }

    public JOCDefaultResponse downloadTaskLog(String accessToken, String queryAccessToken, String jobschedulerId, String taskId, String filename)
            throws Exception {
        TaskFilter taskFilter = setTaskFilter(jobschedulerId, taskId, filename, LogMime.PLAIN);
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return downloadTaskLog(accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse downloadTaskLog(String xAccessToken, String accessToken, TaskFilter taskFilter) throws Exception {
        return downloadTaskLog(getAccessToken(xAccessToken, accessToken), taskFilter);
    }

    public JOCDefaultResponse downloadTaskLog(String accessToken, TaskFilter taskFilter) throws Exception {
        return execute(API_CALL + "/download", accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse getLogInfo(String xAccessToken, String accessToken, TaskFilter taskFilter) throws Exception {
        return getLogInfo(getAccessToken(xAccessToken, accessToken), taskFilter);
    }

    public JOCDefaultResponse getLogInfo(String accessToken, TaskFilter taskFilter) throws Exception {
        return execute(API_CALL + "/info", accessToken, taskFilter);
    }

    public JOCDefaultResponse execute(String apiCall, String accessToken, TaskFilter taskFilter) {

        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, taskFilter, accessToken, taskFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    taskFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);

            switch (apiCall) {
            case API_CALL:
                LogContent200 log = new LogContent200();
                log.setSurveyDate(Date.from(Instant.now()));
                final java.nio.file.Path path = getLogPath(logTaskContent, taskFilter, true);
                try {
                    if (Files.exists(path)) {
                        log.setSurveyDate(Date.from(Files.getLastModifiedTime(path).toInstant()));
                    }
                } catch (Exception e) {
                }
                try {
                    LogContent logContentSchema = new LogContent();
                    if (taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                        logContentSchema.setHtml(logTaskContent.htmlWithColouredLogContent(path));
                    } else {
                        logContentSchema.setPlain(logTaskContent.getLogContent(path));
                    }
                    log.setLog(logContentSchema);
                    log.setDeliveryDate(Date.from(Instant.now()));
                    return JOCDefaultResponse.responseStatus200(log);
                } finally {
                    try {
                        if (path != null) {
                            Files.deleteIfExists(path);
                        }
                    } catch (Exception e1) {
                    }
                }

            case API_CALL + "/html":
                java.nio.file.Path path2 = getLogPath(logTaskContent, taskFilter, true);
                try {
                    return JOCDefaultResponse.responseHtmlStatus200(logTaskContent.htmlPageWithColouredLogContent(path2, "Task " + taskFilter
                            .getTaskId()));
                } finally {
                    try {
                        if (path2 != null) {
                            Files.deleteIfExists(path2);
                        }
                    } catch (Exception e1) {
                    }
                }

            case API_CALL + "/info":
                LogInfo200 logInfo200 = new LogInfo200();
                logInfo200.setSurveyDate(Date.from(Instant.now()));
                final java.nio.file.Path path3 = getLogPath(logTaskContent, taskFilter, false);
                logInfo200.setDeliveryDate(Date.from(Instant.now()));
                LogInfo logInfo = new LogInfo();
                logInfo.setFilename(path3.getFileName().toString());
                logInfo.setSize(0L);
                try {
                    if (Files.exists(path3)) {
                        logInfo.setSize(Files.size(path3));
                    }
                } catch (Exception e) {
                }
                logInfo.setDownload(logInfo.getSize() > Globals.maxSizeOfLogsToDisplay);
                logInfo200.setLog(logInfo);

                DeleteTempFile runnable = new DeleteTempFile(path3);
                new Thread(runnable).start();

                return JOCDefaultResponse.responseStatus200(logInfo200);

            default: // case API_CALL + "/download"
                final java.nio.file.Path path4 = getLogPath(logTaskContent, taskFilter, true);

                StreamingOutput fileStream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream output) throws IOException {
                        InputStream in = null;
                        try {
                            in = Files.newInputStream(path4);
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
                                Files.delete(path4);
                            } catch (Exception e) {
                            }
                        }
                    }
                };

                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(logTaskContent.getJob(), taskFilter
                        .getTaskId()));
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
                    return path;
                }
            }
        }
        checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
        checkRequiredParameter("taskId", taskFilter.getTaskId());
        java.nio.file.Path path = null;
        try {
            path = logTaskContent.writeLogFile();
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

    private TaskFilter setTaskFilter(String jobschedulerId, String taskId, String filename, LogMime mime) {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTaskId(taskId);
        taskFilter.setJobschedulerId(jobschedulerId);
        taskFilter.setMime(mime);
        taskFilter.setFilename(filename);
        return taskFilter;
    }

    private String getFileName(String jobName, String taskId) {
        String fileName = taskId + ".task.log";
        if (jobName != null && !jobName.isEmpty()) {
            fileName = Paths.get(jobName).getFileName() + "." + fileName;
        }
        return fileName;
    }

}
