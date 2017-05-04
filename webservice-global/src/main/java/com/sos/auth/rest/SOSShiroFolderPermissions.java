package com.sos.auth.rest;

import java.util.ArrayList;

import com.sos.jitl.reporting.db.filter.FilterFolder;

public class SOSShiroFolderPermissions {


	private ArrayList<FilterFolder> listOfFolders;

	public SOSShiroFolderPermissions() {
		super();

		listOfFolders = new ArrayList<FilterFolder>();
	}

	public void setFolders(String folders) {
		String[] stringlistOfFolders = folders.split(",");

		for (int i = 0; i < stringlistOfFolders.length; i++) {
			String f = stringlistOfFolders[i].trim();
			FilterFolder filterFolder = new FilterFolder();
			if (f.endsWith("/*")){
			   	filterFolder.setRecursive(true);
			   	filterFolder.setFolder(("/" + f.trim()).replaceAll("//+", "/").replaceFirst("/\\*$", ""));
			}else{
			   	filterFolder.setRecursive(false);
			   	filterFolder.setFolder(f);
			}
			listOfFolders.add(filterFolder);
		}
	}

	public boolean isPermittedForFolder(String folder) {
		if (listOfFolders == null || listOfFolders.size() == 0){
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

	public int size(){
		return listOfFolders.size();
	}
	
	public FilterFolder get(int i){
		return listOfFolders.get(i);
	}
	
	public String getFolderPermissionsWhere(String field) {
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

}
