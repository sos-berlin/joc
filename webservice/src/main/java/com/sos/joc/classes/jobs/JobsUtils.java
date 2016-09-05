package com.sos.joc.classes.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.JobsFilterSchema;


public class JobsUtils {

    private static SimpleDateFormat SDF = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private static SimpleDateFormat SDF2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    public static Boolean getBoolValue(final String value) {
        if("yes".equalsIgnoreCase(value)){
            return true;
        } else if("no".equalsIgnoreCase(value)){
            return false;
        }
        return null;
    }
    
    public static Date getDateFromString(final String dateString) throws Exception{
        Date date = null;
        if (!dateString.contains("T")) {
            date = SDF.parse(dateString);
        } else {
            date = SDF2.parse(dateString);
        }
        return date;
    }
    
    public static String createPostCommand(final JobsFilterSchema body) {
        StringBuilder postCommand = new StringBuilder();
        boolean compact = body.getCompact();
        postCommand.append("<commands>");
        if (!body.getFolders().isEmpty()) {
            for (FoldersSchema folder : body.getFolders()) {
                postCommand.append("<show_state subsystems=\"job folder\" what=\"job_orders task_queue");
                if(!compact){
                    postCommand.append(" job_params");
                }
                String path = folder.getFolder();
                Boolean recursive = folder.getRecursive();
                if(!recursive) {
                    postCommand.append(" no_subfolders");
                }
                postCommand.append("\" ");
                postCommand.append("path=\"").append(path).append("\"/>");
            }
        } else {
            postCommand.append("<show_state subsystems=\"job\" what=\"job_orders task_queue");
            if(!compact){
                postCommand.append(" job_params");
            }
            postCommand.append("\" path=\"/\"/>");
        }
        postCommand.append("</commands>");
        return postCommand.toString();
    }

    public static Integer getSeverityFromStateText(String stateText) {
        switch(stateText.toLowerCase()) {
        case "running":
            return 0;
        case "pending":
            return 1;
        case "not_initialized":
        case "waiting_for_agent":
        case "stopping":
        case "stopped":
        case "removed":
            return 2;
        case "initialized": 
        case "loaded": 
        case "waiting_for_process": 
        case "waiting_for_lock": 
        case "waiting_for_task": 
        case "not_in_period": 
            return 3;
        case "disabled": 
            return 4;
        }
        return null;
    }
}