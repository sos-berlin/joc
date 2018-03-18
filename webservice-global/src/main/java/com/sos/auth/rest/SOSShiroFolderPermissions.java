package com.sos.auth.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sos.joc.model.common.Folder;

public class SOSShiroFolderPermissions {

    private Map<String, Set<Folder>> listOfFoldersForInstance;
    private String schedulerId;
    private boolean force = false;

    public SOSShiroFolderPermissions() {
        listOfFoldersForInstance = new HashMap<String, Set<Folder>>();
    }

    private Set<Folder> getListOfFolders(String jobSchedulerId) {
        Set<Folder> retListOfFolders = new HashSet<Folder>();
        if (jobSchedulerId != null) {
            Set<Folder> listOfFolders = listOfFoldersForInstance.get(jobSchedulerId);
            if (listOfFolders != null) {
                retListOfFolders.addAll(listOfFolders);
            }
        }
        Set<Folder> listOfFoldersDefault = listOfFoldersForInstance.get("");
        if (listOfFoldersDefault != null) {
            retListOfFolders.addAll(listOfFoldersDefault);
        }

        return retListOfFolders;
    }

    public void setFolders(String jobSchedulerId, String folders) {
        String[] stringlistOfFolders = folders.split(",");
        Set<Folder> listOfFolders = listOfFoldersForInstance.get(jobSchedulerId);
        if (listOfFolders == null) {
            listOfFolders = new HashSet<Folder>();
        }

        for (int i = 0; i < stringlistOfFolders.length; i++) {
            String f = stringlistOfFolders[i].trim();
            Folder filterFolder = new Folder();
            filterFolder.setRecursive(f.endsWith("/*"));
            filterFolder.setFolder(normalizeFolder(f));
            listOfFolders.add(filterFolder);
        }
        listOfFoldersForInstance.put(jobSchedulerId, listOfFolders);
    }

    private String normalizeFolder(String folder) {
        folder = ("/" + folder.trim()).replaceAll("//+", "/").replaceFirst("/\\*?$", "");
        if (folder.isEmpty()) {
            folder = "/";
        }
        return folder;
    }

    public boolean isPermittedForFolder(String folder) {
        return isPermittedForFolder(folder, getListOfFolders());
    }
    
    public boolean isPermittedForFolder(String folder, Set<Folder> listOfFolders) {
        if (this.force) {
            return true;
        }
        if (listOfFolders == null || listOfFolders.isEmpty()) {
            return true;
        }
        folder = normalizeFolder(folder);

        for (Folder f : listOfFolders) {
            if (f.getFolder().equals(folder)) {
                return true;
            }
            if (f.getRecursive() && folder.startsWith(f.getFolder() + "/")) {
                return true;
            }
        }
        return false;
    }

    public Set<Folder> getListOfFolders() {
        return getListOfFolders(schedulerId);
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

}
