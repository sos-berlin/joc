package com.sos.joc.db.inventory.files;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryFile;
import com.sos.jitl.reporting.db.DBLayer;


public class InventoryFilesDBLayer extends DBLayer {

    public InventoryFilesDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryFile> getFiles(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryFile> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryFile> getFilesByFileType(Long instanceId, String fileType) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and fileType = :fileType");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("fileType", fileType);
            List<DBItemInventoryFile> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getFolders(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<String> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
//    @SuppressWarnings("unchecked")
//    public List<String> getFoldersByFolder(Long instanceId, String folderName, Boolean recursive) throws Exception {
//        try {
//            StringBuilder sql = new StringBuilder();
//            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
//            sql.append(" where instanceId = :instanceId");
//            if (recursive != null && recursive) {
//                sql.append(" and fileDirectory like :folderName");
//            } else {
//                sql.append(" and fileDirectory = :folderName");
//            }
//            Query query = getConnection().createQuery(sql.toString());
//            query.setParameter("instanceId", instanceId);
//            if (recursive != null && recursive) {
//                query.setParameter("folderName", folderName + "%");
//            } else {
//                query.setParameter("folderName", folderName);
//            }
//            List<String> result = query.list();
//            if (result != null && !result.isEmpty()) {
//                return result;
//            }
//            return null;
//        } catch (Exception ex) {
//            throw new Exception(SOSHibernateConnection.getException(ex));
//        }        
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<String> getFoldersByFileType(Long instanceId, String fileType) throws Exception {
//        try {
//            StringBuilder sql = new StringBuilder();
//            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
//            sql.append(" where instanceId = :instanceId");
//            sql.append(" and fileType = :fileType");
//            Query query = getConnection().createQuery(sql.toString());
//            query.setParameter("instanceId", instanceId);
//            query.setParameter("fileType", fileType);
//            List<String> result = query.list();
//            if (result != null && !result.isEmpty()) {
//                return result;
//            }
//            return null;
//        } catch (Exception ex) {
//            throw new Exception(SOSHibernateConnection.getException(ex));
//        }        
//    }
    
    @SuppressWarnings("unchecked")
    public List<String> getFoldersByFolderAndType(Long instanceId, String folderName, Boolean recursive, List<String> types) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ").append(DBITEM_INVENTORY_FILES);
            sql.append(" where instanceId = :instanceId");
            if (folderName != null) {
                if (recursive != null && recursive) {
                    sql.append(" and fileDirectory like :folderName");
                } else {
                    sql.append(" and fileDirectory = :folderName");
                }
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    sql.append(" and fileType = :fileType");
                } else {
                    sql.append(" and fileType in (:fileType)");
                }
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (folderName != null) {
                if (recursive != null && recursive) {
                    query.setParameter("folderName", folderName + "%");
                } else {
                    query.setParameter("folderName", folderName);
                } 
            }
            if (types != null && !types.isEmpty()) {
                if (types.size() == 1) {
                    query.setParameter("fileType", types.get(0));
                } else {
                    query.setParameterList("fileType", types);
                }
            }
            List<String> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
}