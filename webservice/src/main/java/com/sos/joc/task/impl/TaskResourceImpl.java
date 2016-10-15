package com.sos.joc.task.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.Task;
import com.sos.joc.model.job.Task200;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.model.job.TaskState;
import com.sos.joc.model.job.TaskStateText;
import com.sos.joc.task.resource.ITaskResource;

@Path("task")
public class TaskResourceImpl extends JOCResourceImpl implements ITaskResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResourceImpl.class);

    @Override
    public JOCDefaultResponse postTask(String accessToken, TaskFilter taskFilter) throws Exception {
        LOGGER.debug("init task");
        try {
            JOCDefaultResponse jocDefaultResponse = init(taskFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice

            Task task = new Task();
            task.set_cause(TaskCause.ORDER);
            task.setEnqueued(new Date());
            task.setId(-1);
            task.setIdleSince(new Date());
            task.setInProcessSince(new Date());

            Order order = new Order();
            order.setPath("myPath");
            order.setTitle("myTitle");
            task.setOrder(order);
            task.setPid(-1);
            task.setRunningSince(new Date());
            task.setStartAt(new Date());
            TaskState state = new TaskState();
            state.setSeverity(-1);
            state.set_text(TaskStateText.RUNNING);
            task.setState(state);
            task.setSteps(-1);

            Task200 entity = new Task200();
            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            entity.setTask(task);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
