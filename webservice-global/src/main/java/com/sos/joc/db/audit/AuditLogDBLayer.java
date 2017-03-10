package com.sos.joc.db.audit;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.TemporalType;

import org.hibernate.SessionException;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.order.OrderPath;


@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class AuditLogDBLayer extends DBLayer {

    public AuditLogDBLayer(SOSHibernateSession connection) {
        super(connection);
    }
    
    public List<DBItemAuditLog> getAuditLogByOrders(String schedulerId, List<OrderPath> orders, Integer limit, Date from, Date to, String ticketLink,
            String account) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (from != null) {
                sql.append(" and created >= :from");
            }
            if (to != null) {
                sql.append(" and created <= :to");                
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                sql.append(" and ticketLink = :ticketLink");
            }
            if (account != null && !account.isEmpty()) {
                sql.append(" and account = :account");
            }
            if (orders != null && !orders.isEmpty()) {
                sql.append(" and");
                for(int i = 0; i < orders.size(); i++) {
                    if(i == 0) {
                        sql.append(" (");
                    } else if(i != 0 && i != orders.size()) {
                        sql.append(" or");
                    }
                    sql.append(" (jobChain = :jobChain").append(i);
                    if(orders.get(i).getOrderId() != null && !orders.get(i).getOrderId().isEmpty()) {
                        sql.append(" and orderId = :orderId").append(i);
                    }
                    sql.append(")");
                    if(i == orders.size() -1) {
                        sql.append(")");
                    }
                }
            }
            sql.append(" order by created desc");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                query.setParameter("ticketLink", ticketLink);
            }
            if (account != null && !account.isEmpty()) {
                query.setParameter("account", account);
            }
            if (orders != null && !orders.isEmpty()) {
                for (int i = 0; i < orders.size(); i++) {
                    String jobChain = orders.get(i).getJobChain();
                    query.setParameter("jobChain" + i, jobChain);
                    if(orders.get(i).getOrderId() != null && !orders.get(i).getOrderId().isEmpty()) {
                        query.setParameter("orderId" + i, orders.get(i).getOrderId());
                    }
                }
            }
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByJobs(String schedulerId, List<JobPath> jobs, Integer limit, Date from, Date to, String ticketLink, String account)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (from != null) {
                sql.append(" and created >= :from");
            }
            if (to != null) {
                sql.append(" and created <= :to");                
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                sql.append(" and ticketLink = :ticketLink");
            }
            if (account != null && !account.isEmpty()) {
                sql.append(" and account = :account");
            }
            if (jobs != null && !jobs.isEmpty()) {
                sql.append(" and");
                for(int i = 0; i < jobs.size(); i++) {
                    if(i == 0) {
                        sql.append(" (");
                    } else if(i != 0 && i != jobs.size()) {
                        sql.append(" or");
                    }
                    sql.append(" (job = :job").append(i).append(")");
                    if(i == jobs.size() -1) {
                        sql.append(")");
                    }
                }
            }
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                query.setParameter("ticketLink", ticketLink);
            }
            if (account != null && !account.isEmpty()) {
                query.setParameter("account", account);
            }
            if (jobs != null && !jobs.isEmpty()) {
                for (int i = 0; i < jobs.size(); i++) {
                    String job = jobs.get(i).getJob();
                    query.setParameter("job" + i, job);
                }
            }
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAllAuditLogs(String schedulerId, Integer limit, Date from, Date to, String ticketLink, String account)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (from != null) {
                sql.append(" and created >= :from");
            }
            if (to != null) {
                sql.append(" and created <= :to");                
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                sql.append(" and ticketLink = :ticketLink");
            }
            if (account != null && !account.isEmpty()) {
                sql.append(" and account = :account");
            }
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                query.setParameter("ticketLink", ticketLink);
            }
            if (account != null && !account.isEmpty()) {
                query.setParameter("account", account);
            }
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByFolders(String schedulerId, Set<String> folders, Integer limit, Date from, Date to, String ticketLink,
            String account) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (from != null) {
                sql.append(" and created >= :from");
            }
            if (to != null) {
                sql.append(" and created <= :to");                
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                sql.append(" and ticketLink = :ticketLink");
            }
            if (account != null && !account.isEmpty()) {
                sql.append(" and account = :account");
            }
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    sql.append(" and folder = :folder");
                } else {
                    sql.append(" and folder in (:folder)");
                }
            }
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            if (ticketLink != null && !ticketLink.isEmpty()) {
                query.setParameter("ticketLink", ticketLink);
            }
            if (account != null && !account.isEmpty()) {
                query.setParameter("account", account);
            }
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    query.setParameter("folder", folders.iterator().next());
                } else {
                    query.setParameterList("folder", folders);
                }
            }
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        } 
    }
}
