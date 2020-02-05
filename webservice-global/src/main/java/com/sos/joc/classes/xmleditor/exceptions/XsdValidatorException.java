package com.sos.joc.classes.xmleditor.exceptions;

import org.xml.sax.SAXParseException;

public class XsdValidatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String elementName;
    private String elementPosition;
    private int elementDepth;
    private int lineNumber = 1;
    private int columnNumber = 1;

    public XsdValidatorException(SAXParseException cause, String name, String position, int depth) {
        super(cause);
        elementName = name;
        elementPosition = position;
        elementDepth = depth;
        if (cause != null) {
            lineNumber = cause.getLineNumber();
            columnNumber = cause.getColumnNumber();
        }

    }

    public String getElementName() {
        return elementName;
    }

    public String getElementPosition() {
        return elementPosition;
    }

    public int getElementDepth() {
        return elementDepth;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

}
