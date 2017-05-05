package com.sos.auth.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sos.jitl.reporting.db.filter.FilterFolder;

public class SOSShiroFolderPermissions {

	private Map<String, ArrayList<FilterFolder>> listOfFoldersForInstance;
	private String schedulerId;

	public SOSShiroFolderPermissions() {
		super();
		listOfFoldersForInstance = new HashMap<String, ArrayList<FilterFolder>>();
	}

	private ArrayList<FilterFolder> getListOfFolders(String jobSchedulerId) {
        ArrayList<FilterFolder> retListOfFolders = new ArrayList<FilterFolder>();
		ArrayList<FilterFolder> listOfFolders = listOfFoldersForInstance.get(jobSchedulerId);
		if (listOfFolders != null) {
			retListOfFolders.addAll(listOfFolders);
		}
		ArrayList<FilterFolder> listOfFoldersDefault = listOfFoldersForInstance.get("");
		if (listOfFoldersDefault != null) {
			retListOfFolders.addAll(listOfFoldersDefault);
		}

		return retListOfFolders;
	}

	public void setFolders(String jobSchedulerId, String folders) {
		String[] stringlistOfFolders = folders.split(",");
		ArrayList<FilterFolder> listOfFolders = listOfFoldersForInstance.get(jobSchedulerId);
		if (listOfFolders == null) {
			listOfFolders = new ArrayList<FilterFolder>();
		}

		for (int i = 0; i < stringlistOfFolders.length; i++) {
			String f = stringlistOfFolders[i].trim();
			FilterFolder filterFolder = new FilterFolder();
			if (f.endsWith("/*")) {
				filterFolder.setRecursive(true);
				filterFolder.setFolder(("/" + f.trim()).replaceAll("//+", "/").replaceFirst("/\\*$", ""));
			} else {
				filterFolder.setRecursive(false);
				filterFolder.setFolder(f);
			}
			listOfFolders.add(filterFolder);
		}
		listOfFoldersForInstance.put(jobSchedulerId, listOfFolders);
	}

	public boolean isPermittedForFolder(String folder) {
		ArrayList<FilterFolder> listOfFolders = getListOfFolders(schedulerId);

		if (listOfFolders == null || listOfFolders.size() == 0) {
			return true;
		}

		for (int i = 0; i < listOfFolders.size(); i++) {
			FilterFolder f = listOfFolders.get(i);
			if (f.isRecursive()) {
				if (folder.startsWith(f.getFolder())) {
					return true;
				}
			} else {
				if (f.getFolder().equals(folder)) {
					return true;
				}
			}
		}
		return false;
	}

	public int size() {
		ArrayList<FilterFolder> listOfFolders = getListOfFolders(schedulerId);
		return listOfFolders.size();
	}

	public FilterFolder get(int i) {
		ArrayList<FilterFolder> listOfFolders = getListOfFolders(schedulerId);
		return listOfFolders.get(i);
	}

	public String getFolderPermissionsWhere(String field) {
		ArrayList<FilterFolder> listOfFolders = getListOfFolders(schedulerId);
		String s = "1=1";
		for (int i = 0; i < listOfFolders.size(); i++) {
			FilterFolder f = listOfFolders.get(i);
			if (f.isRecursive()) {
				s = s + " and " + field + " like " + f.getFolder();
			} else {
				s = s + " and " + field + " = " + f.getFolder();
			}
		}
		return s;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

}
