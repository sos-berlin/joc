package com.sos.joc.lock.impl;

import java.time.Instant;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.AssignmentLockDocuAudit;
import com.sos.joc.classes.documentation.Documentation;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.lock.resource.ILockResourceDocumentation;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.docu.DocumentationShowFilter;
import com.sos.joc.model.lock.LockDocuFilter;
import com.sos.schema.JsonValidator;

@Path("lock")
public class LockResourceDocumentationImpl extends JOCResourceImpl implements ILockResourceDocumentation {

    private static final String API_CALL = "./lock/documentation";
    private static final String API_CALL_ASSIGN = API_CALL + "/assign";
    private static final String API_CALL_UNASSIGN = API_CALL + "/unassign";

    @Override
    public JOCDefaultResponse postDocumentation(String xAccessToken, String accessToken, String jobschedulerId, String path) {
        return postDocumentation(getAccessToken(xAccessToken, accessToken), jobschedulerId, path);
    }

    public JOCDefaultResponse postDocumentation(String xAccessToken, String jobschedulerId, String path) {
        SOSHibernateSession connection = null;
        try {
            //String json = String.format("{\"jobschedulerId\": \"%s\", \"path\": \"%s\"}", jobschedulerId, path);
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("jobschedulerId", jobschedulerId);
            builder.add("path", path);
            String json = builder.build().toString();
            JsonValidator.validateFailFast(json.getBytes(), DocumentationShowFilter.class);
            DocumentationShowFilter documentationFilter = Globals.objectMapper.readValue(json, DocumentationShowFilter.class);
            documentationFilter.setType(JobSchedulerObjectType.LOCK);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobschedulerId, xAccessToken).getLock().getView().isDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobschedulerId);
            checkRequiredParameter("lock", path);

            documentationFilter.setPath(normalizePath(documentationFilter.getPath()));
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String docPath = dbLayer.getDocumentationPath(documentationFilter);
            if (docPath == null) {
                throw new DBMissingDataException("The documentation couldn't determine");
            }

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>%n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='../documentation/%s/%s%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    documentationFilter.getJobschedulerId(), xAccessToken, JOCJsonCommand.urlEncodedPath(docPath));

            return JOCDefaultResponse.responseHtmlStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse assignDocu(String xAccessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, LockDocuFilter.class);
            LockDocuFilter filter = Globals.objectMapper.readValue(filterBytes, LockDocuFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ASSIGN, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(
                    filter.getJobschedulerId(), xAccessToken).getLock().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentLockDocuAudit assignAudit = new AssignmentLockDocuAudit(filter);
            logAuditMessage(assignAudit);
            Documentation.assignDocu(filter.getJobschedulerId(), normalizePath(filter.getLock()), filter.getDocumentation(),
                    JobSchedulerObjectType.LOCK, API_CALL_ASSIGN);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }

    @Override
    public JOCDefaultResponse unassignDocu(String xAccessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, LockDocuFilter.class);
            LockDocuFilter filter = Globals.objectMapper.readValue(filterBytes, LockDocuFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_UNASSIGN, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(
                    filter.getJobschedulerId(), xAccessToken).getLock().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentLockDocuAudit unassignAudit = new AssignmentLockDocuAudit(filter);
            logAuditMessage(unassignAudit);
            Documentation.unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getLock()), JobSchedulerObjectType.LOCK, API_CALL_UNASSIGN);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}