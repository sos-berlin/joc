package com.sos.auth.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sos.joc.model.common.Folder;

public class SOSShiroFolderPermissions {

    private Map<String, Set<Folder>> listOfFoldersForInstance;
    private String objectFilter = "";
    private String schedulerId;
    private boolean force = false;

    public SOSShiroFolderPermissions() {
        listOfFoldersForInstance = new HashMap<String, Set<Folder>>();
    }

    public SOSShiroFolderPermissions(String objectFilter) {
        listOfFoldersForInstance = new HashMap<String, Set<Folder>>();
        this.objectFilter = objectFilter;
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
            if (f == null || f.isEmpty()) {
                continue;
            }
            Folder filterFolder = new Folder();
            filterFolder.setRecursive(f.endsWith("/*"));
            filterFolder.setFolder(normalizeFolder(f));
            if (!objectFilter.isEmpty()) {
                if (filterFolder.getFolder().startsWith("/*" + objectFilter)) {
                    String g = filterFolder.getFolder();
                    g = g.replaceFirst("/\\*" + objectFilter, "");
                    filterFolder.setFolder(g);
                    listOfFolders.add(filterFolder);
                }
            } else {
                if (!filterFolder.getFolder().startsWith("/*" + objectFilter)) {
                    listOfFolders.add(filterFolder);
                }

            }
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

    public List<Folder> getPermittedFolders(List<Folder> folders) {
        return getPermittedFolders(folders, getListOfFolders());
    }

    public List<Folder> getPermittedFolders(List<Folder> folders, Set<Folder> listOfFolders) {
        List<Folder> permittedFolders = new ArrayList<Folder>();
        if (folders != null && !folders.isEmpty()) {
            for (Folder folder : folders) {
                permittedFolders.addAll(getPermittedFolders(folder, listOfFolders));
            }
        } else if (listOfFolders != null) {
            permittedFolders.addAll(listOfFolders);
        }
        return permittedFolders;
    }

    public Set<Folder> getPermittedFolders(Set<Folder> folders) {
        return getPermittedFolders(folders, getListOfFolders());
    }

    public Set<Folder> getPermittedFolders(Set<Folder> folders, Set<Folder> listOfFolders) {
        Set<Folder> permittedFolders = new HashSet<Folder>();
        if (folders != null && !folders.isEmpty()) {
            for (Folder folder : folders) {
                permittedFolders.addAll(getPermittedFolders(folder, listOfFolders));
            }
        } else {
            permittedFolders = listOfFolders;
        }
        return permittedFolders;
    }

    public Set<Folder> getPermittedFolders(Folder folder, Set<Folder> listOfFolders) {
        Set<Folder> permittedFolders = new HashSet<Folder>();
        if (this.force) {
            permittedFolders.add(folder);
            return permittedFolders;
        }
        if (listOfFolders == null || listOfFolders.isEmpty()) {
            permittedFolders.add(folder);
            return permittedFolders;
        }

        folder.setFolder(normalizeFolder(folder.getFolder()));
        if (listOfFolders.contains(folder)) {
            permittedFolders.add(folder);
            return permittedFolders;
        }
        String folderPathWithSlash = (folder.getFolder() + "/").replaceAll("//+", "/");
        if (folder.getRecursive()) {
            for (Folder f : listOfFolders) {
                if (f.getRecursive()) {
                    if (f.getFolder().startsWith(folderPathWithSlash)) {
                        permittedFolders.add(f);
                    } else if (folder.getFolder().startsWith((f.getFolder() + "/").replaceAll("//+", "/"))) {
                        permittedFolders.add(folder);
                    }
                } else {
                    if (f.getFolder().equals(folder.getFolder())) {
                        permittedFolders.add(f);
                    } else if (f.getFolder().startsWith(folderPathWithSlash)) {
                        permittedFolders.add(f);
                    }
                }
            }
        } else {
            for (Folder f : listOfFolders) {
                if (f.getFolder().equals(folder.getFolder())) {
                    permittedFolders.add(folder);
                    break;
                }
                if (f.getRecursive() && folder.getFolder().startsWith((f.getFolder() + "/").replaceAll("//+", "/"))) {
                    permittedFolders.add(folder);
                    break;
                }
            }
        }
        return permittedFolders;
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
            if (f.getRecursive() && "/".equals(f.getFolder())) {
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
