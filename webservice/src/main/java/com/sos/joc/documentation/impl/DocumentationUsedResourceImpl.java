package com.sos.joc.documentation.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationUsedResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.docu.DocumentationFilter;
import com.sos.joc.model.docu.UsedBy;
import com.sos.schema.JsonValidator;

@Path("documentation")
public class DocumentationUsedResourceImpl extends JOCResourceImpl implements IDocumentationUsedResource {

    private static final String API_CALL = "./documentation/used";

    @Override
    public JOCDefaultResponse postDocumentationsUsed(String xAccessToken, byte[] filterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(filterBytes, DocumentationFilter.class);
            DocumentationFilter filter = Globals.objectMapper.readValue(filterBytes, DocumentationFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getDocumentation().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", filter.getJobschedulerId());
            checkRequiredParameter("path", filter.getDocumentation());
            UsedBy usedBy = new UsedBy();
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            usedBy.setObjects(dbLayer.getDocumentationUsages(dbItemInventoryInstance.getId(), filter.getJobschedulerId(), normalizePath(filter.getDocumentation())));
            usedBy.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(usedBy);
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
