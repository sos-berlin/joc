package com.sos.joc.audit.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.audit.resource.ICommentsResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.audit.Comments;

@Path("audit_log")
public class CommentsResourceImpl extends JOCResourceImpl implements ICommentsResource {

    private static final String API_CALL = "./audit_log/comments";
    
    @Override
    public JOCDefaultResponse postComments(String xAccessToken, String accessToken) throws Exception {
        return postComments(getAccessToken(xAccessToken, accessToken));
    }

    public JOCDefaultResponse postComments(String accessToken) throws Exception {
        
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Comments entity = new Comments();
            entity.setForceCommentsForAuditLog(Globals.auditLogCommentsAreRequired);
            entity.setComments(readCommentsFromJocProperties());
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } 
    }
    
    private List<String> readCommentsFromJocProperties() {
        List<String> commentsList = new ArrayList<String>();
        if (Globals.sosShiroProperties != null) {
            String[] comments = Globals.sosShiroProperties.getProperty("comments", "").split(";");
            for (int i=0; i < comments.length; i++) {
                commentsList.add(comments[i].trim());
            }
        }
        return commentsList;
    }
}
