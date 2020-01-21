package com.sos.joc.documentations.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.DeleteDocumentationAudit;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsDeleteResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.docu.DocumentationsFilter;
import com.sos.schema.JsonValidator;

@Path("documentations")
public class DocumentationsDeleteResourceImpl extends JOCResourceImpl implements IDocumentationsDeleteResource {

    private static final String API_CALL = "./documentations/delete";

    @Override
    public JOCDefaultResponse deleteDocumentations(String xAccessToken, byte[] filterBytes) {
        
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(filterBytes, DocumentationsFilter.class);
            DocumentationsFilter filter = Globals.objectMapper.readValue(filterBytes, DocumentationsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getDocumentation().isDelete());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            checkRequiredParameter("documentations", filter.getDocumentations());
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = dbLayer.getDocumentations(filter.getJobschedulerId(), filter.getDocumentations());
            for (DBItemDocumentation dbDoc : docs) {
                DeleteDocumentationAudit deleteAudit = new DeleteDocumentationAudit(filter, dbDoc.getPath(), dbDoc.getDirectory());
                logAuditMessage(deleteAudit);
                List<DBItemDocumentationUsage> dbUsages = dbLayer.getDocumentationUsages(filter.getJobschedulerId(), dbDoc.getId());
                if (dbUsages != null && !dbUsages.isEmpty()) {
                    for (DBItemDocumentationUsage dbUsage : dbUsages) {
                        connection.delete(dbUsage);
                    }
                }
                if (dbDoc.getImageId() != null) {
                    DBItemDocumentationImage dbImage = connection.get(DBItemDocumentationImage.class, dbDoc.getImageId());
                    if (dbImage != null) {
                        connection.delete(dbImage);
                    }
                }
                connection.delete(dbDoc);
                storeAuditLogEntry(deleteAudit);
            }
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
