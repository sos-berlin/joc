package com.sos.joc.task.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.job.Task;
import com.sos.joc.model.job.Task200;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.job.TaskFilter;
import com.sos.joc.model.job.TaskState;
import com.sos.joc.model.job.TaskStateText;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.task.resource.ITaskResource;

@Path("task")
public class TaskResourceImpl extends JOCResourceImpl implements ITaskResource {

    private static final String API_CALL = "./task";

    @Override
    public JOCDefaultResponse postTask(String accessToken, TaskFilter taskFilter) throws Exception {
        try {
            initLogging(API_CALL, taskFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, taskFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("taskId", taskFilter.getTaskId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(createTaskPostCommand(taskFilter.getTaskId()), accessToken);
            Element taskElem = (Element) jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/task");

            Task task = new Task();
            try {
                task.set_cause(TaskCause.fromValue(taskElem.getAttribute("cause").toUpperCase()));
            } catch (Exception e) {
                task.set_cause(TaskCause.NONE);
            }
            task.setEnqueued(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElem, "enqueued", null)));
            String pid = jocXmlCommand.getAttributeValue(taskElem, "pid", null);
            task.setPid(pid == null ? null : Integer.parseInt(pid));
            task.setTaskId(taskElem.getAttribute("id"));
            task.setIdleSince(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElem, "idle_since", null)));
            task.setInProcessSince(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElem, "in_process_since", null)));
            task.setStartedAt(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElem, "running_since", null)));
            task.setPlannedStart(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElem, "start_at", null)));
            task.setSteps(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElem, "steps", "0")));
            task.setState(getState(taskElem));

            Element orderElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(taskElem, "order");
            if (orderElem != null) {
                try {
                    checkRequiredParameter("order", orderElem.getAttribute("order"));
                    checkRequiredParameter("job_chain", orderElem.getAttribute("job_chain"));
                    JOCJsonCommand command = new JOCJsonCommand();
                    command.setUriBuilderForOrders(dbItemInventoryInstance.getUrl());
                    OrderFilter orderBody = new OrderFilter();
                    orderBody.setCompact(false);
                    orderBody.setJobChain(orderElem.getAttribute("job_chain"));
                    orderBody.setOrderId(orderElem.getAttribute("order"));
                    command.addOrderCompactQuery(orderBody.getCompact());
                    OrdersVCallable o = new OrdersVCallable(orderBody, command, accessToken);
                    task.setOrder(o.getOrder());
                } catch (JocMissingRequiredParameterException e) {
                    throw new JobSchedulerBadRequestException("missing attributes in order element", e);
                }
            }

            Task200 entity = new Task200();
            entity.setDeliveryDate(Date.from(Instant.now()));
            entity.setSurveyDate(jocXmlCommand.getSurveyDate());
            entity.setTask(task);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private String createTaskPostCommand(String taskId) {
        return "<show_task id=\"" + taskId + "\" what=\"all\" />";
    }

    private TaskState getState(Element taskElem) {
        TaskState state = new TaskState();
        switch (taskElem.getAttribute("state")) {
        case "none":
            state.set_text(TaskStateText.NONE);
            state.setSeverity(3);
            break;
        case "loading":
            state.set_text(TaskStateText.LOADING);
            state.setSeverity(1);
            break;
        case "waiting_for_process":
            state.set_text(TaskStateText.WAITING_FOR_PROCESS);
            state.setSeverity(3);
            break;
        case "opening":
        case "starting":
            state.set_text(TaskStateText.STARTING);
            state.setSeverity(1);
            break;
        case "opening_waiting_for_locks":
        case "running_waiting_for_locks":
            state.set_text(TaskStateText.WAITING_FOR_LOCKS);
            state.setSeverity(3);
            break;
        case "running_process":
            state.set_text(TaskStateText.RUNNING_PROCESS);
            state.setSeverity(0);
            break;
        case "running_remote_process":
            state.set_text(TaskStateText.RUNNING_REMOTE_PROCESS);
            state.setSeverity(0);
            break;
        case "running":
        case "running_delayed":
            state.set_text(TaskStateText.RUNNING);
            state.setSeverity(0);
            break;
        case "running_waiting_for_order":
            state.set_text(TaskStateText.WAITING_FOR_ORDER);
            state.setSeverity(3);
            break;
        case "suspended":
            state.set_text(TaskStateText.SUSPENDED);
            state.setSeverity(3);
            break;
        case "ending_waiting_for_subprocesses":
        case "on_success":
        case "on_error":
        case "ending":
            state.set_text(TaskStateText.ENDING);
            state.setSeverity(0);
            break;
        case "ended":
        case "exit":
        case "release":
        case "delete_files":
        case "closed":
            state.set_text(TaskStateText.CLOSED);
            state.setSeverity(0);
            break;
        default:
            state.set_text(TaskStateText.NONE);
            state.setSeverity(3);
            break;
        }
        return state;
    }
}
