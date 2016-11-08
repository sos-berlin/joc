package com.sos.joc.classes.processclasses;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassV;

public class ProcessClassesVCallable implements Callable<List<ProcessClassV>> {

    private final String processClass;
    private final String regex;
    private final Folder folder;
    private final URI uri;
    private final String accessToken;

    public ProcessClassesVCallable(String processClass, URI uri, String accessToken) {
        this.processClass = processClass;
        this.regex = null;
        this.folder = null;
        this.uri = uri;
        this.accessToken = accessToken;
    }

    public ProcessClassesVCallable(Folder folder, String regex, URI uri, String accessToken) {
        this.processClass = null;
        this.regex = regex;
        this.folder = folder;
        this.uri = uri;
        this.accessToken = accessToken;
    }

    @Override
    public List<ProcessClassV> call() throws JocException {
        if (processClass != null) {
            return getProcessClasses(processClass, uri, accessToken);
        } else {
            return getProcessClasses(folder, regex, uri, accessToken);
        }
    }
    
    public List<ProcessClassV> getProcessClasses() throws JocException {
        return getProcessClasses(new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(folder), accessToken), regex); 
    }

    private List<ProcessClassV> getProcessClasses(Folder folder, String regex, URI uri, String accessToken) throws JocException {
        return getProcessClasses(new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(folder), accessToken), regex);
    }

    private List<ProcessClassV> getProcessClasses(String processClass, URI uri, String accessToken) throws JocException {
        return getProcessClasses(new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(processClass), accessToken));
    }

    private List<ProcessClassV> getProcessClasses(JsonObject json) throws JobSchedulerInvalidResponseDataException {
        return getProcessClasses(json, null);
    }

    private List<ProcessClassV> getProcessClasses(JsonObject json, String regex) throws JobSchedulerInvalidResponseDataException {
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        Map<String, ProcessClassV> mapProcessClasses = new HashMap<String, ProcessClassV>();
        if (json.containsKey("elements")) {
            for (JsonObject processClassItem : json.getJsonArray("elements").getValuesAs(JsonObject.class)) {
                ProcessClassVolatile processClass = new ProcessClassVolatile(processClassItem);
                processClass.setPath();
                if (!FilterAfterResponse.matchRegex(regex, processClass.getPath())) {
                    continue;
                }
                processClass.setFields();
                processClass.setSurveyDate(surveyDate);
                mapProcessClasses.put(processClass.getPath(), processClass);
            }
        } else {
            ProcessClassVolatile processClass = new ProcessClassVolatile(json);
            processClass.setFields();
            mapProcessClasses.put(processClass.getPath(), processClass);
        }
        return new ArrayList<ProcessClassV>(mapProcessClasses.values());
    }

    private String getServiceBody(String processClass) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        processClass = ("/" + processClass.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        if (processClass == "/(default)") {
            processClass = "";
        }
        builder.add("path", processClass);
        return builder.build().toString();
    }

    private String getServiceBody(Folder folder) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String path = folder.getFolder();
        if (path != null && !path.isEmpty()) {
            path = ("/" + path.trim() + "/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
            }
            builder.add("path", path);
        }
        return builder.build().toString();
    }
}
