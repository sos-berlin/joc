package com.sos.joc.db.inventory.files;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.tree.Tree;


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
    
    public boolean isEmptyFolder(Long instanceId, String fileDirectory) throws DBConnectionRefusedException, DBInvalidDataException  {
        try {
            fileDirectory = Globals.normalizePath(fileDirectory);
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileDirectory = :fileDirectory");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileDirectory", fileDirectory);
            List<DBItemInventoryFile> result = getSession().getResultList(query);
            return result==null || result.size()==0;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<DBItemInventoryFile> getFiles(Long instanceId, String folder) throws DBConnectionRefusedException, DBInvalidDataException  {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileDirectory = :fileDirectory");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileDirectory", folder);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public boolean fileExists(Long instanceId, String path) throws DBConnectionRefusedException, DBInvalidDataException  {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileName = :path");
            Query<DBItemInventoryFile> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query) != null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public boolean folderExists(Long instanceId, String path) throws DBConnectionRefusedException, DBInvalidDataException  {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and (fileDirectory = :path or fileDirectory like :likePath)");
            Query<Long> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("path", path);
            query.setParameter("likePath", (path+"/").replaceAll("/+", "/") + "%");
            return getSession().getSingleResult(query) > 0;
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
    
    public Set<Tree> getFoldersByFolderAndType(Long instanceId, String folderName, Set<String> types) throws DBConnectionRefusedException, DBInvalidDataException {
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
            sql.append(" group by fileDirectory");
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
                return result.stream().map(s -> {
                    Tree tree = new Tree();
                    tree.setPath(s);
                    return tree;
                }).collect(Collectors.toSet());
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
}