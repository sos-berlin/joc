package com.sos.joc.db.inventory.jobchains;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryJobChainNode;
import com.sos.jitl.reporting.db.DBLayer;

/** @author SP */
public class InventoryJobChainsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryJobChainsDBLayer.class);
    private String jobSchedulerId;

    public InventoryJobChainsDBLayer(SOSHibernateConnection connection, String jobSchedulerId) {
        super(connection);
        this.jobSchedulerId = jobSchedulerId;
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryJobChain getJobChainByPath(String jobChainPath) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            sql.append(" where name = :name");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", jobChainPath);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryJobChain getJobChainByName(String jobChainName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            sql.append(" where baseName = :name");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", jobChainName);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    public Date getJobChainConfigurationDate(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select ifile.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" ifile, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc ");
            sql.append(" where ifile.id = ijc.fileId");
            sql.append(" and ijc.id = :id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            Object result = query.uniqueResult();
            if (result != null) {
                return (Date)result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChainNode> getJobChainNodesByJobChainId(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("from ").append(DBITEM_INVENTORY_JOB_CHAIN_NODES);
            sql.append(" where jobChainId = :id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventoryJobChainNode> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChain> getJobChainsByFolder(String folderPath, boolean recursive) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            if (recursive) {
                sql.append(" where name like :name");
            } else {
                sql.append(" where name = :name");
            }
            Query query = getConnection().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("name", "%" + folderPath + "%");
            } else {
                query.setParameter("name", folderPath);
            }
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChain> getJobChains() throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select jobchains from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" jobchains, ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" instance ");
            sql.append("where jobchains.instanceId = instance.id ");
            sql.append("and instance.schedulerId = :jobschedulerId");
           Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobschedulerId", this.jobSchedulerId);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }        
    }
    
}