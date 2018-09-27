package com.sos.joc.db.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;

public class CalendarUsageConfiguration {
    
    private Calendar c;

    public CalendarUsageConfiguration(String basedOn, String type, String configuration) throws JsonParseException, JsonMappingException, IOException {
        this.c = new ObjectMapper().readValue(configuration, Calendar.class);
        this.c.setBasedOn(basedOn);
        this.c.setType(CalendarType.fromValue(type));
    }
    
    public Calendar getCalendar() {
        return c;
    }

}
