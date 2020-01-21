package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.classes.jobs.JobsVCallable;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobV200;
import com.sos.schema.JsonValidator;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {

	private static final String API_CALL = "./job";

	@Override
	public JOCDefaultResponse postJob(String accessToken, byte[] jobFilterBytes) {
	    SOSHibernateSession connection = null;
		try {
		    JsonValidator.validateFailFast(jobFilterBytes, JobFilter.class);
            JobFilter jobFilter = Globals.objectMapper.readValue(jobFilterBytes, JobFilter.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken,
					jobFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredParameter("job", jobFilter.getJob());
			String jobPath = normalizePath(jobFilter.getJob());
			JobV200 entity = new JobV200();
			JobV job = null;
			
			if (versionIsOlderThan("1.12.6")) {
			    JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken);
			    job = jocXmlCommand.getJob(jobPath, jobFilter.getCompact(), jobFilter.getCompactView(), false);
			} else {
			    JOCJsonCommand command = new JOCJsonCommand(this);
	            command.setUriBuilderForJobs();
	            command.addJobCompactQuery(jobFilter.getCompact());
	            jobFilter.setJob(jobPath);
	            JobsVCallable j = new JobsVCallable(jobFilter, command, accessToken, false, null);
	            job = j.getJob();
			}
			if (job != null && job.getState() != null && job.getState().get_text() == JobStateText.STOPPED) { //JOC-678
			    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
	            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
	            job.getState().setManually(dbLayer.isManuallyStopped(jobFilter.getJobschedulerId(), job.getPath()));
			}
			entity.setJob(job);
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