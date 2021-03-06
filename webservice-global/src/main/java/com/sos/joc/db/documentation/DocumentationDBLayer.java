package com.sos.joc.db.documentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.hibernate.criterion.MatchMode;
import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.common.JobSchedulerObject;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.docu.DocumentationShowFilter;
import com.sos.joc.model.tree.Tree;

public class DocumentationDBLayer extends DBLayer {

    public DocumentationDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    public DBItemDocumentation getDocumentation(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path = :path");
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Long getDocumentationId(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select id from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and path = :path");
            Query<Long> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemDocumentationImage getDocumentationImage(Long id) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (id == null) {
                return null;
            }
            return getSession().get(DBItemDocumentationImage.class, id);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemDocumentation> getDocumentations(String schedulerId, List<String> paths) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            if (paths != null && !paths.isEmpty()) {
                sql.append(" and path in (:paths)");
            }
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (paths != null && !paths.isEmpty()) {
                query.setParameterList("paths", paths);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemDocumentation> getDocumentations(String schedulerId, String folder) throws DBConnectionRefusedException,
            DBInvalidDataException {
        return getDocumentations(schedulerId, null, folder, false);
    }

    public List<DBItemDocumentation> getDocumentations(String schedulerId, Set<String> types, String folder, boolean recursive)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            if (folder != null && !folder.isEmpty()) {
                if (recursive) {
                    if (!"/".equals(folder)) {
                        sql.append(" and (directory = :folder");
                        sql.append(" or directory like :folder2)");
                    }
                } else {
                    sql.append(" and directory = :folder");
                }
            }
            if (types != null && !types.isEmpty()) {
                sql.append(" and type in (:types)");
            }
            Query<DBItemDocumentation> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (folder != null && !folder.isEmpty()) {
                if (recursive) {
                    if (!"/".equals(folder)) {
                        query.setParameter("folder", folder);
                        query.setParameter("folder2", MatchMode.START.toMatchString(folder + "/"));
                    }
                } else {
                    query.setParameter("folder", folder);
                }
            }
            if (types != null && !types.isEmpty()) {
                query.setParameterList("types", types);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public String getDocumentationPath(DocumentationShowFilter documentationFilter) throws DBConnectionRefusedException, DBInvalidDataException {
        return getDocumentationPath(documentationFilter.getJobschedulerId(), documentationFilter.getType(), documentationFilter.getPath());
    }

    public String getDocumentationPath(String schedulerId, JobSchedulerObjectType objectType, String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        if (JobSchedulerObjectType.WORKINGDAYSCALENDAR == objectType || JobSchedulerObjectType.NONWORKINGDAYSCALENDAR == objectType) {
            return getDocumentationPathOfCalendar(schedulerId, path);
        } else {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("select d.path from ").append(DBITEM_DOCUMENTATION).append(" d, ").append(DBITEM_DOCUMENTATION_USAGE).append(" du");
                sql.append(" where d.id = du.documentationId");
                sql.append(" and du.schedulerId = :schedulerId");
                sql.append(" and du.objectType = :objectType");
                sql.append(" and du.path = :path");
                Query<String> query = getSession().createQuery(sql.toString());
                query.setParameter("schedulerId", schedulerId);
                query.setParameter("objectType", objectType.name());
                query.setParameter("path", path);
                return getSession().getSingleResult(query);
            } catch (SOSHibernateInvalidSessionException ex) {
                throw new DBConnectionRefusedException(ex);
            } catch (Exception ex) {
                throw new DBInvalidDataException(ex);
            }
        }
    }

    public String getDocumentationPathOfCalendar(String schedulerId, String path) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            Set<String> types = new HashSet<String>();
            types.add(JobSchedulerObjectType.WORKINGDAYSCALENDAR.name());
            types.add(JobSchedulerObjectType.NONWORKINGDAYSCALENDAR.name());

            StringBuilder sql = new StringBuilder();

            sql.append("select d.path from ").append(DBITEM_DOCUMENTATION).append(" d, ").append(DBITEM_DOCUMENTATION_USAGE).append(" du");
            sql.append(" where d.id = du.documentationId");
            sql.append(" and du.schedulerId = :schedulerId");
            sql.append(" and du.objectType in (:objectType)");
            sql.append(" and du.path = :path");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameterList("objectType", types);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Map<String, String> getDocumentationPaths(String schedulerId, JobSchedulerObjectType objectType) throws DBConnectionRefusedException,
            DBInvalidDataException {
        if (JobSchedulerObjectType.WORKINGDAYSCALENDAR == objectType || JobSchedulerObjectType.NONWORKINGDAYSCALENDAR == objectType) {
            return getDocumentationPathsOfCalendar(schedulerId);
        } else {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("select new ").append(DocumentationOfObject.class.getName()).append("(d.path, du.path) from ");
                sql.append(DBITEM_DOCUMENTATION).append(" d, ").append(DBITEM_DOCUMENTATION_USAGE).append(" du");
                sql.append(" where d.id = du.documentationId");
                sql.append(" and du.schedulerId = :schedulerId");
                sql.append(" and du.objectType = :objectType");
                Query<DocumentationOfObject> query = getSession().createQuery(sql.toString());
                query.setParameter("schedulerId", schedulerId);
                query.setParameter("objectType", objectType.name());
                List<DocumentationOfObject> result = getSession().getResultList(query);
                if (result == null) {
                    return new HashMap<String, String>();
                }
                return result.stream().collect(Collectors.toMap(DocumentationOfObject::getObjPath, DocumentationOfObject::getDocPath));
            } catch (SOSHibernateInvalidSessionException ex) {
                throw new DBConnectionRefusedException(ex);
            } catch (Exception ex) {
                throw new DBInvalidDataException(ex);
            }
        }
    }

    public Map<String, String> getDocumentationPathsOfCalendar(String schedulerId) throws DBConnectionRefusedException, DBInvalidDataException {
        Set<String> types = new HashSet<String>();
        types.add(JobSchedulerObjectType.WORKINGDAYSCALENDAR.name());
        types.add(JobSchedulerObjectType.NONWORKINGDAYSCALENDAR.name());
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(DocumentationOfObject.class.getName()).append("(d.path, du.path) from ");
            sql.append(DBITEM_DOCUMENTATION).append(" d, ").append(DBITEM_DOCUMENTATION_USAGE).append(" du");
            sql.append(" where d.id = du.documentationId");
            sql.append(" and du.schedulerId = :schedulerId");
            sql.append(" and du.objectType in (:objectType)");
            Query<DocumentationOfObject> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameterList("objectType", types);
            List<DocumentationOfObject> result = getSession().getResultList(query);
            if (result == null) {
                return new HashMap<String, String>();
            }
            return result.stream().collect(Collectors.toMap(DocumentationOfObject::getObjPath, DocumentationOfObject::getDocPath));
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Set<Tree> getFoldersByFolder(String schedulerId, String folderName) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select directory from ").append(DBITEM_DOCUMENTATION);
            sql.append(" where schedulerId = :schedulerId");
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                sql.append(" and ( directory = :folderName or directory like :likeFolderName )");
            }
            sql.append(" group by directory");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            if (folderName != null && !folderName.isEmpty() && !folderName.equals("/")) {
                query.setParameter("folderName", folderName);
                query.setParameter("likeFolderName", MatchMode.START.toMatchString(folderName + "/"));
            }
            List<String> result = getSession().getResultList(query);
            if (result != null && !result.isEmpty()) {
                return result.stream().map(s -> {
                    Tree tree = new Tree();
                    tree.setPath(s);
                    return tree;
                }).collect(Collectors.toSet());
            }
            return null;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemDocumentationUsage> getDocumentationUsages(String schedulerId, Long documentationId) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(DBITEM_DOCUMENTATION_USAGE);
            sql.append(" where schedulerId = :schedulerId");
            sql.append(" and documentationId = :documentationId");
            Query<DBItemDocumentationUsage> query = getSession().createQuery(sql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("documentationId", documentationId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<JobSchedulerObject> getDocumentationUsages(Long instanceId, String schedulerId, String docPath) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("select new ").append(ObjectOfDocumentation.class.getName()).append("(du.path, du.objectType) from ");
            hql.append(DBITEM_DOCUMENTATION_USAGE).append(" du, ");
            hql.append(DBITEM_DOCUMENTATION).append(" d");
            hql.append(" where du.documentationId = d.id");
            hql.append(" and d.schedulerId = :schedulerId");
            hql.append(" and d.path = :path");

            Query<ObjectOfDocumentation> query = getSession().createQuery(hql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", docPath);
            List<ObjectOfDocumentation> result = getSession().getResultList(query);
            if (result == null || result.isEmpty()) {
                return null;
            }
            Map<JobSchedulerObjectType, List<String>> dbUsages = result.stream().collect(Collectors.groupingBy(ObjectOfDocumentation::getType,
                    Collectors.mapping(ObjectOfDocumentation::getPath, Collectors.toList())));
            String hqlStr = "select name from %s where instanceId = :instanceId and name in (:paths)";
            List<JobSchedulerObject> jsObjs = new ArrayList<JobSchedulerObject>();
            for (Entry<JobSchedulerObjectType, List<String>> entry : dbUsages.entrySet()) {
                String sql = null;
                switch (entry.getKey()) {
                case JOB:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_JOBS);
                    break;
                case JOBCHAIN:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_JOB_CHAINS);
                    break;
                case ORDER:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_ORDERS);
                    break;
                case NONWORKINGDAYSCALENDAR:
                case WORKINGDAYSCALENDAR:
                    sql = String.format("select name from %s where schedulerId = :schedulerId and name in (:paths)", DBITEM_CLUSTER_CALENDARS);
                    break;
                case PROCESSCLASS:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_PROCESS_CLASSES);
                    break;
                case LOCK:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_LOCKS);
                    break;
                case SCHEDULE:
                    sql = String.format(hqlStr, DBITEM_INVENTORY_SCHEDULES);
                    break;
                default:
                    break;
                }
                if (sql != null) {
                    Query<String> query2 = getSession().createQuery(sql);
                    if (entry.getKey() == JobSchedulerObjectType.NONWORKINGDAYSCALENDAR || entry.getKey() == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                        query2.setParameter("schedulerId", schedulerId);
                    } else {
                        query2.setParameter("instanceId", instanceId);
                    }
                    query2.setParameterList("paths", entry.getValue());
                    List<String> paths = getSession().getResultList(query2);
                    if (paths != null && !paths.isEmpty()) {
                        for (String path : paths) {
                            JobSchedulerObject jsObj = new JobSchedulerObject();
                            jsObj.setPath(path);
                            jsObj.setType(entry.getKey());
                            jsObjs.add(jsObj);
                        }
                    }
                }
            }
            return jsObjs;
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemDocumentationUsage getDocumentationUsageForAssignment(String schedulerId, String path, String objectType)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("from ").append(DBITEM_DOCUMENTATION_USAGE);
            hql.append(" where schedulerId = :schedulerId");
            hql.append(" and path = :path");
            hql.append(" and objectType = :objectType");
            Query<DBItemDocumentationUsage> query = getSession().createQuery(hql.toString());
            query.setParameter("schedulerId", schedulerId);
            query.setParameter("path", path);
            query.setParameter("objectType", objectType);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Map<String, List<JobSchedulerObject>> getDocumentationUsages(String schedulerId, Collection<String> paths)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("select new ").append(DocumentationOfObject.class.getName()).append("(d.path, du.path, du.objectType) from ");
            hql.append(DBITEM_DOCUMENTATION_USAGE).append(" du, ");
            hql.append(DBITEM_DOCUMENTATION).append(" d");
            hql.append(" where du.documentationId = d.id");
            if (schedulerId != null && !schedulerId.isEmpty()) {
                hql.append(" and d.schedulerId = :schedulerId");
            }
            if (paths != null && !paths.isEmpty()) {
                hql.append(" and d.path in (:paths)");
            }

            Query<DocumentationOfObject> query = getSession().createQuery(hql.toString());
            if (schedulerId != null && !schedulerId.isEmpty()) {
                query.setParameter("schedulerId", schedulerId);
            }
            if (paths != null && !paths.isEmpty()) {
                query.setParameterList("paths", paths);
            }
            List<DocumentationOfObject> result = getSession().getResultList(query);
            if (result == null || result.isEmpty()) {
                return new HashMap<String, List<JobSchedulerObject>>();
            }
            return result.stream().collect(Collectors.groupingBy(DocumentationOfObject::getDocPath, Collectors.mapping(
                    DocumentationOfObject::getDocUsage, Collectors.toList())));
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}
