package com.sos.joc.db.joe;

import java.util.List;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.joe.DBItemJoeObject;

public class DBLayerJoeObjects {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBLayerJoeObjects.class);
    private static final String DBItemJoeObject = DBItemJoeObject.class.getSimpleName();
    private final SOSHibernateSession sosHibernateSession;

    public DBLayerJoeObjects(SOSHibernateSession session) {
        this.sosHibernateSession = session;
    }

    public DBItemJoeObject getJoeObjectDbItem(final Long id) throws SOSHibernateException {
        return (DBItemJoeObject) sosHibernateSession.get(DBItemJoeObject.class, id);
    }

    public FilterJoeObjects resetFilter() {
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setAccount("");
        filter.setObjectType("");
        filter.setPath("");
        filter.setSchedulerId("");
        return filter;
    }

    private String getWhere(FilterJoeObjects filter) {
        String where = "1=1";
        String and = " and ";

        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " o.schedulerId = :schedulerId";
            and = " and ";
        }

        if (filter.getPath() != null && !"".equals(filter.getPath())) {
            where += and + " path = :path";
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

        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    private <T> Query<T> bindParameters(FilterJoeObjects filter, Query<T> query) {

        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (filter.getAccount() != null && !"".equals(filter.getAccount())) {
            query.setParameter("account", filter.getAccount());
        }
        if (filter.getObjectType() != null && !"".equals(filter.getObjectType())) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getPath() != null && !"".equals(filter.getPath())) {
            query.setParameter("path", filter.getPath());
        }
        return query;
    }

    public List<DBItemJoeObject> getJoeObjectList(FilterJoeObjects filter, final int limit) throws SOSHibernateException {
        String q = "from " + DBItemJoeObject + getWhere(filter);

        Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
        query = bindParameters(filter, query);

        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return sosHibernateSession.getResultList(query);
    }

    public DBItemJoeObject getJoeObject(FilterJoeObjects filter) throws SOSHibernateException {
        String q = "from " + DBItemJoeObject + getWhere(filter);

        Query<DBItemJoeObject> query = sosHibernateSession.createQuery(q);
        query = bindParameters(filter, query);

        query.setMaxResults(1);
        List<DBItemJoeObject> l = sosHibernateSession.getResultList(query);
        if (l.size() == 0) {
            return null;
        } else {
            return sosHibernateSession.getResultList(query).get(0);
        }
    }

}