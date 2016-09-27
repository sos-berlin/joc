package com.sos.joc.jobchain.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchain.JobChains;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceP;
import com.sos.joc.model.jobChain.JobChain200PSchema;
import com.sos.joc.model.jobChain.JobChainFilterSchema;

@Path("job_chain")
public class JobChainResourcePImpl extends JOCResourceImpl implements IJobChainResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobChainP(String accessToken, JobChainFilterSchema jobChainFilterSchema) throws Exception {

        LOGGER.debug("init jobs_chain/p");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChain200PSchema entity = new JobChain200PSchema();

            // TODO JOC Cockpit Webservice: Reading from database
            entity.setDeliveryDate(new Date());
            // InventoryJobsDBLayer dbLayer = new
            // InventoryJobsDBLayer(Globals.sosHibernateConnection,
            // jobChainFilterSchema.getJobschedulerId());
            // DBItemInventoryJobChain inventoryJobChain =
            // dbLayer.getInventoryJobChainByName(jobChainFilterSchema.getJobChain());
            entity.setJobChain(JobChains.getPJobChains(jobChainFilterSchema.getCompact()).get(0));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
