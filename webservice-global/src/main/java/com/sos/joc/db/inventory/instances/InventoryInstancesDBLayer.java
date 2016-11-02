package com.sos.joc.db.inventory.instances;

import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.scheduler.model.commands.JSCmdParamGet;
import com.sos.scheduler.model.commands.JSCmdShowState;

/** @author Uwe Risse */
public class InventoryInstancesDBLayer extends DBLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryInstancesDBLayer.class);

    public InventoryInstancesDBLayer(SOSHibernateConnection conn) {
        super(conn);
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken) throws Exception {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId order by precedence", DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            List<DBItemInventoryInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return getRunningJobSchedulerClusterMember(result, accessToken);
            }
            return null;
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryInstance getInventoryInstanceByHostPort(JobSchedulerIdentifier jobSchedulerIdentifier) throws Exception {
        try {
            String sql = String.format("from %s where hostname = :hostname and port = :port", DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("hostname", jobSchedulerIdentifier.getHost());
            query.setParameter("port", jobSchedulerIdentifier.getPort());

            List<DBItemInventoryInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                DBItemInventoryInstance dbItemInventoryInstance = result.get(0);
                if (!dbItemInventoryInstance.getSchedulerId().equals(jobSchedulerIdentifier.getSchedulerId())) {
                    String errMessage = String.format("jobschedulerId %s not assigned for %s:%s in table %s", jobSchedulerIdentifier.getSchedulerId(),
                            jobSchedulerIdentifier.getHost(), jobSchedulerIdentifier.getPort(), DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                } else {
                    return result.get(0);
                }
            }
            return null;
        }catch (DBInvalidDataException e){
            throw e;
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryInstance> getInventoryInstancesBySchedulerId(String schedulerId) throws Exception {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId", DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            List<DBItemInventoryInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result;
            }
            return null;
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryInstance> getInventoryInstances() throws Exception {
        try {
            String sql = "from " + DBITEM_INVENTORY_INSTANCES;
            Query query = getConnection().createQuery(sql);
            return query.list();
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public List<DBItemInventoryInstance> getJobSchedulerIds() throws Exception {
        try {
            String sql = String.format("from %1$s order by created desc", DBITEM_INVENTORY_INSTANCES);
            Query query = getConnection().createQuery(sql);

            return query.list();
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public DBItemInventoryInstance getInventoryInstancesByKey(Long id) throws Exception {
        try {
            String sql = String.format("from %s where id = :id", DBITEM_INVENTORY_INSTANCES);
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql);
            query.setParameter("id", id);
            List<DBItemInventoryInstance> result = query.list();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new DBInvalidDataException(SOSHibernateConnection.getException(ex));
        }
    }

    private DBItemInventoryInstance getRunningJobSchedulerClusterMember(List<DBItemInventoryInstance> schedulerInstancesDBList, String accessToken) {
        switch (schedulerInstancesDBList.get(0).getClusterType()) {
        case "passive":
            for (DBItemInventoryInstance schedulerInstancesDBItem : schedulerInstancesDBList) {
                try {
                    JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
                    jsCmdShowState.setWhat("folders no_subfolders");
                    jsCmdShowState.setPath("/does/not/exist");
                    jsCmdShowState.setSubsystems("folder");
                    JOCXmlCommand resourceImpl = new JOCXmlCommand(schedulerInstancesDBItem.getUrl());
                    resourceImpl.executePost(jsCmdShowState.toXMLString(), accessToken);
                    String state = resourceImpl.getSosxml().selectSingleNodeValue("/spooler/answer/state/@state");
                    if (!"waiting_for_activation,dead".contains(state)) {
                        return schedulerInstancesDBItem;
                    }
                } catch (Exception e) {
                    // unreachable
                }
            }
            break;
        case "active":
            for (DBItemInventoryInstance schedulerInstancesDBItem : schedulerInstancesDBList) {
                try {
                    JSCmdParamGet jsCmdParamGet = Globals.schedulerObjectFactory.createParamGet();
                    jsCmdParamGet.setName("");
                    JOCXmlCommand resourceImpl = new JOCXmlCommand(schedulerInstancesDBItem.getUrl());
                    resourceImpl.executePost(jsCmdParamGet.toXMLString(), accessToken);
                    return schedulerInstancesDBItem;
                } catch (Exception e) {
                    // unreachable
                }
            }
            break;
        default:
            break;
        }
        return schedulerInstancesDBList.get(0);
    }

}