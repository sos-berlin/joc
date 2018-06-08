package com.sos.joc.security.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.security.resource.ITouchResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("touch")
public class TouchResourceImpl extends JOCResourceImpl implements ITouchResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(TouchResourceImpl.class);
    private static final String API_CALL = "./touch";

    @Override
    public JOCDefaultResponse postTouch(String xAccessToken, String accessToken) throws Exception {
        return postTouch(getAccessToken(xAccessToken, accessToken));
    }

    public JOCDefaultResponse postTouch(String accessToken) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, accessToken);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {
                 jobschedulerUser.resetTimeOut();
            } catch (org.apache.shiro.session.InvalidSessionException e) {
                LOGGER.info(e.getMessage());
            }
            return JOCDefaultResponse.responseStatusJSOk(null);
        }catch (DBConnectionRefusedException e) {
        	LOGGER.info(e.getMessage());
        	return JOCDefaultResponse.responseStatusJSOk(null);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
