package com.sos.joc.exceptions;


public class JoeConfigurationException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public JoeConfigurationException() {
    }

    public JoeConfigurationException(Throwable cause) {
        super(cause);
    }

    public JoeConfigurationException(String message) {
        super(message);
    }
    
    public JoeConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
