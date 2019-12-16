package com.sos.joc.classes.xmleditor.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.sos.joc.classes.xmleditor.exceptions.XsdValidatorException;

public class XsdValidatorHandler extends DefaultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidatorHandler.class);
    private final boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private final String PATH_DELIMITER = "-";
    private final String REF_DELIMITER = ";";

    private int currentDepth;
    private int lastDepth;
    private String rootElement;
    private String currentElement;
    private SAXParseException error;
    // key - depth, value - count elements
    private Map<Integer, Integer> currentDepths = new LinkedHashMap<Integer, Integer>();
    private Map<String, String> refElements = new HashMap<String, String>();

    @Override
    public void startElement(String uri, String localName, String qname, Attributes attr) {
        currentElement = qname;
        currentDepth++;

        if (currentDepth < lastDepth) {
            List<Integer> toReset = currentDepths.keySet().stream().filter(x -> x > currentDepth).collect(Collectors.toList());
            for (Integer depthKey : toReset) {
                // depths.put(depthKey, 0);
                currentDepths.remove(depthKey);
            }
        } else {
            if (currentDepth == 1) {
                rootElement = currentElement;
            }
        }

        if (currentDepths.containsKey(currentDepth)) {
            currentDepths.put(currentDepth, (currentDepths.get(currentDepth) + 1));
        } else {
            currentDepths.put(currentDepth, 1);
        }
        lastDepth = currentDepth;

        if (isDebugEnabled) {
            LOGGER.debug(String.format("[depth=%s][%s][localName=%s][uri=%s]", currentDepth, currentElement, localName, uri));
        }

        if (currentElement.endsWith("Ref")) {
            if (attr != null) {
                String ref = attr.getValue("ref");
                if (ref != null) {
                    refElements.put(ref, new StringBuilder(currentElement).append(REF_DELIMITER).append(getCurrentElementPosition()).toString());
                }
            }
        }
        handleError();
    }

    @Override
    public void endElement(String uri, String localName, String qname) {
        currentDepth--;
        handleError();
    }

    @Override
    public void warning(SAXParseException e) {
        LOGGER.warn(e.toString(), e);
    }

    @Override
    public void error(SAXParseException e) throws SAXParseException {
        error = e;
    }

    @Override
    public void fatalError(SAXParseException e) throws XsdValidatorException {
        error = e;
        handleError();
    }

    private void handleError() throws XsdValidatorException {
        if (error != null) {
            String msg = error.getMessage();
            if (msg != null && msg.trim().startsWith("cvc-identity-constraint")) {
                List<String> l = Arrays.asList(msg.split(" ")).stream().filter(x -> x.startsWith("'")).collect(Collectors.toList());
                if (l.size() == 3) {
                    String key = l.get(1).replaceAll("'", "");
                    if (refElements.containsKey(key)) {
                        String[] arr = refElements.get(key).split(REF_DELIMITER);
                        String position = arr[1];
                        throw new XsdValidatorException(error, arr[0], position, position.split(PATH_DELIMITER).length);
                    }
                }
                throw new XsdValidatorException(error, rootElement, "1", 1);
            }
            throw new XsdValidatorException(error, currentElement, getCurrentElementPosition(), currentDepths.size());
        }
    }

    private String getCurrentElementPosition() {
        return currentDepths.values().stream().map(val -> val.toString()).collect(Collectors.joining(PATH_DELIMITER));
    }
}
