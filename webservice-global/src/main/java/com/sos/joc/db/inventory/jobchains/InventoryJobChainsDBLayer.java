package com.sos.joc.db.inventory.jobchains;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryJobChainNode;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

/** @author SP */
public class InventoryJobChainsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryJobChainsDBLayer.class);
    private static final String NESTED_JOB_CHAIN = NestedJobChain.class.getName();

    public InventoryJobChainsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryJobChain getJobChainByPath(String jobChainPath, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            sql.append(" where name = :name");
            sql.append(" and instanceId = :instanceId");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("name", jobChainPath);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
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
    public DBItemInventoryJobChain getJobChainByName(String jobChainName, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            sql.append(" where baseName = :name");
            sql.append(" and instanceId = :instanceId");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("name", jobChainName);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }        
    }
    
    public Date getJobChainConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder("select ifile.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" ifile, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc ");
            sql.append(" where ifile.id = ijc.fileId");
            sql.append(" and ijc.id = :id");
            LOGGER.debug(sql.toString());
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            Object result = query.uniqueResult();
            if (result != null) {
                return (Date)result;
            }
            return null;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryJobChainNode> getJobChainNodesByJobChainId(Long id, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAIN_NODES);
            sql.append(" where jobChainId = :id");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChainNode> result = query.list();
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
    public List<DBItemInventoryJobChain> getJobChainsByFolder(String folderPath, boolean recursive, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
                sql.append(" where name like :name");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select ijc from ");
                sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where ijc.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :name");
                sql.append(" and ijc.instanceId = :instanceId");
            }
            Query query = getSession().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("name", folderPath + "%");
            } else {
                query.setParameter("name", folderPath);
            }
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = query.list();
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
    public List<DBItemInventoryJobChain> getJobChains(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_JOB_CHAINS);
            sql.append(" where instanceId = :instanceId");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryJobChain> result = query.list();
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
    
    public boolean isEndNode(String jobChain, String node, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc");
            sql.append(" where ijcn.jobChainId = ijc.id");
            sql.append(" and ijcn.state = :nodeName");
            sql.append(" and ijcn.nodeType = 5");
            sql.append(" and ijcn.instanceId = :instanceId");
            sql.append(" and ijc.name = :jobChain");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("nodeName", node);
            query.setParameter("jobChain", jobChain);
            query.setParameter("instanceId", instanceId);
            Long result = (Long) query.uniqueResult();
            if (result == null) {
                return false;
            }
            return result > 0;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }        
    }
    
    public boolean isErrorNode(String jobChain, String node, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc");
            sql.append(" where ijcn.jobChainId = ijc.id");
            sql.append(" and ijcn.errorState = :nodeName");
            sql.append(" and ijcn.instanceId = :instanceId");
            sql.append(" and ijc.name = :jobChain");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("nodeName", node);
            query.setParameter("jobChain", jobChain);
            query.setParameter("instanceId", instanceId);
            Long result = (Long) query.uniqueResult();
            if (result == null) {
                return false;
            }
            return result > 0;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
    
    public List<String> getOuterJobChains(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc.name from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn");
            sql.append(" where ijc.id = ijcn.jobChainId and ijcn.nodeType = 2 and ijc.instanceId = :instanceId");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<String> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return new ArrayList<String>();
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
    
    public Map<String, Set<String>> getMapOfOuterJobChains(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            Map<String, Set<String>> nestedJobChains = new HashMap<String, Set<String>>();
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(NESTED_JOB_CHAIN);
            sql.append("(ijc.name, ijcn.nestedJobChainName) from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn");
            sql.append(" where ijc.id = ijcn.jobChainId and ijcn.nodeType = 2 and ijc.instanceId = :instanceId");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<NestedJobChain> result = query.list();
            if (result != null) {
                for (NestedJobChain item : result) {
                   if (nestedJobChains.containsKey(item.getInnerJobChain())) {
                       nestedJobChains.get(item.getInnerJobChain()).add(item.getOuterJobChain());
                   } else {
                       Set<String> outerJobChains = new HashSet<String>();
                       outerJobChains.add(item.getOuterJobChain());
                       nestedJobChains.put(item.getInnerJobChain(), outerJobChains);
                   }
                }
            }
            return nestedJobChains;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
    
    public List<String> getInnerJobChains(String outerJobCain, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijcn.nestedJobChainName from ").append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc");
            sql.append(" where ijc.id = ijcn.jobChainId and ijcn.nodeType = 2");
            sql.append(" and ijc.instanceId = :instanceId");
            sql.append(" and ijc.name = :jobChain");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("jobChain", outerJobCain);
            List<String> result = query.list();
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
    
    public List<String> getJobChainsOfJob(String job, Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ijc.name from ").append(DBITEM_INVENTORY_JOB_CHAINS).append(" ijc, ");
            sql.append(DBITEM_INVENTORY_JOB_CHAIN_NODES).append(" ijcn");
            sql.append(" where ijc.id = ijcn.jobChainId and ijcn.nodeType = 1");
            sql.append(" and ijc.instanceId = :instanceId");
            sql.append(" and ijcn.jobName = :job");
            Query query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("job", job);
            List<String> result = query.list();
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