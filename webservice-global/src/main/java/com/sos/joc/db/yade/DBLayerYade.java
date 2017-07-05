package com.sos.joc.db.yade;

import java.util.Date;

import javax.persistence.TemporalType;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;


public class DBLayerYade extends DBLayer {

    public DBLayerYade(SOSHibernateSession session) {
        super(session);
    }

    // TODO: at the moment only state = 5 (TRANSFERRED) is checked
    public Integer getSuccessfulTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBItemYadeFiles.class.getSimpleName());
            sql.append(" where state = 5");
            sql.append(" and transferId in (select id from ");
            sql.append(DBItemYadeTransfers.class.getSimpleName());
            if (from != null || to != null) {
                sql.append(" where");
            }
            if (from != null) {
                sql.append(" end > :from");
            }
            if (to != null) {
                if (from != null) {
                    sql.append(" and");
                }
                sql.append(" end < :to");
            }
            sql.append(")");
            Query<Integer> query = getSession().createQuery(sql.toString());
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    // TODO: at the moment only state = 7 (TRANSFER_HAS_ERRORS) is checked
    public Integer getFailedTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBItemYadeFiles.class.getSimpleName());
            sql.append(" where state = 7");
            sql.append(" and transferId in (select id from ");
            sql.append(DBItemYadeTransfers.class.getSimpleName());
            if (from != null || to != null) {
                sql.append(" where");
            }
            if (from != null) {
                sql.append(" end > :from");
            }
            if (to != null) {
                if (from != null) {
                    sql.append(" and");
                }
                sql.append(" end < :to");
            }
            sql.append(")");
            Query<Integer> query = getSession().createQuery(sql.toString());
            if (from != null) {
                query.setParameter("from", from, TemporalType.TIMESTAMP);
            }
            if (to != null) {
                query.setParameter("to", to, TemporalType.TIMESTAMP);
            }
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}