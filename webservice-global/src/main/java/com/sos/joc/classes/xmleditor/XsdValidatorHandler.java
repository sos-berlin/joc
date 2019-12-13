package com.sos.joc.classes.xmleditor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XsdValidatorHandler extends DefaultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidatorHandler.class);
    private final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private int currentDepth;
    private int lastDepth;
    private String currentElement;
    private String errorElement;
    private String errorElementPosition;
    // key - depth, value - count elements
    private Map<Integer, Integer> depths = new LinkedHashMap<Integer, Integer>();

    @Override
    public void startElement(String uri, String localName, String qname, Attributes attr) {
        currentElement = qname;
        currentDepth++;

        if (currentDepth < lastDepth) {
            List<Integer> toReset = depths.keySet().stream().filter(x -> x > currentDepth).collect(Collectors.toList());
            for (Integer depthKey : toReset) {
                depths.put(depthKey, 0);
            }
        }

        if (depths.containsKey(currentDepth)) {
            depths.put(currentDepth, (depths.get(currentDepth) + 1));
        } else {
            depths.put(currentDepth, 1);
        }
        lastDepth = currentDepth;

        if (isDebugEnabled) {
            LOGGER.debug(String.format("[depth=%s][%s][localName=%s][uri=%s]", currentDepth, currentElement, localName, uri));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qname) {
        currentDepth--;
    }

    @Override
    public void warning(SAXParseException e) {
        LOGGER.warn(e.toString(), e);
    }

    @Override
    public void error(SAXParseException e) throws SAXParseException {
        handlerError(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXParseException {
        handlerError(e);
    }

    private void handlerError(SAXParseException e) throws SAXParseException {
        errorElement = currentElement;
        errorElementPosition = getErrorElementPosition();
        throw e;
    }

    private String getErrorElementPosition() {
        return depths.values().stream().map(val -> val.toString()).collect(Collectors.joining("-"));
    }

    public int getErrorDepth() {
        return depths.size();
    }

    public String getErrorElement() {
        return errorElement;
    }

    public String getErrorElementPostion() {
        return errorElementPosition;
    }
}
