package com.sos.joc.db.inventory.jobs;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class InventoryJobsDBLayer extends DBLayer {

    private static final Logger LOGGER = Logger.getLogger(InventoryJobsDBLayer.class);
    private String jobSchedulerId;

    public InventoryJobsDBLayer(SOSHibernateConnection conn, String jobSchedulerId) {
        super(conn);
        this.jobSchedulerId = jobSchedulerId;
    }
    
     public DBItemInventoryJob getInventoryJobByName(String name) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select job from ");
            sql.append(DBITEM_INVENTORY_JOBS +" as job, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where job.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "' and");
            sql.append(" (name)  = :name");
            LOGGER.debug(sql);
            Query query = getConnection().createQuery(sql.toString());
            query.setParameter("name", name);

            List<DBItemInventoryJob> result = query.list();
            if (!result.isEmpty()) {
                return result.get(0);
            }
            return null;
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
   
    public List<DBItemInventoryJob> getInventoryJobs() throws Exception {
        try {
            StringBuilder sql = new StringBuilder("select job from ");
            sql.append(DBITEM_INVENTORY_JOBS +" as job, " + DBITEM_INVENTORY_INSTANCES + " as instance");
            sql.append(" where job.instanceId = instance.id and instance.schedulerId = '" + this.jobSchedulerId + "'");
            Query query = getConnection().createQuery(sql.toString());

            return query.list();
        } catch (Exception ex) {
            throw new Exception(SOSHibernateConnection.getException(ex));
        }
    }
    
 
   

  

}