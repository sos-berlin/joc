package com.sos.joc.documentation.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.documentation.resource.IDocumentationResource;
import com.sos.joc.exceptions.JocException;

@Path("/documentation")
public class DocumentationResourceImpl extends JOCResourceImpl implements IDocumentationResource{
    
    private static final String API_CALL = "./documentation";

    @Override
    public JOCDefaultResponse postDocumentation(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return postDocumentation(getAccessToken(xAccessToken, accessToken), jobschedulerId, path);
    }

    private JOCDefaultResponse postDocumentation(String accessToken, String jobschedulerId, String path) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, jobschedulerId, true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            String s = String.format("{%n  \"jobschedulerId\":\"%s\",%n  \"accessToken\":\"%s\",%n  \"path\":\"%s\"%n}", jobschedulerId, accessToken, path);
            
            return JOCDefaultResponse.responseStatus200(s);
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
