package com.sos.joc.classes.xmleditor;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// not used
public class XsdErrorHandler implements ErrorHandler {

    private final List<SAXParseException> exceptions = new LinkedList<SAXParseException>();
    private final List<SAXParseException> warnings = new LinkedList<SAXParseException>();

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        warnings.add(exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        exceptions.add(exception);

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        exceptions.add(exception);
    }

}
