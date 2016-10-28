package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JobChainPermanent;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceP;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.jobChain.JobChainP200;

@Path("job_chain")
public class JobChainResourcePImpl extends JOCResourceImpl implements IJobChainResourceP {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourcePImpl.class);
    private static final String API_CALL = "./job_chain/p";

    @Override
    public JOCDefaultResponse postJobChainP(String accessToken, JobChainFilter jobChainFilter) throws Exception {

        LOGGER.debug(API_CALL);

        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainFilter.getJobschedulerId(), getPermissons(accessToken).getJob()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainP200 entity = new JobChainP200();
            InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(Globals.sosHibernateConnection);
            Long instanceId = dbItemInventoryInstance.getId();
            DBItemInventoryJobChain inventoryJobChain = dbLayer.getJobChainByPath(jobChainFilter.getJobChain(), instanceId);
            JobChainP jobChain = JobChainPermanent.initJobChainP(dbLayer, inventoryJobChain, jobChainFilter.getCompact(), instanceId);
            if (jobChain != null) {
                entity.setJobChain(jobChain);
                if (!JobChainPermanent.NESTED_JOB_CHAIN_NAMES.isEmpty()) {
                    List<JobChainP> nestedJobChains = new ArrayList<JobChainP>();
                    for (String nestedJobChainName : JobChainPermanent.NESTED_JOB_CHAIN_NAMES) {
                        DBItemInventoryJobChain nestedJobChain = null;
                        if (nestedJobChainName.contains("/")) {
                            nestedJobChain = dbLayer.getJobChainByPath(nestedJobChainName, instanceId);
                        } else {
                            nestedJobChain = dbLayer.getJobChainByName(nestedJobChainName, instanceId);
                        }
                        if (nestedJobChain != null) {
                            JobChainP nestedJobChainP = JobChainPermanent.initJobChainP(dbLayer, nestedJobChain, jobChainFilter.getCompact(),
                                    instanceId);
                            if (nestedJobChainP != null) {
                                nestedJobChains.add(nestedJobChainP);
                            }
                        }
                    }
                    if (!nestedJobChains.isEmpty()) {
                        entity.setNestedJobChains(nestedJobChains);
                        JobChainPermanent.NESTED_JOB_CHAIN_NAMES.clear();
                    }
                } else {
                    entity.setNestedJobChains(null);
                }
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobChainFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobChainFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}