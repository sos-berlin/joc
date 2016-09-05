package com.sos.joc.tasks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.job.History;
import com.sos.joc.model.job.HistorySchema;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.State.Text;
import com.sos.joc.tasks.resource.ITasksResourceHistory;

@Path("tasks")
public class TasksResourceHistoryImpl extends JOCResourceImpl implements ITasksResourceHistory {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResourceHistoryImpl.class);

    @Override
    public JOCDefaultResponse postTasksHistory(String accessToken, JobsFilterSchema jobsFilterSchema) throws Exception {
        LOGGER.debug("init Tasks History");
        JOCDefaultResponse jocDefaultResponse = init( jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getHistory().isView());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        
        try {
 
            HistorySchema entity = new HistorySchema();
            List<History> listOfHistory = new ArrayList<History> ();
            entity.setDeliveryDate(new Date());
            
            History history1 = new History();
            history1.setAgent("myAgent");
            history1.setClusterMember(-1);
            history1.setEndTime(new Date());
            ErrorSchema errorSchema = new ErrorSchema();
            errorSchema.setCode("myCode");
            errorSchema.setMessage("myMessage");
            history1.setError(errorSchema);
            
            history1.setExitCode(-1);
            history1.setJob("myJob");
            history1.setStartTime(new Date());
            com.sos.joc.model.job.State state = new com.sos.joc.model.job.State();
            state.setSeverity(-1);
            state.setText(Text.SUCCESSFUL);
            history1.setState(state);
            
            history1.setSteps(-1);
            history1.setTaskId(-1);
            listOfHistory.add(history1);
            
            entity.setHistory(listOfHistory);

            History history2 = new History();
            history2.setAgent("myAgent");
            history2.setClusterMember(-1);
            history2.setEndTime(new Date());
            history2.setError(errorSchema);
            
            history2.setExitCode(-1);
            history2.setJob("myJob");
            history2.setStartTime(new Date());
            history2.setState(state);
            
            history2.setSteps(-1);
            history2.setTaskId(-1);
            listOfHistory.add(history1);
            entity.setHistory(listOfHistory);
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            System.out.println(e.getCause() + ":" + e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
