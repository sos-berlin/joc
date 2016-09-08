package com.sos.joc.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JobSchedulerDate {
    private static final SimpleDateFormat SDF = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    
    public static Date getDate(String dateString) throws ParseException{
        if (dateString != null) {
            if (!dateString.contains("T")) {
                return SDF.parse(dateString);
            } else {
                return SDF2.parse(dateString);
            }
        } else {
            return null;
        }
    }
            
}
