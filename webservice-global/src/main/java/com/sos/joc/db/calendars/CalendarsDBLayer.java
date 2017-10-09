package com.sos.joc.db.calendars;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
            Path p = Paths.get(newPath);
            calendarDbItem.setBaseName(p.getFileName().toString());
            calendarDbItem.setDirectory(p.getParent().toString().replace('\\', '/'));
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

    public Date saveOrUpdateCalendar(DBItemCalendar calendarDbItem, Calendar calendar) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            Date now = Date.from(Instant.now());
            boolean newCalendar = (calendarDbItem == null);
            if (newCalendar) {
                calendarDbItem = new DBItemCalendar();
                Path p = Paths.get(calendar.getPath());
                calendarDbItem.setBaseName(p.getFileName().toString());
                calendarDbItem.setDirectory(p.getParent().toString().replace('\\', '/'));
                calendarDbItem.setName(calendar.getPath());
                calendarDbItem.setCreated(now);
            }
            if (calendar.getCategory() != null) {
                calendarDbItem.setCategory(calendar.getCategory());
            } else {
                calendarDbItem.setCategory("");
            }
            calendarDbItem.setTitle(calendar.getTitle());
            calendarDbItem.setType(calendar.getType().name());
            calendar.setId(null);
            calendar.setPath(null);
            calendar.setName(null);
            calendarDbItem.setConfiguration(new ObjectMapper().writeValueAsString(calendar));
            calendarDbItem.setModified(now);
            if (newCalendar) {
                getSession().save(calendarDbItem);
            } else {
                getSession().update(calendarDbItem);
            }
            return now;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteCalendars(Set<String> paths) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            for (DBItemCalendar calendarDbItem : getCalendars(paths)) {
                getSession().delete(calendarDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteCalendar(String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            DBItemCalendar dbCalendar = getCalendar(path);
            if (dbCalendar != null) {
                getSession().delete(dbCalendar); 
                getSession().beginTransaction();
                CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(getSession());
                calendarUsageDBLayer.deleteCalendarUsage(dbCalendar.getId());
                getSession().commit();
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemCalendar> getCalendars(Set<String> paths) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            if (paths != null && !paths.isEmpty()) {
                if (paths.size() == 1) {
                    sql.append(" where name = :name");
                } else {
                    sql.append(" where name in (:name)");
                }
            }
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            if (paths != null && !paths.isEmpty()) {
                if (paths.size() == 1) {
                    query.setParameter("name", paths.iterator().next());
                } else {
                    query.setParameterList("name", paths);
                }
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemCalendar> getCalendars(String type, Set<String> categories, Set<String> folders, Set<String> recuriveFolders)
            throws DBConnectionRefusedException, DBInvalidDataException {
        // all recursiveFolders are included in folders too
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            if (type != null && !type.isEmpty()) {
                sql.append(" where type = :type");
            } else {
                sql.append(" where 1=1");
            }
            if (categories != null && !categories.isEmpty()) {
                if (categories.size() == 1) {
                    sql.append(" and category = :category");
                } else {
                    sql.append(" and category in (:category)");
                }
            }
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    if (recuriveFolders != null && recuriveFolders.contains(folders.iterator().next())) {
                        sql.append(" and (directory = :directory or directory like :likeDirectory)");
                    } else {
                        sql.append(" and directory = :directory");
                    }
                } else {
                    if (recuriveFolders != null && !recuriveFolders.isEmpty()) {
                        sql.append(" and (directory in (:directory)");
                        for (int i = 0; i < recuriveFolders.size(); i++) {
                            sql.append(" or directory like :likeDirectory" + i);
                        }
                        sql.append(")");
                    } else {
                        sql.append(" and directory in (:directory)");
                    }
                }
            }
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            if (type != null && !type.isEmpty()) {
                query.setParameter("type", type.toUpperCase());
            }
            if (categories != null && !categories.isEmpty()) {
                if (categories.size() == 1) {
                    query.setParameter("category", categories.iterator().next());
                } else {
                    query.setParameterList("category", categories);
                }
            }
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    query.setParameter("directory", folders.iterator().next());
                    if (recuriveFolders != null && recuriveFolders.contains(folders.iterator().next())) {
                        query.setParameter("likeDirectory", recuriveFolders.iterator().next() + "/%");
                    }
                } else {
                    query.setParameterList("directory", folders);
                    if (recuriveFolders != null && !recuriveFolders.isEmpty()) {
                        int index = 0;
                        for (String recuriveFolder : recuriveFolders) {
                            query.setParameter("likeDirectory" + index, recuriveFolder + "/%");
                            index++;
                        }
                    }
                }
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getFoldersByFolder(String folderName) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select directory from ").append(DBITEM_CALENDARS);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" where directory = :folderName or directory like :likeFolderName");
            }
            Query<String> query = getSession().createQuery(sql.toString());
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", folderName + "/%");
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
