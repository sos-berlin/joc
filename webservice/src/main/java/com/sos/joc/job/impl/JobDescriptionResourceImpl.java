package com.sos.joc.job.impl;

import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobDescriptionResource;
import com.sos.joc.model.job.JobFilter;
import com.sos.schema.JsonValidator;

@Path("job")
public class JobDescriptionResourceImpl extends JOCResourceImpl implements IJobDescriptionResource {

    private static final String API_CALL = "./job/description";
    private static final String XSL_FILE = "scheduler_job_documentation_v1.1.xsl";

    @Override
    public JOCDefaultResponse getJobDescription(String accessToken, String queryAccessToken, String jobschedulerId, String job) {

        try {
            String jsonStr = String.format("{\"jobschedulerId\": \"%s\", \"job\": \"%s\"}", jobschedulerId, job);
            JsonValidator.validateFailFast(jsonStr.getBytes(), JobFilter.class);
            JobFilter jobFilter = Globals.objectMapper.readValue(jsonStr, JobFilter.class);
            jobFilter.setCompact(null);

            if (accessToken == null) {
                accessToken = queryAccessToken;
            }

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken, jobschedulerId, getPermissonsJocCockpit(jobschedulerId, accessToken)
                    .getJob().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobFilter.getJobschedulerId());
            checkRequiredParameter("job", jobFilter.getJob());
            checkFolderPermissions(jobFilter.getJob());

            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
            jocJsonCommand.setUriBuilderForJobs();
            jocJsonCommand.addJobDescriptionQuery();
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("path", jobFilter.getJob());
            JsonObject json = jocJsonCommand.getJsonObjectFromPostWithRetry(builder.build().toString(), accessToken);
            String description = json.getString("description", "").trim();
            if (description.isEmpty()) {
                throw new JobSchedulerBadRequestException(String.format("%1$s doesn't have a description", jobFilter.getJob()));
            }
            if (Pattern.compile("<\\?xml-stylesheet [^\\?]* href\\s*=\\s*\"" + Pattern.quote(XSL_FILE) + "\"[^\\?]*\\?>").matcher(description)
                    .find()) {
                // JITL-Job description
                description = ConfigurationUtils.transformXmlToHtml(description, getClass().getResourceAsStream("/" + XSL_FILE));
            }
            return JOCDefaultResponse.responseHtmlStatus200(description);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }
}
