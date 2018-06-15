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

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogContent200;
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
    public JOCDefaultResponse getTaskLogHtml(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String taskId)
            throws Exception {
        return getTaskLogHtml(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, taskId);
    }

    public JOCDefaultResponse getTaskLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String taskId) throws Exception {
        TaskFilter taskFilter = setTaskFilter(jobschedulerId, taskId, LogMime.HTML);
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        return execute(API_CALL + "/html", accessToken, taskFilter);
    }

    @Override
    public JOCDefaultResponse downloadTaskLog(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String taskId)
            throws Exception {
        return downloadTaskLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, taskId);
    }

    public JOCDefaultResponse downloadTaskLog(String accessToken, String queryAccessToken, String jobschedulerId, String taskId) throws Exception {
        TaskFilter taskFilter = setTaskFilter(jobschedulerId, taskId, LogMime.PLAIN);
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

    public JOCDefaultResponse execute(String apiCall, String accessToken, TaskFilter taskFilter) {

        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, taskFilter, accessToken, taskFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    taskFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);

            switch (apiCall) {
            case API_CALL:
                LogContent200 entity = new LogContent200();
                // TODO surveyDate from database
                entity.setSurveyDate(Date.from(Instant.now()));
                LogContent logContentSchema = new LogContent();
                if (taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                    java.nio.file.Path path = null;
                    try {
                        path = logTaskContent.writeLogFile();
                        logContentSchema.setHtml(logTaskContent.htmlWithColouredLogContent(path));
                    } finally {
                        try {
                            if (path != null) {
                                Files.deleteIfExists(path);
                            }
                        } catch (Exception e2) {
                        }
                    }
                } else {
                    logContentSchema.setPlain(logTaskContent.getLog());
                }
                entity.setLog(logContentSchema);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);

            case API_CALL + "/html":
                return JOCDefaultResponse.responseHtmlStatus200(logTaskContent.htmlPageWithColouredLogContent(logTaskContent.getLog(), "Task "
                        + taskFilter.getTaskId()));

            default: //case API_CALL + "/download"
                java.nio.file.Path path = null;
                try {
                    path = logTaskContent.writeLogFile();
                } catch (Exception e1) {
                    try {
                        if (path != null) {
                            Files.deleteIfExists(path);
                        }
                    } catch (Exception e2) {
                    }
                    throw e1;
                }
                final java.nio.file.Path tmpPath = path;

                StreamingOutput fileStream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream output) throws IOException {
                        InputStream in = null;
                        try {
                            in = Files.newInputStream(tmpPath);
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
                                Files.delete(tmpPath);
                            } catch (Exception e) {
                            }
                        }
                    }
                };

                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(logTaskContent.getJob(), taskFilter.getTaskId()));
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
    
    private TaskFilter setTaskFilter(String jobschedulerId, String taskId, LogMime mime) {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTaskId(taskId);
        taskFilter.setJobschedulerId(jobschedulerId);
        taskFilter.setMime(mime);
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
