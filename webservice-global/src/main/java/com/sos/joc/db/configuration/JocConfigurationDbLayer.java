package com.sos.joc.db.configuration;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.jitl.reporting.db.DBLayer;

/** @author Uwe Risse */
public class JocConfigurationDbLayer extends DBLayer {

    private static final String JOC_CONFIGURATION_DB_ITEM = JocConfigurationDbItem.class.getName();
    private JocConfigurationFilter filter = null;

    public JocConfigurationDbLayer(SOSHibernateSession sosHibernateSession) {
        super(sosHibernateSession);
        resetFilter();
    }

    public JocConfigurationDbItem getJocConfigurationDbItem(final Long id) throws SOSHibernateException {
        return (JocConfigurationDbItem) (this.getSession().get(JocConfigurationDbItem.class, id));
    }

    public void resetFilter() {
        filter = new JocConfigurationFilter();
        filter.setSchedulerId(null);
        filter.setName("");
        filter.setConfigurationType("");
        filter.setObjectType("");
        filter.setAccount("");
        filter.setShared(null);
        filter.setId(null);
    }

    public int delete() throws SOSHibernateException {
        String hql = "delete from " + JOC_CONFIGURATION_DB_ITEM + " " + getWhere();
        Query<Integer> query = null;
        query = this.getSession().createQuery(hql);
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
        return this.getSession().executeUpdate(query);
    }

    private String getWhere() {
        String where = "";
        String and = "";
        if (filter.getName() != null && !"".equals(filter.getName())) {
            where += and + " name = :name";
            and = " and ";
        }
        if (filter.getSchedulerId() != null) {
            where += and + " schedulerId = :schedulerId ";
            and = " and ";
        }
        if (filter.getId() != null) {
            where += and + " id = :id ";
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

    private void bindParameters(Query<JocConfigurationDbItem> query) {
        if (filter.getName() != null && !"".equals(filter.getName())) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getSchedulerId() != null) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (filter.getId() != null) {
            query.setParameter("id", filter.getId());
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
            query.setBoolean("shared", filter.isShared());
        }
    }

    public List<JocConfigurationDbItem> getJocConfigurationList(final int limit) throws SOSHibernateException {

        String sql = "from " + JOC_CONFIGURATION_DB_ITEM + " " + getWhere() + filter.getOrderCriteria() + filter.getSortMode();
        Query<JocConfigurationDbItem> query = this.getSession().createQuery(sql);
        bindParameters(query);
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return this.getSession().getResultList(query);

    }

    public JocConfigurationDbItem getJocConfiguration(Long id) throws SOSHibernateException {
        StringBuilder sql = new StringBuilder();
        sql.append("from ").append(JOC_CONFIGURATION_DB_ITEM).append(" where id = :id");
        Query<JocConfigurationDbItem> query = this.getSession().createQuery(sql.toString());
        query.setParameter("id", id);
        return this.getSession().getSingleResult(query);
    }

    public List<JocConfigurationDbItem> getJocConfigurations(final int limit) throws SOSHibernateException {
        StringBuilder sql = new StringBuilder();
        sql.append("from ").append(JOC_CONFIGURATION_DB_ITEM).append(" ").append(getWhere()).append(filter.getOrderCriteria()).append(filter
                .getSortMode());
        Query<JocConfigurationDbItem> query = this.getSession().createQuery(sql.toString());
        bindParameters(query);
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return this.getSession().getResultList(query);
    }

    public Long saveOrUpdateConfiguration(JocConfigurationDbItem jocConfigurationDbItem) throws SOSHibernateException {
        jocConfigurationDbItem.setModified(new Date());
        if (jocConfigurationDbItem.getId() == null) {
            this.getSession().save(jocConfigurationDbItem);
        } else {
            this.getSession().update(jocConfigurationDbItem);
        }
        return jocConfigurationDbItem.getId();
    }

    public int deleteConfiguration() throws SOSHibernateInvalidSessionException, SOSHibernateException {
        List<JocConfigurationDbItem> l = getJocConfigurationList(1);
        int size = (l != null) ? l.size() : 0;
        if (size > 0) {
            this.getSession().delete(l.get(0));
        }
        return size;
    }

    public void deleteConfiguration(JocConfigurationDbItem dbItem) throws SOSHibernateException {
        this.getSession().delete(dbItem);
    }

    public JocConfigurationFilter getFilter() {
        return filter;
    }

    public void setFilter(final JocConfigurationFilter filter) {
        this.filter = filter;
    }

}