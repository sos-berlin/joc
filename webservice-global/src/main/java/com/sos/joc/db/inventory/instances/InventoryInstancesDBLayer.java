package com.sos.joc.db.inventory.instances;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;

/** @author Uwe Risse */
public class InventoryInstancesDBLayer extends DBLayer {

    public InventoryInstancesDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken)
            throws DBInvalidDataException, DBMissingDataException, DBConnectionRefusedException {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId order by precedence", DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            List<DBItemInventoryInstance> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return getRunningJobSchedulerClusterMember(result, accessToken);
            } else {
                String errMessage = String.format("jobschedulerId %1$s not found in table %2$s", schedulerId,
                        DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBMissingDataException(errMessage);
            }
        } catch (DBMissingDataException ex) {
            throw ex;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryInstance getInventoryInstanceByHostPort(String host, Integer port, String schedulerId)
            throws DBMissingDataException, DBInvalidDataException, DBConnectionRefusedException {
        try {
            String sql = String.format("from %s where hostname = :hostname and port = :port", DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("hostname", host);
            query.setParameter("port", port);

            List<DBItemInventoryInstance> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                DBItemInventoryInstance dbItemInventoryInstance = result.get(0);
                if (!dbItemInventoryInstance.getSchedulerId().equals(schedulerId)) {
                    String errMessage = String.format("jobschedulerId %s not assigned for %s:%s in table %s", schedulerId, host,
                            port, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                } else {
                    return result.get(0);
                }
            } else {
                String errMessage =
                        String.format("jobscheduler with id:%1$s, host:%2$s and port:%3$s couldn't be found in table %4$s",
                                schedulerId, host, port, DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBMissingDataException(errMessage);
            }
        } catch (DBMissingDataException e){
            throw e;
        } catch (DBInvalidDataException e){
            throw e;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryInstance> getInventoryInstancesBySchedulerId(String schedulerId)
            throws DBInvalidDataException, DBConnectionRefusedException   {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId", DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            List<DBItemInventoryInstance> result = getSession().getResultList(query);
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

    public List<DBItemInventoryInstance> getInventoryInstances() throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            return getSession().getResultList("from " + DBITEM_INVENTORY_INSTANCES);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryInstance> getJobSchedulerIds() throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            return getSession().getResultList(String.format("from %1$s order by created desc", DBITEM_INVENTORY_INSTANCES));
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryInstance getInventoryInstancesByKey(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            String sql = String.format("from %s where id = :id", DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql);
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public long getInventoryMods() throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            String sql = String.format("select modified from %s", DBITEM_INVENTORY_INSTANCES);
            Query<Date> query = getSession().createQuery(sql);
            List<Date> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                long mods = 0L;
                for (Date mod : result) {
                    mods += mod.getTime()/1000;
                }
                return mods;
            }
            return 0L;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    private DBItemInventoryInstance getRunningJobSchedulerClusterMember(List<DBItemInventoryInstance> schedulerInstancesDBList,
            String accessToken) {
        switch (schedulerInstancesDBList.get(0).getClusterType()) {
        case "passive":
            DBItemInventoryInstance schedulerInstancesDBItemOfWaitingScheduler = null;
            for (DBItemInventoryInstance schedulerInstancesDBItem : schedulerInstancesDBList) {
                try {
                    String xml = "<show_state subsystems=\"folder\" what=\"folders no_subfolders\" path=\"/does/not/exist\" />";
                    JOCXmlCommand resourceImpl = new JOCXmlCommand(schedulerInstancesDBItem);
                    resourceImpl.executePost(xml, accessToken);
                    String state = resourceImpl.getSosxml().selectSingleNodeValue("/spooler/answer/state/@state");
                    if ("running,paused".contains(state)) {
                        schedulerInstancesDBItemOfWaitingScheduler = null;
                        return schedulerInstancesDBItem;
                    }
                    if (schedulerInstancesDBItemOfWaitingScheduler == null && "waiting_for_activation".equals(state)) {
                        schedulerInstancesDBItemOfWaitingScheduler = schedulerInstancesDBItem;
                    }
                } catch (Exception e) {
                    // unreachable
                }
            }
//            if (schedulerInstancesDBItemOfWaitingScheduler != null) {
//                return schedulerInstancesDBItemOfWaitingScheduler;
//            }
            break;
        case "active":
            for (DBItemInventoryInstance schedulerInstancesDBItem : schedulerInstancesDBList) {
                try {
                    String xml = "<param.get name=\"\" />";
                    JOCXmlCommand resourceImpl = new JOCXmlCommand(schedulerInstancesDBItem);
                    resourceImpl.executePost(xml, accessToken);
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