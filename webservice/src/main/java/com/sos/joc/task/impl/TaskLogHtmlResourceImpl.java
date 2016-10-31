package com.sos.joc.task.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.task.resource.ITaskLogHtmlResource;

@Path("task")
public class TaskLogHtmlResourceImpl extends JOCResourceImpl implements ITaskLogHtmlResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogHtmlResourceImpl.class);
    private static final String API_CALL = "./task/log/html";

    @Override
    public JOCDefaultResponse getTaskLogHtml(String accessToken, String jobschedulerId, String taskId) throws Exception {
        LOGGER.debug(API_CALL);
        TaskFilter taskFilter = new TaskFilter();

        try {

            taskFilter.setTaskId(taskId);
            taskFilter.setJobschedulerId(jobschedulerId);
            taskFilter.setMime(LogMime.HTML);

            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerId, getPermissons(accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogTaskContent logTaskContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);
            String log = logTaskContent.getLog();

            return JOCDefaultResponse.responseHtmlStatus200(logTaskContent.htmlPageWithColouredLogContent(log, "Task " + taskFilter.getTaskId()));

        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, taskFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e);

        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, taskFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e, err);
        }
    }

}
