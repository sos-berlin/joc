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
    
    public JocException(Throwable cause) {
        super(cause);
        this.error = new JocError();
        error.setMessage(cause.getMessage());
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
    
    public void setErrorMessage(String message) {
        if (this.error == null) {
            this.error = new JocError(); 
        }
        this.error.setMessage(message);
    }
    
    public void addErrorMetaInfo(String ...metaInfo) {
        if (this.error == null) {
            this.error = new JocError(); 
        }
        this.error.addMetaInfo(metaInfo);
    }
    
    public void setErrorMessageAndAddMetaInfo(String message, String ...metaInfo) {
        setErrorMessage(message);
        this.error.addMetaInfo(metaInfo);
    }

}
