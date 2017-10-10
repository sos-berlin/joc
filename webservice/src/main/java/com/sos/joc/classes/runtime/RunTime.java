package com.sos.joc.classes.runtime;

import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
 
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.common.RunTime200;

public class RunTime {

    public static RunTime200 set(String path, JOCXmlCommand jocXmlCommand, String postCommand, String xPath, String accessToken) throws Exception {
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);
        RunTime200 runTimeAnswer = new RunTime200();

        com.sos.joc.model.common.RunTime runTime = new com.sos.joc.model.common.RunTime();
        runTime.setRunTimeIsTemporary(false);
        runTime.setSurveyDate(jocXmlCommand.getSurveyDate());
        Path parent = Paths.get(path).getParent();
        runTime.setRunTime(getRuntimeXmlString(parent, null));
        runTimeAnswer.setRunTime(runTime);
        runTimeAnswer.setDeliveryDate(Date.from(Instant.now()));
        return runTimeAnswer;
    }

    public static String getRuntimeXmlString(String path, JOCXmlCommand jocXmlCommand, String postCommand, String xPath, String accessToken) throws Exception {
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);
        Node runtimeNode = jocXmlCommand.getSosxml().selectSingleNode(xPath);
        Path parent = Paths.get(path).getParent();
        return getRuntimeXmlString(parent, runtimeNode);
    }

    public static String getRuntimeXmlString(Path path, Node runtimeNode) throws Exception {
        if (runtimeNode == null) {
            return "<run_time/>";
        }
        StringWriter writer = new StringWriter();
        try {
            Element runtimeElem = (Element) runtimeNode;
            String schedule = runtimeElem.getAttribute("schedule");
            if (schedule != null && !schedule.isEmpty() && path != null) {
                runtimeElem.setAttribute("schedule", path.resolve(schedule).normalize().toString().replace('\\', '/'));
            }
            Source source = new DOMSource(runtimeElem);
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
