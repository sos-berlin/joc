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
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.UsedBy;

public class CalendarsDBLayer extends DBLayer {

    public CalendarsDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemInventoryClusterCalendar getCalendar(Long id) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where id = :id");
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryClusterCalendar getCalendar(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and name = :name");
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("name", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryClusterCalendar renameCalendar(String schedulerId, String path, String newPath) throws JocException {
        try {
            DBItemInventoryClusterCalendar calendarDbItem = getCalendar(schedulerId, path);
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

    public List<String> getCategories(String schedulerId) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select category from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where schedulerId = :schedulerId").append(" group by category order by category");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryClusterCalendar saveOrUpdateCalendar(String schedulerId, DBItemInventoryClusterCalendar calendarDbItem, Calendar calendar) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            Date now = Date.from(Instant.now());
            boolean newCalendar = (calendarDbItem == null);
            if (newCalendar) {
                calendarDbItem = new DBItemInventoryClusterCalendar();
                calendarDbItem.setSchedulerId(schedulerId);
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
            UsedBy usedby = calendar.getUsedBy(); 
            calendar.setUsedBy(null);
            calendarDbItem.setConfiguration(new ObjectMapper().writeValueAsString(calendar));
            calendarDbItem.setModified(now);
            calendar.setPath(calendarDbItem.getName());
            calendar.setName(calendarDbItem.getBaseName());
            calendar.setUsedBy(usedby);
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

    public void deleteCalendars(String schedulerId, Set<String> paths) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            for (DBItemInventoryClusterCalendar calendarDbItem : getCalendarsFromPaths(schedulerId, paths)) {
                getSession().delete(calendarDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void deleteCalendar(DBItemInventoryClusterCalendar dbCalendar) throws DBConnectionRefusedException, DBInvalidDataException {
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

    public List<DBItemInventoryClusterCalendar> getCalendarsFromIds(Set<Long> ids) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CLUSTER_CALENDARS);
            if (ids != null && !ids.isEmpty()) {
                if (ids.size() == 1) {
                    sql.append(" where id = :id");
                } else {
                    sql.append(" where id in (:id)");
                }
            }
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
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

    public List<DBItemInventoryClusterCalendar> getCalendarsFromPaths(String schedulerId, Set<String> paths) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where schedulerId = :schedulerId");
            if (paths != null && !paths.isEmpty()) {
                if (paths.size() == 1) {
                    sql.append(" and name = :name");
                } else {
                    sql.append(" and name in (:name)");
                }
            }
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
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

    public List<DBItemInventoryClusterCalendar> getCalendars(String schedulerId, String type, Set<String> categories, Set<String> folders, Set<String> recursiveFolders)
            throws DBConnectionRefusedException, DBInvalidDataException {
        // all recursiveFolders are included in folders too
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where schedulerId = :schedulerId");
            if (type != null && !type.isEmpty()) {
                sql.append(" and type = :type");
            } else {
                sql.append(" and type in ('WORKING_DAYS','NON_WORKING_DAYS')"); 
            }
            if (categories != null && !categories.isEmpty()) {
                if (categories.size() == 1) {
                    sql.append(" and category = :category");
                } else {
                    sql.append(" and category in (:category)");
                }
            }
            String folder = null;
            if (folders != null && !folders.isEmpty()) {
                if (folders.size() == 1) {
                    folder = folders.iterator().next();
                    if (recursiveFolders != null && recursiveFolders.contains(folder)) {
                        sql.append(" and (directory = :directory or directory like :likeDirectory)");
                    } else {
                        sql.append(" and directory = :directory");
                    }
                } else {
                    if (recursiveFolders != null && !recursiveFolders.isEmpty()) {
                        sql.append(" and (directory in (:directory)");
                        for (int i = 0; i < recursiveFolders.size(); i++) {
                            sql.append(" or directory like :likeDirectory" + i);
                        }
                        sql.append(")");
                    } else {
                        sql.append(" and directory in (:directory)");
                    }
                }
            }
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
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
                if (folders.size() == 1 && folder != null) {
                    query.setParameter("directory", folder);
                    if (recursiveFolders != null && recursiveFolders.contains(folder)) {
                        if (folder.equals("/")) {
                            query.setParameter("likeDirectory", recursiveFolders.iterator().next() + "%");
                        } else {
                            query.setParameter("likeDirectory", recursiveFolders.iterator().next() + "/%");
                        }
                    }
                } else {
                    query.setParameterList("directory", folders);
                    if (recursiveFolders != null && !recursiveFolders.isEmpty()) {
                        int index = 0;
                        for (String recuriveFolder : recursiveFolders) {
                            if (recuriveFolder.equals("/")) {
                                query.setParameter("likeDirectory" + index, recuriveFolder + "%");
                            } else {
                                query.setParameter("likeDirectory" + index, recuriveFolder + "/%");
                            }
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

    public List<String> getFoldersByFolder(String schedulerId, String folderName, Set<String> types) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (types == null) {
                types = new HashSet<String>();
            }
            if (types.isEmpty()) {
                types.add("WORKING_DAYS");
                types.add("NON_WORKING_DAYS");
            }
            StringBuilder sql = new StringBuilder();
            sql.append("select directory from ").append(DBITEM_CLUSTER_CALENDARS);
            sql.append(" where schedulerId = :schedulerId");
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
            query.setParameter("schedulerId", schedulerId);
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

    public List<DBItemInventoryClusterCalendar> getCalendarsOfAnObject(String schedulerId, String objectType, String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select c from ").append(DBITEM_CLUSTER_CALENDARS).append(" c, ");
            sql.append(DBITEM_INVENTORY_CLUSTER_CALENDAR_USAGE).append(" icu ");
            sql.append("where c.id = icu.calendarId ");
            sql.append("and icu.schedulerId = :schedulerId ");
            sql.append("and icu.objectType = :objectType ");
            sql.append("and icu.path = :path");
            Query<DBItemInventoryClusterCalendar> query = getSession().createQuery(sql.toString());
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

}
