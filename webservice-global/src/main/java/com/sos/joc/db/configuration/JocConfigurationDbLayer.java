package com.sos.joc.db.configuration;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.classes.SOSHibernateFactory.Dbms;
import com.sos.hibernate.layer.SOSHibernateDBLayer;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.jitl.reporting.db.DBItemReportTrigger;

/** @author Uwe Risse */
public class JocConfigurationDbLayer extends SOSHibernateDBLayer {

    private static final String JocConfigurationDBItem = JocConfigurationDbItem.class.getName();

    private JocConfigurationFilter filter = null;
    private static final Logger LOGGER = Logger.getLogger(JocConfigurationDbLayer.class);

    public JocConfigurationDbLayer(SOSHibernateConnection connection) throws Exception {
        super();
        this.connection = connection;
        resetFilter();
    }

    public JocConfigurationDbItem getJocConfigurationDbItem(final Long id) throws Exception {
        return (JocConfigurationDbItem) (connection.get(JocConfigurationDbItem.class, id));
    }

    public void resetFilter() {
        filter = new JocConfigurationFilter();
        filter.setInstanceId(null);
        filter.setName("");
        filter.setConfigurationType("");
        filter.setObjectType("");
        filter.setAccount("");
        filter.setShared(null);
    }

    public int delete() throws Exception {
        String hql = "delete from " + JocConfigurationDBItem + " " + getWhere();
        Query query = null;
        int row = 0;
        query = connection.createQuery(hql);
        if (filter.getName() != null && !"".equals(filter.getName())) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getConfigurationType() != null && !"".equals(filter.getConfigurationType())) {
            query.setParameter("configurationType", filter.getConfigurationType());
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            query.setParameter("account", filter.getAccount());
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
        if (filter.getInstanceId() != null) {
            where += and + " instanceId = :instanceId ";
            and = " and ";
        }
        if (filter.getConfigurationType() != null && !"".equals(filter.getConfigurationType())) {
            where += and + " configurationType = :configurationType ";
            and = " and ";
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            where += and + " objectType = :objectType";
            and = " and ";
        }
        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            where += and + " account = :account";
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

    private void bindParameters(Query query) {
        if (filter.getInstanceId() != null) {
            query.setParameter("instanceId", filter.getInstanceId());
        }
        if (filter.getName() != null && !"".equals(filter.getName())) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getConfigurationType() != null && !"".equals(filter.getConfigurationType())) {
            query.setParameter("configurationType", filter.getConfigurationType());
        }
        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            query.setParameter("account", filter.getAccount());
        }
        if (filter.isShared() != null) {
            query.setBoolean("shared", filter.isShared());
        }
    }

    @SuppressWarnings("unchecked")
    public List<JocConfigurationDbItem> getJocConfigurationList(final int limit) throws Exception {

        List<JocConfigurationDbItem> configurationsList = null;

        String sql = "from " + JocConfigurationDBItem + " " + getWhere() + filter.getOrderCriteria() + filter.getSortMode();
        Query<JocConfigurationDbItem> query = connection.createQuery(sql);
        bindParameters(query);
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        configurationsList = query.getResultList();

        return configurationsList;

    }

    public int saveConfiguration(JocConfigurationDbItem jocConfigurationDbItem, Boolean shared, String configurationItem) throws Exception {

        List<JocConfigurationDbItem> l = getJocConfigurationList(1);
        if (l.size() > 0) {
            jocConfigurationDbItem = l.get(0);
        }

        if (configurationItem != null) {
            jocConfigurationDbItem.setConfigurationItem(configurationItem);
        }
        if (shared != null) {
            jocConfigurationDbItem.setShared(shared);
        }
        jocConfigurationDbItem.setModified(new Date());
        connection.saveOrUpdate(jocConfigurationDbItem);
        return l.size();
    }

    public int deleteConfiguration() throws Exception {
        List<JocConfigurationDbItem> l = getJocConfigurationList(1);
        if (l.size() > 0) {
            JocConfigurationDbItem jocConfigurationDbItem = l.get(0);
            connection.delete(jocConfigurationDbItem);
        }
        return l.size();
    }

    public JocConfigurationFilter getFilter() {
        return filter;
    }

    public void setFilter(final JocConfigurationFilter filter) {
        this.filter = filter;
    }

}