package com.sos.joc.db.configuration;

 
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.layer.SOSHibernateDBLayer;

/** @author Uwe Risse */
public class JocConfigurationDbLayer extends SOSHibernateDBLayer {

    private static final String JocConfigurationDBItem = JocConfigurationDbItem.class.getName();

    private JocConfigurationFilter filter= null;
    private static final Logger LOGGER = Logger.getLogger(JocConfigurationDbLayer.class);
 
    public JocConfigurationDbLayer(SOSHibernateConnection connection) {
        super();
        resetFilter();
    }

 
    public JocConfigurationDbItem getJocConfigurationDbItem(final Long id) throws Exception {
        return (JocConfigurationDbItem) (connection.get(JocConfigurationDbItem.class, id));
    }

    public void resetFilter() {
        filter = new JocConfigurationFilter();
        filter.setInstanceId(null);
        filter.setName("");
        filter.setObjectSource("");
        filter.setObjectType("");
        filter.setOwner("");
        filter.setShare(null);
    }

    public int delete() throws Exception {
        String hql = "delete from " + JocConfigurationDBItem + " " + getWhere();
        Query query = null;
        int row = 0;
        query = connection.createQuery(hql);
        if (filter.getName() != null && !"".equals(filter.getName())) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getObjectSource() != null && !"".equals(filter.getObjectSource())) {
            query.setParameter("objectSource", filter.getObjectSource());
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getOwner() != null && !"".equals(filter.getOwner())) {
            query.setParameter("owner", filter.getOwner());
        }
        if (filter.isShared() != null) {
            query.setParameter("shared", filter.isShared());
        }
        
        row = query.executeUpdate();
        return row;
    }

   
    private String getWhere() {
        String where = "";
        String and = "";
        if (filter.getName() != null && !"".equals(filter.getName())) {
            where += and + " name = :name";
            and = " and ";
        }
        if (filter.getObjectSource() != null && !"".equals(filter.getObjectSource())) {
            where += and + " objectSource < :objectSource ";
            and = " and ";
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            where += and + " objectType = :objectType";
            and = " and ";
        }
        if (filter.getOwner() != null && !"".equals(filter.getOwner())) {
            where += and + " owner = :owner";
            and = " and ";
        }
        if (filter.isShared() != null) {
            where += and + " shared = :shared";
            and = " and ";
        }

        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    @SuppressWarnings("unchecked")
    public List<JocConfigurationDbItem> getJocConfigurationList(final int limit) throws Exception {

        Query<JocConfigurationDbItem> query = null;
        List<JocConfigurationDbItem> configurationsList = null;
      //  query = connection.createQuery("from " + JocConfigurationDBItem + " " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());
        if (filter.getName() != null && !"".equals(filter.getName())) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getObjectSource() != null && !"".equals(filter.getObjectSource())) {
            query.setParameter("objectSource", filter.getObjectSource());
        }
        if (filter.getOwner() != null && !"".equals(filter.getOwner())) {
            query.setParameter("owner", filter.getOwner());
        }
        if (filter.isShared() != null) {
            query.setParameter("shared", filter.isShared());
        }

        if (limit > 0) {
            query.setMaxResults(limit);
        }
        configurationsList = query.list();
        return configurationsList;
    }
    
    public void saveConfiguration(JocConfigurationDbItem jocConfigurationDbItem) throws Exception{
        try {
            connection.update(jocConfigurationDbItem);
        }catch (Exception e){
            connection.save(jocConfigurationDbItem);
        }
    }

     
    public JocConfigurationFilter getFilter() {
        return filter;
    }

  
    public void setFilter(final JocConfigurationFilter filter) {
        this.filter = filter;
    }

}