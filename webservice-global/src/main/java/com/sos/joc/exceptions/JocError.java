package com.sos.joc.exceptions;


public class JocError {
    
    private String code = "JOC-420";
    private String message = "";

    public JocError() {
    }

    public JocError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return String.format("%1$s: %2$s", code, message);
    }

}
