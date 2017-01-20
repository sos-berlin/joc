package com.sos.joc.db.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.layer.SOSHibernateDBLayer;

/** @author Uwe Risse */
public class JocConfigurationDbLayer extends SOSHibernateDBLayer {

    private static final String JocConfigurationDBItem = JocConfigurationDbItem.class.getName();

    private JocConfigurationFilter filter= null;
    private static final Logger LOGGER = Logger.getLogger(JocConfigurationDbLayer.class);

    public JocConfigurationDbLayer(final String configurationFilename) {
        super();
        this.setConfigurationFileName(configurationFilename);
        this.initConnection(this.getConfigurationFileName());
        resetFilter();
    }

    public JocConfigurationDbLayer(SOSHibernateConnection connection) {
        super();
        this.initConnection(connection);
        resetFilter();
    }

    public JocConfigurationDbLayer(final File configurationFile) {
        super();
        try {
            this.setConfigurationFileName(configurationFile.getCanonicalPath());
        } catch (IOException e) {
            this.setConfigurationFileName("");
            LOGGER.error(e.getMessage(), e);
        }
        this.initConnection(this.getConfigurationFileName());
        resetFilter();
    }

    public JocConfigurationDbItem getJocConfigurationDbItem(final Long id) throws Exception {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        return (JocConfigurationDbItem) ((Session) connection.getCurrentSession()).get(JocConfigurationDbItem.class, id);
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
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
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
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<JocConfigurationDbItem> daysScheduleList = null;
        query = connection.createQuery("from " + JocConfigurationDBItem + " " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());
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
        daysScheduleList = query.list();
        return daysScheduleList;
    }

    

    public JocConfigurationFilter getFilter() {
        return filter;
    }

  
    public void setFilter(final JocConfigurationFilter filter) {
        this.filter = filter;
    }

}