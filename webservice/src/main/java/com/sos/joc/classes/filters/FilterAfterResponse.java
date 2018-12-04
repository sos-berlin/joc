package com.sos.joc.classes.filters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.sos.joc.model.common.Folder;

public class FilterAfterResponse {
    
    public static boolean matchRegex(String regex, String path) {
        if (regex == null || regex.isEmpty()) {
            return true;
        }
        if (path == null) {
            return false;
        }
        return Pattern.compile(regex).matcher(path).find();
    }
    
    public static boolean matchRegex(Pattern pattern, String path) {
        if (pattern == null) {
            return true;
        }
        if (path == null) {
            return false;
        }
        return pattern.matcher(path).find();
    }
    
    public static boolean filterStateHasState(List<? extends Enum<?>> filterStates, Enum<?> state) {
        if (filterStates == null || filterStates.isEmpty()) {
            return true;
        }
        boolean filterStatesContainsState = false;
        for (Enum<?> filterState : filterStates) {
           if (filterState.name().equals(state.name())) {
               filterStatesContainsState = true;
               break;
           }
        }
        return filterStatesContainsState;
    }
    
    public static boolean folderContainsObject(Folder folder, Path objectPath) {
        boolean folderFound = false;
        Path folderPath = Paths.get(folder.getFolder());
        if (folder.getRecursive() != null && folder.getRecursive()) {
            if (objectPath.startsWith(folderPath)) {
                folderFound = true;
            }
        } else {
            if (objectPath.getParent().toString().equals(folderPath.toString())) {
                folderFound = true;
            }
        }
        return folderFound;
    }
    
    public static boolean matchRegex(String regex, Set<String> paths) {
        if (regex == null || regex.isEmpty()) {
            return true;
        }
        if (paths == null || paths.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        for (String path : paths) {
            if (p.matcher(path).find()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean matchRegex(Pattern pattern, Set<String> paths) {
        if (pattern == null) {
            return true;
        }
        if (paths == null || paths.isEmpty()) {
            return false;
        }
        for (String path : paths) {
            if (pattern.matcher(path).find()) {
                return true;
            }
        }
        return false;
    }
}
