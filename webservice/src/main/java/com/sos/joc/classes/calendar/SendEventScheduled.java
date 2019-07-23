package com.sos.joc.classes.calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.jobscheduler.model.event.CustomEventVariables;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;

public class SendEventScheduled implements Runnable {

    private String orderPath;
    private JOCXmlCommand jocXmlCommand;
    private String accessToken;
    private Long seconds;

    public SendEventScheduled(String orderPath, JOCXmlCommand jocXmlCommand, String accessToken) {
        this.orderPath = orderPath;
        this.jocXmlCommand = jocXmlCommand;
        this.accessToken = accessToken;
        this.seconds = 0L;
    }
    
    public SendEventScheduled(String orderPath, JOCXmlCommand jocXmlCommand, String accessToken, Long seconds) {
        this.orderPath = orderPath;
        this.jocXmlCommand = jocXmlCommand;
        this.accessToken = accessToken;
        this.seconds = seconds < 0L ? 0L : seconds;
    }
    
    public Long getSeconds() {
        return this.seconds;
    }

    @Override
    public void run() {
        try {
            CustomEvent customEvt = new CustomEvent();
            customEvt.setKey("OrderStarted");
            CustomEventVariables vars = new CustomEventVariables();
            vars.setAdditionalProperty("path", orderPath);
            customEvt.setVariables(vars);
            jocXmlCommand.executePostWithRetry("<publish_event>" + new ObjectMapper().writeValueAsString(customEvt) + "</publish_event>",
                    accessToken);
        } catch (JsonProcessingException e) {
        } catch (JocException e) {
        }
    }

}
