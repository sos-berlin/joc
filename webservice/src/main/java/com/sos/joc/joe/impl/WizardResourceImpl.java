package com.sos.joc.joe.impl;

import java.io.IOException;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.IWizardResource;
import com.sos.joc.model.joe.wizard.Job;
import com.sos.joc.model.joe.wizard.JobWizardFilter;
import com.sos.joc.model.joe.wizard.Jobs;
import com.sos.joc.model.joe.wizard.JobsWizardFilter;
import com.sos.joc.model.joe.wizard.Param;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

@Path("joe/wizard")
public class WizardResourceImpl extends JOCResourceImpl implements IWizardResource {

    private static final String API_CALL_JOBS = "./joe/wizard/jobs";
    private static final String API_CALL_JOB = "./joe/wizard/job";
    private static final String XSL_FILE = "scheduler_job_documentation_v1.1.xsl";
    private static final Logger LOGGER = LoggerFactory.getLogger(WizardResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobs(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobsWizardFilter.class);
            JobsWizardFilter body = Globals.objectMapper.readValue(filterBytes, JobsWizardFilter.class);
            
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
                SAXReader reader = XMLBuilder.createSaxReader();

                List<Job> jobList = new ArrayList<Job>();
                for (DBItemDocumentation jitlDoc : jitlDocs) {
                    if (!jitlDoc.getName().endsWith(".xml")) {
                        continue;
                    }
                    if (jitlDoc.getName().endsWith(".languages.xml")) {
                        continue;
                    }
                    Document doc;
                    try {
                        doc = reader.read(new StringReader(jitlDoc.getContent()));
                    } catch (DocumentException e) {
                        Throwable nested = e.getNestedException();
                        if (nested != null && SAXParseException.class.isInstance(nested)) {
                            // On Apache, this should be thrown when disallowing DOCTYPE
                            throw new SAXException("A DOCTYPE was passed into the XML document", e);
                        } else if (nested != null && IOException.class.isInstance(nested)) {
                            // XXE that points to a file that doesn't exist
                            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
                        } else {
                            throw e;
                        }
                    }
                    if (doc == null) {
                        continue;
                    }
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
                    job.setDocPath(jitlDoc.getPath());
                    job.setDocName(jobNode.attributeValue("name"));
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
    public JOCDefaultResponse postJob(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobWizardFilter.class);
            JobWizardFilter body = Globals.objectMapper.readValue(filterBytes, JobWizardFilter.class);
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JOB, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("docPath", body.getDocPath());
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_JOB);
            DocumentationDBLayer docDbLayer = new DocumentationDBLayer(sosHibernateSession);
            DBItemDocumentation jitlDoc = docDbLayer.getDocumentation(body.getJobschedulerId(), normalizePath(body.getDocPath()));
            if (jitlDoc == null) {
                throw new DBMissingDataException(String.format("The documentation '%s' is missing", body.getDocPath()));
            }

            Map<String, String> uris = new HashMap<String, String>();
            uris.put("jobdoc", "http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1");

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(getClass().getResourceAsStream("/" + XSL_FILE)));
            transformer.setParameter("lang", "en");

            XPath xpathParam = DocumentHelper.createXPath(
                    "/jobdoc:description/jobdoc:configuration/jobdoc:params[not(@id) or @id='job_parameter']/jobdoc:param");
            xpathParam.setNamespaceURIs(uris);
            XPath xpathNote = DocumentHelper.createXPath("jobdoc:note[@language='en']");
            xpathNote.setNamespaceURIs(uris);

            Document doc = XMLBuilder.parse(jitlDoc.getContent());
            Element jobNode = doc.getRootElement().element("job");

            Job job = new Job();
            job.setDeliveryDate(Date.from(Instant.now()));
            job.setJavaClass(jobNode.element("script").attributeValue("java_class"));
            job.setDocName(jobNode.attributeValue("name"));
            job.setDocPath(jitlDoc.getPath());
            job.setTitle(jobNode.attributeValue("title"));
            List<Param> params = new ArrayList<Param>();
            
            for (Object paramObj : xpathParam.selectNodes(doc)) {
                Element paramElem = (Element) paramObj;
                Param param = new Param();
                param.setDefaultValue(paramElem.attributeValue("default_value"));
                param.setDescription(getDescription(transformer, xpathNote.selectSingleNode(paramElem)));
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

    private String transform(Transformer transformer, Document doc) throws TransformerException {
        final StringWriter writer = new StringWriter();
        DocumentSource source = new DocumentSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
    
    private String getDescription(Transformer transformer, Node note) {
        String paramDoc = null;
        try {
            if (note != null) {
                paramDoc = transform(transformer, XMLBuilder.parse(note.asXML()));
                if (paramDoc != null) {
                    paramDoc = paramDoc.replaceFirst("^\\s*<\\?xml[^\\?]+\\?>", "").replaceFirst("^\\s*<!DOCTYPE[^>]+>\\s*", "").replaceAll(
                            " xmlns=\"http://www.w3.org/1999/xhtml\"", "").replaceAll("\\s+", " ").replaceAll("> <", "><");
                    paramDoc = "<div class=\"jitl-job-param\">" + paramDoc + "</div>";
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
        return paramDoc;
    }

}
