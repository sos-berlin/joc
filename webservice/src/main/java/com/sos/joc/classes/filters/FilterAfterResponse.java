package com.sos.joc.classes.filters;

import java.nio.file.Paths;
import java.util.regex.Pattern;

public class FilterAfterResponse {

    public static boolean matchReqex(String regex, String path) {
        if (regex == null) {
            return true;
        }
        return Pattern.compile(regex).matcher(path).find();
    }
    
    public static boolean isLocatedInSubFolder(String refFolder, String jobSchedulerObject) {
        if (refFolder == null) {
            return false;
        }
        return Paths.get(jobSchedulerObject).getParent().getNameCount() > Paths.get(refFolder).getNameCount();
    }

}
