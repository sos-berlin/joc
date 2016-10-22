package com.sos.joc.classes;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.xml.SOSXmlCommand;

public class JOCXmlCommand extends SOSXmlCommand {

    private Date surveyDate;
    private Map<String, NodeList> listOfNodeLists;
    private URI uriForJsonCommand;

    public JOCXmlCommand(String url) {
        super(url + WebserviceConstants.XML_COMMAND_API_PATH);
        listOfNodeLists = new HashMap<String, NodeList>();
    }

    public Date getSurveyDate() {
        if (surveyDate == null || "".equals(surveyDate)) {
            try {
                String surveyDateStr = getSosxml().selectSingleNodeValue("/spooler/answer/@time");
                surveyDate = JobSchedulerDate.getDateFromISO8601String(surveyDateStr);
            } catch (Exception e) {}
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
    
    public void throwJobSchedulerError() throws Exception {
        String xPath = "/spooler/answer/ERROR";
        Element errorElem = (Element) getSosxml().selectSingleNode(xPath);
        if (errorElem != null) {
            throw new JobSchedulerInvalidResponseDataException(new JocError(errorElem.getAttribute("code"), errorElem.getAttribute("text")));
        }
    }
    
    @Override
    public String executePost(String xmlCommand) throws JobSchedulerConnectionRefusedException {
        try {
            return super.executePost(xmlCommand);
        } catch (Exception e) {
            throw new JobSchedulerConnectionRefusedException(getUrl(), e);
        }
    }
}
