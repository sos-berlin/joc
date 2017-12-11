package com.sos.joc.db.calendars;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
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

    public DBItemCalendar getCalendar(Long id) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where id = :id");
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemCalendar getCalendar(Long instanceId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and name = :name");
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("name", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemCalendar renameCalendar(Long instanceId, String path, String newPath) throws JocException {
        try {
            DBItemCalendar calendarDbItem = getCalendar(instanceId, path);
            if (calendarDbItem == null) {
                throw new DBMissingDataException(String.format("calendar '%1$s' not found", path));
            }
            calendarDbItem.setName(newPath);
            Path p = Paths.get(newPath);
            calendarDbItem.setBaseName(p.getFileName().toString());
            calendarDbItem.setDirectory(p.getParent().toString().replace('\\', '/'));
            calendarDbItem.setModified(Date.from(Instant.now()));
            getSession().update(calendarDbItem);
            return calendarDbItem;
        } catch (JocException ex) {
            throw ex;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getCategories(Long instanceId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select category from ").append(DBITEM_CALENDARS);
            sql.append(" where instanceId = :instanceId").append(" group by category order by category");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemCalendar saveOrUpdateCalendar(Long instanceId, DBItemCalendar calendarDbItem, Calendar calendar) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            Date now = Date.from(Instant.now());
            boolean newCalendar = (calendarDbItem == null);
            if (newCalendar) {
                calendarDbItem = new DBItemCalendar();
                calendarDbItem.setInstanceId(instanceId);
                calendarDbItem.setCreated(now);
            }
            if (calendar.getCategory() != null) {
                calendarDbItem.setCategory(calendar.getCategory());
            } else {
                calendarDbItem.setCategory("");
            }
            Path p = Paths.get(calendar.getPath());
            calendarDbItem.setBaseName(p.getFileName().toString());
            calendarDbItem.setDirectory(p.getParent().toString().replace('\\', '/'));
            calendarDbItem.setName(calendar.getPath());
            calendarDbItem.setTitle(calendar.getTitle());
            calendarDbItem.setType(calendar.getType().name());
            calendar.setId(null);
            calendar.setPath(null);
            calendar.setName(null);
            calendar.setUsedBy(null);
            calendarDbItem.setConfiguration(new ObjectMapper().writeValueAsString(calendar));
            calendarDbItem.setModified(now);
            calendar.setPath(calendarDbItem.getName());
            calendar.setName(calendarDbItem.getBaseName());
            if (newCalendar) {
                getSession().save(calendarDbItem);
            } else {
                calendar.setId(calendarDbItem.getId());
                getSession().update(calendarDbItem);
            }
            return calendarDbItem;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteCalendars(Long instanceId, Set<String> paths) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            for (DBItemCalendar calendarDbItem : getCalendarsFromPaths(instanceId, paths)) {
                getSession().delete(calendarDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteCalendar(DBItemCalendar dbCalendar) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (dbCalendar != null) {
                getSession().delete(dbCalendar);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemCalendar> getCalendarsFromIds(Set<Long> ids) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            if (ids != null && !ids.isEmpty()) {
                if (ids.size() == 1) {
                    sql.append(" where id = :id");
                } else {
                    sql.append(" where id in (:id)");
                }
            }
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            if (ids != null && !ids.isEmpty()) {
                if (ids.size() == 1) {
                    query.setParameter("id", ids.iterator().next());
                } else {
                    query.setParameterList("id", ids);
                }
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemCalendar> getCalendarsFromPaths(Long instanceId, Set<String> paths) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where instanceId = :instanceId");
            if (paths != null && !paths.isEmpty()) {
                if (paths.size() == 1) {
                    sql.append(" and name = :name");
                } else {
                    sql.append(" and name in (:name)");
                }
            }
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
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

    public List<DBItemCalendar> getCalendars(Long instanceId, String type, Set<String> categories, Set<String> folders, Set<String> recuriveFolders)
            throws DBConnectionRefusedException, DBInvalidDataException {
        // all recursiveFolders are included in folders too
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CALENDARS);
            sql.append(" where instanceId = :instanceId");
            if (type != null && !type.isEmpty()) {
                sql.append(" and type = :type");
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
            query.setParameter("instanceId", instanceId);
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

    public List<String> getFoldersByFolder(Long instanceId, String folderName, Set<String> types) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (types == null) {
                types = new HashSet<String>();
            }
            if (types.isEmpty()) {
                types.add("WORKING_DAYS");
                types.add("NON_WORKING_DAYS");
            }
            StringBuilder sql = new StringBuilder();
            sql.append("select directory from ").append(DBITEM_CALENDARS);
            sql.append(" where instanceId = :instanceId");
            if (types.size() == 1) {
                sql.append(" and type = :type");
            } else {
                sql.append(" and type in (:type)");
            }
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( directory = :folderName or directory like :likeFolderName )");
            }
            sql.append(" group by directory");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            if (types.size() == 1) {
                query.setParameter("type", types.iterator().next());
            } else {
                query.setParameterList("type", types);
            }
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

    public List<DBItemCalendar> getCalendarsOfAnObject(Long instanceId, String objectType, String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select c from ").append(DBITEM_CALENDARS).append(" c, ");
            sql.append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu ");
            sql.append("where c.id = icu.calendarId ");
            sql.append("and icu.instanceId = :instanceId ");
            sql.append("and icu.objectType = :objectType ");
            sql.append("and icu.path = :path");
            Query<DBItemCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", path);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
