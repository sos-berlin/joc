package com.sos.joc.jobchains.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JOCXmlJobChainCommand;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.jobchains.resource.IJobChainsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsV;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {

    private static final String API_CALL = "./job_chains";

    @Override
    public JOCDefaultResponse postJobChains(String xAccessToken, String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        return postJobChains(getAccessToken(xAccessToken, accessToken), jobChainsFilter);
    }

    public JOCDefaultResponse postJobChains(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainsFilter, accessToken, jobChainsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainsV entity = new JobChainsV();
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
            List<String> ordersWithTempRunTime = dbLayer.getOrdersWithTemporaryRuntime(dbItemInventoryInstance.getId());

            JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(this, accessToken, ordersWithTempRunTime);
            List<JobChainPath> jobChains = jobChainsFilter.getJobChains();
            List<Folder> folders = addPermittedFolder(jobChainsFilter.getFolders());
            List<JobChainV> listOfJobChains = null;

            if (jobChains != null && !jobChains.isEmpty()) {
                listOfJobChains = jocXmlCommand.getJobChainsFromShowJobChain(jobChains, jobChainsFilter);
            } else if (folders != null && !folders.isEmpty()) {
                listOfJobChains = jocXmlCommand.getJobChainsFromShowState(folders, jobChainsFilter);
            } else {
                listOfJobChains = jocXmlCommand.getJobChainsFromShowState(jobChainsFilter);
            }
            listOfJobChains = addAllPermittedJobChainss(listOfJobChains);
            entity.setJobChains(listOfJobChains);
            entity.setNestedJobChains(jocXmlCommand.getNestedJobChains());
            entity.setDeliveryDate(new Date());

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

    private List<JobChainV> addAllPermittedJobChainss(List<JobChainV> jobChainsToAdd) throws SessionNotExistException {
        List<JobChainV> listOfJobChains = new ArrayList<JobChainV>();
        if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
            for (JobChainV jobChain : jobChainsToAdd)
                if (canAdd(jobChain, jobChain.getPath())) {
                    listOfJobChains.add(jobChain);
                }
        } else {
            listOfJobChains.addAll(jobChainsToAdd);
        }
        return listOfJobChains;

    }
}
