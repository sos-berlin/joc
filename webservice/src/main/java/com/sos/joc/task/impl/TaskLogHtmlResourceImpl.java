package com.sos.joc.task.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.task.resource.ITaskLogHtmlResource;

@Path("task")
public class TaskLogHtmlResourceImpl extends JOCResourceImpl implements ITaskLogHtmlResource {

    private static final String API_CALL = "./task/log/html";

    @Override
    public JOCDefaultResponse getTaskLogHtml(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String taskId)
            throws Exception {
        return getTaskLogHtml(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, taskId);
    }

    public JOCDefaultResponse getTaskLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String taskId) throws Exception {
        TaskFilter taskFilter = new TaskFilter();

        try {
            taskFilter.setTaskId(taskId);
            taskFilter.setJobschedulerId(jobschedulerId);
            taskFilter.setMime(LogMime.HTML);

            if (accessToken == null) {
                accessToken = queryAccessToken;
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, taskFilter, accessToken, jobschedulerId, getPermissonsJocCockpit(accessToken)
                    .getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);
            String log = logTaskContent.getLog();

            return JOCDefaultResponse.responseHtmlStatus200(logTaskContent.htmlPageWithColouredLogContent(log, "Task " + taskFilter.getTaskId()));

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }

}
