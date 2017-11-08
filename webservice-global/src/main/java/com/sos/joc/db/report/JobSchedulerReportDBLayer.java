package com.sos.joc.db.report;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.report.Agent;
import com.sos.joc.model.report.Agents;

public class JobSchedulerReportDBLayer extends DBLayer {
    
    private static final String AGENT_TASKS = AgentTasks.class.getName();
    
    public JobSchedulerReportDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public Agents getExecutedAgentTasks(String jobschedulerId, List<String> agentList, Date from, Date to) throws DBConnectionRefusedException, DBInvalidDataException{
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_TASKS);
            sql.append(" (count(id), schedulerId, agentUrl, cause) from ").append(DBITEM_REPORT_TASKS);
            sql.append(" where error = 0");
            sql.append(" and endTime is not null");
            sql.append(" and agentUrl is not null");
            if (jobschedulerId != null && !jobschedulerId.isEmpty()) {
                sql.append(" and schedulerId = :jobschedulerId"); 
            }
            if (from != null) {
                sql.append(" and startTime >= :dateFrom"); 
            }
            if (to != null) {
                sql.append(" and startTime < :dateTo"); 
            }
            if (agentList != null && !agentList.isEmpty()) {
               if (agentList.size() == 1) {
                   sql.append(" and agentUrl = :agent");  
               } else {
                   sql.append(" and agentUrl in (:agents)");
               }
            }
            sql.append(" group by schedulerId, agentUrl, cause");
            Query<Agent> query = getSession().createQuery(sql.toString());
            if (jobschedulerId != null && !jobschedulerId.isEmpty()) {
                query.setParameter("jobschedulerId", jobschedulerId);
            }
            if (from != null) {
                query.setParameter("dateFrom", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("dateTo", to, TemporalType.TIMESTAMP);
            }
            if (agentList != null && !agentList.isEmpty()) {
                if (agentList.size() == 1) {
                    query.setParameter("agent", agentList.get(0));
                } else {
                    query.setParameterList("agents", agentList);
                }
            }
            Agents agents = new Agents();
            List<Agent> agentsL = getSession().getResultList(query);
            Long totalNumOfSuccessfulTasks = 0L;
            if (agentsL != null && !agentsL.isEmpty()) {
               for (Agent a : agentsL) {
                   totalNumOfSuccessfulTasks = totalNumOfSuccessfulTasks + a.getNumOfSuccessfulTasks(); 
               }
            }
            agents.setTotalNumOfSuccessfulTasks(totalNumOfSuccessfulTasks);
            agents.setAgents(agentsL);
            agents.setDeliveryDate(Date.from(Instant.now()));
            return agents;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}