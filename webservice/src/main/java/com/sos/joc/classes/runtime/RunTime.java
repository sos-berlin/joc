package com.sos.joc.classes.runtime;

import java.io.StringWriter;
import java.time.Instant;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.common.RunTime200;


public class RunTime {

    public static RunTime200 set(JOCXmlCommand jocXmlCommand, String postCommand, String xPath, String accessToken, Boolean runTimeIsTemporary) throws Exception {
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);
        RunTime200 runTimeAnswer = new RunTime200();
        if (runTimeIsTemporary == null) {
            runTimeIsTemporary = false;
        }
        NodeList runtimeNodes = jocXmlCommand.getSosxml().selectNodeList(xPath);
        com.sos.joc.model.common.RunTime runTime = new com.sos.joc.model.common.RunTime();
        runTime.setRunTimeIsTemporary(runTimeIsTemporary);
        for (int i=0; i < runtimeNodes.getLength(); i++) {
            Node runtimeNode = runtimeNodes.item(i);
            if ("source".equals(runtimeNode.getParentNode().getParentNode().getNodeName())) {
                if (runTimeIsTemporary) {
                    runTime.setPermanentRunTime(getRuntimeXmlString(runtimeNode));
                }
            } else {
                runTime.setRunTime(getRuntimeXmlString(runtimeNode));
            }
        }
        runTimeAnswer.setRunTime(runTime);
        runTimeAnswer.setDeliveryDate(Date.from(Instant.now()));
        return runTimeAnswer;
    }
    
    public static String getRuntimeXmlString(JOCXmlCommand jocXmlCommand, String postCommand, String xPath, String accessToken) throws Exception {
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);
        Node runtimeNode = jocXmlCommand.getSosxml().selectSingleNode(xPath);
        return getRuntimeXmlString(runtimeNode);
    }
    
    public static String getRuntimeXmlString(Node runtimeNode) throws Exception {
        if (runtimeNode == null) {
            return "<run_time/>";
        }
        StringWriter writer = new StringWriter();
        try {
            Source source = new DOMSource(runtimeNode);
            Result result = new StreamResult(writer);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.transform(source, result);
            return writer.toString().trim();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
