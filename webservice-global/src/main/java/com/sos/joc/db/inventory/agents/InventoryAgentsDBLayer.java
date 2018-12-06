package com.sos.joc.db.inventory.agents;

import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;

public class InventoryAgentsDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAgentsDBLayer.class);
    private static final String AGENT_CLUSTER_MEMBER = AgentClusterMember.class.getName();
    private static final String AGENT_CLUSTER_P = AgentClusterPermanent.class.getName();

    public InventoryAgentsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemInventoryAgentInstance getInventoryAgentInstance(String url, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where url = :url");
            sql.append(" and instanceId = :instanceId");
            Query<DBItemInventoryAgentInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("url", url);
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public void updateInventoryAgentInstance(Long instanceId, AgentOfCluster agent) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            if (agent.getState().get_text() != JobSchedulerStateText.UNKNOWN_AGENT) {
                DBItemInventoryAgentInstance item = getInventoryAgentInstance(agent.getUrl(), instanceId);
                if (item != null) {
                    if (agent.getState().get_text() == JobSchedulerStateText.UNREACHABLE) {
                        item.setStartedAt(null);
                        item.setState(1);
                    } else {
                        item.setHostname(agent.getHost());
                        item.setStartedAt(agent.getStartedAt());
                        item.setState(0);
                        item.setVersion(agent.getVersion());
                    }
                    item.setModified(agent.getSurveyDate());
                    getSession().update(item);
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("Problem during update INVENTORY_AGENT_INSTANCES", ex);
        }
    }

    public List<DBItemInventoryAgentInstance> getInventoryAgentInstances(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_INSTANCES);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryAgentInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentInstance> result = getSession().getResultList(query);
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
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("agentId", agentId);
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

    public List<DBItemInventoryAgentCluster> getAgentClusters(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_AGENT_CLUSTER);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryAgentCluster> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            List<DBItemInventoryAgentCluster> result = getSession().getResultList(query);
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
            Query<AgentClusterPermanent> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentClusters != null && !agentClusters.isEmpty()) {
                if (agentClusters.size() == 1) {
                    query.setParameter("agentCluster", agentClusters.iterator().next());
                } else {
                    query.setParameterList("agentCluster", agentClusters);
                }
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<AgentClusterMember> getInventoryAgentClusterMembersByUrls(Long instanceId, Set<String> agentUrls)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_CLUSTER_MEMBER);
            sql.append(" (iai.url, iai.version, ios.hostname, ios.name, ios.architecture, ios.distribution) from ");
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai, ");
            sql.append(DBITEM_INVENTORY_OPERATING_SYSTEMS).append(" ios ");
            sql.append("where iai.osId = ios.id ");
            sql.append("and iai.instanceId = :instanceId");
            if (agentUrls != null && !agentUrls.isEmpty()) {
                if (agentUrls.size() == 1) {
                    sql.append(" and iai.url = :agentUrl");
                } else {
                    sql.append(" and iai.url in (:agentUrl)");
                }
            }
            Query<AgentClusterMember> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentUrls != null && !agentUrls.isEmpty()) {
                if (agentUrls.size() == 1) {
                    query.setParameter("agentUrl", agentUrls.iterator().next());
                } else {
                    query.setParameterList("agentUrl", agentUrls);
                }
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<AgentClusterMember> getInventoryAgentClusterMembersById(Long instanceId, Long agentClusterId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try { //TODO left outer join otherwise missing rows if iai.osId == 0
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(AGENT_CLUSTER_MEMBER);
            sql.append(" (iacm.agentClusterId, iai.url, iai.modified, iai.version, iai.state, iai.startedAt, ios.hostname, ");
            sql.append("ios.name, ios.architecture, ios.distribution) from ");
            sql.append(DBITEM_INVENTORY_AGENT_CLUSTERMEMBERS).append(" iacm, ");
            sql.append(DBITEM_INVENTORY_AGENT_INSTANCES).append(" iai left outer join ");
            sql.append(DBITEM_INVENTORY_OPERATING_SYSTEMS).append(" ios ");
            sql.append("on iai.osId = ios.id ");
            sql.append("where iacm.agentInstanceId = iai.id ");
            sql.append("and iacm.instanceId = :instanceId");
            if (agentClusterId != null) {
                sql.append(" and iacm.agentClusterId = :agentClusterId");
            }
            sql.append(" order by iacm.ordering");
            Query<AgentClusterMember> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (agentClusterId != null) {
                query.setParameter("agentClusterId", agentClusterId);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
}