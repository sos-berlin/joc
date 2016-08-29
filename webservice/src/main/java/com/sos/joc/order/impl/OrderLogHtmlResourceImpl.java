package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.order.resource.IOrderLogHtmlResource;

@Path("order")
public class OrderLogHtmlResourceImpl extends JOCResourceImpl implements IOrderLogHtmlResource {
    private static final Logger LOGGER = Logger.getLogger(OrderLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getOrderLogHtml(String accessToken, String jobschedulerId) throws Exception {
        LOGGER.debug("init OrderHistory");

        JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            String s = "<html><body>myLog</body></html>";
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(s);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
