package com.sos.joc.classes;

import java.util.Date;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;

public class JoeConfigurationHandler {

    public JoeConfigurationHandler() {
        super();
    }

    public JoeConfigurationHandlerReturn read(String xPath, String command, JOCResourceImpl jocResourceImpl) throws Exception {
        JoeConfigurationHandlerReturn joeConfigurationHandlerReturn = new JoeConfigurationHandlerReturn();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);

        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, jocResourceImpl.getAccessToken());
        joeConfigurationHandlerReturn.setSourceNode(jocXmlCommand.getSosxml().selectSingleNode(xPath));
        Element fileBased = (Element) jocXmlCommand.getSosxml().selectSingleNode(joeConfigurationHandlerReturn.getSourceNode().getParentNode()
                .getParentNode(), "file_based");
        if (fileBased != null) {
            joeConfigurationHandlerReturn.setLastWrite(JobSchedulerDate.getDateFromISO8601String(fileBased.getAttribute("last_write_time")));
        }
        return joeConfigurationHandlerReturn;

    }

    public Date executeCommand(String command, JOCResourceImpl jocResourceImpl) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, jocResourceImpl.getAccessToken());
        return jocXmlCommand.getSurveyDate();

    }

    public byte[] getXmlConfiguration(JoeConfigurationHandlerReturn joeConfigurationHandlerReturn) throws JobSchedulerBadRequestException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return ConfigurationUtils.getSourceXmlBytes(joeConfigurationHandlerReturn.getSourceNode());
    }

    public XmlMapper getXmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return xmlMapper;
    }

}
