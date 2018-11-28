package com.sos.joc.documentations.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentations.resource.IDocumentationsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.docu.Documentation;
import com.sos.joc.model.docu.DocumentationsFilter;

@Path("/documentations")
public class DocumentationsResourceImpl extends JOCResourceImpl implements IDocumentationsResource{

    private static final String API_CALL = "/documentations";

    @Override
    public JOCDefaultResponse postDocumentations(String xAccessToken, DocumentationsFilter filter) throws Exception {
        // TODO: permissions
        JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), true);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
        SOSHibernateSession connection = null;
        List<String> documentations = filter.getDocumentations();
        List<Folder> folders = filter.getFolders();
        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            List<DBItemDocumentation> dbDocs = new ArrayList<DBItemDocumentation>();
            if (documentations != null && !documentations.isEmpty()) {
                for (String documentation : documentations) {
                    String folder = Paths.get(documentation).getParent().toString().replace('\\', '/');
                    dbDocs.addAll(dbLayer.getDocumentation(filter.getJobschedulerId(), folder));
                }
            } else if (folders != null) {
                for (Folder folder : folders) {
                    dbDocs.addAll(dbLayer.getDocumentation(filter.getJobschedulerId(), folder.getFolder(), folder.getRecursive()));
                }
            }
            return JOCDefaultResponse.responseStatus200(mapDbItemsToDocumentations(dbDocs));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
    
    private List<Documentation> mapDbItemsToDocumentations(List<DBItemDocumentation> dbDocs) {
        List<Documentation> docs = new ArrayList<Documentation>();
        for(DBItemDocumentation dbDoc : dbDocs) {
            Documentation doc = new Documentation();
            doc.setId(dbDoc.getId());
            doc.setJobschedulerId(dbDoc.getSchedulerId());
            doc.setName(dbDoc.getName());
            doc.setPath(Paths.get(dbDoc.getDirectory(), dbDoc.getName()).toString().replace('\\', '/'));
            doc.setType(dbDoc.getType());
            doc.setModified(dbDoc.getModified());
            docs.add(doc);
        }
        return docs;
    }

}
