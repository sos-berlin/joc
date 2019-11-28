package com.sos.joc.joe.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        case "job":
            return clazz.cast(deserializeJob(xml));
        case "Config": //NODEPARAMS
            return clazz.cast(deserializeNodeParams(xml));
        default:
            return Globals.xmlMapper.readValue(xml, clazz);
        }
    }

    public static <T> T deserialize(Document doc, Class<T> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException, DocumentException {
        switch (clazz.getSimpleName()) {
        case "job":
            return clazz.cast(deserializeJob(doc));
        case "Config": //NODEPARAMS
            return clazz.cast(deserializeNodeParams(doc));
        default:
            return Globals.xmlMapper.readValue(doc.asXML(), clazz);
        }
    }

    private static Job deserializeJob(byte[] xml) throws DocumentException, JsonParseException, JsonMappingException, IOException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document doc = reader.read(new ByteArrayInputStream(xml));
        return deserializeJob(doc);
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
        Job job = Globals.xmlMapper.readValue(doc.asXML(), Job.class);
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
    
    private static Config deserializeNodeParams(byte[] xml) throws JsonParseException, JsonMappingException, IOException, DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document doc = reader.read(new ByteArrayInputStream(xml));
        return deserializeNodeParams(doc);
    }
    
    private static Config deserializeNodeParams(Document doc) throws JsonParseException, JsonMappingException, IOException {
        @SuppressWarnings("unchecked")
        List<Node> notes = new ArrayList<Node>(doc.selectNodes("//note"));
        for (Node note : notes) {
            note.getParent().remove(note);
        }
        return Globals.xmlMapper.readValue(doc.asXML(), Config.class);
    }
}
