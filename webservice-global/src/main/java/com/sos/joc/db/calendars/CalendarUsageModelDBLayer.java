package com.sos.joc.db.calendars;

import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class CalendarUsageModelDBLayer extends DBLayer {
    
    private static final String CALENDAR_USAGE = CalendarUsage.class.getName();

    public CalendarUsageModelDBLayer(SOSHibernateSession connection) {
        super(connection);
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
