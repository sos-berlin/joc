package com.sos.joc.task.impl;

import java.time.Instant;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogTaskContent;
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
    private static final String API_CALL = "./task/log";
    
    @Override
    public JOCDefaultResponse postTaskLog(String accessToken, TaskFilter taskFilter) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            JOCDefaultResponse jocDefaultResponse = init(taskFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LogContent200 entity = new LogContent200();
            LogTaskContent logOrderContent = new LogTaskContent(taskFilter,dbItemInventoryInstance);
            //TODO surveyDate from database
            entity.setSurveyDate(Date.from(Instant.now()));
            
            LogContent logContentSchema = new LogContent();
            String log = logOrderContent.getLog();
             
            if (taskFilter.getMime() != null && taskFilter.getMime() == LogMime.HTML) {
                logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(log));
            } else {
                logContentSchema.setPlain(log);
            }
            entity.setLog(logContentSchema);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, taskFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, taskFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

 

}
