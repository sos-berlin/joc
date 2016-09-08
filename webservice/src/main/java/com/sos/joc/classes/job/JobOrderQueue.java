package com.sos.joc.classes.job;

import com.sos.joc.model.job.JobOrderQueueFilterSchema;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdShowJob;
import com.sos.scheduler.model.objects.Spooler;


public class JobOrderQueue {

    public static String createJobOrderQueuePostCommand(final JobOrderQueueFilterSchema body) {
        boolean compact = body.getCompact();
        String jobChainNode = body.getNode();
        // if set => ???
        String orderId = body.getOrderId();
        // if set => ???
        
        SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
        schedulerObjectFactory.initMarshaller(Spooler.class);
        JSCmdShowJob showJob = schedulerObjectFactory.createShowJob();
        if (!body.getJob().isEmpty()) {
            // if body.getJob() is set => job_orders AND if compact => task_queue job_params
            // if body.getJobChain() is set => job_chain_orders job_chains AND if compact => task_queue job_params
            if(!compact) {
                if(body.getJobChain() != null && !body.getJobChain().isEmpty()) {
                    showJob.setWhat("job_orders job_chain_orders");
                }else {
                    showJob.setWhat("job_orders");
                }
            } else {
                if(body.getJobChain() != null && !body.getJobChain().isEmpty()) {
                    showJob.setWhat("job_orders task_queue job_params");
                } else {
                    showJob.setWhat("job_orders job_chain_orders job_chains task_queue job_params");
                }
            }
            showJob.setJob(body.getJob());
            showJob.setJobChain(body.getJobChain());
        }
        return schedulerObjectFactory.toXMLString(showJob);
    }

}
