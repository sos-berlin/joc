package com.sos.joc.db.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;

public class CalendarUsageConfiguration {
    
    private Calendar c = null;

    public CalendarUsageConfiguration(String basedOn, String type, String configuration) {
        if (configuration != null) {
            try {
                this.c = new ObjectMapper().readValue(configuration, Calendar.class);
            } catch (Exception e) {
            }
        }
        if (this.c != null) {
            this.c.setBasedOn(basedOn);
            CalendarType ctype = CalendarType.WORKING_DAYS;
            if (type != null) {
                try {
                    ctype = CalendarType.fromValue(type);
                } catch (IllegalArgumentException e) {
                //
                } 
            }
            this.c.setType(ctype);
        }
    }
    
    public Calendar getCalendar() {
        return c;
    }

}
