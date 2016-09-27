package com.sos.joc.task.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.Order_;
import com.sos.joc.model.job.State___;
import com.sos.joc.model.job.Task200Schema;
import com.sos.joc.model.job.TaskFilterSchema;
import com.sos.joc.model.job.TaskSchema;
import com.sos.joc.task.resource.ITaskResource;

@Path("task")
public class TaskResourceImpl extends JOCResourceImpl implements ITaskResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResourceImpl.class);

    @Override
    public JOCDefaultResponse postTask(String accessToken, TaskFilterSchema taskFilterSchema) throws Exception {
        LOGGER.debug("init task");
        try {
            JOCDefaultResponse jocDefaultResponse = init(taskFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice

            Task200Schema entity = new Task200Schema();
            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());

            TaskSchema taskSchema = new TaskSchema();
            taskSchema.setCause(TaskSchema.Cause.ORDER);
            taskSchema.setEnqueued(new Date());
            taskSchema.setId(-1);
            taskSchema.setIdleSince(new Date());
            taskSchema.setInProcessSince(new Date());

            Order_ order = new Order_();
            order.setPath("myPath");
            order.setTitle("myTitle");
            taskSchema.setOrder(order);
            taskSchema.setPid(-1);
            taskSchema.setRunningSince(new Date());
            taskSchema.setStartAt(new Date());
            State___ state = new State___();
            state.setSeverity(-1);
            state.setText(State___.Text.RUNNING);
            taskSchema.setState(state);
            taskSchema.setSteps(-1);

            entity.setTask(taskSchema);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
