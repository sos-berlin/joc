package com.sos.joc.db.submissions;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemSubmission;
import com.sos.jitl.reporting.db.DBItemSubmittedObject;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class SubmissionsDBLayer extends DBLayer {

    public SubmissionsDBLayer(SOSHibernateSession session) {
        super(session);
    }

    public List<DBItemSubmission> getSubmissionsBySubmissionId(Long submissionId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_SUBMISSIONS);
            sql.append(" where submissionId = :submissionId");
            Query<DBItemSubmission> query = getSession().createQuery(sql.toString());
            query.setParameter("submissionId", submissionId);
            List<DBItemSubmission> result = getSession().getResultList(query);
            if (result == null) {
                result = new ArrayList<DBItemSubmission>();
            }
            return result;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemSubmission saveSubmission(DBItemSubmission dbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().save(dbItem);
            return dbItem;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteSubmission(DBItemSubmission dbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().delete(dbItem);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemSubmittedObject getSubmittedObject(String schedulerId, String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_SUBMITTED_OBJECTS);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path = :path");
            Query<DBItemSubmittedObject> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemSubmittedObject saveOrUpdateSubmittedObject(DBItemSubmittedObject dbItem) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            dbItem.setModified(Date.from(Instant.now()));
            if (dbItem.getId() != null) {
                getSession().update(dbItem);
            } else {
                getSession().save(dbItem);
            }
            return dbItem;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteSubmittedObject(DBItemSubmittedObject dbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().delete(dbItem);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
