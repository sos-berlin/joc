package com.sos.joc.db.inventory.agents;

import java.util.List;
import java.util.Set;

import org.hibernate.SessionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class InventoryAgentsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAgentsDBLayer.class);
    private static final String AGENT_CLUSTER_MEMBER = AgentClusterMember.class.getName();
    private static final String AGENT_CLUSTER_P = AgentClusterPermanent.class.getName();

    public InventoryAgentsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemInventoryAgentInstance getInventoryAgentInstances(String url, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where url = :url");
            sql.append(" and instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query<DBItemInventoryAgentInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("url", url);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = query.getResultList();
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

    public List<DBItemInventoryAgentInstance> getInventoryAgentInstances(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query<DBItemInventoryAgentInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = query.getResultList();
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

    public List<String> getProcessClassesFromAgentCluster(Long agentId, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
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
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("agentId", agentId);
            query.setParameter("instanceId", instanceId);
            List<String> result = query.getResultList();
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

    public List<DBItemInventoryAgentCluster> getAgentClusters(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where instanceId = :instanceId");
            LOGGER.debug(sql.toString());
            Query<DBItemInventoryAgentCluster> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = query.getResultList();
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

    public List<AgentClusterPermanent> getInventoryAgentClusters(Long instanceId, Set<String> agentClusters)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_CLUSTER_P);
            sql.append("(iac.id, iac.schedulingType, iac.numberOfAgents, iac.modified, ipc.name, ipc.maxProcesses) from ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTER).append(" iac, ");
            sql.append(DBITEM_INVENTORY_PROCESS_CLASSES).append(" ipc ");
            sql.append("where iac.processClassId = ipc.id ");
            sql.append("and iac.instanceId = :instanceId");
            if (agentClusters != null && !agentClusters.isEmpty()) {
                if (agentClusters.size() == 1) {
                    sql.append(" and ipc.name = :agentCluster");
                } else {
                    sql.append(" and ipc.name in (:agentCluster)");
                }
            }
            LOGGER.debug(sql.toString());
            Query<AgentClusterPermanent> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentClusters != null && !agentClusters.isEmpty()) {
                if (agentClusters.size() == 1) {
                    query.setParameter("agentCluster", agentClusters.iterator().next());
                } else {
                    query.setParameterList("agentCluster", agentClusters);
                }
            }
            List<AgentClusterPermanent> result = query.getResultList();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public List<AgentClusterMember> getInventoryAgentClusterMembers(Long instanceId, Set<Long> agentClusterIds)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_CLUSTER_MEMBER);
            sql.append(" (iacm.agentClusterId, iacm.url, iacm.ordering, iacm.modified, iai.version, iai.state, iai.startedAt, ");
            sql.append("ios.hostname, ios.name, ios.architecture, ios.distribution) from ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm, ");
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai, ");
            sql.append(DBITEM_INVENTORY_OPERATING_SYSTEMS).append(" ios ");
            sql.append("where iacm.agentInstanceId = iai.id ");
            sql.append("and iai.osId = ios.id ");
            sql.append("and iacm.instanceId = :instanceId");
            if (agentClusterIds != null && !agentClusterIds.isEmpty()) {
                if (agentClusterIds.size() == 1) {
                    sql.append(" and iacm.agentClusterId = :agentClusterId");
                } else {
                    sql.append(" and iacm.agentClusterId in (:agentClusterId)");
                }
            }
            LOGGER.debug(sql.toString());
            Query<AgentClusterMember> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentClusterIds != null && !agentClusterIds.isEmpty()) {
                if (agentClusterIds.size() == 1) {
                    query.setParameter("agentClusterId", agentClusterIds.iterator().next());
                } else {
                    query.setParameterList("agentClusterId", agentClusterIds);
                }
            }
            List<AgentClusterMember> result = query.getResultList();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }

    public List<AgentClusterMember> getInventoryAgentClusterMembersById(Long instanceId, Long agentClusterId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_CLUSTER_MEMBER);
            sql.append(" (iacm.agentClusterId, iacm.url, iacm.modified, iai.version, iai.state, iai.startedAt, ios.hostname, ");
            sql.append("ios.name, ios.architecture, ios.distribution) from ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm, ");
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai, ");
            sql.append(DBITEM_INVENTORY_OPERATING_SYSTEMS).append(" ios ");
            sql.append("where iacm.agentInstanceId = iai.id ");
            sql.append("and iai.osId = ios.id ");
            sql.append("and iacm.instanceId = :instanceId");
            if (agentClusterId != null) {
                sql.append(" and iacm.agentClusterId = :agentClusterId");
            }
            sql.append(" order by iacm.ordering");
            LOGGER.debug(sql.toString());
            Query<AgentClusterMember> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentClusterId != null) {
                query.setParameter("agentClusterId", agentClusterId);
            }
            List<AgentClusterMember> result = query.getResultList();
            return result;
        } catch (SessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateSession.getException(ex));
        }
    }
}