package com.sos.joc.classes.configuration;

import java.time.Instant;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.order.ModifyOrder;

import sos.xml.SOSXMLXPath;

public class JSObjectConfiguration {

    private String accessToken;
    private boolean getSourceWithXMLCommand = true;

    public JSObjectConfiguration() {
    }

    public JSObjectConfiguration(String accessToken) {
        this.accessToken = accessToken;
    }

    public JSObjectConfiguration(String accessToken, boolean getSourceWithXMLCommand) {
        this.accessToken = accessToken;
        this.getSourceWithXMLCommand = getSourceWithXMLCommand;
    }

    public Configuration200 getOrderConfiguration(JOCResourceImpl jocResourceImpl, String jobChain, String orderId, boolean responseInHtml)
            throws JocException {
        Configuration200 entity = new Configuration200();
        if (getSourceWithXMLCommand) {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
            String orderCommand = jocXmlCommand.getShowOrderCommand(JOCResourceImpl.normalizePath(jobChain), orderId, "source");
            entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, orderCommand, "/spooler/answer/order", "order",
                    responseInHtml, accessToken));
        } else {
            try {
                entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(jocResourceImpl), jobChain + "," + orderId,
                        JobSchedulerObjectType.ORDER, responseInHtml));
            } catch (JobSchedulerObjectNotExistException e) {
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
                String orderCommand = jocXmlCommand.getShowOrderCommand(JOCResourceImpl.normalizePath(jobChain), orderId, "source");
                entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, orderCommand, "/spooler/answer/order", "order",
                        responseInHtml, accessToken));
            }
        }
        entity.setDeliveryDate(Date.from(Instant.now()));
        return entity;
    }

    public Configuration200 getJobConfiguration(JOCResourceImpl jocResourceImpl, String job, boolean responseInHtml) throws JocException {
        Configuration200 entity = new Configuration200();

        if (getSourceWithXMLCommand) {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
            String jobCommand = jocXmlCommand.getShowJobCommand(JOCResourceImpl.normalizePath(job), "source", 0, 0);
            entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobCommand, "/spooler/answer/job", "job", responseInHtml,
                    accessToken));
        } else {
            try {
                entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(jocResourceImpl), job, JobSchedulerObjectType.JOB,
                        responseInHtml));
            } catch (JobSchedulerObjectNotExistException e) {
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
                String jobCommand = jocXmlCommand.getShowJobCommand(JOCResourceImpl.normalizePath(job), "source", 0, 0);
                entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobCommand, "/spooler/answer/job", "job", responseInHtml,
                        accessToken));
            }
        }
        entity.setDeliveryDate(Date.from(Instant.now()));
        return entity;
    }

    public ModifyOrder modifyOrderRuntime(String newRunTime, JOCResourceImpl jocResourceImpl, String jobChain, String orderId) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String orderCommand = jocXmlCommand.getShowOrderCommand(JOCResourceImpl.normalizePath(jobChain), orderId, "source");
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(orderCommand, jocResourceImpl.getAccessToken());
        try {
            Node orderNode = jocXmlCommand.getSosxml().selectSingleNode("//source/order");
            if (orderNode == null) {
                return null;
            }
            if (newRunTime != null && !newRunTime.trim().isEmpty()) {
                orderNode = modifyOrderRuntimeNode(jocXmlCommand.getSosxml(), cleanEmptyCalendarDates(new SOSXMLXPath(new StringBuffer(newRunTime))));
            }
            String nestedChain = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/order/@path", null);
            if (nestedChain != null && nestedChain.contains(",")) {
                nestedChain = nestedChain.split(",", 2)[0];
            } else {
                nestedChain = jobChain;
            }
            ModifyOrder o = new ModifyOrder();
            o.setJobChain(nestedChain);
            o.setOrderId(orderId);
            o.setRunTimeXml(jocXmlCommand.getXmlString(orderNode));
            return o;
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public Node modifyOrderRuntimeNode(SOSXMLXPath oldRunTime, Element newRunTimeElem) throws JocException {
        try {
            Node orderNode = oldRunTime.selectSingleNode("//source/order");
            Node runTime = oldRunTime.selectSingleNode(orderNode, "run_time");
            Node newRunTimeNode = orderNode.getOwnerDocument().adoptNode(newRunTimeElem);
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
                        textNode = orderNode.getOwnerDocument().createTextNode("\n    ");
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
        String jobCommand = jocXmlCommand.getShowJobCommand(JOCResourceImpl.normalizePath(job), "source", 0, 0);
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(jobCommand, jocResourceImpl.getAccessToken());
        try {
            String x = jocXmlCommand.getXmlString(jocXmlCommand.getSosxml().getRoot());
            Node jobNode = jocXmlCommand.getSosxml().selectSingleNode("//source/job");
            if (newRunTime != null && !newRunTime.trim().isEmpty()) {
                jobNode = modifyJobRuntimeNode(jocXmlCommand.getSosxml(), cleanEmptyCalendarDates(new SOSXMLXPath(new StringBuffer(newRunTime))));
            }
            return jocXmlCommand.getXmlString(jobNode);
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    private Node modifyJobRuntimeNode(SOSXMLXPath oldRunTime, Element newRunTimeElem) throws JocException {
        try {
            Node jobNode = oldRunTime.selectSingleNode("//source/job");
            Node runTime = oldRunTime.selectSingleNode(jobNode, "run_time");
            Node newRunTimeNode = jobNode.getOwnerDocument().adoptNode(newRunTimeElem);
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
                        textNode = jobNode.getOwnerDocument().createTextNode("\n    ");
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

    private Element cleanEmptyCalendarDates(SOSXMLXPath runTime) throws Exception {
        Element runTimeElement = runTime.getRoot();
        NodeList emptyDates = runTime.selectNodeList("date[not(period)]");
        for (int i = 0; i < emptyDates.getLength(); i++) {
            Node textNode = emptyDates.item(i).getPreviousSibling();
            if (textNode != null && textNode.getNodeType() == Node.TEXT_NODE) {
                runTimeElement.removeChild(textNode);
            }
            runTimeElement.removeChild(emptyDates.item(i));
        }
        return runTimeElement;
    }

}
