package com.sos.joc.db.report;

import java.time.Instant;
import java.util.Date;

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

    public Agents getExecutedAgentTasks(String jobschedulerId, Date from, Date to) throws DBConnectionRefusedException, DBInvalidDataException{
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
            Agents agents = new Agents();
            agents.setAgents(getSession().getResultList(query));
            agents.setDeliveryDate(Date.from(Instant.now()));
            return agents;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}