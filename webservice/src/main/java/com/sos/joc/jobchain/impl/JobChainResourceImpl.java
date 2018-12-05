package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JOCXmlJobChainCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResource;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainV200;

@Path("job_chain")
public class JobChainResourceImpl extends JOCResourceImpl implements IJobChainResource {

    private static final String API_CALL = "./job_chain";

    @Override
    public JOCDefaultResponse postJobChain(String xAccessToken, String accessToken, JobChainFilter jobChainFilter) throws Exception {
        return postJobChain(getAccessToken(xAccessToken, accessToken), jobChainFilter);
    }

    public JOCDefaultResponse postJobChain(String accessToken, JobChainFilter jobChainFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainFilter, accessToken, jobChainFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobChainFilter.getJobschedulerId(), accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JobChainV200 entity = new JobChainV200();
            if (checkRequiredParameter("jobChain", jobChainFilter.getJobChain())) {
                String jobChainPath = normalizePath(jobChainFilter.getJobChain());

                JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(this, accessToken);
                entity.setDeliveryDate(Date.from(Instant.now()));
                entity.setJobChain(jocXmlCommand.getJobChain(jobChainPath, jobChainFilter.getCompact(), jobChainFilter.getCompactView(),
                        jobChainFilter.getMaxOrders()));
                entity.setNestedJobChains(jocXmlCommand.getNestedJobChains(jobChainFilter.getCompactView()));
            }
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}