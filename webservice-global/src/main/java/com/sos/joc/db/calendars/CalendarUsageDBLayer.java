package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class CalendarUsageDBLayer extends DBLayer {
    
    private static final String CALENDAR_USAGE = CalendarUsage.class.getName();
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
                getSession().save(calendarUsageDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    /*
     * Check whether this is needed
     * 
     * 
     * private DBItemCalendarUsage getCalendarUsage(CalendarUsageFilter filter)
     * throws DBConnectionRefusedException, DBInvalidDataException { try { if
     * (filter.getObjectType() == null) { filter.setObjectType(""); } if
     * (filter.getInstanceId() == null) { filter.setInstanceId(""); } if
     * (filter.getPath() == null) { filter.setPath(""); } if (filter.getCalendarId()
     * == null) { filter.setCalendarId(-1L); }
     * 
     * String sql = "from " + DBITEM_CALENDAR_USAGE + getWhere(filter); query =
     * getSession().createQuery(sql); bindParameters(filter);
     * List<DBItemCalendarUsage> l = getSession().getResultList(query); if (l !=
     * null && l.size() > 0) { return l.get(0); } else { return null; }
     * 
     * } catch (SOSHibernateInvalidSessionException ex) { throw new
     * DBConnectionRefusedException(ex); } catch (Exception ex) { throw new
     * DBInvalidDataException(ex); } }
     */
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

    /*
     * Check whether this is needed
     * 
     * public void deleteCalendarUsage(CalendarUsageFilter filter) throws
     * DBConnectionRefusedException, DBInvalidDataException { try {
     * DBItemCalendarUsage dbCalendarUsage = getCalendarUsage(filter); if
     * (dbCalendarUsage != null) { getSession().delete(dbCalendarUsage); } } catch
     * (SOSHibernateInvalidSessionException ex) { throw new
     * DBConnectionRefusedException(ex); } catch (Exception ex) { throw new
     * DBInvalidDataException(ex); } }
     */

    public int deleteCalendarUsage(Long calendarId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {

            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setCalendarId(calendarId);
            String hql = "delete from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            int row = 0;
            query = getSession().createQuery(hql);
            bindParameters(filter);
            row = getSession().executeUpdate(query);
            return row;

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryCalendarUsage> getListOfCalendarUsage(Long calendarId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
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
    
    public List<CalendarUsage> getCalendarUsages(Long calendarId)
            throws DBInvalidDataException, DBConnectionRefusedException {
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
    
    public List<CalendarUsage> getCalendarUsages(String calendarPath)
            throws DBInvalidDataException, DBConnectionRefusedException {
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

}
