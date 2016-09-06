package com.sos.joc.task.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.task.resource.ITaskLogHtmlResource;

@Path("task")
public class TaskLogHtmlResourceImpl extends JOCResourceImpl implements ITaskLogHtmlResource {
    private static final Logger LOGGER = Logger.getLogger(TaskLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getTaskLogHtml(String accessToken, String jobschedulerId) throws Exception {
        LOGGER.debug("init OrderHistory");

        JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getJob().getView().isTaskLog());
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
