package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.configuration.CalendarUsageConfiguration;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class CalendarUsageDBLayer extends DBLayer {

    private static final String CALENDAR_USAGES_INSTANCE = CalendarUsagesAndInstance.class.getName();
    private static final String CALENDAR_USAGE_CONFIGURATION = CalendarUsageConfiguration.class.getName();
    private Query<DBItemInventoryClusterCalendarUsage> query;

    public CalendarUsageDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    private String getWhere(CalendarUsageFilter filter) {
        String where = "";
        String and = "";
        if (filter.getObjectType() != null && !filter.getObjectType().isEmpty()) {
            where += and + " objectType = :objectType";
            and = " and ";
        }
        if (filter.getSchedulerId() != null && !filter.getSchedulerId().isEmpty()) {
            where += and + " schedulerId = :schedulerId";
            and = " and ";
        }
        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            where += and + " path = :path";
            and = " and ";
        }
        if (filter.getCalendarId() != null) {
            where += and + " calendarId = :calendarId";
            and = " and ";
        }
        if (filter.getEdited() != null) {
            where += and + " edited = :edited";
            and = " and ";
        }
        if (!where.trim().isEmpty()) {
            where = " where " + where;
        }
        return where;
    }

    public void saveCalendarUsage(DBItemInventoryClusterCalendarUsage calendarUsageDbItem) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            if (calendarUsageDbItem != null) {
                calendarUsageDbItem.setCreated(new Date());
                calendarUsageDbItem.setModified(new Date());
                getSession().save(calendarUsageDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void updateCalendarUsage(DBItemInventoryClusterCalendarUsage calendarUsageDbItem) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            if (calendarUsageDbItem != null) {
                calendarUsageDbItem.setModified(new Date());
                getSession().update(calendarUsageDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    private void bindParameters(CalendarUsageFilter filter) {
        if (filter.getCalendarId() != null) {
            query.setParameter("calendarId", filter.getCalendarId());
        }
        if (filter.getSchedulerId() != null && !filter.getSchedulerId().isEmpty()) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (filter.getObjectType() != null && !filter.getObjectType().isEmpty()) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            query.setParameter("path", filter.getPath());
        }
        if (filter.getEdited() != null) {
            query.setParameter("edited", filter.getEdited());
        }
    }

    public int deleteCalendarUsage(CalendarUsageFilter calendarUsageFilter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String hql = "delete from " + DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE + getWhere(calendarUsageFilter);
            int row = 0;
            Query<Integer> query = getSession().createQuery(hql);
            if (calendarUsageFilter.getCalendarId() != null) {
                query.setParameter("calendarId", calendarUsageFilter.getCalendarId());
            }
            if (calendarUsageFilter.getSchedulerId() != null && !calendarUsageFilter.getSchedulerId().isEmpty()) {
                query.setParameter("schedulerId", calendarUsageFilter.getSchedulerId());
            }
            if (calendarUsageFilter.getObjectType() != null && !calendarUsageFilter.getObjectType().isEmpty()) {
                query.setParameter("objectType", calendarUsageFilter.getObjectType());
            }
            if (calendarUsageFilter.getPath() != null && !calendarUsageFilter.getPath().isEmpty()) {
                query.setParameter("path", calendarUsageFilter.getPath());
            }
            if (calendarUsageFilter.getEdited() != null) {
                query.setParameter("edited", calendarUsageFilter.getEdited());
            }
            row = getSession().executeUpdate(query);
            return row;

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public int deleteCalendarUsage(Long calendarId) throws DBConnectionRefusedException, DBInvalidDataException {
        CalendarUsageFilter calendarUsageFilter = new CalendarUsageFilter();
        calendarUsageFilter.setCalendarId(calendarId);
        int row = deleteCalendarUsage(calendarUsageFilter);
        return row;
    }

    public void deleteCalendarUsage(DBItemInventoryClusterCalendarUsage calendarUsage) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().delete(calendarUsage);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public int updateEditFlag(DBItemInventoryClusterCalendarUsage calendarUsage) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String hql = "update " + DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE + " set edited = :edited where id = :id";
            int row = 0;
            Query<Integer> query = getSession().createQuery(hql);
            query.setParameter("id", calendarUsage.getId());
            query.setParameter("edited", calendarUsage.getEdited());
            row = getSession().executeUpdate(query);
            return row;

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void updateEditFlag(Set<DBItemInventoryClusterCalendarUsage> calendarUsages, boolean update) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            if (calendarUsages != null) {
                for (DBItemInventoryClusterCalendarUsage item : calendarUsages) {
                    if (update) {
                        if (item.getEdited() == null) {
                            getSession().delete(item);
                        } else {
                            item.setModified(new Date());
                            getSession().update(item);
                        }
                    } else {
                        if (item.getEdited() != null && item.getEdited()) {
                            item.setModified(new Date());
                            getSession().update(item);
                        } else {
                            getSession().delete(item);
                        }
                    }
                }
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryClusterCalendarUsage> getCalendarUsagesOfAnObject(String schedulerId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setSchedulerId(schedulerId);
            filter.setObjectType(objectType);
            filter.setPath(path);
            String sql = "from " + DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryClusterCalendarUsage getCalendarUsageOfAnObject(String schedulerId, String calendarPath, String objectType,
            String objectPath) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select icu from ").append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CLUSTER_CALENDARS).append(" ic ");
            sql.append(" where ic.id = icu.calendarId");
            sql.append(" and ic.name = :calendarPath");
            sql.append(" and icu.schedulerId = :schedulerId");
            sql.append(" and icu.objectType = :objectType");
            sql.append(" and icu.path = :path");
            query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", objectPath);
            query.setParameter("calendarPath", calendarPath);
            return getSession().getSingleResult(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<CalendarUsageConfiguration> getConfigurationsOfAnObject(String schedulerId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGE_CONFIGURATION);
            sql.append("(ic.name, ic.type, icu.configuration) from ");
            sql.append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CLUSTER_CALENDARS).append(" ic ");
            sql.append(" where ic.id = icu.calendarId");
            sql.append(" and icu.schedulerId = :schedulerId");
            sql.append(" and icu.objectType = :objectType");
            sql.append(" and icu.path = :path");
            sql.append(" and icu.configuration != null");
            Query<CalendarUsageConfiguration> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", path);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getWorkingDaysCalendarUsagesOfAnObject(String schedulerId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select icu.configuration from ").append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CLUSTER_CALENDARS).append(" ic ");
            sql.append(" where ic.id = icu.calendarId");
            sql.append(" and ic.type = 'WORKING_DAYS'");
            sql.append(" and icu.schedulerId = :schedulerId");
            sql.append(" and icu.objectType = :objectType");
            sql.append(" and icu.path = :path");
            sql.append(" and icu.configuration is not null");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", path);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryClusterCalendarUsage> getCalendarUsages(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        CalendarUsageFilter filter = new CalendarUsageFilter();
        filter.setCalendarId(calendarId);
        filter.setEdited(false);
        return getCalendarUsages(filter);
    }

    public List<DBItemInventoryClusterCalendarUsage> getCalendarUsages(CalendarUsageFilter filter) throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            String sql = "from " + DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryClusterCalendarUsage> getCalendarUsages(String schedulerId, String calendarPath) throws DBInvalidDataException,
            DBConnectionRefusedException {
        CalendarUsageFilter filter = new CalendarUsageFilter();
        filter.setSchedulerId(schedulerId);
        filter.setPath(calendarPath);
        filter.setEdited(false);
        return getCalendarUsages(filter);
    }
    
    public List<DBItemInventoryClusterCalendarUsage> getCalendarUsagesOfFolder(String schedulerId, String folderPath) throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path like :folder");
            Query<DBItemInventoryClusterCalendarUsage> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("folder", (folderPath + "/").replaceAll("//+", "/") + "%");
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryClusterCalendarUsage getCalendarUsageByConstraint(String schedulerId, Long calendarId, String objectType,
            String calendarPath) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setSchedulerId(schedulerId);
            filter.setCalendarId(calendarId);
            filter.setObjectType(objectType);
            filter.setPath(calendarPath);
            String sql = "from " + DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getSingleResult(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<CalendarUsagesAndInstance> getInstancesFromCalendar(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGES_INSTANCE).append(" (ii) from ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii, ");
            sql.append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE).append(" icu ");
            sql.append("where ii.schedulerId = icu.schedulerId ");
            if (calendarId != null) {
                sql.append("and icu.calendarId = :calendarId ");
            }
            sql.append("group by ii.id");
            Query<CalendarUsagesAndInstance> query = getSession().createQuery(sql.toString());
            if (calendarId != null) {
                query.setParameter("calendarId", calendarId);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
}
