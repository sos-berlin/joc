package com.sos.joc.classes.runtime;

import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.RunTime200;

public class RunTime {

    public static RunTime200 set(String path, JOCXmlCommand jocXmlCommand, String postCommand, String xPath, String accessToken) throws Exception {
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(postCommand, accessToken);
        RunTime200 runTimeAnswer = new RunTime200();

        NodeList runtimeNodes = jocXmlCommand.getSosxml().selectNodeList(xPath);
        com.sos.joc.model.common.RunTime runTime = new com.sos.joc.model.common.RunTime();
        runTime.setRunTimeIsTemporary(false);
        runTime.setSurveyDate(jocXmlCommand.getSurveyDate());
        Path parent = Paths.get(path).getParent();

        if (runtimeNodes != null && runtimeNodes.getLength() > 0) { // adhoc and file orders
            Node runtimeNode = runtimeNodes.item(0);
            runTime.setRunTime(getRuntimeXmlString(parent, runtimeNode));
        } else {
            runTime.setRunTime(getRuntimeXmlString(parent, null));
        }

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
