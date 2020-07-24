package com.sos.joc.job.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;
import com.sos.schema.JsonValidator;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {

    private static final String API_CALL = "./job/p";

    @Override
    public JOCDefaultResponse postJobP(String accessToken, byte[] jobFilterBytes) {

        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(jobFilterBytes, JobFilter.class);
            JobFilter jobFilter = Globals.objectMapper.readValue(jobFilterBytes, JobFilter.class);
            
            // TODO: folder permissions
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("job", jobFilter.getJob());
            String jobPath = normalizePath(jobFilter.getJob());
            checkFolderPermissions(jobPath);
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryJobsDBLayer dbJobsLayer = new InventoryJobsDBLayer(connection);
            Long instanceId = dbItemInventoryInstance.getId();
            String documentation = null;
            DBItemInventoryJob inventoryJob = null;
            if ("/scheduler_file_order_sink".equals(jobPath)) {
                inventoryJob = new DBItemInventoryJob();
                inventoryJob.setName("/scheduler_file_order_sink");
            } else {
                inventoryJob = dbJobsLayer.getInventoryJobByName(jobPath, instanceId);
                DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
                documentation = dbDocLayer.getDocumentationPath(jobFilter.getJobschedulerId(), JobSchedulerObjectType.JOB, jobPath);
                if (inventoryJob == null) {
                    throw new DBMissingDataException("no entry found in DB for job: " + jobFilter.getJob());
                }
            }
            JobP job = JobPermanent.getJob(inventoryJob, dbJobsLayer, documentation, jobFilter.getCompact(), instanceId);
            JobP200 entity = new JobP200();
            entity.setJob(job);
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