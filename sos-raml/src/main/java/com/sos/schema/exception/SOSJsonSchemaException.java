package com.sos.schema.exception;

import com.sos.exception.SOSInvalidDataException;

public class SOSJsonSchemaException extends SOSInvalidDataException {

    private static final long serialVersionUID = 1L;
    
    public SOSJsonSchemaException() {
        super();
    }

    public SOSJsonSchemaException(String message) {
        super(message);
    }
    
    public SOSJsonSchemaException(Throwable cause) {
        super(cause);
    }
    
    public SOSJsonSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOSJsonSchemaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
