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
        setCode(WebserviceConstants.DEFAULT_ERROR_CODE);
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
            metaInfo.add(s);
        }
    }
    
    @JsonIgnore
    public void addMetaInfoOnTop(String ...metaInfos) {
        List<String> onTop = new ArrayList<String>();
        for(String s : metaInfos) {
            onTop.add(s);
        }
        if (!onTop.isEmpty()) {
            metaInfo.addAll(0,onTop); 
        }
    }
    
    @JsonIgnore
    public void addMetaInfoOnTop(List<String> metaInfos) {
        if (metaInfos != null && !metaInfos.isEmpty()) {
            metaInfo.addAll(0,metaInfos); 
        }
    }
    
    @JsonIgnore
    public List<String> getMetaInfo() {
        return metaInfo;
    }
    
    @JsonIgnore
    public String printMetaInfo() {
        StringBuilder s = new StringBuilder();
        for (String str : metaInfo) {
            s.append(str).append("\n");
        }
        return s.toString().replaceFirst("\\s+$", "");
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        String m = getMessage();
        if (m != null && !m.isEmpty()) {
            s.append(getMessage()).append(" ");
        }
        for (String str : metaInfo) {
            s.append(str).append(" ");
        }
        return s.toString().trim();
    }
}
