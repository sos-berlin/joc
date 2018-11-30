package com.sos.joc.documentations.impl;

import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationImage;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsDeleteResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.docu.DocumentationsFilter;

@Path("/documentations")
public class DocumentationsDeleteResourceImpl extends JOCResourceImpl implements IDocumentationsDeleteResource {

    private static final String API_CALL = "/documentations/delete";

    @Override
    public JOCDefaultResponse deleteDocumentations(String xAccessToken, DocumentationsFilter filter) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                .getJobschedulerId(), xAccessToken).getDocumentation().isDelete());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        SOSHibernateSession connection = null;
        try {
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            checkRequiredParameter("documentations", filter.getDocumentations());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> docs = new ArrayList<DBItemDocumentation>();
            for (String documentation : filter.getDocumentations()) {
                String folder = Paths.get(documentation).getParent().toString().replace('\\', '/');
                docs.addAll(dbLayer.getDocumentations(filter.getJobschedulerId(), folder));
            }
            for (DBItemDocumentation dbDoc : docs) {
                List<DBItemDocumentationUsage> dbUsages = dbLayer.getDocumentationUsage(filter.getJobschedulerId(), dbDoc.getId());
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
