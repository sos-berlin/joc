package com.sos.joc.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.common.Err;

public class JocError extends Err{
    
    @JsonIgnore
    private StringBuilder metaInfo = new StringBuilder();

    @JsonIgnore
    public JocError() {
        setCode("JOC-420");
    }

    @JsonIgnore
    public JocError(String message) {
        setCode("JOC-420");
        setMessage(message);
    }

    @JsonIgnore
    public JocError(String code, String message) {
        setCode(code);
        setMessage(message);
    }

    @JsonIgnore
    public void addMetaInfo(String ...str) {
        for(String s : str) {
            metaInfo.append(s + "\n");
        }
    }
    
    @JsonIgnore
    public String getMetaInfo() {
        return metaInfo.toString();
    }
}
