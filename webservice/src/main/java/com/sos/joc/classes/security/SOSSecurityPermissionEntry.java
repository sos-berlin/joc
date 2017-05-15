package com.sos.joc.classes.security;

public class SOSSecurityPermissionEntry {

    private boolean excluded;
    private String permission;
    private String normalizedPermission;
    private String master;

    public SOSSecurityPermissionEntry(String permission) {
        super();
        this.permission = permission.trim();
        this.master = extractMaster();
        this.normalizedPermission = normalizePermission();
    }

    
    public String getMaster() {
        return master;
    }

    public boolean isExcluded() {
        return excluded;
    }

    public String getPermission() {
        return permission;
    }

    public String getNormalizedPermission() {
        return normalizedPermission;
    }

    private String extractMaster() {
        String s = permission;
        if (permission.startsWith("-")) {
            s=permission.substring(1);
        }
            
        
        if (s.startsWith("sos:products")) {
            return "";
        } else {
            return s.replaceAll("^(.*):sos:products:.*$", "$1");
        }
    }

    private String normalizePermission() {
        if (permission.startsWith("-")) {
            permission = permission.substring(1);
            excluded = true;
        }
        return permission.replaceAll("^.*:(sos:products:.*$)", "$1");

    }

}
