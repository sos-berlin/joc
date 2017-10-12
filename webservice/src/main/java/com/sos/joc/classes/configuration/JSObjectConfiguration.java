package com.sos.joc.classes.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;

import sos.xml.SOSXMLXPath;

public class JSObjectConfiguration {

    private String accessToken;
    private String configuration;
    
    public JSObjectConfiguration() {
    }

    public JSObjectConfiguration(String accessToken) {
        this.accessToken = accessToken;
    }

    public Configuration200 getOrderConfiguration(JOCResourceImpl jocResourceImpl, String jobChain, String orderId, boolean responseInHtml)
            throws JocException {
        Configuration200 entity = new Configuration200();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String orderCommand = jocXmlCommand.getShowOrderCommand(jocResourceImpl.normalizePath(jobChain), orderId, "source");
        entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, orderCommand, "/spooler/answer/order", "order", responseInHtml,
                accessToken);
        configuration = entity.getConfiguration().getContent().getXml();
        return entity;
    }

    public Configuration200 getJobConfiguration(JOCResourceImpl jocResourceImpl, String job, boolean responseInHtml) throws JocException {
        Configuration200 entity = new Configuration200();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String jobCommand = jocXmlCommand.getShowJobCommand(jocResourceImpl.normalizePath(job), "source", 0, 0);
        entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobCommand, "/spooler/answer/job", "job", responseInHtml, accessToken);
        configuration = entity.getConfiguration().getContent().getXml();
        return entity;
    }

    public String changeRuntimeElement(String newRunTime) {
        Matcher m = Pattern.compile("<run_time.*>.*<\\/run_time>", Pattern.DOTALL).matcher("");
        String newConfiguration = m.reset(configuration).replaceAll(newRunTime);

        if (newConfiguration.equals(configuration)) {
            newConfiguration = newConfiguration.replaceAll("<run_time.*\\/>", newRunTime);
        }
        configuration = newConfiguration;
        return newConfiguration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String modifyOrderRuntime(String newRunTime, JOCResourceImpl jocResourceImpl, String jobChain, String orderId) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String orderCommand = jocXmlCommand.getShowOrderCommand(jocResourceImpl.normalizePath(jobChain), orderId, "source");
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(orderCommand, jocResourceImpl.getAccessToken());
        try {
            Node orderNode = modifyOrderRuntimeNode(jocXmlCommand.getSosxml(), new SOSXMLXPath(new StringBuffer(newRunTime)));
            return jocXmlCommand.getXmlString(orderNode);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
    public Node modifyOrderRuntimeNode(SOSXMLXPath oldRunTime, SOSXMLXPath newRunTime) throws JocException {
        try {
            Node orderNode = oldRunTime.selectSingleNode("//source/order");
            Node runTime = oldRunTime.selectSingleNode(orderNode, "run_time");
            Node newRunTimeNode = orderNode.getOwnerDocument().adoptNode(newRunTime.getRoot());
            Node textNode = null;
            if (runTime != null) {
                orderNode.replaceChild(newRunTimeNode, runTime);
            } else {
                NodeList payloads = oldRunTime.selectNodeList(orderNode, "payload|xml_payload");
                switch (payloads.getLength()) {
                case 0:
                    if (orderNode.hasChildNodes() && orderNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                        textNode = orderNode.getFirstChild().cloneNode(false);
                    } else {
                        textNode = orderNode.getOwnerDocument().createTextNode("    ");
                    }
                    orderNode.appendChild(textNode);
                    orderNode.appendChild(newRunTimeNode);
                    break;
                case 1:
                    textNode = payloads.item(0).getPreviousSibling();
                    if (textNode != null && textNode.getNodeType() == Node.TEXT_NODE) {
                        orderNode.insertBefore(textNode.cloneNode(false), textNode); 
                        orderNode.insertBefore(newRunTimeNode, textNode); 
                    } else {
                        orderNode.insertBefore(newRunTimeNode, payloads.item(0));
                    }
                    break;
                case 2:
                    for (int i = 0; i < payloads.getLength(); i++) {
                        if (payloads.item(i).getNodeName().equals("payload")) {
                            textNode = payloads.item(i).getPreviousSibling();
                            if (textNode != null && textNode.getNodeType() == Node.TEXT_NODE) {
                                orderNode.insertBefore(textNode.cloneNode(false), textNode); 
                                orderNode.insertBefore(newRunTimeNode, textNode); 
                            } else {
                                orderNode.insertBefore(newRunTimeNode, payloads.item(i));
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            return orderNode;
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
    public String modifyJobRuntime(String newRunTime, JOCResourceImpl jocResourceImpl, String job) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String jobCommand = jocXmlCommand.getShowJobCommand(jocResourceImpl.normalizePath(job), "source", 0, 0);
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(jobCommand, jocResourceImpl.getAccessToken());
        try {
            Node jobNode = modifyJobRuntimeNode(jocXmlCommand.getSosxml(), new SOSXMLXPath(new StringBuffer(newRunTime)));
            return jocXmlCommand.getXmlString(jobNode);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
    public Node modifyJobRuntimeNode(SOSXMLXPath oldRunTime, SOSXMLXPath newRunTime) throws JocException {
        try {
            Node jobNode = oldRunTime.selectSingleNode("//source/job");
            Node runTime = oldRunTime.selectSingleNode(jobNode, "run_time");
            Node newRunTimeNode = jobNode.getOwnerDocument().adoptNode(newRunTime.getRoot());
            Node textNode = null;
            if (runTime != null) {
                jobNode.replaceChild(newRunTimeNode, runTime);
            } else {
                Node commands = oldRunTime.selectSingleNode(jobNode, "commands");
                if (commands != null) {
                    textNode = commands.getPreviousSibling();
                    if (textNode != null && textNode.getNodeType() == Node.TEXT_NODE) {
                        jobNode.insertBefore(textNode.cloneNode(false), textNode); 
                        jobNode.insertBefore(newRunTimeNode, textNode); 
                    } else {
                        jobNode.insertBefore(newRunTimeNode, commands);
                    }
                } else {
                    if (jobNode.hasChildNodes() && jobNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                        textNode = jobNode.getFirstChild().cloneNode(false);
                    } else {
                        textNode = jobNode.getOwnerDocument().createTextNode("    ");
                    }
                    jobNode.appendChild(textNode);
                    jobNode.appendChild(newRunTimeNode);
                }
            }
            return jobNode;
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

}
