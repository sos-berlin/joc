package com.sos.joc.classes.security;

import com.sos.joc.model.security.SecurityConfigurationPermission;

public class SOSSecurityPermissionItem {

    private Boolean excluded;
    private String permission;
    private String normalizedPermission;
    private String master;

    public SOSSecurityPermissionItem(String permission) {
        super();
        this.permission = permission.trim();
        this.master = extractMaster();
        this.normalizedPermission = normalizePermission();
    }
    
    public SOSSecurityPermissionItem(String master, SecurityConfigurationPermission securityConfigurationPermission){
        super();
        this.permission = securityConfigurationPermission.getPath();
        this.excluded = securityConfigurationPermission.getExcluded();
        this.master = master;
        this.normalizedPermission = permission;
    }

    public String getIniValue(){
        String s = "";
        if (isExcluded()){
            s= "-";
        }
        if ("".equals(master)){
            return s + permission;
        }else{
            return s + master + ":" + permission;
        }
    }
    
    public String getMaster() {
        return master;
    }

    public Boolean isExcluded() {
        if(excluded == null) {
            excluded = Boolean.FALSE;
        }
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
            return s.replaceAll("^(.*):sos:products(:.+)*.*$", "$1");
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
