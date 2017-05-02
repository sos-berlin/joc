package com.sos.auth.rest;

import java.util.ArrayList;

public class SOSShiroFolderPermissions {

	public class FolderItem{
		protected boolean recursive;
		protected String folder;
		
		public boolean isRecursive() {
			return recursive;
		}
		public String getFolder() {
			return folder;
		}
	
	}
	
	private ArrayList<FolderItem> listOfFolders;

	public SOSShiroFolderPermissions() {
		super();

		listOfFolders = new ArrayList<FolderItem>();
	}

	public void setFolders(String folders) {
		String[] stringlistOfFolders = folders.split(",");

		for (int i = 0; i < stringlistOfFolders.length; i++) {
			String f = stringlistOfFolders[i].trim();
			FolderItem folderItem = new FolderItem();
			if (f.endsWith("*")){
			   	folderItem.recursive = true;
			   	folderItem.folder=f.replace("*", "");
			}else{
			   	folderItem.recursive = false;
			   	folderItem.folder=f;
			}
			listOfFolders.add(folderItem);
		}
	}

	public boolean isPermittedForFolder(String folder) {
		if (listOfFolders == null || listOfFolders.size() == 0){
			return true;
		}
		
		for (int i = 0; i < listOfFolders.size(); i++) {
			FolderItem f = listOfFolders.get(i);
			if (f.recursive) {
				if (folder.startsWith(f.folder)) {
					return true;
				}
			} else {
				if (f.equals(folder)) {
					return true;
				}
			}
		}
		return false;
	}

	public int size(){
		return listOfFolders.size();
	}
	
	public FolderItem get(int i){
		return listOfFolders.get(i);
	}
	
	public String getFolderPermissionsWhere(String field) {
		String s = "1=1";
		for (int i = 0; i < listOfFolders.size(); i++) {
			FolderItem f = listOfFolders.get(i);
			if (f.recursive) {
				s = s + " and " + field + " like " + f.folder;
			} else {
				s = s + " and " + field + " = " + f.folder;
			}
		}
		return s;
	}

}
