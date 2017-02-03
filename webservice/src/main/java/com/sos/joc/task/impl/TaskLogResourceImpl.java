package com.sos.joc.task.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

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
    public JOCDefaultResponse postTaskLog(String accessToken, TaskFilter taskFilter) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, taskFilter, accessToken, taskFilter.getJobschedulerId(), 
                    getPermissonsJocCockpit(accessToken).getJob().getView().isTaskLog(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", taskFilter.getJobschedulerId());
            checkRequiredParameter("taskId", taskFilter.getTaskId());

            LogContent200 entity = new LogContent200();
            LogTaskContent logOrderContent = new LogTaskContent(taskFilter, dbItemInventoryInstance, accessToken);
            // TODO surveyDate from database
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
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
