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


public class AuditLogDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogDBLayer.class);
    
    public AuditLogDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }
    
    public List<DBItemAuditLog> getAuditLogByOrders(String schedulerId, Set<String> orders) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_AUDIT_LOG);
            sql.append(" where schedulerId = :schedulerId");
            if (orders != null && !orders.isEmpty()) {
                if (orders.size() == 1) {
                    sql.append(" and orderId = :orderId");
                } else {
                    sql.append(" and orderId in (:orderId)");
                }
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (orders != null && !orders.isEmpty()) {
                if (orders.size() == 1) {
                    query.setParameter("orderId", orders.iterator().next());
                } else {
                    query.setParameterList("orderId", orders);
                }
            }
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByJobs(String schedulerId, Set<String> jobs) throws DBConnectionRefusedException, DBInvalidDataException {
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
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

    public List<DBItemAuditLog> getAuditLogByFolders(String schedulerId, Set<String> folders) throws DBConnectionRefusedException, DBInvalidDataException {
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
            List<DBItemAuditLog> result = query.list();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        } 
    }

}
