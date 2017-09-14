package com.sos.joc.db.calendars;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;

public class CalendarsDBLayer extends DBLayer {

    public CalendarsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemCalendar getCalendar(String path, String category) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where name = :name").append(" and category = :category");
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("name", path);
            query.setParameter("category", category);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Date moveCalendar(String path, String category, String newPath, String newCategory) throws JocException {
        try {
            DBItemCalendar calendarDbItem = getCalendar(path, category);
            if (calendarDbItem == null) {
                throw new DBMissingDataException(String.format("calendar '%1$s'.'%2$s' not found", path, category));
            }
            DBItemCalendar calendarNewDbItem = getCalendar(newPath, newCategory);
            if (calendarNewDbItem != null) {
                throw new DBInvalidDataException(String.format("calendar '%1$s'.'%2$s' already exists", newPath, newCategory));
            }
            Date now = Date.from(Instant.now());
            calendarDbItem.setName(newPath);
            calendarDbItem.setCategory(newCategory);
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
    
    public List<String> getCategories(String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select category from ").append(DBITEM_CALENDARS);
            sql.append(" where name = :name").append(" and category = :category");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("name", path);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public Date saveOrUpdateCalendar(String path, String category, String configuration) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            DBItemCalendar calendarDbItem = getCalendar(path, category);
            Date now = Date.from(Instant.now());
            if (calendarDbItem == null) {
                calendarDbItem = new DBItemCalendar();
                calendarDbItem.setBaseName(Paths.get(path).getFileName().toString());
                calendarDbItem.setName(path);
                calendarDbItem.setCategory(category);
                calendarDbItem.setCreated(now);
                calendarDbItem.setConfiguration(configuration);
                calendarDbItem.setModified(now);
                getSession().save(calendarDbItem);
            } else {
                calendarDbItem.setConfiguration(configuration);
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
