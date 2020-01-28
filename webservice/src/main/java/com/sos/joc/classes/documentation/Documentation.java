package com.sos.joc.classes.documentation;

import java.time.Instant;
import java.util.Date;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.common.JobSchedulerObjectType;

public class Documentation {

    public static String getDocumentationPath(String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType, String apiCall)
            throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            return getDocumentationPath(connection, jobschedulerId, jsObjectPath, objType);
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static String getDocumentationPath(SOSHibernateSession connection, String jobschedulerId, String jsObjectPath,
            JobSchedulerObjectType objType) throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, DBOpenSessionException {
        DBItemDocumentationUsage dbDocUsage = getDocumentationUsage(connection, jobschedulerId, jsObjectPath, objType);
        if (dbDocUsage == null) {
            return null;
        }
        return dbDocUsage.getPath();
    }

    public static DBItemDocumentationUsage getDocumentationUsage(String jobschedulerId, String jsObjectPath, String docPath,
            JobSchedulerObjectType objType, String apiCall) throws JocMissingRequiredParameterException, JocConfigurationException,
            DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            return getDocumentationUsage(connection, jobschedulerId, jsObjectPath, objType);
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static DBItemDocumentationUsage getDocumentationUsage(SOSHibernateSession connection, String jobschedulerId, String jsObjectPath,
            JobSchedulerObjectType objType) throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, DBOpenSessionException {
        DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
        String type = getObjectType(connection, jobschedulerId, jsObjectPath, objType);
        return dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
    }
    
    public static DBItemDocumentationUsage getDocumentationUsage(DocumentationDBLayer dbLayer, String jobschedulerId, String jsObjectPath,
            JobSchedulerObjectType objType) throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, DBOpenSessionException {
        String type = getObjectType(dbLayer.getSession(), jobschedulerId, jsObjectPath, objType);
        return dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
    }

    public static void assignDocu(String jobschedulerId, String jsObjectPath, String docPath, JobSchedulerObjectType objType, String apiCall)
            throws JocMissingRequiredParameterException, JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            SOSHibernateException, DBOpenSessionException {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            assignDocu(connection, jobschedulerId, jsObjectPath, docPath, objType);
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static void assignDocu(SOSHibernateSession connection, String jobschedulerId, String jsObjectPath, String docPath,
            JobSchedulerObjectType objType) throws JocMissingRequiredParameterException, JocConfigurationException, DBConnectionRefusedException,
            DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
        assignDocu(dbLayer, jobschedulerId, jsObjectPath, docPath, objType);
    }
    
    public static void assignDocu(DocumentationDBLayer dbLayer, String jobschedulerId, String jsObjectPath, String docPath,
            JobSchedulerObjectType objType) throws JocMissingRequiredParameterException, JocConfigurationException, DBConnectionRefusedException,
            DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        if (docPath == null || docPath.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", "documentation"));
        }
        String type = getObjectType(dbLayer.getSession(), jobschedulerId, jsObjectPath, objType);
        DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
        DBItemDocumentation dbDoc = dbLayer.getDocumentation(jobschedulerId, docPath);
        if (dbDocUsage != null) {
            dbDocUsage.setDocumentationId(dbDoc.getId());
            dbDocUsage.setModified(Date.from(Instant.now()));
            dbLayer.getSession().update(dbDocUsage);
        } else {
            DBItemDocumentationUsage newUsage = new DBItemDocumentationUsage();
            newUsage.setSchedulerId(jobschedulerId);
            newUsage.setPath(jsObjectPath);
            newUsage.setObjectType(type);
            newUsage.setDocumentationId(dbDoc.getId());
            newUsage.setCreated(Date.from(Instant.now()));
            newUsage.setModified(newUsage.getCreated());
            dbLayer.getSession().save(newUsage);
        }
    }

    public static void unassignDocu(String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType, String apiCall)
            throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            unassignDocu(connection, jobschedulerId, jsObjectPath, objType);
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static void unassignDocu(SOSHibernateSession connection, String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType)
            throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
        unassignDocu(dbLayer, jobschedulerId, jsObjectPath, objType);
    }
    
    public static void unassignDocu(DocumentationDBLayer dbLayer, String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType)
            throws JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException, DBOpenSessionException {
        String type = getObjectType(dbLayer.getSession(), jobschedulerId, jsObjectPath, objType);
        DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
        if (dbDocUsage != null) {
            dbLayer.getSession().delete(dbDocUsage);
        }
    }

    private static String getObjectType(SOSHibernateSession connection, String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType)
            throws DBConnectionRefusedException, DBInvalidDataException {
        String type = objType.value();
        if (objType == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
            CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
            DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
            if (CalendarType.NON_WORKING_DAYS.value().equals(dbCalendar.getType())) {
                type = JobSchedulerObjectType.NONWORKINGDAYSCALENDAR.value();
            }
        }
        return type;
    }
}
