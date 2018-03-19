package com.sos.auth.rest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

import com.sos.joc.model.common.Folder;

public class SOSShiroFolderPermissionTest {

    @Test
    public void testGetPermittedFolders() {
        
        SOSShiroFolderPermissions sosShiroFolderPermissions = new SOSShiroFolderPermissions();
        sosShiroFolderPermissions.setFolders("", "/a/b/*, /a/c/d");
        //sosShiroFolderPermissions.setFolders("", "/sos/dailyplan/*, /sos/housekeeping/*");
        
        Set<Folder> folders = new HashSet<Folder>();
        Folder f1 = new Folder();
        f1.setFolder("/a/b/c");
        f1.setRecursive(false);
        folders.add(f1);
        Set<Folder> permittedFolders = sosShiroFolderPermissions.getPermittedFolders(folders, sosShiroFolderPermissions.getListOfFolders());
        
        assertEquals("testGetPermittedFolders", folders, permittedFolders);
    }
    
    @Test
    public void testGetPermittedFolders2() {
        
        SOSShiroFolderPermissions sosShiroFolderPermissions = new SOSShiroFolderPermissions();
        sosShiroFolderPermissions.setFolders("", "");
        
        Set<Folder> folders = new HashSet<Folder>();
        Folder f1 = new Folder();
        f1.setFolder("/a/c/d");
        f1.setRecursive(true);
        folders.add(f1);
        Set<Folder> permittedFolders = sosShiroFolderPermissions.getPermittedFolders(folders, sosShiroFolderPermissions.getListOfFolders());
        
        assertEquals("testGetPermittedFolders2", folders, permittedFolders);
    }

}
