package com.sos.joc.db.documentation;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class DocumentationDBLayer extends DBLayer {

    public DocumentationDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemDocumentation getDocumentation(String schedulerId, String directory, String name) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and directory = :directory");
            sql.append(" and name = :name");
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("directory", directory);
            query.setParameter("name", name);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemDocumentationImage getDocumentationImage(String schedulerId, byte[] image) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION_IMAGES);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and image = :image");
            Query<DBItemDocumentationImage> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("image", image);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemDocumentation> getDocumentations (String schedulerId, List<String> folders)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and directory in :folders");
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("folders", folders);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemDocumentation> getDocumentation (String schedulerId, String folder)
            throws DBConnectionRefusedException, DBInvalidDataException {
        return getDocumentation(schedulerId, folder, false);
    }

    public List<DBItemDocumentation> getDocumentation (String schedulerId, String folder, boolean recursive)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and directory in :folders");
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (recursive) {
                query.setParameter("folders", MatchMode.END.toMatchString(folder));
            } else {
                query.setParameter("folders", MatchMode.START.toMatchString(folder));
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemDocumentationImage getDocumentationImageById (Long id) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION_IMAGES);
            sql.append(" where id = :id");
            sql.append(" and image = :image");
            Query<DBItemDocumentationImage> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
