package com.sos.joc.classes.documentation;

import java.sql.Date;
import java.time.Instant;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.model.common.JobSchedulerObjectType;

public class Documentation {

    public static void assignDocu(String jobschedulerId, String jsObjectPath, String docPath, JobSchedulerObjectType objType, String apiCall)
            throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String type = objType.name();
            if (objType == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
                DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
                if (!type.equals(dbCalendar.getType())) {
                    type = dbCalendar.getType();
                }
            }
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
            DBItemDocumentation dbDoc = dbLayer.getDocumentation(jobschedulerId, docPath);
            if (dbDocUsage != null) {
                dbDocUsage.setDocumentationId(dbDoc.getId());
                dbDocUsage.setModified(Date.from(Instant.now()));
                connection.update(dbDocUsage);
            } else {
                DBItemDocumentationUsage newUsage = new DBItemDocumentationUsage();
                newUsage.setSchedulerId(jobschedulerId);
                newUsage.setPath(jsObjectPath);
                newUsage.setObjectType(type);
                newUsage.setDocumentationId(dbDoc.getId());
                newUsage.setCreated(Date.from(Instant.now()));
                newUsage.setModified(newUsage.getCreated());
                connection.save(newUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static void unassignDocu(String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType, String apiCall) throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String type = objType.name();
            if (objType == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
                DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
                if (!type.equals(dbCalendar.getType())) {
                    type = dbCalendar.getType();
                }
            }
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
            if (dbDocUsage != null) {
                connection.delete(dbDocUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }
}
