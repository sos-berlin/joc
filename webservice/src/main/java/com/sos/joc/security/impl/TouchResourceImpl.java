package com.sos.joc.security.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.security.resource.ITouchResource;

@Path("touch")
public class TouchResourceImpl extends JOCResourceImpl implements ITouchResource {

    private static final String API_CALL = "./touch";

    @Override
    public JOCDefaultResponse postTouch(String accessToken) throws Exception {
        try {
            initLogging(API_CALL, null);
            JOCDefaultResponse jocDefaultResponse = init(accessToken);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                if (!jobschedulerUser.resetTimeOut()) {
                    return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
                }
            } catch (org.apache.shiro.session.InvalidSessionException e) {
                return JOCDefaultResponse.responseStatus440(JOCDefaultResponse.getError401Schema(jobschedulerUser, e.getMessage()));
            }
            return JOCDefaultResponse.responseStatusJSOk(null);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
