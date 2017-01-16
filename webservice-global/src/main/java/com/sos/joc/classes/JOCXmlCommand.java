package com.sos.joc.classes;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.exception.NoResponseException;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCJsonCommand.class);
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
                if (xmlCommand != null) {
                    err.appendMetaInfo("JS-URL: " + getUrl(), "JS-REQUEST: " + xmlCommand); 
                }
                JobSchedulerBadRequestException badRequestException = new JobSchedulerBadRequestException(err);
                badRequestException.setSurveyDate(getSurveyDate());
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
        } catch (NoResponseException e) {
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
        if (maxTaskHistory != null) {
            showjob.addAttribute("max_task_history", maxTaskHistory.toString());
        }
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
        if (maxOrderHistory != null) {
            showjobChain.addAttribute("max_order_history", maxOrderHistory.toString());
        }
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
}
