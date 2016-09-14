package com.sos.joc.jobchain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobchain.resource.IJobChainResourceHistory;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.jobChain.JobChainHistoryFilterSchema;
import com.sos.joc.model.order.History;
import com.sos.joc.model.order.HistorySchema;
import com.sos.joc.model.order.State;

@Path("job_chain")
public class JobChainResourceHistoryImpl extends JOCResourceImpl implements IJobChainResourceHistory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceHistoryImpl.class);

    @Override
    public JOCDefaultResponse postJobChainHistory(String accessToken, JobChainHistoryFilterSchema jobChainHistoryFilterSchema) throws Exception {

        LOGGER.debug("init job_chain/history");
        JOCDefaultResponse jocDefaultResponse = init(jobChainHistoryFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            HistorySchema entity = new HistorySchema();

            // TODO JO C Cockpit Webservice
            entity.setDeliveryDate(new Date());
            List<History>listOfHistory = new ArrayList<History>();
            History history = new History();
            history.setEndTime(new Date());
            history.setHistoryId(-1);
            history.setJobChain("myJobChain");
            history.setNode("myNode");
            history.setOrderId("myOrderId");
            history.setPath("myPath");
            history.setStartTime(new Date());
            State state = new State();
            state.setSeverity(-1);
            state.setText(State.Text.SUCCESSFUL);
            history.setState(state);
           
            listOfHistory.add(history);
            
            entity.setHistory(listOfHistory);
           

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
