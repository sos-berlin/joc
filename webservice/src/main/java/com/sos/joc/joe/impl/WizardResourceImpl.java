package com.sos.joc.joe.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.IWizardResource;
import com.sos.joc.model.joe.wizard.Job;
import com.sos.joc.model.joe.wizard.JobFilter;
import com.sos.joc.model.joe.wizard.Jobs;
import com.sos.joc.model.joe.wizard.JobsFilter;
import com.sos.joc.model.joe.wizard.Param;

@Path("joe/wizzard")
public class WizardResourceImpl extends JOCResourceImpl implements IWizardResource {

    private static final String API_CALL_JOBS = "./joe/wizzard/jobs";
    private static final String API_CALL_JOB = "./joe/wizzard/job";
    private static final String XSL_FILE = "scheduler_job_documentation_v1.1.xsl";
    private Transformer transformer;

    @Override
    public JOCDefaultResponse postJobs(final String accessToken, final JobsFilter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JOBS, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_JOBS);
            DocumentationDBLayer docDbLayer = new DocumentationDBLayer(sosHibernateSession);
            List<DBItemDocumentation> jitlDocs = docDbLayer.getDocumentations(body.getJobschedulerId(), "/sos/jitl-jobs");
            Jobs jobs = new Jobs();
            if (jitlDocs != null) {
                SAXReader reader = new SAXReader();
                reader.setValidation(false);
                reader.setStripWhitespaceText(true);
                reader.setIgnoreComments(true);
                
                List<Job> jobList = new ArrayList<Job>();
                for (DBItemDocumentation jitlDoc : jitlDocs) {
                    if (!jitlDoc.getName().endsWith(".xml")) {
                        continue;
                    }
                    if (jitlDoc.getName().endsWith(".languages.xml")) {
                        continue;
                    }
                    Document doc = reader.read(new StringReader(jitlDoc.getContent()));
                    Element jobNode = doc.getRootElement().element("job");
                    
                    if (jobNode == null) {
                        continue; 
                    }
                    if (body.getIsOrderJob() != null && body.getIsOrderJob() && "no".equals(jobNode.attributeValue("order"))) {
                        continue;
                    }
                    if (body.getIsOrderJob() != null && !body.getIsOrderJob() && "yes".equals(jobNode.attributeValue("order"))) {
                        continue;
                    }
                    
                    Job job = new Job();
                    job.setJitlPath(jitlDoc.getPath());
                    job.setJitlName(jobNode.attributeValue("name"));
                    job.setTitle(jobNode.attributeValue("title"));
                    job.setJavaClass(jobNode.element("script").attributeValue("java_class"));
                    jobList.add(job);
                }
                jobs.setJobs(jobList);
            }
            jobs.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(jobs);
        } catch (JoeFolderAlreadyLockedException e) {
            return JOEHelper.get434Response(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse postJob(String accessToken, JobFilter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JOB, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jitlName", body.getJitlPath());
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_JOB);
            DocumentationDBLayer docDbLayer = new DocumentationDBLayer(sosHibernateSession);
            DBItemDocumentation jitlDoc = docDbLayer.getDocumentation(body.getJobschedulerId(), normalizePath(body.getJitlPath()));
            
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            reader.setStripWhitespaceText(true);
            reader.setIgnoreComments(true);
            
            Map<String, String> uris = new HashMap<>();
            uris.put("jobdoc", "http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1");
            
            loadStyleSheet();
            
            Document doc = reader.read(new StringReader(jitlDoc.getContent()));
            Element jobNode = doc.getRootElement().element("job");
            
            Job job = new Job();
            job.setDeliveryDate(Date.from(Instant.now()));
            job.setJavaClass(jobNode.element("script").attributeValue("java_class"));
            job.setJitlName(jobNode.attributeValue("name"));
            job.setJitlPath(jitlDoc.getPath());
            job.setTitle(jobNode.attributeValue("title"));
            List<Param> params = new ArrayList<Param>();
            
            XPath xpathObj = DocumentHelper.createXPath("/jobdoc:description/jobdoc:configuration/jobdoc:params[@id='job_parameter']/jobdoc:param");
            xpathObj.setNamespaceURIs(uris);
            for (Object paramObj : xpathObj.selectNodes(doc)) {
                Element paramElem = (Element) paramObj;
                //String paramDoc = transform(DocumentHelper.parseText(paramElem.asXML()));
                Param param = new Param();
                param.setDefaultValue(paramElem.attributeValue("default_value"));
                //param.setDescription(paramDoc);
                param.setName(paramElem.attributeValue("name"));
                param.setRequired("true".equals(paramElem.attributeValue("required")));
                params.add(param);
            }
            
            job.setParams(params);

            return JOCDefaultResponse.responseStatus200(job);
        } catch (JoeFolderAlreadyLockedException e) {
            return JOEHelper.get434Response(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private void loadStyleSheet() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        transformer = factory.newTransformer( new StreamSource(getClass().getResourceAsStream("/" + XSL_FILE)) );
    }
    
    private String transform(Document doc) throws TransformerException {
        final StringWriter writer = new StringWriter();
        DocumentSource source = new DocumentSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }

}
