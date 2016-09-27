package com.sos.joc.task.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.task.resource.ITaskLogHtmlResource;

@Path("task")
public class TaskLogHtmlResourceImpl extends JOCResourceImpl implements ITaskLogHtmlResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogHtmlResourceImpl.class);

    @Override
    public JOCDefaultResponse getTaskLogHtml(String accessToken, String jobschedulerId) throws Exception {
        LOGGER.debug("init task/log/html");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getJob().getView().isTaskLog());
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
