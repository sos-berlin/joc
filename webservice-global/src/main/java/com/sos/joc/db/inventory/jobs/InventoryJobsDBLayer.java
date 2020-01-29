package com.sos.joc.db.inventory.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.job.JobCriticalityFilter;

/** @author Uwe Risse */
public class InventoryJobsDBLayer extends DBLayer {

    public InventoryJobsDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public DBItemInventoryJob getInventoryJobByName(String name, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and name  = :name");
            Query<DBItemInventoryJob> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("name", name);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryJob> getInventoryJobs(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryJob> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryJob> getInventoryJobs(Boolean isOrderJob, List<JobCriticalityFilter> criticality, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            if (criticality != null && criticality.size() > 0) {
                sql.append( " and (");
                for (JobCriticalityFilter criticalityEntry : criticality) {
                    sql.append( "criticality='" + criticalityEntry.value().toLowerCase() + "' or ");
                }
                sql.append("1=0) ");
            }

            Query<DBItemInventoryJob> query = getSession().createQuery(sql.toString());
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Map<Long, String> getInventoryJobIdsWithProcessClasses(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(ProcessClassJob.class.getName()).append(" (id, processClassName) from ");
            sql.append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and processClassName != :processClassName");
            sql.append(" and isOrderJob = 1");
            Query<ProcessClassJob> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("processClassName", DBLayer.DEFAULT_NAME);
            List<ProcessClassJob> result = getSession().getResultList(query);
            if (result != null) {
                return result.stream().collect(Collectors.toMap(ProcessClassJob::getId, ProcessClassJob::getProcessClassName));
            }
            return new HashMap<Long, String>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
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
            Query<Date> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryLock> getLocksIfExists(Long id, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select il from ").append(DBITEM_INVENTORY_LOCKS).append(" il, ");
            sql.append(DBITEM_INVENTORY_APPLIED_LOCKS).append(" ial");
            sql.append(" where il.id = ial.lockId");
            sql.append(" and ial.jobId = :id");
            sql.append(" and il.instanceId = :instanceId");
            Query<DBItemInventoryLock> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryLock> result = getSession().getResultList(query);
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

    public List<DBItemInventoryJobChain> getJobChainsByJobId(Long id, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn ");
            sql.append("where ijc.id = ijcn.jobChainId ");
            sql.append("and ijcn.jobId = :id ");
            sql.append("and ijc.instanceId = :instanceId");
            Query<DBItemInventoryJobChain> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return result;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryJobChain> getJobChainsWithFileSink(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn ");
            sql.append("where ijc.id = ijcn.jobChainId ");
            sql.append("and ijcn.nodeType = 4 ");
            sql.append("and ijc.instanceId = :instanceId");
            Query<DBItemInventoryJobChain> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return result;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryJob> getInventoryJobsFilteredByJobPath(String jobPath, Boolean isOrderJob, List<JobCriticalityFilter> criticality,
            Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where name = :jobPath");
            if (isOrderJob != null) {
                sql.append(" and isOrderJob = :isOrderJob");
            }
            if (criticality != null && criticality.size() > 0) {
                sql.append( " and (");
                for (JobCriticalityFilter criticalityEntry : criticality) {
                    sql.append( "criticality='" + criticalityEntry.value().toLowerCase() + "' or ");
                }
                sql.append("1=0) ");
            }

            sql.append(" and instanceId = :instanceId");
            Query<DBItemInventoryJob> query = getSession().createQuery(sql.toString());
            query.setParameter("jobPath", jobPath);
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }

            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJob> result = getSession().getResultList(query);
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

    public List<DBItemInventoryJob> getInventoryJobsFilteredByFolder(String folderName, Boolean isOrderJob, List<JobCriticalityFilter> criticality,
            boolean recursive, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_JOBS);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
                if (isOrderJob != null) {
                    sql.append(" and isOrderJob = :isOrderJob");
                }
                if (criticality != null && criticality.size() > 0) {
                    sql.append( " and (");
                    for (JobCriticalityFilter criticalityEntry : criticality) {
                        sql.append( "criticality='" + criticalityEntry.value().toLowerCase() + "' or ");
                    }
                    sql.append("1=0) ");
                }

            } else {
                sql.append("select ij from ");
                sql.append(DBITEM_INVENTORY_JOBS).append(" ij, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where ij.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and ij.instanceId = :instanceId");
                if (isOrderJob != null) {
                    sql.append(" and ij.isOrderJob = :isOrderJob");
                }
                if (criticality != null && criticality.size() > 0) {
                    sql.append( " and (");
                    for (JobCriticalityFilter criticalityEntry : criticality) {
                        sql.append( "criticality='" + criticalityEntry.value().toLowerCase() + "' or ");
                    }
                    sql.append("1=0) ");
                }

            }
            Query<DBItemInventoryJob> query = getSession().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("folderName", (folderName + "/%").replaceAll("//+", "/"));
            } else {
                query.setParameter("folderName", folderName);
            }
            query.setParameter("instanceId", instanceId);
            if (isOrderJob != null) {
                query.setParameter("isOrderJob", isOrderJob);
            }
            
            List<DBItemInventoryJob> result = getSession().getResultList(query);
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

    public List<String> getJobsWithTemporaryRuntime(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        return getJobsWithTemporaryRuntime(instanceId, null);
    }

    public List<String> getJobsWithTemporaryRuntime(Long instanceId, String jobPath) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select name from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and runTimeIsTemporary = :runTimeIsTemporary");
            if (jobPath != null) {
                sql.append(" and name = :jobPath");
            }
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("runTimeIsTemporary", true);
            if (jobPath != null) {
                query.setParameter("jobPath", jobPath);
            }
            List<String> result = getSession().getResultList(query);
            if (result != null) {
                return result;
            }
            return new ArrayList<String>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getYadeJobs(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select name from ").append(DBITEM_INVENTORY_JOBS);
            sql.append(" where instanceId = :instanceId"); // isYadeJob
            sql.append(" and isYadeJob = true");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<String> result = getSession().getResultList(query);
            if (result != null) {
                return result;
            }
            return new ArrayList<String>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}