package com.sos.joc.task.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogContent;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200Schema;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.model.job.TaskFilterSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema.Mime;
import com.sos.joc.order.resource.IOrderLogResource;
import com.sos.joc.task.resource.ITaskLogResource;

@Path("task")
public class TaskLogResourceImpl extends JOCResourceImpl implements ITaskLogResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogResourceImpl.class);

    @Override
    public JOCDefaultResponse postTaskLog(String accessToken, TaskFilterSchema taskFilterSchema) throws Exception {
        LOGGER.debug("init task/log");

        try {
            checkRequiredParameter("jobschedulerId", taskFilterSchema.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilterSchema.getTaskId());

            JOCDefaultResponse jocDefaultResponse = init(taskFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200Schema entity = new LogContent200Schema();
            LogTaskContent logOrderContent = new LogTaskContent(taskFilterSchema,dbItemInventoryInstance);

            entity.setDeliveryDate(new Date());
            LogContentSchema logContentSchema = new LogContentSchema();
            String log = logOrderContent.getLog();
             
            if (taskFilterSchema.getMime() != null && taskFilterSchema.getMime().toString().equals(Mime.HTML.toString())) {
                logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(log));
            } else {
                if (taskFilterSchema.getMime() == null || taskFilterSchema.getMime().toString().equals(Mime.PLAIN.toString())) {
                    logContentSchema.setPlain(log);
                } else {
                    JocError jocError = new JocError();
                    jocError.setCode(WebserviceConstants.WRONG_MIME_TYPE);
                    jocError.setMessage("Unknow mime type: " + taskFilterSchema.getMime());
                    throw new JocException(jocError);
                }
            }
            entity.setLog(logContentSchema);
            entity.setSurveyDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

 

}
