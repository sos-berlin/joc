package com.sos.joc.job.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobV200;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {

    private static final String API_CALL = "./job";

    public JOCDefaultResponse postJob(String accessToken, JobFilter jobFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("job", jobFilter.getJob());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(connection);
            String jobPath = normalizePath(jobFilter.getJob());
            List<String> jobsWithTempRunTime = dbLayer.getJobsWithTemporaryRuntime(dbItemInventoryInstance.getId(), jobPath);
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance, accessToken, jobsWithTempRunTime);
            JobV200 entity = new JobV200();
            entity.setJob(jocXmlCommand.getJob(jobPath, jobFilter.getCompact(), true, null));
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
}