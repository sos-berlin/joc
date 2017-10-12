package com.sos.joc.classes;

import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.xml.SOSXmlCommand;

public class JOCXmlCommand extends SOSXmlCommand {

    public static final String XML_COMMAND_API_PATH = "/jobscheduler/master/api/command";
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlCommand.class);
    private Date surveyDate;
    private Map<String, NodeList> listOfNodeLists = new HashMap<String, NodeList>();;
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
        if (surveyDate == null || "".equals(surveyDate)) {
            try {
                String surveyDateStr = getSosxml().selectSingleNodeValue("/spooler/answer/@time");
                surveyDate = JobSchedulerDate.getDateFromISO8601String(surveyDateStr);
                if(surveyDate == null) {
                    surveyDate = Date.from(Instant.now()); 
                }
            } catch (Exception e) {
                surveyDate = Date.from(Instant.now());
            }
        }
        return surveyDate;
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
                HashMap<String, String> attrs = new HashMap<String, String>();
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
    
    public void throwJobSchedulerError() throws JobSchedulerBadRequestException {
        String xPath = "/spooler/answer/ERROR";
        try {
            Element errorElem = (Element) getSosxml().selectSingleNode(xPath);
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
    
    public String executePost(String xmlCommand) throws JocException {
        return executePost(xmlCommand, UUID.randomUUID().toString());
    }
    
    public String executePost(String xmlCommand, String accessToken) throws JocException {
        this.xmlCommand = xmlCommand;
        try {
            return executeXMLPost(xmlCommand, accessToken);
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
        try {
            return executePost(xmlCommand, accessToken);
        } catch (JobSchedulerConnectionRefusedException e) {
            String url = null;
            if (jocResourceImpl != null) {
                url = jocResourceImpl.retrySchedulerInstance(); 
            }
            if (url != null) {
                setUrl(url + XML_COMMAND_API_PATH);
                LOGGER.debug("...retry with " + url + XML_COMMAND_API_PATH);
                return executePost(xmlCommand, accessToken);
            } else {
                throw e;
            }
        } catch (JocException e) {
            throw e;
        }
    }
    
    public String executePostWithThrowBadRequest(String xmlCommand, String accessToken) throws JocException {
        String s = executePost(xmlCommand, accessToken);
        throwJobSchedulerError();
        return s;
    }
    
    public String executePostWithThrowBadRequestAfterRetry(String xmlCommand, String accessToken) throws JocException {
        String s = executePostWithRetry(xmlCommand, accessToken);
        LOGGER.debug(s);
        throwJobSchedulerError();
        return s;
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
        XMLBuilder modifyHotFolder = new XMLBuilder("modify_hot_folder");
        org.dom4j.Element elem = XMLBuilder.parse(getXmlString(jobSchedulerObjectElement));
        if (jobSchedulerObjectElement.getNodeName().equals("order")) {
            String[] orderPath = p.getFileName().toString().split(",", 2);
            jobSchedulerObjectElement.setAttribute("job_chain", orderPath[0]);
            jobSchedulerObjectElement.setAttribute("id", orderPath[1]);
        } else {
            jobSchedulerObjectElement.setAttribute("name", p.getFileName().toString());
        }
        modifyHotFolder.addAttribute("folder", p.getParent().toString().replace('\\', '/')).add(elem);
        return modifyHotFolder.asXML();
    }
    
    public Element updateCalendarInRuntimes(List<String> dates, String objectType, String path, Long calendarId) throws Exception {
        Node curObject = getSosxml().selectSingleNode(String.format("//%1$s[@path='%2$s']/source", objectType.toLowerCase(), path));
        NodeList dateParentList = getSosxml().selectNodeList(curObject, String.format(".//date[@calendar='%2$s']/parent::*", objectType.toLowerCase(), path, calendarId));
        NodeList holidayParentList = getSosxml().selectNodeList(curObject, String.format(".//holiday[@calendar='%2$s']/parent::*", objectType.toLowerCase(), path, calendarId));
        
        for (int i=0; i < dateParentList.getLength(); i++) {
            NodeList dateList = getSosxml().selectNodeList(dateParentList.item(i), String.format("date[@calendar='%2$s']", calendarId));
            updateCalendarInRuntime(dateList, dates); 
        }
        for (int i=0; i < holidayParentList.getLength(); i++) {
            NodeList holidayList = getSosxml().selectNodeList(holidayParentList.item(i), String.format("holiday[@calendar='%2$s']", calendarId));
            updateCalendarInRuntime(holidayList, dates); 
        }
        return (Element) curObject.getFirstChild(); 
    }
    
    private void updateCalendarInRuntime(NodeList nodeList, List<String> dates) {
        Element firstElem = null;
        Node parentOfFirstElem = null;
        Node textNode = null;
        
        if (nodeList.getLength() > 0) {
            firstElem = (Element) nodeList.item(0);
            parentOfFirstElem = firstElem.getParentNode();
            if (firstElem.getPreviousSibling().getNodeType() == Node.TEXT_NODE) {
                textNode = firstElem.getPreviousSibling(); 
            }
        }
        for (int i=1; i < nodeList.getLength(); i++) {
            parentOfFirstElem.removeChild(nodeList.item(i));
        }
        if (firstElem != null) {
            if (dates.isEmpty()) {
                parentOfFirstElem.removeChild(firstElem);
            } else {
                firstElem.setAttribute("date", dates.get(0));
                for (int i=1; i < dates.size(); i++) {
                    Element newElem = (Element) firstElem.cloneNode(true);
                    newElem.setAttribute("date", dates.get(i));
                    if (textNode != null) {
                        parentOfFirstElem.insertBefore(textNode.cloneNode(false), textNode);
                        parentOfFirstElem.insertBefore(newElem, textNode);
                    } else {
                        parentOfFirstElem.insertBefore(newElem, firstElem);
                    }
                }
            }
        }
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
