package com.sos.joc.exceptions;


public class JocException extends Exception {

    private static final long serialVersionUID = 1L;
    private JocError error;
    
    public JocException() {
        super();
    }

    public JocException(JocError error) {
        super(error.toString());
        this.error = error;
    }

    public JocException(JocError error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }

   public JocException(JocError error, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(error.toString(), cause, enableSuppression, writableStackTrace);
        this.error = error;
    }

    public JocError getError() {
        return error;
    }

    public void setError(JocError error) {
        this.error = error;
    }

}
