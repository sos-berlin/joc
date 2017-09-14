package com.sos.joc.db.calendars;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

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

}
