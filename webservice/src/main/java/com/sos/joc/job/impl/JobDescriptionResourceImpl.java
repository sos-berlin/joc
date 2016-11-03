package com.sos.joc.job.impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobDescriptionResource;
import com.sos.joc.model.job.JobFilter;
import com.sos.scheduler.model.commands.JSCmdShowJob;

@Path("job")
public class JobDescriptionResourceImpl extends JOCResourceImpl implements IJobDescriptionResource {

    private static final String API_CALL = "./job/description";
    private static final String XSL_FILE = "scheduler_job_documentation_v1.1.xsl";

    @Override
    public JOCDefaultResponse getJobDescription(String accessToken, String jobschedulerId, String job) throws Exception {
        JobFilter jobFilter = new JobFilter();
        
        try {
            jobFilter.setJob(job);
            jobFilter.setJobschedulerId(jobschedulerId);
            initLogging(API_CALL, jobFilter);
            
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerId, getPermissons(accessToken).getJob().getView()
                    .isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            checkRequiredParameter("jobschedulerId", jobFilter.getJobschedulerId());
            checkRequiredParameter("job", jobFilter.getJob());
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(createJobDescriptionCommand(jobFilter.getJob()), accessToken);
            String description = jocXmlCommand.getSosxml().selectSingleNodeValue("spooler/answer/job/description");
            if (description == null) {
                throw new JobSchedulerBadRequestException(String.format("Description of %1$s could not determine", jobFilter.getJob()));
            }
            description = description.trim();
            
            if (Pattern.compile("<\\?xml-stylesheet [^>]* href\\s*=\\s*\""+Pattern.quote(XSL_FILE)+"\"[^>]*\\?>").matcher(description).find()) {
                //JITL-Job description
                description = ConfigurationUtils.transformXmlToHtml(description, getClass().getResourceAsStream("/"+XSL_FILE));
            }
            return JOCDefaultResponse.responseHtmlStatus200(description);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }
    
    public String createJobDescriptionCommand(String job) {
        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        showJob.setJob(normalizePath(job));
        return showJob.toXMLString();
    }

}
