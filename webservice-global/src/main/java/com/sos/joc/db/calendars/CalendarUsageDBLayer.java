package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class CalendarUsageDBLayer extends DBLayer {

    private static final String CALENDAR_USAGE = CalendarUsage.class.getName();
    private static final String CALENDAR_USAGES_INSTANCE = CalendarUsagesAndInstance.class.getName();
    private Query<DBItemInventoryCalendarUsage> query;

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

        if (filter.getInstanceId() != null) {
            where += and + " instanceId = :instanceId";
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
        if (!where.trim().isEmpty()) {
            where = " where " + where;
        }
        return where;
    }

    public void saveCalendarUsage(DBItemInventoryCalendarUsage calendarUsageDbItem) throws DBConnectionRefusedException, DBInvalidDataException {
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

    private void bindParameters(CalendarUsageFilter filter) {
        if (filter.getCalendarId() != null) {
            query.setParameter("calendarId", filter.getCalendarId());
        }
        if (filter.getInstanceId() != null) {
            query.setParameter("instanceId", filter.getInstanceId());
        }
        if (filter.getObjectType() != null && !filter.getObjectType().isEmpty()) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            query.setParameter("path", filter.getPath());
        }
    }

    public int deleteCalendarUsage(CalendarUsageFilter calendarUsageFilter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String hql = "delete from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(calendarUsageFilter);
            int row = 0;
            query = getSession().createQuery(hql);
            bindParameters(calendarUsageFilter);
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

    public void deleteCalendarUsage(DBItemInventoryCalendarUsage calendarUsage) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().delete(calendarUsage);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void updateEditFlag(Set<DBItemInventoryCalendarUsage> calendarUsages, boolean update) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            if (calendarUsages != null) {
                for (DBItemInventoryCalendarUsage calendarUsage : calendarUsages) {
                    if (update) {
                        if (calendarUsage.getEdited() == null) {
                            getSession().delete(calendarUsage);
                        } else {
                            calendarUsage.setModified(new Date());
                            getSession().update(calendarUsage);
                        }
                    } else {
                        if (calendarUsage.getEdited() != null && calendarUsage.getEdited()) {
                            calendarUsage.setModified(new Date()); 
                            getSession().update(calendarUsage);
                        } else {
                            getSession().delete(calendarUsage);
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

    public List<DBItemInventoryCalendarUsage> getCalendarUsagesOfAnInstance(Long instanceId, Long calendarId) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setInstanceId(instanceId);
            filter.setCalendarId(calendarId);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryCalendarUsage> getCalendarUsagesOfAnObject(Long instanceId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setInstanceId(instanceId);
            filter.setObjectType(objectType);
            filter.setPath(path);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Long getObjectIdOfAnCalendarUsageObject(Long instanceId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        if (instanceId == null) {
            throw new DBInvalidDataException("undefined instanceId"); 
        }
        if (objectType == null) {
            throw new DBInvalidDataException("undefined objectType"); 
        }
        if (path == null) {
            throw new DBInvalidDataException("undefined path"); 
        }
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select id from ");
            switch (objectType.toUpperCase()) {
            case "ORDER":
                sql.append(DBITEM_INVENTORY_ORDERS);
                break;
            case "JOB":
                sql.append(DBITEM_INVENTORY_JOBS);
                break;
            case "SCHEDULE":
                sql.append(DBITEM_INVENTORY_SCHEDULES);
                break;
            }
            sql.append(" where name = :path");
            sql.append(" and instanceId = :instanceId");
            Query<Long> query = getSession().createQuery(sql.toString());
            query.setParameter("path", path);
            query.setParameter("instanceId", instanceId);
            Long id = getSession().getSingleResult(query);
            if (id == null) {
                throw new DBInvalidDataException(String.format("cannot determine id of %$1: %2$s", objectType, path)); 
            }
            return id;
            
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (DBInvalidDataException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<CalendarUsage> getCalendarUsages(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGE);
            sql.append(" (icu.instanceId, icu.path, icu.objectType, ii.schedulerId, ii.hostname, ii.port) from ");
            sql.append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii ");
            sql.append("where icu.instanceId = ii.id ");
            if (calendarId != null) {
                sql.append(" and icu.calendarId = :calendarId");
            }
            Query<CalendarUsage> query = getSession().createQuery(sql.toString());
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

    public List<CalendarUsage> getCalendarUsages(String calendarPath) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGE);
            sql.append(" (icu.instanceId, icu.path, icu.objectType, ii.schedulerId, ii.hostname, ii.port) from ");
            sql.append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CALENDARS).append(" c, ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii ");
            sql.append("where icu.calendarId = c.id ");
            sql.append("and icu.instanceId = ii.id ");
            if (calendarPath != null) {
                sql.append(" and c.name = :calendarPath");
            }
            Query<CalendarUsage> query = getSession().createQuery(sql.toString());
            if (calendarPath != null) {
                query.setParameter("calendarPath", calendarPath);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<CalendarUsagesAndInstance> getInstancesFormCalendar(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGES_INSTANCE).append(" (ii) from ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii, ");
            sql.append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu ");
            sql.append("where ii.id = icu.instanceId ");
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
