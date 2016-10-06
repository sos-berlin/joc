package com.sos.joc.task.impl;


import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.TaskFilterSchema;
import com.sos.joc.task.resource.ITaskLogHtmlResource;

@Path("task")
public class TaskLogHtmlResourceImpl extends JOCResourceImpl implements ITaskLogHtmlResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getTaskLogHtml(String accessToken, String jobschedulerId, String taskId) throws Exception {
        LOGGER.debug("init task/log/html");

        try {
            
            TaskFilterSchema taskFilterSchema = new TaskFilterSchema();
            if (taskId != null){
                taskFilterSchema.setTaskId(Long.parseLong(taskId));
            }
            taskFilterSchema.setJobschedulerId(jobschedulerId);
            taskFilterSchema.setMime(TaskFilterSchema.Mime.HTML);
           
            checkRequiredParameter("jobschedulerId", taskFilterSchema.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilterSchema.getTaskId());

            JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogTaskContent logTaskContent = new LogTaskContent(taskFilterSchema, dbItemInventoryInstance);

            String log = logTaskContent.getLog();
            return JOCDefaultResponse.responseStatus200(logTaskContent.htmlPageWithColouredLogContent(log));

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}
