package com.sos.joc.xmleditor.exceptions;

import com.sos.exception.SOSException;

public class XmlNotMatchSchemaException extends SOSException {

    private static final long serialVersionUID = 1L;

    public XmlNotMatchSchemaException(String msg) {
        super(msg);
    }

}
