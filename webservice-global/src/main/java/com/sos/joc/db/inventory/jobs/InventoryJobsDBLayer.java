package com.sos.joc.db.inventory.jobs;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

/** @author Uwe Risse */
public class InventoryJobsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryJobsDBLayer.class);

    public InventoryJobsDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryJob getInventoryJobByName(String name, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and name  = :name");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("name", name);
            List<DBItemInventoryJob> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobs(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return query.list();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobs(Boolean isOrderJob, Long instanceId) throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            Query query = getConnection().createQuery(sql.toString());
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            query.setParameter("instanceId", instanceId);
            return query.list();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public Date getJobConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
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
                return (Date) result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryLock> getLocksIfExists(Long id, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select il from ").append(DBITEM_INVENTORY_LOCKS).append(" il, ");
            sql.append(DBITEM_INVENTORY_APPLIED_LOCKS).append(" ial");
            sql.append(" where il.id = ial.lockId");
            sql.append(" and ial.jobId = :id");
            sql.append(" and il.instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryLock> result = query.list();
            if (result != null) {
                return result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChain> getJobChainsByJobId(Long id, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn ");
            sql.append("where ijc.id = ijcn.jobChainId ");
            sql.append("and ijcn.jobId = :id ");
            sql.append("and ijc.instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null) {
                return result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobsFilteredByJobPath(String jobPath, Boolean isOrderJob, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where name = :jobPath");
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            sql.append(" and instanceId = :instanceId");
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("jobPath", jobPath);
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJob> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJob> getInventoryJobsFilteredByFolder(String folderName, Boolean isOrderJob, boolean recursive, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_JOBS);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select ij from ");
                sql.append(DBITEM_INVENTORY_JOBS).append(" ij, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where ij.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and ij.instanceId = :instanceId");
            }
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            Query query = getConnection().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("folderName", folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            query.setParameter("instanceId", instanceId);
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            List<DBItemInventoryJob> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
}