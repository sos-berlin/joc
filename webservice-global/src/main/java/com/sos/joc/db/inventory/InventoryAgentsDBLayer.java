package com.sos.joc.db.inventory;

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
    public DBItemInventoryAgentInstance getInventoryAgentInstances(String url) throws Exception {
        try {
            String sql = String.format("from %s where url = :url", DBITEM_INVENTORY_AGENT_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("url", url);
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
    public List<DBItemInventoryAgentInstance> getInventoryAgentInstances() throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
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
    public List<DBItemInventoryAgentClusterMember> getInventoryAgentClusterMembersByAgentId(Long agentInstanceId) throws Exception {
        try {
            String sql = String.format("from %s where agentInstanceId = :agentInstanceId", DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentInstanceId", agentInstanceId);
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
    public List<DBItemInventoryAgentClusterMember> getInventoryAgentClusterMembersByClusterId(Long agentClusterId) throws Exception {
        try {
            String sql = String.format("from %s where agentClusterId = :agentClusterId", DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentClusterId", agentClusterId);
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
    public DBItemInventoryAgentCluster getInventoryAgentClusterById(Long id) throws Exception {
        try {
            String sql = String.format("from %s where id = :id", DBITEM_INVENTORY_AGENT_CLUSTER);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", id);
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    public List<String> getProcessClassesFromAgentCluster(Long agentId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ipc.name from ")
                .append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ")
                .append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ")
                .append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm ")
                .append("where ipc.id = iac.processClassId and iac.id = iacm.agentClusterId and iacm.agentInstanceId = :agentId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentId", agentId);
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
    public List<DBItemInventoryAgentInstance> getInventoryAgentInstancesByClusterId(Long agentClusterId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ")
                .append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai, ")
                .append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm ")
                .append("where iai.id = iacm.processClassId and iacm.agentClusterId = :agentClusterId");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("agentClusterId", agentClusterId);
            List<DBItemInventoryAgentInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

    public List<DBItemInventoryAgentCluster> getAgentClusters() throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            List<DBItemInventoryAgentCluster> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
    public DBItemInventoryAgentCluster getAgentClusterByProcessClass(String processClassName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ")
            .append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc, ")
            .append("where iac.processClassId = ipc.id and ipc.name = :processClassName");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("processClassName", processClassName);
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
    public DBItemInventoryProcessClass getInventoryClusterProcessClass(String processClassName) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where name = :name");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", processClassName);
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
    public DBItemInventoryAgentCluster getInventoryClusterByProcessClassId(Long processClassId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where processClassId = :id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", processClassId);
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
                .append("where id = fileId");
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
    public DBItemInventoryProcessClass getInventoryProcessClassById(Long processClassId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_PROCESS_CLASSES);
            sql.append(" where id = :id");
            LOGGER.debug(sql.toString());
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("id", processClassId);
            List<DBItemInventoryProcessClass> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }

}