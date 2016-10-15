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
import com.sos.joc.model.jobChain.JobChainP200;
import com.sos.joc.model.jobChain.JobChainFilter;

@Path("job_chain")
public class JobChainResourcePImpl extends JOCResourceImpl implements IJobChainResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobChainP(String accessToken, JobChainFilter jobChainFilter) throws Exception {

        LOGGER.debug("init jobs_chain/p");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO JOC Cockpit Webservice: Reading from database
            // InventoryJobsDBLayer dbLayer = new
            // InventoryJobsDBLayer(Globals.sosHibernateConnection,
            // jobChainFilterSchema.getJobschedulerId());
            // DBItemInventoryJobChain inventoryJobChain =
            // dbLayer.getInventoryJobChainByName(jobChainFilterSchema.getJobChain());
            JobChainP200 entity = new JobChainP200();
            entity.setJobChain(JobChains.getPJobChains(jobChainFilter.getCompact()).get(0));
            entity.setDeliveryDate(new Date());
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
