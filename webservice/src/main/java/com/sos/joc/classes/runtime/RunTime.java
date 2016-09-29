package com.sos.joc.classes.runtime;

import java.io.StringWriter;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.common.RuntimeSchema;


public class RunTime {

    public static Runtime200Schema set(JOCXmlCommand jocXmlCommand, String postCommand, String xPath) throws Exception {
        jocXmlCommand.excutePost(postCommand);
        Runtime200Schema runTimeAnswer = new Runtime200Schema();
        runTimeAnswer.setDeliveryDate(new Date());
        Node runtimeNode = jocXmlCommand.getSosxml().selectSingleNode(xPath);
        RuntimeSchema runTime = new RuntimeSchema();
        runTime.setRunTime(getRuntimeXmlString(runtimeNode));
        runTimeAnswer.setRunTime(runTime);
        return runTimeAnswer;
    }
    
    public static String getRuntimeXmlString(Node runtimeNode) throws Exception {
        StringWriter writer = new StringWriter();
        try {
            Source source = new DOMSource(runtimeNode);
            Result result = new StreamResult(writer);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.transform(source, result);
            return writer.toString();
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