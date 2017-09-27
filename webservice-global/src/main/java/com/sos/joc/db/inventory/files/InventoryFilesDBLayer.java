package com.sos.joc.db.inventory.files;

import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;


public class InventoryFilesDBLayer extends DBLayer {

    public InventoryFilesDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public List<DBItemInventoryFile> getFiles(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException  {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }       
    }
    
    public List<DBItemInventoryFile> getFilesByFileType(Long instanceId, String fileType) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileType = :fileType");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileType", fileType);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }       
    }
    
    public List<String> getFolders(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<String> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }       
    }
    
    public List<String> getFoldersByFolderAndType(Long instanceId, String folderName, Set<String> types) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and (fileDirectory = :folderName or fileDirectory like :likeFolderName)");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    sql.append(" and fileType = :fileType");
                } else {
                    sql.append(" and fileType in (:fileType)");
                }
            }
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    query.setParameter("fileType", types.iterator().next());
                } else {
                    query.setParameterList("fileType", types);
                }
            }
            List<String> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }      
    }
    
}