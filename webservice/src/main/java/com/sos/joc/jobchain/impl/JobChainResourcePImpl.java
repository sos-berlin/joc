package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JobChainPermanent;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceP;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.jobChain.JobChainP200;

@Path("job_chain")
public class JobChainResourcePImpl extends JOCResourceImpl implements IJobChainResourceP {

    private static final String API_CALL = "./job_chain/p";

    @Override
    public JOCDefaultResponse postJobChainP(String accessToken, JobChainFilter jobChainFilter) {


        SOSHibernateConnection connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            initLogging(API_CALL, jobChainFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobChain()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainP200 entity = new JobChainP200();
            Long instanceId = dbItemInventoryInstance.getId();
            InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(connection);
            DBItemInventoryJobChain inventoryJobChain = dbLayer.getJobChainByPath(normalizePath(jobChainFilter.getJobChain()), instanceId);
            if (inventoryJobChain == null){
                    String errMessage = String.format("job chain %s for instance %s with internal id %s not found in table %s", jobChainFilter.getJobChain(),jobChainFilter.getJobschedulerId(),instanceId,
                            DBLayer.TABLE_INVENTORY_JOB_CHAINS);
                    throw new DBInvalidDataException(errMessage);
            }
            JobChainP jobChain = JobChainPermanent.initJobChainP(dbLayer, inventoryJobChain, jobChainFilter.getCompact(), instanceId);
            if (jobChain != null) {
                entity.setJobChain(jobChain);
                if (!JobChainPermanent.NESTED_JOB_CHAIN_NAMES.isEmpty()) {
                    List<JobChainP> nestedJobChains = new ArrayList<JobChainP>();
                    for (String nestedJobChainName : JobChainPermanent.NESTED_JOB_CHAIN_NAMES) {
                        DBItemInventoryJobChain nestedJobChain = null;
                        if (nestedJobChainName.contains("/")) {
                            nestedJobChain = dbLayer.getJobChainByPath(normalizePath(nestedJobChainName), instanceId);
                        } else {
                            nestedJobChain = dbLayer.getJobChainByName(normalizePath(nestedJobChainName), instanceId);
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
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
        

}