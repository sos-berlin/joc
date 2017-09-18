package com.sos.joc.db.calendars;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;

public class CalendarsDBLayer extends DBLayer {

    public CalendarsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemCalendar getCalendar(String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where name = :name");
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("name", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Date renameCalendar(String path, String newPath) throws JocException {
        try {
            DBItemCalendar calendarDbItem = getCalendar(path);
            if (calendarDbItem == null) {
                throw new DBMissingDataException(String.format("calendar '%1$s' not found", path));
            }
            DBItemCalendar calendarNewDbItem = getCalendar(newPath);
            if (calendarNewDbItem != null) {
                throw new DBInvalidDataException(String.format("calendar '%1$s' already exists", newPath));
            }
            Date now = Date.from(Instant.now());
            calendarDbItem.setName(newPath);
            calendarDbItem.setBaseName(Paths.get(newPath).getFileName().toString());
            calendarDbItem.setModified(now);
            getSession().update(calendarDbItem);
            return now;
        } catch (JocException ex) {
            throw ex;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<String> getCategories() throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select category from ").append(DBITEM_CALENDARS).append(" group by category order by category");
            Query<String> query = getSession().createQuery(sql.toString());
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Date saveOrUpdateCalendar(Calendar calendar) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            DBItemCalendar calendarDbItem = getCalendar(calendar.getPath());
            Date now = Date.from(Instant.now());
            if (calendarDbItem == null) {
                calendarDbItem = new DBItemCalendar();
                calendarDbItem.setBaseName(Paths.get(calendar.getPath()).getFileName().toString());
                calendarDbItem.setName(calendar.getPath());
                if (calendar.getCategory() != null) {
                    calendarDbItem.setCategory(calendar.getCategory()); 
                } else {
                    calendarDbItem.setCategory(""); 
                }
                calendarDbItem.setTitle(calendar.getTitle());
                calendarDbItem.setType(calendar.getType().name());
                calendarDbItem.setCreated(now);
                calendarDbItem.setConfiguration(new ObjectMapper().writeValueAsString(calendar));
                calendarDbItem.setModified(now);
                getSession().save(calendarDbItem);
            } else {
                if (calendar.getCategory() != null) {
                    calendarDbItem.setCategory(calendar.getCategory()); 
                } else {
                    calendarDbItem.setCategory(""); 
                }
                calendarDbItem.setTitle(calendar.getTitle());
                calendarDbItem.setType(calendar.getType().name());
                calendarDbItem.setConfiguration(new ObjectMapper().writeValueAsString(calendar));
                calendarDbItem.setModified(now);
                getSession().update(calendarDbItem);
            }
            return now;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
