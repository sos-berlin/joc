package com.sos.joc.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JobSchedulerDate {
    private SimpleDateFormat sdf = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private SimpleDateFormat sdf2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    
    public Date getDate(String dateString) throws ParseException{
        if (!dateString.contains("T")) {
            return sdf.parse(dateString);
        } else {
            return sdf2.parse(dateString);
            
        }
    }
            
}
