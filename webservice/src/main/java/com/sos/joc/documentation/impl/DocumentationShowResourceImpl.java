package com.sos.joc.documentation.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.documentation.resource.IDocumentationShowResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.docu.DocumentationShowFilter;

@Path("documentation")
public class DocumentationShowResourceImpl extends JOCResourceImpl implements IDocumentationShowResource{
    
    private static final String API_CALL = "/documentation/show";

    @Override
    public JOCDefaultResponse show(String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            //TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            //TODO Objekt aus DB holen gemäß Filter, html generieren, wenn XML oder Markdown, "links" auflösen
            
            Object entity = null;
            
            return JOCDefaultResponse.responseHtmlStatus200(entity);
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
