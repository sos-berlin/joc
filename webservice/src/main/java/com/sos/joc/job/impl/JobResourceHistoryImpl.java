package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.job.resource.IJobResourceHistory;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.job.History;
import com.sos.joc.model.job.HistorySchema;
import com.sos.joc.model.job.JobHistoryFilterSchema;
import com.sos.joc.model.job.State;

@Path("job")
public class JobResourceHistoryImpl extends JOCResourceImpl implements IJobResourceHistory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceHistoryImpl.class);

    @Override
    public JOCDefaultResponse postJobHistory(String accessToken, JobHistoryFilterSchema jobHistoryFilterSchema) throws Exception {

        LOGGER.debug("init jobs/history");
        JOCDefaultResponse jocDefaultResponse = init(jobHistoryFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            HistorySchema entity = new HistorySchema();

            // TODO JOC Cockpit Webservice
            entity.setDeliveryDate(new Date());
            List<History>listOfHistory = new ArrayList<History>();
            History history = new History();
            history.setAgent("myAgent");
            history.setClusterMember(-1);
            history.setEndTime(new Date());
            ErrorSchema errorSchema = new ErrorSchema();
            errorSchema.setCode("myCode");
            errorSchema.setMessage("myMessage");
            history.setError(errorSchema);
            history.setExitCode(-1);
            history.setJob("myJob");
            history.setStartTime(new Date());
            State state = new State();
            state.setSeverity(-1);
            state.setText(State.Text.INCOMPLETE);
            history.setState(state);
            history.setSteps(-1);
            history.setTaskId(-1);
            listOfHistory.add(history);
            
            entity.setHistory(listOfHistory);
           

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
