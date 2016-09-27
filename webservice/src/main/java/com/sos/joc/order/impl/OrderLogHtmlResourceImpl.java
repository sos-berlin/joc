package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.order.resource.IOrderLogHtmlResource;

@Path("order")
public class OrderLogHtmlResourceImpl extends JOCResourceImpl implements IOrderLogHtmlResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getOrderLogHtml(String accessToken, String jobschedulerId) throws Exception {
        LOGGER.debug("init OrderHistory");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getOrder().getView().isOrderLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String s = "<html><body>myLog</body></html>";
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(s);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
