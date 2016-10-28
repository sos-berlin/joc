package com.sos.joc.db.inventory.agents;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentClusterMember;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.jitl.reporting.db.DBLayer;


public class InventoryAgentsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAgentsDBLayer.class);

    public InventoryAgentsDBLayer(SOSHibernateConnection connection) {
        super(connection);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryAgentInstance getInventoryAgentInstances(String url, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where url = :url");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("url", url);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryAgentInstance> getInventoryAgentInstances(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryAgentClusterMember> getInventoryAgentClusterMembersByAgentId(Long agentInstanceId, Long instanceId)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS);
            sql.append(" where agentInstanceId = :agentInstanceId");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentInstanceId", agentInstanceId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentClusterMember> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryAgentClusterMember> getInventoryAgentClusterMembersByClusterId(Long agentClusterId, Long instanceId)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS);
            sql.append(" where agentClusterId = :agentClusterId");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentClusterId", agentClusterId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentClusterMember> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryAgentCluster getInventoryAgentClusterById(Long id, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where id = :id");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getProcessClassesFromAgentCluster(Long agentId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ipc.name from ");
            sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm ");
            sql.append("where ipc.id = iac.processClassId");
            sql.append(" and iac.id = iacm.agentClusterId");
            sql.append(" and iacm.agentInstanceId = :agentId");
            sql.append(" and ipc.instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentId", agentId);
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
    
    @SuppressWarnings("unchecked")
    public List<DBItemInventoryAgentInstance> getInventoryAgentInstancesByClusterId(Long agentClusterId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select iai from ");
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai, ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm ");
            sql.append("where iai.id = iacm.agentInstanceId");
            sql.append(" and iacm.agentClusterId = :agentClusterId");
            sql.append(" and iai.instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentClusterId", agentClusterId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryAgentCluster> getAgentClusters(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryAgentCluster getAgentClusterByProcessClass(String processClassName, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select iac from ").append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ");
            sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ");
            sql.append("where iac.processClassId = ipc.id");
            sql.append(" and ipc.name = :processClassName");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("processClassName", processClassName);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryProcessClass getInventoryClusterProcessClass(String processClassName, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where name = :name");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", processClassName);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryProcessClass> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryAgentCluster getInventoryClusterByProcessClassId(Long processClassId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where processClassId = :processClassId");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("processClassId", processClassId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    public String getPathForProcessClass(Long fileId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select fileDirectory from ")
                .append(DBITEM_INVENTORY_FILES)
                .append(" where id = :fileId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("fileId", fileId);
            String result = (String)query.uniqueResult();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    @SuppressWarnings("unchecked")
    public DBItemInventoryProcessClass getInventoryProcessClassById(Long processClassId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where id = :id");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", processClassId);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryProcessClass> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryProcessClass> getInventoryProcessClasses(Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ipc from ").append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac ");
            sql.append(" where ipc.id = iac.processClassId");
            sql.append(" and ipc.instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryProcessClass> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    public String getInventoryProcessClassFileName(Long processClassId, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ifile.fileName from ").append(DBITEM_INVENTORY_FILES).append(" invf, ");
            sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ");
            sql.append(" where ipc.fileId = invf.id");
            sql.append(" and ipc.id = :id");
            sql.append(" and ipc.instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", processClassId);
            query.setParameter("instanceId", instanceId);
            Object result = query.uniqueResult();
            if (result != null) {
                return (String)result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryProcessClass> getInventoryProcessClassByState(Integer state, Long instanceId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ipc from ").append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, "); 
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm, "); 
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai "); 
            sql.append("where ipc.id = iac.processClassId ");
            sql.append("and iac.id = iacm.agentClusterId ");
            sql.append("and iacm.agentInstanceId = iai.id ");
            sql.append("and ipc.instanceId = :instanceId ");
            if(state != null) {
                sql.append("and iai.state = :state ");
            }
            sql.append("group by ipc.id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if(state != null) {
                query.setParameter("state", state);
            }
            List<DBItemInventoryProcessClass> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

}