package com.sos.joc.report.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.joda.time.DateTimeComparator;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.report.JobSchedulerReportDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.report.Agent;
import com.sos.joc.model.report.Agents;
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
			List<DBItemReportTask> allStartedAgentTasks = new JobSchedulerReportDBLayer(connection).getAllStartedAgentTasks(
                    agentsFilter.getJobschedulerId(), agentsFilter.getAgents(),
                    JobSchedulerDate.getDateFrom(agentsFilter.getDateFrom(), agentsFilter.getTimeZone()),
                    JobSchedulerDate.getDateTo(agentsFilter.getDateTo(), agentsFilter.getTimeZone()));
			Agents agents = initAgents(allStartedAgentTasks, agentsFilter.getJobschedulerId());
			return JOCDefaultResponse.responseStatus200(agents);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}
	
	private Agents initAgents (List<DBItemReportTask> allStartedAgentTasks, String jobschedulerId) {
	    Set<String> agentUrls = new HashSet<String>();
	    Agents agents = new Agents();
	    // determine all different agents by url
        allStartedAgentTasks.forEach(agentTasks -> {agentUrls.add(agentTasks.getAgentUrl());});
        Long totalSuccessfulTasks = 0L;
        agentUrls.forEach(agentUrl -> {
            Agent agent = new Agent();
            agent.setJobschedulerId(jobschedulerId);
            agent.setAgent(agentUrl);
            agent.setNumOfSuccessfulTasks(allStartedAgentTasks.stream().filter(task -> task.getAgentUrl().equals(agentUrl) 
                    && task.getExitCode() == 0).count());
            agent.setNumOfJobs(countJobsOncePerDay(allStartedAgentTasks));
            agent.setCause(TaskCause.fromValue(allStartedAgentTasks.stream().filter(task -> task.getAgentUrl().equals(agentUrl) && task.getCause() != null)
                    .findAny().get().getCause().toUpperCase()));
            agents.getAgents().add(agent);
            });
        if (!agents.getAgents().isEmpty()) {
            totalSuccessfulTasks += agents.getAgents().get(agents.getAgents().size() -1).getNumOfSuccessfulTasks();
            agents.setTotalNumOfSuccessfulTasks(totalSuccessfulTasks);
        }
        agents.setDeliveryDate(Date.from(Instant.now()));
 	    return agents;
	}
 	
	private Long countJobsOncePerDay (List<DBItemReportTask> tasks) {
	    Long count = 0L;
	    if (tasks != null && tasks.size() > 0) {
	        count = 1L;
	    }
        DateTimeComparator dateCompare = DateTimeComparator.getDateOnlyInstance();
	    for (int i = 0; i < tasks.size() -1; i++) {
	        int retVal = dateCompare.compare(tasks.get(i).getStartTime(), tasks.get(i+1).getStartTime());
	        if (retVal != 0) {
	            count++;
	        }
	    }
	    return count;
	}
}