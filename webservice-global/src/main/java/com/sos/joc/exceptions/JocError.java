package com.sos.joc.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.Err;

public class JocError extends Err{
    
    @JsonIgnore
    private List<String> metaInfo = new ArrayList<String>();

    @JsonIgnore
    public JocError() {
        setCode("JOC-420");
    }

    @JsonIgnore
    public JocError(String message) {
        setCode(WebserviceConstants.DEFAULT_ERROR_CODE);
        setMessage(message);
    }

    @JsonIgnore
    public JocError(String code, String message) {
        setCode(code);
        setMessage(message);
    }
    
    @JsonIgnore
    public JocError(String code, String message, String ...metaInfos) {
        setCode(code);
        setMessage(message);
        appendMetaInfo(metaInfos);
    }

    @JsonIgnore
    public void appendMetaInfo(String ...metaInfos) {
        for(String s : metaInfos) {
            metaInfo.add(s + "\n");
        }
    }
    
    @JsonIgnore
    public void addMetaInfoOnTop(String ...metaInfos) {
        List<String> onTop = new ArrayList<String>();
        for(String s : metaInfos) {
            onTop.add(s + "\n");
        }
        if (onTop.size() > 0) {
            metaInfo.addAll(0,onTop); 
        }
    }
    
    @JsonIgnore
    public String printMetaInfo() {
        StringBuilder s = new StringBuilder();
        for (String str : metaInfo) {
            s.append(str);
        }
        return s.toString();
    }
}
