package com.sos.joc.db.inventory.instances;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.inventory.db.InventoryCleanup;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.UnknownJobSchedulerMasterException;

/** @author Uwe Risse */
public class InventoryInstancesDBLayer extends DBLayer {

    public InventoryInstancesDBLayer(SOSHibernateSession conn) {
        super(conn);
    }
    
    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken) throws DBInvalidDataException,
    DBMissingDataException, DBConnectionRefusedException {
        return getInventoryInstanceBySchedulerId(schedulerId, accessToken, false);
    }

    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken, boolean verbose) throws DBInvalidDataException,
            DBMissingDataException, DBConnectionRefusedException {
        return getInventoryInstanceBySchedulerId(schedulerId, accessToken, verbose, null);
    }
    
    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken, DBItemInventoryInstance curInstance)
            throws DBInvalidDataException, DBMissingDataException, DBConnectionRefusedException {
        return getInventoryInstanceBySchedulerId(schedulerId, accessToken, false, curInstance);
    }

    public DBItemInventoryInstance getInventoryInstanceBySchedulerId(String schedulerId, String accessToken, boolean verbose, DBItemInventoryInstance curInstance)
            throws DBInvalidDataException, DBMissingDataException, DBConnectionRefusedException {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId order by precedence", DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            List<DBItemInventoryInstance> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return setMappedUrl(getRunningJobSchedulerClusterMember(result, accessToken, curInstance), verbose);
            } else {
                String errMessage = String.format("jobschedulerId %1$s not found in table %2$s", schedulerId, DBLayer.TABLE_INVENTORY_INSTANCES);
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

    public DBItemInventoryInstance getInventoryInstanceByHostPort(String host, Integer port, String schedulerId) throws DBInvalidDataException,
            DBConnectionRefusedException, UnknownJobSchedulerMasterException {
        try {
            String sql = String.format("from %s where schedulerId = :schedulerId and hostname = :hostname and port = :port",
                    DBITEM_INVENTORY_INSTANCES);
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            query.setParameter("hostname", host);
            query.setParameter("port", port);
            query.setParameter("schedulerId", schedulerId);

            List<DBItemInventoryInstance> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return setMappedUrl(result.get(0));
            } else {
                String errMessage = String.format("JobScheduler with id:%1$s, host:%2$s and port:%3$s couldn't be found in table %4$s", schedulerId,
                        host, port, DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new UnknownJobSchedulerMasterException(errMessage);
            }
        } catch (JocException e) {
            throw e;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemInventoryInstance getInventoryInstanceByClusterMemberId(String clusterMemberId) throws DBInvalidDataException,
            DBConnectionRefusedException, UnknownJobSchedulerMasterException {
        try {
            Matcher m = Pattern.compile("(.+)/([^/]+):(\\d+)").matcher(clusterMemberId);
            if (m.find()) {
                String sql = String.format("from %s where schedulerId = :schedulerId and hostname = :hostname and url like :port",
                        DBITEM_INVENTORY_INSTANCES);
                Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
                query.setParameter("hostname", m.group(2));
                query.setParameter("port", "%:" + m.group(3));
                query.setParameter("schedulerId", m.group(1));
                DBItemInventoryInstance result = getSession().getSingleResult(query);
                if (result != null) {
                    return setMappedUrl(result);
                } 
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryInstance> getInventoryInstancesBySchedulerId(String schedulerId) throws DBInvalidDataException, DBConnectionRefusedException {
        return getInventoryInstancesBySchedulerId(schedulerId, false);
    }

    public List<DBItemInventoryInstance> getInventoryInstancesBySchedulerId(String schedulerId, boolean ordered) throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            if (schedulerId == null) {
                schedulerId = "";  
            }
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_INSTANCES);
            if (!schedulerId.isEmpty()) {
                sql.append(" where schedulerId = :schedulerId").append(" order by precedence");
            } else {
                sql.append(" order by schedulerId, precedence");
            }
            Query<DBItemInventoryInstance> query = getSession().createQuery(sql.toString());
            if (!schedulerId.isEmpty()) {
                query.setParameter("schedulerId", schedulerId);
            }
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

    public DBItemInventoryInstance getInventoryInstanceByKey(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            return setMappedUrl(this.getSession().get(DBItemInventoryInstance.class, id));
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
                    mods += mod.getTime() / 1000;
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

    public void cleanUp(DBItemInventoryInstance schedulerInstanceFromDb) throws DBInvalidDataException {
        StatelessSession session = null;
        Transaction tr = null;
        try {
            session = (StatelessSession) getSession().getCurrentSession();
            tr = session.beginTransaction();
            new InventoryCleanup().cleanup(getSession(), schedulerInstanceFromDb);
            tr.commit();
        } catch (SOSHibernateException ex) {
            try {
                if (tr != null) {
                    tr.rollback();
                }
            } catch (Exception e) {
                //
            }
            throw new DBInvalidDataException(ex);
        }
    }

    private DBItemInventoryInstance getRunningJobSchedulerClusterMember(List<DBItemInventoryInstance> schedulerInstancesDBList, String accessToken, DBItemInventoryInstance curInstance) {
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
            if (curInstance != null) {
                schedulerInstancesDBList.add(0, curInstance);
            }
            DBItemInventoryInstance schedulerInstancesDBItemOfPausedScheduler = null;
            for (DBItemInventoryInstance schedulerInstancesDBItem : schedulerInstancesDBList) {
                try {
                    String xml = "<show_state subsystems=\"folder\" what=\"folders no_subfolders\" path=\"/does/not/exist\" />";
                    JOCXmlCommand resourceImpl = new JOCXmlCommand(schedulerInstancesDBItem);
                    resourceImpl.executePost(xml, accessToken);
                    String state = resourceImpl.getSosxml().selectSingleNodeValue("/spooler/answer/state/@state");
                    if ("running".equals(state)) {
                        schedulerInstancesDBItemOfPausedScheduler = null;
                        return schedulerInstancesDBItem;
                    } 
                    if (schedulerInstancesDBItemOfPausedScheduler == null && "paused".equals(state)) {
                        schedulerInstancesDBItemOfPausedScheduler = schedulerInstancesDBItem;
                    }
                } catch (Exception e) {
                    // unreachable
                }
            }
            if (schedulerInstancesDBItemOfPausedScheduler != null) {
                return schedulerInstancesDBItemOfPausedScheduler;
            }
            break;
        default:
            break;
        }
        return schedulerInstancesDBList.get(0);
    }
    
    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        return setMappedUrl(instance, false);
    }
    
    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance, boolean verbose) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance, verbose);
        }
        return instance;
    }

}