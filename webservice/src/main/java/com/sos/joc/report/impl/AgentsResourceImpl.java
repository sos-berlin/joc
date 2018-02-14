package com.sos.joc.report.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.report.JobSchedulerReportDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.report.AgentsFilter;
import com.sos.joc.report.resource.IAgentsResource;

@Path("report")
public class AgentsResourceImpl extends JOCResourceImpl implements IAgentsResource {

	private static final String API_CALL = "./report/agents";

	@Override
	public JOCDefaultResponse postAgentsReport(String accessToken, AgentsFilter agentsFilter) throws Exception {
		SOSHibernateSession connection = null;

		try {
			if (agentsFilter.getJobschedulerId() == null) {
				agentsFilter.setJobschedulerId("");
			}
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, agentsFilter, accessToken,
					agentsFilter.getJobschedulerId(),
					getPermissonsJocCockpit(agentsFilter.getJobschedulerId(), accessToken)
							.getJobschedulerUniversalAgent().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			return JOCDefaultResponse.responseStatus200(new JobSchedulerReportDBLayer(connection).getExecutedAgentTasks(
					agentsFilter.getJobschedulerId(), agentsFilter.getAgents(),
					JobSchedulerDate.getDateFrom(agentsFilter.getDateFrom(), agentsFilter.getTimeZone()),
					JobSchedulerDate.getDateTo(agentsFilter.getDateTo(), agentsFilter.getTimeZone())));

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