package com.sos.joc.db.inventory.jobs;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryJobsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryJobsDBLayer.class);
    private String jobSchedulerId;

    public InventoryJobsDBLayer(SOSHibernateConnection conn, String jobSchedulerId) {
        super(conn);
        this.jobSchedulerId = jobSchedulerId;
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryJob getInventoryJobByName(String name) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select job from ");
            sql.append(DBITEM_INVENTORY_JOBS + " as job, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where job.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "' and");
            sql.append(" (name)  = :name");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", name);

            List<DBItemInventoryJob> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobs() throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select job from ");
            sql.append(DBITEM_INVENTORY_JOBS + " as job, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where job.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "'");
            Query query = getConnection().createQuery(sql.toString());

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobs(Boolean isOrderJob) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select job from ");
            sql.append(DBITEM_INVENTORY_JOBS).append(" job, ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" instance");
            sql.append(" where job.instanceId = instance.id");
            sql.append(" and instance.schedulerId = '" + this.jobSchedulerId + "'");
            sql.append(" and job.isOrderJob = :isOrderJob");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("isOrderJob", isOrderJob);
            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    public Date getJobConfigurationDate(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ifile.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" ifile, ");
            sql.append(DBITEM_INVENTORY_JOBS).append(" ij ");
            sql.append(" where ifile.id = ij.fileId");
            sql.append(" and ij.id = :id");
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
    public List<DBItemInventoryLock> getLocksIfExists(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select il from ").append(DBITEM_INVENTORY_LOCKS).append(" il, ");
            sql.append(DBITEM_INVENTORY_APPLIED_LOCKS).append(" ial ");
            sql.append("where il.id = ial.lockId ");
            sql.append("and ial.jobId = :id");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventoryLock> result = query.list();
            if (result != null) {
                return result;
            }
            return null;
        } catch (Exception e) {
            throw new Exception(SOSHibernateConnection.getException(e));
        }
    }
 
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChain> getJobChainsByJobId(Long id) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn ");
            sql.append("where ijc.id = ijcn.jobChainId ");
            sql.append("and ijcn.jobId = :id");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null) {
                return result;
            }
            return null;
        } catch (Exception e) {
            throw new Exception(SOSHibernateConnection.getException(e));
        }
    }
 
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobsFilteredByJobPath(String jobPath, Boolean isOrderJob) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where name = :jobPath");
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobPath", jobPath);
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            List<DBItemInventoryJob> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobsFilteredByFolder(String folderName, Boolean isOrderJob, boolean recursive) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            if (recursive) {
                sql.append(" where name like :folderName ");
            } else {
                sql.append(" where name = :folderName ");
            }
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            Query query = getConnection().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("folderName", "%" + folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            List<DBItemInventoryJob> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
}