package com.sos.joc.joe.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.model.joe.job.Description;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.nodeparams.Config;

public class XmlDeserializer {

    public static <T> T deserialize(byte[] xml, Class<T> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException, DocumentException {
        switch (clazz.getSimpleName()) {
        case "Job":
            return clazz.cast(deserializeJob(getDocument(xml)));
        case "Config": //NODEPARAMS
            return clazz.cast(deserializeNodeParams(getDocument(xml)));
        default:
            return Globals.xmlMapper.readValue(bytesToString(xml), clazz);
        }
    }
    
    public static <T> T deserialize(String xml, Class<T> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException,
            JobSchedulerBadRequestException, DocumentException {
        switch (clazz.getSimpleName()) {
        case "Job":
            return clazz.cast(deserializeJob(getDocument(xml)));
        case "Config": // NODEPARAMS
            return clazz.cast(deserializeNodeParams(getDocument(xml)));
        default:
            return Globals.xmlMapper.readValue(stripWhitespaceText(xml), clazz);
        }
    }

    public static <T> T deserialize(Document doc, Class<T> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException, DocumentException {
        switch (clazz.getSimpleName()) {
        case "Job":
            return clazz.cast(deserializeJob(doc));
        case "Config": //NODEPARAMS
            return clazz.cast(deserializeNodeParams(doc));
        default:
            return Globals.xmlMapper.readValue(domToString(doc), clazz);
        }
    }

    private static Document getDocument(byte[] xml) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setStripWhitespaceText(true);
        reader.setIgnoreComments(true);
        return reader.read(new ByteArrayInputStream(xml));
    }
    
    private static Document getDocument(String xml) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setStripWhitespaceText(true);
        reader.setIgnoreComments(true);
        return reader.read(new StringReader(xml));
    }
    
    private static String domToString(Document doc) {
        return stripWhitespaceText(doc.asXML());
    }
    
    private static String bytesToString(byte[] xml) {
        return stripWhitespaceText(new String(xml));
    }
    
    private static String stripWhitespaceText(String xml) {
        // converts a pair of open and close tag to a closed tag if the content is empty
        // e.g. <params>   </params> -> <params/>
        // otherwise Jackson's xmlMapper raises 
        // com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot construct instance of 
        // `com.sos.joc.model.joe.XXX` (although at least one Creator exists): no String-argument 
        // constructor/factory method to deserialize from String value ...
        return xml.replaceAll("<([a-zA-Z_]+\\b)([^>]*)>\\s*</\\1>", "<$1$2/>");
    }
    
    private static Job deserializeJob(Document doc) throws JsonParseException, JsonMappingException, IOException {
        String descContent = "";
        Element descriptionNode = (Element) doc.selectSingleNode("/job/description");
        if (descriptionNode != null) {
            descContent = descriptionNode.getText().trim();
            if (descriptionNode.hasContent()) {
                @SuppressWarnings("unchecked")
                List<Node> children = new ArrayList<Node>(descriptionNode.content());
                for (Node child : children) {
                    if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        descriptionNode.remove(child);
                    }
                }
            }
        }
        String scriptContent = "";
        Element scriptNode = (Element) doc.selectSingleNode("/job/script");
        if (scriptNode != null) {
            scriptContent = scriptNode.getText().trim();
            if (scriptNode.hasContent()) {
                @SuppressWarnings("unchecked")
                List<Node> children = new ArrayList<Node>(scriptNode.content());
                for (Node child : children) {
                    if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        scriptNode.remove(child);
                    }
                }
            }
        }
        Job job = Globals.xmlMapper.readValue(domToString(doc), Job.class);
        if (!descContent.isEmpty()) {
            Description desc = job.getDocumentation();
            if (desc == null) {
                desc = new Description();
            }
            desc.setContent(descContent);
            job.setDocumentation(desc);
        }
        if (!scriptContent.isEmpty()) {
            Script scr = job.getScript();
            if (scr == null) {
                scr = new Script();
            }
            scr.setContent(scriptContent);
            job.setScript(scr);
        }
        return job;
    }
    
    private static Config deserializeNodeParams(Document doc) throws JsonParseException, JsonMappingException, IOException {
        @SuppressWarnings("unchecked")
        List<Node> notes = new ArrayList<Node>(doc.selectNodes("//note"));
        for (Node note : notes) {
            note.getParent().remove(note);
        }
        return Globals.xmlMapper.readValue(domToString(doc), Config.class);
    }
}
