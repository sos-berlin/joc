package com.sos.joc.exceptions;


public class ForcedClosingHttpClientException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-410";

    public ForcedClosingHttpClientException() {
    }

    public ForcedClosingHttpClientException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public ForcedClosingHttpClientException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public ForcedClosingHttpClientException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public ForcedClosingHttpClientException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public ForcedClosingHttpClientException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public ForcedClosingHttpClientException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public ForcedClosingHttpClientException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
