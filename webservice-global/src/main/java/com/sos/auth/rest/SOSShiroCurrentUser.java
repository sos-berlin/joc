package com.sos.auth.rest;

import java.io.File;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMaster;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMasters;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMaster;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMasters;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCJsonCommand;

public class SOSShiroCurrentUser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSShiroCurrentUser.class);
    private Subject currentSubject;
    private String username;
    private String password;
    private String accessToken;
    private String authorization;
    private Boolean haveAnyIpPermission;
    private HttpServletRequest httpServletRequest;

    private SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters;
    private SOSPermissionCommandsMasters sosPermissionCommandsMasters;
    private Map<String, DBItemInventoryInstance> listOfSchedulerInstances;
    private Map<String, SOSPermissionJocCockpit> listOfSOSPermissionJocCockpit;
    private Map<String, SOSPermissionCommands> listOfSOSPermissionCommands;
    private SOSShiroFolderPermissions sosShiroFolderPermissions;
    private SOSShiroFolderPermissions sosShiroCalendarFolderPermissions;
    private Set<JOCJsonCommand> jocJsonCommands;

    public SOSShiroCurrentUser(String username, String password) {
        super();
        initFolders();
        this.listOfSchedulerInstances = new HashMap<String, DBItemInventoryInstance>();
        this.username = username;
        this.password = password;
    }

    public SOSShiroCurrentUser(String username, String password, String authorization) {
        super();
        this.listOfSchedulerInstances = new HashMap<String, DBItemInventoryInstance>();
        this.username = username;
        this.authorization = authorization;
        this.password = password;
    }

    private Boolean getHaveAnyIpPermission() {
        if (this.haveAnyIpPermission == null) {

            if (currentSubject != null) {
                SOSListOfPermissions sosListOfPermissions = new SOSListOfPermissions(this, false);

                this.haveAnyIpPermission = false;
                String[] ipParts = this.getCallerIpAddress().split("\\.");
                for (String p : sosListOfPermissions.getSosPermissionShiro().getSOSPermissions().getSOSPermissionListJoc().getSOSPermission()) {
                    if (this.haveAnyIpPermission) {
                        break;
                    }
                    String es = "ip=";

                    for (int i = 0; i < ipParts.length; i++) {
                        es = es + ipParts[i];
                        this.haveAnyIpPermission = currentSubject.isPermitted(es + ":" + p) || currentSubject.isPermitted("-" + es + ":" + p);
                        if (this.haveAnyIpPermission) {
                            break;
                        }
                        es = es + ".";
                    }

                }

            }
        }
        return this.haveAnyIpPermission;
    }

    public SOSPermissionJocCockpit getSosPermissionJocCockpit(String masterId) {
        if (listOfSOSPermissionJocCockpit == null) {
            initListOfSOSPermissionJocCockpit();
        }

        if (httpServletRequest != null) {
            String ip = getCallerIpAddress();
            String ipMasterKey = "ip=" + ip + ":" + masterId;
            String ipKey = "ip=" + ip;

            if (listOfSOSPermissionJocCockpit.containsKey(ipMasterKey)) {
                return listOfSOSPermissionJocCockpit.get(ipMasterKey);
            }

            if (listOfSOSPermissionJocCockpit.containsKey(ipKey)) {
                return listOfSOSPermissionJocCockpit.get(ipKey);
            }
        }

        if (listOfSOSPermissionJocCockpit.containsKey(masterId)) {
            return listOfSOSPermissionJocCockpit.get(masterId);
        } else {
            return listOfSOSPermissionJocCockpit.get("");
        }
    }

    private void initListOfSOSPermissionJocCockpit() {
        listOfSOSPermissionJocCockpit = new HashMap<String, SOSPermissionJocCockpit>();
        for (SOSPermissionJocCockpitMaster permission : sosPermissionJocCockpitMasters.getSOSPermissionJocCockpitMaster()) {
            listOfSOSPermissionJocCockpit.put(permission.getJobSchedulerMaster(), permission.getSOSPermissionJocCockpit());
        }
    }

    private void initListOfSOSPermissionCommands() {
        listOfSOSPermissionCommands = new HashMap<String, SOSPermissionCommands>();
        for (SOSPermissionCommandsMaster permission : sosPermissionCommandsMasters.getSOSPermissionCommandsMaster()) {
            listOfSOSPermissionCommands.put(permission.getJobSchedulerMaster(), permission.getSOSPermissionCommands());
        }

    }

    public SOSPermissionJocCockpitMasters getSosPermissionJocCockpitMasters() {
        return sosPermissionJocCockpitMasters;
    }

    public void setSosPermissionJocCockpitMasters(SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters) {
        this.sosPermissionJocCockpitMasters = sosPermissionJocCockpitMasters;
    }

    public SOSPermissionCommandsMasters getSosPermissionCommandsMasters() {
        return sosPermissionCommandsMasters;
    }

    public void setSosPermissionCommandsMasters(SOSPermissionCommandsMasters sosPermissionCommandsMasters) {
        this.sosPermissionCommandsMasters = sosPermissionCommandsMasters;
    }

    public SOSPermissionCommands getSosPermissionCommands(String masterId) {
        if (listOfSOSPermissionCommands == null) {
            initListOfSOSPermissionCommands();
        }
        return listOfSOSPermissionCommands.get(masterId);
    }

    public void setSosPermissionCommands(SOSPermissionCommandsMasters sosPermissionCommands) {
        this.sosPermissionCommandsMasters = sosPermissionCommands;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Subject getCurrentSubject() {
        return currentSubject;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCurrentSubject(Subject currentSubject) {
        this.currentSubject = currentSubject;
    }

    public boolean hasRole(String role) {
        if (currentSubject != null) {
            return currentSubject.hasRole(role);
        } else {
            return false;
        }
    }

    private boolean getExcludedMaster(String permission, String masterId) {
        boolean excluded = false;
        if (currentSubject != null) {
            Path path = Paths.get(permission.replace(':', '/'));
            int nameCount = path.getNameCount();
            for (int i = 0; i < nameCount - 1; i++) {
                if (excluded) {
                    break;
                }
                String s = path.subpath(0, nameCount - i).toString().replace(File.separatorChar, ':');
                excluded = currentSubject.isPermitted("-" + s) || currentSubject.isPermitted("-" + masterId + ":" + s);
            }
        }

        return excluded;
    }

    private boolean getExcludedIp(String permission, String masterId) {
        String[] ipParts = this.getCallerIpAddress().split("\\.");
        boolean excluded = false;
        String es = "";
        for (int i = 0; i < ipParts.length; i++) {
            es = es + ipParts[i];
            excluded = excluded || getExcludedMaster(permission, "ip=" + es) || getExcludedMaster(permission, "ip=" + es + ":" + masterId);
            if (excluded) {
                break;
            }
            es = es + ".";
        }
        return excluded;
    }

    private boolean getExcluded(String permission, String masterId) {
        if (this.getHaveAnyIpPermission()) {
            return this.getExcludedMaster(permission, masterId) || this.getExcludedIp(permission, masterId);
        } else {
            return this.getExcludedMaster(permission, masterId);
        }
    }

    public boolean testGetExcluded(String permission, String masterId) {
        return getExcluded(permission, masterId);
    }

    private boolean ipPermission(String permission, String master, String[] ipParts, int parts) {
        boolean b = false;
        String s = "";

        for (int i = 0; i < parts; i++) {
            s = s + ipParts[i] + ".";
        }
        s = s + ipParts[parts];

        b = (currentSubject.isPermitted("ip=" + s + ":" + permission) || currentSubject.isPermitted("ip=" + s + ":" + master + ":" + permission));
        return b;
    }

    private boolean handleIpPermission(String masterId, String permission) {

        String ipAddress = this.getCallerIpAddress();
        InetAddress inetAddress;
        boolean ipv6 = false;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
            ipv6 = inetAddress instanceof Inet6Address;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String[] ipParts = ipAddress.split("\\.");

        if ((ipParts.length < 4) && !ipv6 || (ipParts.length < 8 && ipv6)) {
            LOGGER.warn("Wrong ip address found: " + ipAddress);
            return false;
        }

        return ipPermission(permission, masterId, ipParts, 0) || ipPermission(permission, masterId, ipParts, 1) || ipPermission(permission, masterId,
                ipParts, 2) || ipPermission(permission, masterId, ipParts, 3);

    }

    private boolean getPermissionFromSubject(String masterId, String permission) {
        if (this.getHaveAnyIpPermission()) {
            return (handleIpPermission(masterId, permission) || currentSubject.isPermitted(permission) || currentSubject.isPermitted(masterId + ":"
                    + permission)) && !getExcluded(permission, masterId);
        } else {
            return (currentSubject.isPermitted(permission) || currentSubject.isPermitted(masterId + ":" + permission)) && !getExcluded(permission,
                    masterId);
        }
    }

    public boolean isPermitted(String masterId, String permission) {
        if (currentSubject != null) {
            return getPermissionFromSubject(masterId, permission);
        } else {
            return false;
        }
    }

    public boolean isPermitted(String permission) {
        if (currentSubject != null) {
            return getPermissionFromSubject("", permission);
        } else {
            return false;
        }
    }

    public boolean isAuthenticated() {
        if (currentSubject != null) {
            try {
                return currentSubject.isAuthenticated();
            } catch (UnknownSessionException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    public Map<String, DBItemInventoryInstance> getMapOfSchedulerInstances() {
        return listOfSchedulerInstances;
    }

    public DBItemInventoryInstance getSchedulerInstanceDBItem(String jobSchedulerId) {
        return listOfSchedulerInstances.get(jobSchedulerId);
    }

    public DBItemInventoryInstance removeSchedulerInstanceDBItem(String jobSchedulerId) {
        return listOfSchedulerInstances.remove(jobSchedulerId);
    }

    public void addSchedulerInstanceDBItem(String jobSchedulerId, DBItemInventoryInstance schedulerInstancesDBItem) {
        listOfSchedulerInstances.put(jobSchedulerId, schedulerInstancesDBItem);
    }

    public DBItemInventoryInstance getSchedulerInstanceByKey(Long id) {
        for (Map.Entry<String, DBItemInventoryInstance> entry : listOfSchedulerInstances.entrySet()) {
            DBItemInventoryInstance instance = entry.getValue();
            if (instance.getId().equals(id)) {
                return instance;
            }
        }
        return null;

    }

    public void initFolders() {
        sosShiroFolderPermissions = new SOSShiroFolderPermissions();
        sosShiroCalendarFolderPermissions = new SOSShiroFolderPermissions("calendar");
    }

    public void addFolder(String role, String folders) {
        if (sosShiroFolderPermissions == null) {
            this.initFolders();
        }

        String jobSchedulerId = "";
        if (role.contains("|")) {
            String[] s = role.split("\\|");
            if (s.length > 1) {
                jobSchedulerId = s[0];
                role = s[1];
            }
        }

        if (hasRole(role)) {
            LOGGER.debug(String.format("Adding folders %s for role %s", folders, role));
            sosShiroFolderPermissions.setFolders(jobSchedulerId, folders);
            sosShiroCalendarFolderPermissions.setFolders(jobSchedulerId, folders);
        }
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public Set<JOCJsonCommand> getJocJsonCommands() {
        return jocJsonCommands;
    }

    public void setJocJsonCommands(Set<JOCJsonCommand> jocJsonCommands) {
        this.jocJsonCommands = jocJsonCommands;
    }

    public SOSShiroFolderPermissions getSosShiroFolderPermissions() {
        return sosShiroFolderPermissions;
    }

    public SOSShiroFolderPermissions getSosShiroCalendarFolderPermissions() {
        return sosShiroCalendarFolderPermissions;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getCallerIpAddress() {
        if (httpServletRequest != null) {
            String s = httpServletRequest.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(s)) {
                return "127.0.0.1";
            } else {
                return s;
            }
        } else {
            return "0.0.0.0";
        }
    }

    public String getCallerHostName() {
        if (httpServletRequest != null) {
            return httpServletRequest.getRemoteHost();
        } else {
            return "";
        }
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

}
