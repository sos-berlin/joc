package com.sos.joc.task.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.task.resource.ITaskLogResource;

@Path("task")
public class TaskLogResourceImpl extends JOCResourceImpl implements ITaskLogResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogResourceImpl.class);

    @Override
    public JOCDefaultResponse postTaskLog(String accessToken, TaskFilter taskFilter) throws Exception {
        LOGGER.debug("init task/log");

        try {
            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            JOCDefaultResponse jocDefaultResponse = init(taskFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200 entity = new LogContent200();
            LogTaskContent logOrderContent = new LogTaskContent(taskFilter,dbItemInventoryInstance);

            entity.setDeliveryDate(new Date());
            LogContent logContentSchema = new LogContent();
            String log = logOrderContent.getLog();
             
            if (taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(log));
            } else {
                if (taskFilter.getMime() == null || taskFilter.getMime() == LogMime.PLAIN) {
                    logContentSchema.setPlain(log);
                } else {
                    JocError jocError = new JocError();
                    jocError.setCode(WebserviceConstants.WRONG_MIME_TYPE);
                    jocError.setMessage("Unknow mime type: " + taskFilter.getMime());
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
