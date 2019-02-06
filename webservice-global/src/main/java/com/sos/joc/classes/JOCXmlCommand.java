package com.sos.joc.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.exception.SOSNoResponseException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.RuntimeResolver;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.xml.SOSXmlCommand;

import sos.xml.SOSXMLXPath;

public class JOCXmlCommand extends SOSXmlCommand {

    public static final String XML_COMMAND_API_PATH = "/jobscheduler/master/api/command";
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlCommand.class);
    private Date surveyDate;
    private Map<String, NodeList> listOfNodeLists = new HashMap<String, NodeList>();
    private URI uriForJsonCommand;
    private String xmlCommand = null;
    private JOCResourceImpl jocResourceImpl;

    public JOCXmlCommand(String url) {
        super(url + XML_COMMAND_API_PATH);
        setAllowAllHostnameVerifier(!Globals.withHostnameVerification);
        setConnectTimeout(Globals.httpConnectionTimeout);
        setReadTimeout(Globals.httpSocketTimeout);
    }
    
    public JOCXmlCommand(DBItemInventoryInstance dbItemInventoryInstance) {
        super(dbItemInventoryInstance.getUrl() + XML_COMMAND_API_PATH);
        setBasicAuthorization(dbItemInventoryInstance.getAuth());
        setAllowAllHostnameVerifier(!Globals.withHostnameVerification);
        setConnectTimeout(Globals.httpConnectionTimeout);
        setReadTimeout(Globals.httpSocketTimeout);
    }
    
    public JOCXmlCommand(JOCResourceImpl jocResourceImpl) {
        super(jocResourceImpl.getUrl() + XML_COMMAND_API_PATH);
        setBasicAuthorization(jocResourceImpl.getBasicAuthorization());
        setAllowAllHostnameVerifier(!Globals.withHostnameVerification);
        setConnectTimeout(Globals.httpConnectionTimeout);
        setReadTimeout(Globals.httpSocketTimeout);
        this.jocResourceImpl = jocResourceImpl;
    }
    
    public void setJOCResourceImpl(JOCResourceImpl jocResourceImpl) {
        this.jocResourceImpl = jocResourceImpl;
    }
    
    public JOCResourceImpl getJOCResourceImpl() {
        return jocResourceImpl;
    }
    
    public Date getSurveyDate() {
        if (surveyDate == null) {
            try {
                surveyDate = Date.from(getSurveyInstant()); 
            } catch (Exception e) {
                surveyDate = Date.from(Instant.now());
            }
        }
        return surveyDate;
    }
    
    public Instant getSurveyInstant() {
        Instant surveyInstant = null;
        try {
            String surveyDateStr = getSosxml().selectSingleNodeValue("/spooler/answer/@time");
            surveyInstant = JobSchedulerDate.getInstantFromISO8601String(surveyDateStr);
        } catch (Exception e) {
        }
        if (surveyInstant == null) {
            surveyInstant = Instant.now();
        }
        return surveyInstant;
    }
    
    public URI getUriForJsonCommand() {
        return uriForJsonCommand;
    }

    public void setUriForJsonCommand(URI uriForJsonCommand) {
        this.uriForJsonCommand = uriForJsonCommand;
    }
    
    public void createNodeList(String key, String xpath) throws Exception {
        NodeList nodeList = selectNodelist(xpath);
        listOfNodeLists.put(key, nodeList);
    }

    public void createNodeList(String xpath) throws Exception {
        createNodeList("", xpath);
    }

    public NodeList getNodeList(String key) {
        return listOfNodeLists.get(key);
    }

    public NodeList getNodeList() {
        return listOfNodeLists.get("");
    }

    public Element getElementFromList(String key, int i) throws Exception {
        NodeList nodeList = listOfNodeLists.get(key);
        Element element = null;
        if (nodeList != null) {
            Node n = nodeList.item(i);
            if (n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) n;
                Map<String, String> attrs = new HashMap<String, String>();
                if (element != null) {
                    NamedNodeMap map = n.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        attrs.put(map.item(j).getNodeName(), map.item(j).getNodeValue());
                    }
                }
                attributes.put(key, attrs);
            }
        }
        return element;
    }

    public Element getElementFromList(int i) throws Exception {
        return getElementFromList("", i);
    }
    
    public String getAttributeValue(Element elem, String attributeName, String default_) {
        String val = elem.getAttribute(attributeName);
        if (val == null || val.isEmpty()) {
            val = default_;
        }
        return val;
    }
    
    public Boolean getBoolValue(final String value, Boolean default_) {
        if (WebserviceConstants.YES.equalsIgnoreCase(value)) {
            return true;
        } else if (WebserviceConstants.NO.equalsIgnoreCase(value)) {
            return false;
        }
        return default_;
    }
    
    public void throwJobSchedulerError(SOSXMLXPath response) throws JobSchedulerBadRequestException {
        String xPath = "/spooler/answer/ERROR";
        try {
            Element errorElem = null;
            if (response == null) {
                errorElem = (Element) getSosxml().selectSingleNode(xPath);
            } else {
                errorElem = (Element) response.selectSingleNode(xPath);
            }
            if (errorElem != null) {
                JocError err = new JocError(errorElem.getAttribute("code"), errorElem.getAttribute("text"));
                JobSchedulerBadRequestException badRequestException = new JobSchedulerBadRequestException(err);
                badRequestException.setSurveyDate(getSurveyDate());
                if (xmlCommand != null) {
                    badRequestException.addErrorMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand); 
                }
                throw badRequestException;
            }
        } catch (JobSchedulerBadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
    public void throwJobSchedulerError() throws JobSchedulerBadRequestException {
        throwJobSchedulerError(null);
    }
    
//    public String executePost(String xmlCommand) throws JocException {
//        return executePost(xmlCommand, UUID.randomUUID().toString());
//    }
    
    public String executePost(String xmlCommand, String accessToken) throws JocException {
        return executePost(xmlCommand, ResponseStream.TO_SOSXML, accessToken);
    }
    
    public String executePost(String xmlCommand, ResponseStream responseStream, String accessToken) throws JocException {
        this.xmlCommand = xmlCommand;
        try {
            return executeXMLPost(xmlCommand, responseStream, accessToken);
        } catch (SOSNoResponseException e) {
            JobSchedulerNoResponseException ee = new JobSchedulerNoResponseException(e.getCause());
            ee.addErrorMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand);
            throw ee;
        } catch (Exception e) {
            JobSchedulerConnectionRefusedException ee = new JobSchedulerConnectionRefusedException(e.getCause());
            ee.addErrorMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand);
            throw ee;
        }
    }
    
    public String executePostWithRetry(String xmlCommand, String accessToken) throws JocException {
        return executePostWithRetry(xmlCommand, ResponseStream.TO_SOSXML, accessToken);
    }
    
    public String executePostWithRetry(String xmlCommand, ResponseStream responseStream, String accessToken) throws JocException {
        try {
            return executePost(xmlCommand, responseStream, accessToken);
        } catch (JobSchedulerConnectionRefusedException e) {
            String url = null;
            if (jocResourceImpl != null) {
                url = jocResourceImpl.retrySchedulerInstance(); 
            }
            if (url != null) {
                setUrl(url + XML_COMMAND_API_PATH);
                LOGGER.debug("...retry with " + url + XML_COMMAND_API_PATH);
                return executePost(xmlCommand, responseStream, accessToken);
            } else {
                throw e;
            }
        } catch (JocException e) {
            throw e;
        }
    }
    
    public void executePostWithThrowBadRequest(String xmlCommand, String accessToken) throws JocException {
        executePost(xmlCommand, ResponseStream.TO_SOSXML, accessToken);
        throwJobSchedulerError();
    }
    
    public void executePostWithThrowBadRequestAfterRetry(String xmlCommand, String accessToken) throws JocException {
        if (LOGGER.isDebugEnabled()) {
            String s = executePostWithRetry(xmlCommand, ResponseStream.TO_STRING_AND_SOSXML, accessToken);
            LOGGER.debug(s);
        } else {
            executePostWithRetry(xmlCommand, accessToken);
        }
        throwJobSchedulerError();
    }
    
    public Path getLogPath(String xmlCommand, String accessToken, String prefix, boolean withGzipEncoding) throws JocException {
        try {
            return getFilePathResponseFromLog(requestXMLPost(xmlCommand, accessToken),prefix, withGzipEncoding);
        } catch (JobSchedulerNoResponseException | JobSchedulerBadRequestException e) {
            e.addErrorMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand);
            throw e;
        } catch (Exception e) {
            JobSchedulerConnectionRefusedException ee = null;
            if (e.getCause() != null) {
                ee = new JobSchedulerConnectionRefusedException(e.getCause());
            } else {
                ee = new JobSchedulerConnectionRefusedException(e);
            }
            ee.addErrorMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand);
            throw ee;
        }
    }
    
    private Path getFilePathResponseFromLog(HttpURLConnection connection, String prefix, boolean withGzipEncoding) throws JobSchedulerNoResponseException, JobSchedulerBadRequestException {
        Path path = null;
        try {
            if (connection != null) {
                InputStream instream = connection.getInputStream();
                OutputStream out = null;
                if (instream != null) {
                    try {
                        if (prefix == null) {
                            prefix = "sos-download-"; 
                        }
                        path = Files.createTempFile(prefix, null);
                        if (withGzipEncoding) {
                            out = new GZIPOutputStream(Files.newOutputStream(path));
                        } else {
                            out = Files.newOutputStream(path);
                        }
                        LOGGER.info(Instant.now().getEpochSecond()+"");
                        int bufferSize = 4096;
                        boolean logBeginIsFound = false;
                        Pattern logStartPattern = Pattern.compile(".*>\\s*<log [^>]*level\\s*=[^>]*/?>(.*)", Pattern.DOTALL + Pattern.MULTILINE);
                        String logEndPattern = "(\\s*)(?:</log>)?(?:\\s*<[^>]+/?>)*\\s*</(?:order|task)>\\s*</answer>\\s*</spooler>\\s*$";
                        Matcher m = null;
                        byte[] buffer1 = new byte[bufferSize];
                        byte[] buffer2 = new byte[bufferSize];
                        int length1;
                        int length2;
                        StringBuilder str = new StringBuilder();
                        String str1 = null;
                        String str2 = null;
                        while ((length1 = instream.read(buffer1)) > 0) {
                            if (logBeginIsFound) {
                                if (instream.available() < bufferSize) {
                                    length2 = instream.read(buffer2);
                                    str1 = new String(buffer1, 0, length1, "UTF-8");
                                    if (length2 > 0) {
                                        str2 = new String(buffer2, 0, length2, "UTF-8");
                                    } else {
                                        str2 = "";
                                    }
                                    out.write((str1+str2).replaceFirst(logEndPattern, "$1").getBytes("UTF-8"));
                                } else {
                                    out.write(buffer1, 0, length1);
                                }
                            } else {
                                str1 = new String(buffer1, 0, length1, "UTF-8");
                                str.append(str1);
                                m = logStartPattern.matcher(str);
                                if (m.find()) {
                                    if (instream.available() < bufferSize) {
                                        length2 = instream.read(buffer2);
                                        if (length2 > 0) {
                                            str2 = new String(buffer2, 0, length2, "UTF-8");
                                        } else {
                                            str2 = "";
                                        }
                                        out.write((m.group(1)+str2).replaceFirst(logEndPattern, "$1").getBytes("UTF-8"));
                                    } else {
                                        out.write(m.group(1).replaceFirst(logEndPattern, "$1").getBytes("UTF-8"));
                                    }
                                    logBeginIsFound = true;
                                }
                            }
                        }
                        out.flush();
                        LOGGER.info(Instant.now().getEpochSecond()+"");
                        if (!logBeginIsFound) {
                            throwJobSchedulerError(new SOSXMLXPath(new StringBuffer(str))); 
                        }
                    } finally {
                        try {
                            instream.close();
                            instream = null;
                        } catch (Exception e) {}
                        try {
                            if (out != null) {
                                out.close(); 
                            }
                        } catch (Exception e) {}
                    }
                }
            }
            return path;
        } catch (JobSchedulerBadRequestException e) {
            if (path != null) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e1) {}
            }
            throw e;
        } catch (Exception e) {
            if (path != null) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e1) {}
            }
            if (e.getCause() != null) {
                throw new JobSchedulerNoResponseException(e.getCause());
            } else {
                throw new JobSchedulerNoResponseException(e);
            }
        } catch (Throwable e) {
            if (path != null) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e1) {}
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public String getShowStateCommand(String subsystems, String what, String path) {
        return getShowStateCommand(subsystems, what, path, null, null);
    }
    
    public String getShowStateCommand(String subsystems, String what, String path, Integer maxOrders, Integer maxOrderHistory) {
        XMLBuilder showState = new XMLBuilder("show_state");
        if (subsystems != null && !subsystems.isEmpty()) {
            showState.addAttribute("subsystems", subsystems);
        }
        if (what != null && !what.isEmpty()) {
            showState.addAttribute("what", what);
        }
        if (path != null && !path.isEmpty()) {
            showState.addAttribute("path", path);
        }
        if (maxOrders != null) {
            showState.addAttribute("max_orders", maxOrders.toString());
        }
        if (maxOrderHistory != null) {
            showState.addAttribute("max_order_history", maxOrderHistory.toString());
        }
        return showState.asXML();
    }
    
    public String getShowJobCommand(String job, String what) {
        return getShowJobCommand(job, what, null, null);
    }
    
    public String getShowJobCommand(String job, String what, Integer maxOrders, Integer maxTaskHistory) {
        XMLBuilder showjob = new XMLBuilder("show_job");
        showjob.addAttribute("job", job);
        if (what != null && !what.isEmpty()) {
            showjob.addAttribute("what", what);
        }
        if (maxTaskHistory == null) {
            maxTaskHistory = 0; 
        }
        showjob.addAttribute("max_task_history", maxTaskHistory.toString());
        if (maxOrders != null) {
            showjob.addAttribute("max_orders", maxOrders.toString());
        }
        return showjob.asXML();
    }
    
    public String getShowJobChainCommand(String jobChain, String what) {
        return getShowJobChainCommand(jobChain, what, null, null);
    }
    
    public String getShowJobChainCommand(String jobChain, String what, Integer maxOrders) {
        return getShowJobChainCommand(jobChain, what, maxOrders, null);
    }
    
    public String getShowJobChainCommand(String jobChain, String what, Integer maxOrders, Integer maxOrderHistory) {
        XMLBuilder showjobChain = new XMLBuilder("show_job_chain");
        showjobChain.addAttribute("job_chain", jobChain);
        if (what != null && !what.isEmpty()) {
            showjobChain.addAttribute("what", what);
        }
        if (maxOrders != null) {
            showjobChain.addAttribute("max_orders", maxOrders.toString());
        }
        if (maxOrderHistory == null) {
            maxOrderHistory = 0; 
        }
        showjobChain.addAttribute("max_order_history", maxOrderHistory.toString());
        return showjobChain.asXML();
    }
    
    public String getShowOrderCommand(String jobChain, String orderId, String what) {
        XMLBuilder showOrder = new XMLBuilder("show_order");
        showOrder.addAttribute("job_chain", jobChain);
        showOrder.addAttribute("order", orderId);
        if (what != null && !what.isEmpty()) {
            showOrder.addAttribute("what", what);
        }
        return showOrder.asXML();
    }
    
    public String getModifyHotFolderCommand(String path, Element jobSchedulerObjectElement) throws Exception {
        Path p = Paths.get(path);
        if (jobSchedulerObjectElement.getNodeName().equals("order")) {
            String[] orderPath = p.getFileName().toString().split(",", 2);
            jobSchedulerObjectElement.setAttribute("job_chain", orderPath[0]);
            jobSchedulerObjectElement.setAttribute("id", orderPath[1]);
        } else {
            jobSchedulerObjectElement.setAttribute("name", p.getFileName().toString());
        }
        XMLBuilder modifyHotFolder = new XMLBuilder("modify_hot_folder");
        org.dom4j.Element elem = XMLBuilder.parse(getXmlString(jobSchedulerObjectElement));
        modifyHotFolder.addAttribute("folder", p.getParent().toString().replace('\\', '/')).add(elem);
        return modifyHotFolder.asXML();
    }
    
    public Element updateCalendarInRuntimes(List<String> dates, String objectType, String path, String calendarPath, String calendarOldPath)
            throws Exception {
        Node curObject = getSosxml().selectSingleNode(String.format("//%1$s[@path='%2$s']/source", objectType.toLowerCase(), path));
        if (curObject == null) {
            throw new JobSchedulerObjectNotExistException(objectType + ": " + path);
        }
        Node node = RuntimeResolver.updateCalendarInRuntimes(getSosxml(), curObject, dates, objectType, path, calendarPath, calendarOldPath);
        if (node != null) {
            return (Element) node.getFirstChild();
        }
        return null; 
    }
    
    public String getXmlString(Node node) throws Exception {
//        String encoding = "UTF-8";
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        OutputStreamWriter writer = new OutputStreamWriter(bos, encoding);
        StringWriter writer = new StringWriter();
        try {
            Source source = new DOMSource(node);
            Result result = new StreamResult(writer);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            return writer.toString().trim();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
//            try {
//                bos.close();
//            } catch (Exception e) {
//            }
        }
    }
}
