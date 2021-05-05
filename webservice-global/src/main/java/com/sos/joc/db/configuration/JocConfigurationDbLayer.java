package com.sos.joc.db.configuration;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.configuration.Profile;

public class JocConfigurationDbLayer extends DBLayer {

    private static final String JOC_CONFIGURATION_DB_ITEM = JocConfigurationDbItem.class.getSimpleName();
    private static final String CONFIGURATION_PROFILE = ConfigurationProfile.class.getName();
 
    public JocConfigurationDbLayer(SOSHibernateSession sosHibernateSession) {
        super(sosHibernateSession);
    }

    public JocConfigurationDbItem getJocConfigurationDbItem(final Long id) throws SOSHibernateException {
        return this.getSession().get(JocConfigurationDbItem.class, id);
    }

    public int delete(JocConfigurationFilter filter) throws SOSHibernateException {
        String hql = "delete from " + JOC_CONFIGURATION_DB_ITEM + " " + getWhere(filter);
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

    private String getWhere(JocConfigurationFilter filter) {
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
            where += and + " lower(account) = :account";
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

    private void bindParameters(JocConfigurationFilter filter, Query<JocConfigurationDbItem> query) {
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
            query.setParameter("shared", filter.isShared());
        }
    }

    public List<JocConfigurationDbItem> getJocConfigurationList(JocConfigurationFilter filter, final int limit) throws SOSHibernateException {

        String sql = "from " + JOC_CONFIGURATION_DB_ITEM + " " + getWhere(filter) + filter.getOrderCriteria() + filter.getSortMode();
        Query<JocConfigurationDbItem> query = this.getSession().createQuery(sql);
        bindParameters(filter, query);
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return this.getSession().getResultList(query);

    }

    public List<Profile> getJocConfigurationProfiles(JocConfigurationFilter filter) throws SOSHibernateException {
        String sql = "select new " + CONFIGURATION_PROFILE + "(c.account, c.modified) from " + JOC_CONFIGURATION_DB_ITEM + " c " + getWhere(filter)
                + filter.getOrderCriteria() + filter.getSortMode();
        Query<ConfigurationProfile> query = this.getSession().createQuery(sql);
        query.setParameter("configurationType", filter.getConfigurationType());
        List<ConfigurationProfile> profiles = this.getSession().getResultList(query);

        if (profiles == null) {
            return null;
        }
        return profiles.stream().distinct().collect(Collectors.toList());
    }

    public JocConfigurationDbItem getJocConfiguration(Long id) throws SOSHibernateException {
        return this.getSession().get(JocConfigurationDbItem.class, id);
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

    public int deleteConfiguration(JocConfigurationFilter filter) throws SOSHibernateInvalidSessionException, SOSHibernateException {
        List<JocConfigurationDbItem> l = getJocConfigurationList(filter, 1);
        int size = (l != null) ? l.size() : 0;
        if (size > 0) {
            this.getSession().delete(l.get(0));
        }
        return size;
    }

    public void deleteConfiguration(JocConfigurationDbItem dbItem) throws SOSHibernateException {
        this.getSession().delete(dbItem);
    }

    public int deleteConfigurations(Collection<String> accounts) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String hql = "delete from " + JOC_CONFIGURATION_DB_ITEM + " where lower(account) in (:accounts)";
            Query<Integer> query = this.getSession().createQuery(hql);
            query.setParameterList("accounts", accounts.stream().map(String::toLowerCase).collect(Collectors.toSet()));
            return this.getSession().executeUpdate(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}