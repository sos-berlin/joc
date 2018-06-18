package com.sos.joc.task.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        try {
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
                    logInfo.setSize(Files.size(path));
                }
            } catch (Exception e) {
            }
            logInfo.setDownload(logInfo.getSize() > Globals.maxSizeOfLogsToDisplay);
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

    public JOCDefaultResponse execute(String apiCall, String accessToken, TaskFilter taskFilter) {

        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, taskFilter, accessToken, taskFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    taskFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);
            boolean offerredAsDownload = false;
            java.nio.file.Path path = getLogPath(logTaskContent, taskFilter, true);
            switch (apiCall) {
            case API_CALL + "/download":
                offerredAsDownload = true;
                break;
            default:
                try {
                    if (Files.exists(path) && Files.size(path) > Globals.maxSizeOfLogsToDisplay) {
                        offerredAsDownload = true;
                    }
                } catch (Exception e) {
                }
                break;
            }

            if ((API_CALL + "/html").equals(apiCall)) {
                path = logTaskContent.pathOfHtmlPageWithColouredLogContent(path, "Task " + taskFilter.getTaskId());
            } else if ((API_CALL).equals(apiCall) && taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                path = logTaskContent.pathOfHtmlWithColouredLogContent(path);
            }

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
                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(logTaskContent.getJob(), taskFilter
                        .getTaskId()));
            } else {
                if ((API_CALL + "/html").equals(apiCall)) {
                    return JOCDefaultResponse.responseHtmlStatus200(fileStream);
                } else {
                    return JOCDefaultResponse.responsePlainStatus200(fileStream);
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
