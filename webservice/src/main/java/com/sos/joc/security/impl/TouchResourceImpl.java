package com.sos.joc.security.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.security.resource.ITouchResource;

@Path("touch")
public class TouchResourceImpl extends JOCResourceImpl implements ITouchResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TouchResourceImpl.class);
    private static final String API_CALL = "./touch";

    @Override
    public JOCDefaultResponse postTouch(String accessToken) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                if(!jobschedulerUser.resetTimeOut()) {
                    return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
                }
            } catch (org.apache.shiro.session.InvalidSessionException e) {
                return JOCDefaultResponse.responseStatus440(JOCDefaultResponse.getError401Schema(jobschedulerUser, e.getMessage()));
            } 
            return JOCDefaultResponse.responseStatusJSOk(null);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, null));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, null));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
