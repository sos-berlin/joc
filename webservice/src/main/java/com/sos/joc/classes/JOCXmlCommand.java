package com.sos.joc.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.xml.SOSXmlCommand;

public class JOCXmlCommand extends SOSXmlCommand {

    private Date surveyDate;
    private HashMap<String,NodeList> listOfNodeLists;

    public JOCXmlCommand(String url) {
        super(url);
        listOfNodeLists = new HashMap<String,NodeList>();
 
    }

    public Date getSurveyDate() {
        if (surveyDate == null || "".equals(surveyDate)) {
            try {
                executeXPath("/spooler/answer");
                SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
                surveyDate = formatter.parse(getAttribute("time"));
            } catch (Exception e) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);
                    surveyDate = formatter.parse(getAttribute("time"));
                } catch (Exception ee) {
                }
            }
        }
        return surveyDate;
    }

    public void createNodeList(String key,String xpath) throws Exception {
        NodeList nodeList = selectNodelist(xpath);
        listOfNodeLists.put(key, nodeList);
    }
    
    public void createNodeList(String xpath) throws Exception {
        createNodeList("",xpath);
    }

    public NodeList getNodeList(String key) {
        return listOfNodeLists.get(key);
    }

    public NodeList getNodeList() {
        return listOfNodeLists.get("");
    }
    
    public void getElementFromList(String key, int i) throws Exception {
        NodeList nodeList = listOfNodeLists.get(key);
        if (nodeList != null) {
            Node n = nodeList.item(i);
            if (n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) n;
                HashMap <String, String> attrs = new HashMap<String, String>();
                 if (element != null) {
                    NamedNodeMap map = n.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        attrs.put(map.item(j).getNodeName(), map.item(j).getNodeValue());
                    }
                }
                 attributes.put(key, attrs);
            }
        }
    }
    
    public void getElementFromList(int i) throws Exception {
        getElementFromList("",i);
    }
    
    public String getAttributeValue(Element elem, String attributeName, String default_) {
        String val = elem.getAttribute(attributeName);
        if (val == null || val.isEmpty()) {
            val = default_;
        }
        return val;
    }

    public boolean checkRequiredParameter(String paramKey, String paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

}
