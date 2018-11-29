package com.sos.joc.db.documentation;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.docu.DocumentationShowFilter;

public class DocumentationDBLayer extends DBLayer {

    public DocumentationDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemDocumentation getDocumentation(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path = :path");
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemDocumentationImage getDocumentationImage(Long id) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (id == null) {
               return null; 
            }
            return getSession().get(DBItemDocumentationImage.class, id);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemDocumentation> getDocumentations(String schedulerId, List<String> folders)
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
    
    public List<DBItemDocumentation> getDocumentations(String schedulerId, String folder)
            throws DBConnectionRefusedException, DBInvalidDataException {
        return getDocumentations(schedulerId, folder, false);
    }

    public List<DBItemDocumentation> getDocumentations(String schedulerId, String folder, boolean recursive)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and directory = :folder");
            if (recursive) {
                sql.append(" or directory like :folder2");
            }
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("folder", folder);
            if (recursive) {
                query.setParameter("folder2", MatchMode.START.toMatchString(folder + "/"));
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public String getDocumentationPath(DocumentationShowFilter documentationFilter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select d.path from ")
                .append(DBITEM_DOCUMENTATION).append(" d, ")
                .append(DBITEM_DOCUMENTATION_USAGE).append(" du");
            sql.append(" where d.id = du.documentationId");
            sql.append(" and du.schedulerId = :schedulerId");
            sql.append(" and du.objectType = :objectType");
            sql.append(" and du.path = :path");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", documentationFilter.getJobschedulerId());
            query.setParameter("objectType", documentationFilter.getType().name());
            query.setParameter("path", documentationFilter.getPath());
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getFoldersByFolder(String schedulerId, String folderName) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select directory from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( directory = :folderName or directory like :likeFolderName )");
            }
            sql.append(" group by directory");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", MatchMode.START.toMatchString(folderName + "/"));
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemDocumentationUsage> getDocumentationUsage (String schedulerId, Long documentationId)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION_USAGE);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and documentationId = :documentationId");
            Query<DBItemDocumentationUsage> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("documentationId", documentationId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemDocumentationUsage> getDocumentationUsage (String schedulerId, String directory, String name)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("from ").append(DBITEM_DOCUMENTATION_USAGE).append(" du, ");
            hql.append(DBITEM_DOCUMENTATION).append(" d");
            hql.append(" where d.schedulerId = :schedulerId");
            hql.append(" and d.directory = :directory");
            hql.append(" and d.name = :name");
            hql.append(" and du.documentationId = d.id");
            Query<DBItemDocumentationUsage> query = getSession().createQuery(hql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("directory", directory);
            query.setParameter("name", name);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
