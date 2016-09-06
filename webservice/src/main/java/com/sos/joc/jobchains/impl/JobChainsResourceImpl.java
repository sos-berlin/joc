package com.sos.joc.jobchains.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.jobchain.JobChains;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.jobchains.resource.IJobChainsResource;
import com.sos.joc.model.jobChain.JobChain200VSchema;
import com.sos.joc.model.jobChain.JobChain__;
import com.sos.joc.model.jobChain.JobChain___;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.jobChain.Node__;
import com.sos.joc.model.jobChain.Node___;
import com.sos.joc.model.jobChain.OrdersSummary;
import com.sos.joc.model.jobChain.State;
import com.sos.joc.model.jobChain.State_;
import com.sos.joc.model.jobChain.State__;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceImpl.class);  
    
    private JobChain___ getJobChain(boolean compact){
        JobChain___ jobChain = new JobChain___();
        jobChain.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
        jobChain.setFileOrderSources(JobChains.getFileOrderSources());
        jobChain.setName("myName");
        
        List<Node___> listOfNodes = new ArrayList<Node___>();
        Node___ node2 = new Node___();
        node2.setJob(Jobs.getJob(compact));
        node2.setName("myName");
        node2.setNumOfOrders(-1);
        node2.setOrders(Orders.getOrderQueueList());
        com.sos.joc.model.jobChain.State___ state = new com.sos.joc.model.jobChain.State___();
        state.setSeverity(-1);
        state.setText(com.sos.joc.model.jobChain.State___.Text.ACTIVE);
        node2.setState(state);
        
        jobChain.setNodes(listOfNodes);
        jobChain.setNumOfNodes(-1);
        jobChain.setNumOfOrders(-1);
        jobChain.setPath("myPath");
        
        State__ stateJobChain = new State__();
        stateJobChain.setSeverity(-1);
        stateJobChain.setText(State__.Text.ACTIVE);
        jobChain.setState(stateJobChain);
        jobChain.setSurveyDate(new Date());
        return jobChain;

    }
     
    @Override
    public JOCDefaultResponse postJobChains(String accessToken, JobChainsFilterSchema  jobChainsFilterSchema) throws Exception {
        LOGGER.debug("init Job Chains");
        JOCDefaultResponse jocDefaultResponse = init(jobChainsFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
            // TODO JOC Cockpit Webservice
 
            JobChain200VSchema entity = new JobChain200VSchema();
            entity.setDeliveryDate(new Date());
             
            JobChain__ jobChain = new JobChain__();
            jobChain.setName("myName2");
            jobChain.setSurveyDate(new Date());
            jobChain.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            jobChain.setFileOrderSources(JobChains.getFileOrderSources());

            
            //Nodes
            List<Node__> listOfNodes = new ArrayList<Node__>();
            Node__ node = new Node__();
            node.setJob(Jobs.getJob(jobChainsFilterSchema.getCompact()));
            node.setJobChain(getJobChain(jobChainsFilterSchema.getCompact()));
            node.setName("myNodeName");
            node.setNumOfOrders(-1);
            node.setOrders(Orders.getOrderQueueList());
            State_ state = new State_();
            state.setSeverity(-1);
            state.setText(State_.Text.ACTIVE);
            node.setState(state);
            listOfNodes.add(node);
            jobChain.setNodes(listOfNodes);
            
            jobChain.setNumOfNodes(-1);
            jobChain.setNumOfOrders(-1);
            OrdersSummary ordersSummary = new OrdersSummary();
            ordersSummary.setBlacklist(-1);
            ordersSummary.setPending(-1);
            ordersSummary.setRunning(-1);
            ordersSummary.setSetback(-1);
            ordersSummary.setSuspended(-1);
            ordersSummary.setWaitingForResource(-1);

            jobChain.setOrdersSummary(ordersSummary);
            jobChain.setPath("myPath");
            
            State jobChainState = new State();
            jobChainState.setSeverity(-1);
            jobChainState.setText(State.Text.ACTIVE);
            jobChain.setState(jobChainState);
            
            entity.setJobChain(jobChain);
              
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
