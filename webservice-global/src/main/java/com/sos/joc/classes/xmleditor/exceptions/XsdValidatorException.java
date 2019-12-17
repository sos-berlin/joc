package com.sos.joc.classes.xmleditor.exceptions;

import org.xml.sax.SAXParseException;

public class XsdValidatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String elementName;
    private final String elementPosition;
    private final int elementDepth;
    private final int lineNumber;
    private final int columnNumber;

    public XsdValidatorException(SAXParseException cause, String name, String position, int depth) {
        super(cause);
        elementName = name;
        elementPosition = position;
        elementDepth = depth;
        lineNumber = cause.getLineNumber();
        columnNumber = cause.getColumnNumber();

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
