package com.sos.joc.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.xml.SOSXmlCommand;

public class JOCXmlCommand extends SOSXmlCommand {

    private Date surveyDate;
    private NodeList nodeList;

    public JOCXmlCommand(String url) {
        super(url);
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

    public void createNodeList(String xpath) throws Exception {
        nodeList = selectNodelist("//spooler/answer/state/cluster/cluster_member");
        nodeList = selectNodelist(xpath);
    }

    public NodeList getNodeList() {
        return nodeList;
    }

    public void getElementFromList(int i) throws Exception {
        if (nodeList != null) {
            Node n = nodeList.item(i);
            if (n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) n;
                attributes = new HashMap<String, String>();
                 if (element != null) {
                    NamedNodeMap map = n.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        attributes.put(map.item(j).getNodeName(), map.item(j).getNodeValue());
                    }
                }
            }
        }
    }

}
