package com.sos.joc.db.audit;

import java.util.List;
import java.util.Set;

import org.hibernate.SessionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.order.OrderPath;


public class AuditLogDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogDBLayer.class);
    
    public AuditLogDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }
    
    public List<DBItemAuditLog> getAuditLogByOrders(String schedulerId, List<OrderPath> orders, Integer limit) throws DBConnectionRefusedException,
        DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (orders != null && !orders.isEmpty()) {
                sql.append(" and");
                boolean first = true;
                for(int i = 0; i < orders.size(); i++) {
                    if(!first) {
                        sql.append(" or");
                    }
                    sql.append(" (orderId = :orderId").append(i);
                    sql.append(" and jobChain = :jobChain").append(i);
                    sql.append(" and folder = :folder").append(i).append(")");
                    first = false;
                }
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (orders != null && !orders.isEmpty()) {
                for (int i = 0; i < orders.size(); i++) {
                    String jobChain = orders.get(i).getJobChain().substring(orders.get(i).getJobChain().lastIndexOf("/") + 1);
                    String folder = orders.get(i).getJobChain().substring(0, orders.get(i).getJobChain().lastIndexOf("/"));;
                    query.setParameter("orderId" + i, orders.get(i).getOrderId());
                    query.setParameter("jobChain" + i, jobChain);
                    query.setParameter("folder" + i, folder);
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
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByJobs(String schedulerId, Set<String> jobs, Integer limit) throws DBConnectionRefusedException,
        DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (jobs != null && !jobs.isEmpty()) {
                if (jobs.size() == 1) {
                    sql.append(" and job = :job");
                } else {
                    sql.append(" and job in (:job)");
                }
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (jobs != null && !jobs.isEmpty()) {
                if (jobs.size() == 1) {
                    query.setParameter("job", jobs.iterator().next());
                } else {
                    query.setParameterList("job", jobs);
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
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByFolders(String schedulerId, Set<String> folders, Integer limit) throws DBConnectionRefusedException,
        DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    sql.append(" and folder = :folder");
                } else {
                    sql.append(" and folder in (:folder)");
                }
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
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
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

}
