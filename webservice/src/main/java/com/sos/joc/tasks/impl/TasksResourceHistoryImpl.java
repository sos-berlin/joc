package com.sos.joc.tasks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryItem;
import com.sos.joc.model.job.TaskHistoryState;
import com.sos.joc.model.job.TaskHistoryStateText;
import com.sos.joc.tasks.resource.ITasksResourceHistory;

@Path("tasks")
public class TasksResourceHistoryImpl extends JOCResourceImpl implements ITasksResourceHistory {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceHistoryImpl.class);
    private static final String API_CALL = "./tasks/history";
    
    @Override
    public JOCDefaultResponse postTasksHistory(String accessToken, JobsFilter jobsFilter) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobsFilter.getJobschedulerId(), getPermissons(accessToken).getHistory().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            TaskHistory entity = new TaskHistory();
            entity.setDeliveryDate(new Date());
            
            List<TaskHistoryItem> listOfHistory = new ArrayList<TaskHistoryItem>();
            
            TaskHistoryItem history1 = new TaskHistoryItem();
            history1.setAgent("myAgent");
            history1.setClusterMember("myClusterMember");
            history1.setEndTime(new Date());
            Err error = new Err();
            error.setCode("myCode");
            error.setMessage("myMessage");
            history1.setError(error);

            history1.setExitCode(-1);
            history1.setJob("myJob");
            history1.setStartTime(new Date());
            TaskHistoryState state = new TaskHistoryState();
            state.setSeverity(-1);
            state.set_text(TaskHistoryStateText.SUCCESSFUL);
            history1.setState(state);

            history1.setSteps(-1);
            history1.setTaskId("-1");
            listOfHistory.add(history1);

            entity.setHistory(listOfHistory);

            TaskHistoryItem history2 = new TaskHistoryItem();
            history2.setAgent("myAgent");
            history2.setClusterMember("myClusterMember");
            history2.setEndTime(new Date());
            history2.setError(error);

            history2.setExitCode(-1);
            history2.setJob("myJob");
            history2.setStartTime(new Date());
            history2.setState(state);

            history2.setSteps(-1);
            history2.setTaskId("-1");
            listOfHistory.add(history1);
            entity.setHistory(listOfHistory);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobsFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobsFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }
}
