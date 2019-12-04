package com.sos.auth.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.ObjectFactory;
import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMaster;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMasters;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMaster;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMasters;
import com.sos.auth.rest.permission.model.SOSPermissionRoles;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;

public class SOSPermissionsCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSPermissionsCreator.class);
    private SOSShiroCurrentUser currentUser;
    private SOSPermissionRoles roles;
    private Ini ini;

    public SOSPermissionsCreator(SOSShiroCurrentUser currentUser) {
        super();
        this.currentUser = currentUser;
    }

    public void loginFromAccessToken(String accessToken) throws JocException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            if (Globals.jocWebserviceDataContainer.getCurrentUsersList() == null || Globals.jocWebserviceDataContainer.getCurrentUsersList().getUser(
                    accessToken) == null) {

                LOGGER.debug("loginFromAccessToken --> hand over session.");
                LOGGER.debug("loginFromAccessToken --> login with accessToken=" + accessToken);
                Globals.sosShiroProperties = new JocCockpitProperties();
                Globals.setProperties();

                sosHibernateSession = Globals.createSosHibernateStatelessConnection("JOC: loginFromAccessToken");
                LOGGER.debug("loginFromAccessToken --> hibernateSession created");
                SOSShiroIniShare sosShiroIniShare = new SOSShiroIniShare(sosHibernateSession);
                try {
                    sosShiroIniShare.provideIniFile();
                    LOGGER.debug("loginFromAccessToken --> ini file provided");
                } catch (IOException e) {
                    throw new JocException(e);
                } catch (SOSHibernateException e) {
                    throw new DBInvalidDataException(e);
                }
                sosHibernateSession.close();

                IniSecurityManagerFactory factory = Globals.getShiroIniSecurityManagerFactory();
                SecurityManager securityManager = factory.getInstance();
                SecurityUtils.setSecurityManager(securityManager);
                LOGGER.debug("loginFromAccessToken --> securityManager created");

                Subject subject = new Subject.Builder().sessionId(accessToken).buildSubject();
                LOGGER.debug("loginFromAccessToken --> subject created");
                if (subject.isAuthenticated()) {
                    LOGGER.debug(getClass().getName() + ": loginFromAccessToken --> subject is authenticated");
                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                    currentUser = new SOSShiroCurrentUser((String) subject.getPrincipals().getPrimaryPrincipal(), "", "");

                    if (Globals.jocWebserviceDataContainer.getCurrentUsersList() == null) {
                        Globals.jocWebserviceDataContainer.setCurrentUsersList(new SOSShiroCurrentUsersList());
                    }

                    LOGGER.debug("loginFromAccessToken --> removeTimedOutUser");
                    Globals.jocWebserviceDataContainer.getCurrentUsersList().removeTimedOutUser(currentUser.getUsername());

                    currentUser.setCurrentSubject(subject);
                    currentUser.setAccessToken(accessToken);

                    SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters = createJocCockpitPermissionMasterObjectList(accessToken);
                    LOGGER.debug("loginFromAccessToken --> JocCockpitPermissionMasterObjectList created");
                    currentUser.setSosPermissionJocCockpitMasters(sosPermissionJocCockpitMasters);
                    currentUser.initFolders();
                    LOGGER.debug(getClass().getName() + ": loginFromAccessToken --> folders initialized");

                    Section section = getIni().getSection("folders");
                    if (section != null) {
                        for (String role : section.keySet()) {
                            currentUser.addFolder(role, section.get(role));
                        }
                    }
                    SOSPermissionCommandsMasters sosPermissionCommandsMasters = createCommandsPermissionMasterObjectList(accessToken);
                    LOGGER.debug("loginFromAccessToken --> CommandsPermissionMasterObjectList created");

                    currentUser.setSosPermissionCommandsMasters(sosPermissionCommandsMasters);
                    Globals.jocWebserviceDataContainer.getCurrentUsersList().addUser(currentUser);
                }
            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void addSosPermissionJocCockpit(String masterId, SOSPermissionRoles sosPermissionRoles, Set<String> unique,
            SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters) {
        SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(currentUser);
        SOSPermissionJocCockpitMaster sosPermissionJocCockpitMaster = new SOSPermissionJocCockpitMaster();
        SOSPermissionJocCockpit sosPermissionJocCockpit = sosPermissionsCreator.getSosPermissionJocCockpit(masterId);
        sosPermissionJocCockpit.setSOSPermissionRoles(sosPermissionRoles);
        sosPermissionJocCockpit.setPrecedence(-1);
        sosPermissionJocCockpitMaster.setSOSPermissionJocCockpit(sosPermissionJocCockpit);
        sosPermissionJocCockpitMaster.setJobSchedulerMaster(masterId);
        if (!unique.contains(masterId)) {
            sosPermissionJocCockpitMasters.getSOSPermissionJocCockpitMaster().add(sosPermissionJocCockpitMaster);
            unique.add(masterId);
        }
    }

    public SOSPermissionJocCockpitMasters createJocCockpitPermissionMasterObjectList(String accessToken) throws JocException {

        Set<String> unique = new HashSet<String>();
        SOSHibernateSession session = Globals.createSosHibernateStatelessConnection("getSchedulerInstance");
        SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters = new SOSPermissionJocCockpitMasters();
        try {
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
            Globals.beginTransaction(session);
            List<DBItemInventoryInstance> listOfInstances = dbLayer.getInventoryInstances();
            Globals.rollback(session);
            session.close();
            SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(currentUser);
            SOSPermissionRoles sosPermissionRoles = sosPermissionsCreator.getRoles(true);

            addSosPermissionJocCockpit("", sosPermissionRoles, unique, sosPermissionJocCockpitMasters);
           
            for (DBItemInventoryInstance instance : listOfInstances) {
                addSosPermissionJocCockpit(instance.getSchedulerId(), sosPermissionRoles, unique, sosPermissionJocCockpitMasters);
            }
        } finally {
            Globals.disconnect(session);
            session = null;
        }
        return sosPermissionJocCockpitMasters;
    }

    public SOSPermissionCommandsMasters createCommandsPermissionMasterObjectList(String accessToken) throws JocException {
        Map<String, String> unique = new HashMap<String, String>();
        SOSHibernateSession session = Globals.createSosHibernateStatelessConnection("getSchedulerInstance");
        SOSPermissionCommandsMasters sosPermissionCommandsMasters = new SOSPermissionCommandsMasters();
        try {
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
            Globals.beginTransaction(session);
            List<DBItemInventoryInstance> listOfInstances = dbLayer.getInventoryInstances();
            Globals.rollback(session);
            session.close();
            SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(currentUser);

            SOSPermissionCommands sosPermissionCommands = sosPermissionsCreator.getSosPermissionCommands("");
            SOSPermissionCommandsMaster sosPermissionCommandsMaster = new SOSPermissionCommandsMaster();
            sosPermissionCommandsMaster.setSOSPermissionCommands(sosPermissionCommands);
            sosPermissionCommandsMaster.setJobSchedulerMaster("");
            sosPermissionCommandsMasters.getSOSPermissionCommandsMaster().add(sosPermissionCommandsMaster);
            unique.put("", "");

            for (DBItemInventoryInstance instance : listOfInstances) {
                sosPermissionCommandsMaster = new SOSPermissionCommandsMaster();

                sosPermissionCommands = sosPermissionsCreator.getSosPermissionCommands(instance.getSchedulerId());
                sosPermissionCommandsMaster.setSOSPermissionCommands(sosPermissionCommands);
                sosPermissionCommandsMaster.setJobSchedulerMaster(instance.getSchedulerId());
                if (unique.get(sosPermissionCommandsMaster.getJobSchedulerMaster()) == null) {
                    sosPermissionCommandsMasters.getSOSPermissionCommandsMaster().add(sosPermissionCommandsMaster);
                    unique.put(sosPermissionCommandsMaster.getJobSchedulerMaster(), "");
                }
            }

        } finally {
            Globals.disconnect(session);
            session = null;
        }
        return sosPermissionCommandsMasters;
    }

    protected SOSPermissionJocCockpit getSosPermissionJocCockpit(String masterId) {

        ObjectFactory o = new ObjectFactory();

        SOSPermissionJocCockpit sosPermissionJocCockpit = o.createSOSPermissionJocCockpit();

        if (currentUser != null && currentUser.getCurrentSubject() != null) {

            sosPermissionJocCockpit.setIsAuthenticated(currentUser.isAuthenticated());
            sosPermissionJocCockpit.setAccessToken(currentUser.getAccessToken());
            sosPermissionJocCockpit.setUser(currentUser.getUsername());

            sosPermissionJocCockpit.setJobschedulerMaster(o.createSOSPermissionJocCockpitJobschedulerMaster());
            sosPermissionJocCockpit.setJobschedulerMasterCluster(o.createSOSPermissionJocCockpitJobschedulerMasterCluster());
            sosPermissionJocCockpit.setJobschedulerUniversalAgent(o.createSOSPermissionJocCockpitJobschedulerUniversalAgent());
            sosPermissionJocCockpit.setDailyPlan(o.createSOSPermissionJocCockpitDailyPlan());
            sosPermissionJocCockpit.setHistory(o.createSOSPermissionJocCockpitHistory());
            sosPermissionJocCockpit.setOrder(o.createSOSPermissionJocCockpitOrder());
            sosPermissionJocCockpit.setJobChain(o.createSOSPermissionJocCockpitJobChain());
            sosPermissionJocCockpit.setJob(o.createSOSPermissionJocCockpitJob());
            sosPermissionJocCockpit.setProcessClass(o.createSOSPermissionJocCockpitProcessClass());
            sosPermissionJocCockpit.setSchedule(o.createSOSPermissionJocCockpitSchedule());
            sosPermissionJocCockpit.setLock(o.createSOSPermissionJocCockpitLock());
            sosPermissionJocCockpit.setEvent(o.createSOSPermissionJocCockpitEvent());
            sosPermissionJocCockpit.setHolidayCalendar(o.createSOSPermissionJocCockpitHolidayCalendar());
            sosPermissionJocCockpit.setAuditLog(o.createSOSPermissionJocCockpitAuditLog());
            sosPermissionJocCockpit.setMaintenanceWindow(o.createSOSPermissionJocCockpitMaintenanceWindow());
            sosPermissionJocCockpit.setYADE(o.createSOSPermissionJocCockpitYADE());
            sosPermissionJocCockpit.setRuntime(o.createSOSPermissionJocCockpitRuntime());
            sosPermissionJocCockpit.getRuntime().setExecute(o.createSOSPermissionJocCockpitRuntimeExecute());
            sosPermissionJocCockpit.setJoc(o.createSOSPermissionJocCockpitJoc());
            sosPermissionJocCockpit.getJoc().setView(o.createSOSPermissionJocCockpitJocView());

            sosPermissionJocCockpit.setCalendar(o.createSOSPermissionJocCockpitCalendar());
            sosPermissionJocCockpit.getCalendar().setView(o.createSOSPermissionJocCockpitCalendarView());
            sosPermissionJocCockpit.getCalendar().setEdit(o.createSOSPermissionJocCockpitCalendarEdit());
            sosPermissionJocCockpit.getCalendar().getEdit().setAssign(o.createSOSPermissionJocCockpitCalendarEditAssign());

            sosPermissionJocCockpit.setJOCConfigurations(o.createSOSPermissionJocCockpitJOCConfigurations());
            sosPermissionJocCockpit.getJOCConfigurations().setShare(o.createSOSPermissionJocCockpitJOCConfigurationsShare());
            sosPermissionJocCockpit.getJOCConfigurations().getShare().setView(o.createSOSPermissionJocCockpitJOCConfigurationsShareView());
            sosPermissionJocCockpit.getJOCConfigurations().getShare().setChange(o.createSOSPermissionJocCockpitJOCConfigurationsShareChange());
            sosPermissionJocCockpit.getJOCConfigurations().getShare().getChange().setSharedStatus(o
                    .createSOSPermissionJocCockpitJOCConfigurationsShareChangeSharedStatus());

            sosPermissionJocCockpit.getJobschedulerMaster().setView(o.createSOSPermissionJocCockpitJobschedulerMasterView());
            sosPermissionJocCockpit.getJobschedulerMaster().setAdministration(o.createSOSPermissionJocCockpitJobschedulerMasterAdministration());
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().setConfigurations(o.createSOSPermissionJocCockpitJobschedulerMasterAdministrationConfigurations());
            sosPermissionJocCockpit.getJobschedulerMaster().setExecute(o.createSOSPermissionJocCockpitJobschedulerMasterExecute());
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().setDeploy(o.createSOSPermissionJocCockpitJobschedulerMasterAdministrationConfigurationsDeploy());

            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().setRestart(o
                    .createSOSPermissionJocCockpitJobschedulerMasterExecuteRestart());

            sosPermissionJocCockpit.setDocumentation(o.createSOSPermissionJocCockpitDocumentation());

            sosPermissionJocCockpit.getJobschedulerMasterCluster().setView(o.createSOSPermissionJocCockpitJobschedulerMasterClusterView());
            sosPermissionJocCockpit.getJobschedulerMasterCluster().setExecute(o.createSOSPermissionJocCockpitJobschedulerMasterClusterExecute());

            sosPermissionJocCockpit.getJobschedulerUniversalAgent().setView(o.createSOSPermissionJocCockpitJobschedulerUniversalAgentView());
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().setExecute(o.createSOSPermissionJocCockpitJobschedulerUniversalAgentExecute());
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getExecute().setRestart(o
                    .createSOSPermissionJocCockpitJobschedulerUniversalAgentExecuteRestart());

            sosPermissionJocCockpit.getDailyPlan().setView(o.createSOSPermissionJocCockpitDailyPlanView());
            sosPermissionJocCockpit.getOrder().setView(o.createSOSPermissionJocCockpitOrderView());
            sosPermissionJocCockpit.getOrder().setChange(o.createSOSPermissionJocCockpitOrderChange());
            sosPermissionJocCockpit.getOrder().setDelete(o.createSOSPermissionJocCockpitOrderDelete());
            sosPermissionJocCockpit.getOrder().setExecute(o.createSOSPermissionJocCockpitOrderExecute());

            sosPermissionJocCockpit.getJobChain().setView(o.createSOSPermissionJocCockpitJobChainView());
            sosPermissionJocCockpit.getJobChain().setExecute(o.createSOSPermissionJocCockpitJobChainExecute());

            sosPermissionJocCockpit.getJob().setView(o.createSOSPermissionJocCockpitJobView());
            sosPermissionJocCockpit.getJob().setChange(o.createSOSPermissionJocCockpitJobChange());
            sosPermissionJocCockpit.getJob().setExecute(o.createSOSPermissionJocCockpitJobExecute());

            sosPermissionJocCockpit.getProcessClass().setView(o.createSOSPermissionJocCockpitProcessClassView());

            sosPermissionJocCockpit.getSchedule().setView(o.createSOSPermissionJocCockpitScheduleView());
            sosPermissionJocCockpit.getSchedule().setChange(o.createSOSPermissionJocCockpitScheduleChange());

            sosPermissionJocCockpit.getLock().setView(o.createSOSPermissionJocCockpitLockView());
            sosPermissionJocCockpit.getEvent().setView(o.createSOSPermissionJocCockpitEventView());
            sosPermissionJocCockpit.getEvent().setExecute(o.createSOSPermissionJocCockpitEventExecute());

            sosPermissionJocCockpit.getHolidayCalendar().setView(o.createSOSPermissionJocCockpitHolidayCalendarView());
            sosPermissionJocCockpit.getAuditLog().setView(o.createSOSPermissionJocCockpitAuditLogView());
            sosPermissionJocCockpit.getMaintenanceWindow().setView(o.createSOSPermissionJocCockpitMaintenanceWindowView());
            sosPermissionJocCockpit.getHistory().setView(o.createSOSPermissionJocCockpitHistoryView());

            sosPermissionJocCockpit.getYADE().setView(o.createSOSPermissionJocCockpitYADEView());
            sosPermissionJocCockpit.getYADE().setExecute(o.createSOSPermissionJocCockpitYADEExecute());

            sosPermissionJocCockpit.getJoc().getView().setLog(haveRight(masterId, "sos:products:joc_cockpit:joc:view:log"));

            sosPermissionJocCockpit.getJobschedulerMaster().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:view:status"));
            sosPermissionJocCockpit.getJobschedulerMaster().getView().setMainlog(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:view:mainlog"));
            sosPermissionJocCockpit.getJobschedulerMaster().getView().setParameter(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:view:parameter"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().getRestart().setAbort(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:restart:terminate"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().getRestart().setTerminate(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:restart:abort"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().setPause(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:pause"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().setContinue(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:continue"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().setTerminate(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:terminate"));
            sosPermissionJocCockpit.getJobschedulerMaster().getExecute().setAbort(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:execute:abort"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().setManageCategories(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:manage_categories"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().setEditPermissions(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:edit_permissions"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().setRemoveOldInstances(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:remove_old_instances"));

            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().setDelete(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:delete"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().setEdit(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:edit"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().setView(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:view"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setJob(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:job"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setJobChain(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:job_chain"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setLock(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:lock"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setMonitor(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:monitor"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setOrder(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:order"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setProcessClass(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:process_class"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setSchedule(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:schedule"));
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().setXmlEditor(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:xml_editor"));
            
            sosPermissionJocCockpit.getJobschedulerMasterCluster().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master_cluster:view:status"));
            sosPermissionJocCockpit.getJobschedulerMasterCluster().getExecute().setTerminateFailSafe(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:terminate_fail_safe"));
            sosPermissionJocCockpit.getJobschedulerMasterCluster().getExecute().setRestart(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:restart"));
            sosPermissionJocCockpit.getJobschedulerMasterCluster().getExecute().setTerminate(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:terminate"));

            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_universal_agent:view:status"));
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getExecute().getRestart().setAbort(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:restart:abort"));
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getExecute().getRestart().setTerminate(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:restart:terminate"));
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getExecute().setAbort(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:abort"));
            sosPermissionJocCockpit.getJobschedulerUniversalAgent().getExecute().setTerminate(haveRight(masterId,
                    "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:terminate"));

            sosPermissionJocCockpit.getDocumentation().setView(haveRight(masterId, "sos:products:joc_cockpit:documentation:view"));
            sosPermissionJocCockpit.getDocumentation().setImport(haveRight(masterId, "sos:products:joc_cockpit:documentation:import"));
            sosPermissionJocCockpit.getDocumentation().setExport(haveRight(masterId, "sos:products:joc_cockpit:documentation:export"));
            sosPermissionJocCockpit.getDocumentation().setDelete(haveRight(masterId, "sos:products:joc_cockpit:documentation:delete"));

            sosPermissionJocCockpit.getDailyPlan().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:daily_plan:view:status"));

            sosPermissionJocCockpit.getHistory().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:history:view:status"));

            sosPermissionJocCockpit.getOrder().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:order:view:status"));
            sosPermissionJocCockpit.getOrder().getView().setConfiguration(haveRight(masterId, "sos:products:joc_cockpit:order:view:configuration"));
            sosPermissionJocCockpit.getOrder().getView().setOrderLog(haveRight(masterId, "sos:products:joc_cockpit:order:view:order_log"));
            sosPermissionJocCockpit.getOrder().getView().setDocumentation(haveRight(masterId, "sos:products:joc_cockpit:order:view:documentation"));
            sosPermissionJocCockpit.getOrder().getChange().setStartAndEndNode(haveRight(masterId,
                    "sos:products:joc_cockpit:order:change:start_and_end_node"));
            sosPermissionJocCockpit.getOrder().getChange().setTimeForAdhocOrder(haveRight(masterId,
                    "sos:products:joc_cockpit:order:change:time_for_adhoc_orders"));
            sosPermissionJocCockpit.getOrder().getChange().setParameter(haveRight(masterId, "sos:products:joc_cockpit:order:change:parameter"));
            sosPermissionJocCockpit.getOrder().getChange().setRunTime(haveRight(masterId, "sos:products:joc_cockpit:order:change:run_time"));
            sosPermissionJocCockpit.getOrder().getChange().setState(haveRight(masterId, "sos:products:joc_cockpit:order:change:state"));
            sosPermissionJocCockpit.getOrder().getExecute().setStart(haveRight(masterId, "sos:products:joc_cockpit:order:execute:start"));
            sosPermissionJocCockpit.getOrder().getExecute().setUpdate(haveRight(masterId, "sos:products:joc_cockpit:order:execute:update"));
            sosPermissionJocCockpit.getOrder().getExecute().setSuspend(haveRight(masterId, "sos:products:joc_cockpit:order:execute:suspend"));
            sosPermissionJocCockpit.getOrder().getExecute().setResume(haveRight(masterId, "sos:products:joc_cockpit:order:execute:resume"));
            sosPermissionJocCockpit.getOrder().getExecute().setReset(haveRight(masterId, "sos:products:joc_cockpit:order:execute:reset"));
            sosPermissionJocCockpit.getOrder().getExecute().setRemoveSetback(haveRight(masterId,
                    "sos:products:joc_cockpit:order:execute:remove_setback"));
            sosPermissionJocCockpit.getOrder().getDelete().setPermanent(haveRight(masterId, "sos:products:joc_cockpit:order:delete:permanent"));
            sosPermissionJocCockpit.getOrder().getDelete().setTemporary(haveRight(masterId, "sos:products:joc_cockpit:order:delete:temporary"));
            sosPermissionJocCockpit.getOrder().setAssignDocumentation(haveRight(masterId, "sos:products:joc_cockpit:order:assign_documentation"));

            sosPermissionJocCockpit.getJobChain().getView().setConfiguration(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:view:configuration"));
            sosPermissionJocCockpit.getJobChain().getView().setHistory(haveRight(masterId, "sos:products:joc_cockpit:job_chain:view:history"));
            sosPermissionJocCockpit.getJobChain().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:job_chain:view:status"));
            sosPermissionJocCockpit.getJobChain().getView().setDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:view:documentation"));
            sosPermissionJocCockpit.getJobChain().getExecute().setStop(haveRight(masterId, "sos:products:joc_cockpit:job_chain:execute:stop"));
            sosPermissionJocCockpit.getJobChain().getExecute().setUnstop(haveRight(masterId, "sos:products:joc_cockpit:job_chain:execute:unstop"));
            sosPermissionJocCockpit.getJobChain().getExecute().setAddOrder(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:execute:add_order"));
            sosPermissionJocCockpit.getJobChain().getExecute().setSkipJobChainNode(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:execute:skip_jobchain_node"));
            sosPermissionJocCockpit.getJobChain().getExecute().setProcessJobChainNode(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:execute:process_jobchain_node"));
            sosPermissionJocCockpit.getJobChain().getExecute().setStopJobChainNode(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:execute:stop_jobchain_node"));
            sosPermissionJocCockpit.getJobChain().setAssignDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:job_chain:assign_documentation"));

            sosPermissionJocCockpit.getJob().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:job:view:status"));
            sosPermissionJocCockpit.getJob().getView().setTaskLog(haveRight(masterId, "sos:products:joc_cockpit:job:view:task_log"));
            sosPermissionJocCockpit.getJob().getView().setConfiguration(haveRight(masterId, "sos:products:joc_cockpit:job:view:configuration"));
            sosPermissionJocCockpit.getJob().getView().setHistory(haveRight(masterId, "sos:products:joc_cockpit:job:view:history"));
            sosPermissionJocCockpit.getJob().getView().setDocumentation(haveRight(masterId, "sos:products:joc_cockpit:job:view:documentation"));
            sosPermissionJocCockpit.getJob().getChange().setRunTime(haveRight(masterId, "sos:products:joc_cockpit:job:change:run_time"));
            sosPermissionJocCockpit.getJob().getExecute().setStart(haveRight(masterId, "sos:products:joc_cockpit:job:execute:start"));
            sosPermissionJocCockpit.getJob().getExecute().setStop(haveRight(masterId, "sos:products:joc_cockpit:job:execute:stop"));
            sosPermissionJocCockpit.getJob().getExecute().setUnstop(haveRight(masterId, "sos:products:joc_cockpit:job:execute:unstop"));
            sosPermissionJocCockpit.getJob().getExecute().setTerminate(haveRight(masterId, "sos:products:joc_cockpit:job:execute:terminate"));
            sosPermissionJocCockpit.getJob().getExecute().setKill(haveRight(masterId, "sos:products:joc_cockpit:job:execute:kill"));
            sosPermissionJocCockpit.getJob().getExecute().setEndAllTasks(haveRight(masterId, "sos:products:joc_cockpit:job:execute:end_all_tasks"));
            sosPermissionJocCockpit.getJob().getExecute().setSuspendAllTasks(haveRight(masterId,
                    "sos:products:joc_cockpit:job:execute:suspend_all_tasks"));
            sosPermissionJocCockpit.getJob().getExecute().setContinueAllTasks(haveRight(masterId,
                    "sos:products:joc_cockpit:job:execute:continue_all_tasks"));
            sosPermissionJocCockpit.getJob().setAssignDocumentation(haveRight(masterId, "sos:products:joc_cockpit:job:assign_documentation"));

            sosPermissionJocCockpit.getProcessClass().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:process_class:view:status"));
            sosPermissionJocCockpit.getProcessClass().getView().setConfiguration(haveRight(masterId,
                    "sos:products:joc_cockpit:process_class:view:configuration"));
            sosPermissionJocCockpit.getProcessClass().getView().setDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:process_class:view:documentation"));
            sosPermissionJocCockpit.getProcessClass().setAssignDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:process_class:assign_documentation"));

            sosPermissionJocCockpit.getSchedule().getView().setConfiguration(haveRight(masterId,
                    "sos:products:joc_cockpit:schedule:view:configuration"));
            sosPermissionJocCockpit.getSchedule().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:schedule:view:status"));
            sosPermissionJocCockpit.getSchedule().getView().setDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:schedule:view:documentation"));
            sosPermissionJocCockpit.getSchedule().getChange().setEditContent(haveRight(masterId,
                    "sos:products:joc_cockpit:schedule:change:edit_content"));
            sosPermissionJocCockpit.getSchedule().getChange().setAddSubstitute(haveRight(masterId,
                    "sos:products:joc_cockpit:schedule:change:add_substitute"));
            sosPermissionJocCockpit.getSchedule().setAssignDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:schedule:asssign_documentation"));

            sosPermissionJocCockpit.getLock().getView().setConfiguration(haveRight(masterId, "sos:products:joc_cockpit:lock:view:configuration"));
            sosPermissionJocCockpit.getLock().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:lock:view:status"));
            sosPermissionJocCockpit.getLock().getView().setDocumentation(haveRight(masterId, "sos:products:joc_cockpit:lock:view:documentation"));
            sosPermissionJocCockpit.getLock().setAssignDocumentation(haveRight(masterId, "sos:products:joc_cockpit:lock:assign_documentation"));

            sosPermissionJocCockpit.getEvent().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:event:view:status"));
            sosPermissionJocCockpit.getEvent().getExecute().setDelete(haveRight(masterId, "sos:products:joc_cockpit:event:execute:delete"));
            sosPermissionJocCockpit.getEvent().getExecute().setAdd(haveRight(masterId, "sos:products:joc_cockpit:event:execute:add"));

            sosPermissionJocCockpit.getHolidayCalendar().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:holiday_calendar:view:status"));
            sosPermissionJocCockpit.getAuditLog().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:audit_log:view:status"));

            sosPermissionJocCockpit.getJOCConfigurations().getShare().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:customization:share:view:status"));
            sosPermissionJocCockpit.getJOCConfigurations().getShare().getChange().setDelete(haveRight(masterId,
                    "sos:products:joc_cockpit:customization:share:change:delete"));
            sosPermissionJocCockpit.getJOCConfigurations().getShare().getChange().setEditContent(haveRight(masterId,
                    "sos:products:joc_cockpit:customization:share:change:edit_content"));
            sosPermissionJocCockpit.getJOCConfigurations().getShare().getChange().getSharedStatus().setMakePrivate(haveRight(masterId,
                    "sos:products:joc_cockpit:customization:share:change:shared_status:make_private"));
            sosPermissionJocCockpit.getJOCConfigurations().getShare().getChange().getSharedStatus().setMakeShared(haveRight(masterId,
                    "sos:products:joc_cockpit:customization:share:change:shared_status:make_share"));

            sosPermissionJocCockpit.getMaintenanceWindow().getView().setStatus(haveRight(masterId,
                    "sos:products:joc_cockpit:maintenance_window:view:status"));
            sosPermissionJocCockpit.getMaintenanceWindow().setEnableDisableMaintenanceWindow(haveRight(masterId,
                    "sos:products:joc_cockpit:maintenance_window:enable_disable_maintenance_window"));

            sosPermissionJocCockpit.getYADE().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:yade:view:status"));
            sosPermissionJocCockpit.getYADE().getView().setFiles(haveRight(masterId, "sos:products:joc_cockpit:yade:view:files"));
            sosPermissionJocCockpit.getYADE().getExecute().setTransferStart(haveRight(masterId,
                    "sos:products:joc_cockpit:yade:execute:transfer_start"));

            sosPermissionJocCockpit.getCalendar().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:calendar:view:status"));
            sosPermissionJocCockpit.getCalendar().getView().setDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:calendar:view:documentation"));
            sosPermissionJocCockpit.getCalendar().getEdit().setChange(haveRight(masterId, "sos:products:joc_cockpit:calendar:edit:change"));
            sosPermissionJocCockpit.getCalendar().getEdit().setDelete(haveRight(masterId, "sos:products:joc_cockpit:calendar:edit:delete"));
            sosPermissionJocCockpit.getCalendar().getEdit().setCreate(haveRight(masterId, "sos:products:joc_cockpit:calendar:edit:create"));
            sosPermissionJocCockpit.getCalendar().setAssignDocumentation(haveRight(masterId,
                    "sos:products:joc_cockpit:calendar:assign_documentation"));

            sosPermissionJocCockpit.getCalendar().getEdit().getAssign().setChange(haveRight(masterId,
                    "sos:products:joc_cockpit:calendar:assign:change"));
            sosPermissionJocCockpit.getCalendar().getEdit().getAssign().setNonworking(haveRight(masterId,
                    "sos:products:joc_cockpit:calendar:assign:nonworking"));
            sosPermissionJocCockpit.getCalendar().getEdit().getAssign().setRuntime(haveRight(masterId,
                    "sos:products:joc_cockpit:calendar:assign:runtime"));

            sosPermissionJocCockpit.getRuntime().getExecute().setEditXml(haveRight(masterId, "sos:products:joc_cockpit:runtime:execute:edit_xml"));

            sosPermissionJocCockpit.setJobStream(o.createSOSPermissionJocCockpitJobStream());
            sosPermissionJocCockpit.getJobStream().setView(o.createSOSPermissionJocCockpitJobStreamView());
            sosPermissionJocCockpit.getJobStream().setChange(o.createSOSPermissionJocCockpitJobStreamChange());
            sosPermissionJocCockpit.getJobStream().getChange().setEvents(o.createSOSPermissionJocCockpitJobStreamChangeEvents());

            sosPermissionJocCockpit.getJobStream().getView().setEventlist(haveRight(masterId, "sos:products:joc_cockpit:jobstream:view:eventlist"));
            sosPermissionJocCockpit.getJobStream().getView().setGraph(haveRight(masterId, "sos:products:joc_cockpit:jobstream:view:graph"));
            sosPermissionJocCockpit.getJobStream().getView().setStatus(haveRight(masterId, "sos:products:joc_cockpit:jobstream:view:status"));
            sosPermissionJocCockpit.getJobStream().getChange().setConditions(haveRight(masterId,
                    "sos:products:joc_cockpit:jobstream:change:conditions"));
            sosPermissionJocCockpit.getJobStream().getChange().getEvents().setAdd(haveRight(masterId,
                    "sos:products:joc_cockpit:jobstream:change:events:add"));
            sosPermissionJocCockpit.getJobStream().getChange().getEvents().setRemove(haveRight(masterId,
                    "sos:products:joc_cockpit:jobstream:change:events:remove"));

        }
        return sosPermissionJocCockpit;
    }

    protected SOSPermissionCommands getSosPermissionCommands(String masterId) {

        ObjectFactory o = new ObjectFactory();

        SOSPermissionCommands sosPermissionCommands = o.createSOSPermissionCommands();

        if (currentUser != null && currentUser.getCurrentSubject() != null) {

            sosPermissionCommands.setIsAuthenticated(currentUser.isAuthenticated());
            sosPermissionCommands.setAccessToken(currentUser.getAccessToken());
            sosPermissionCommands.setUser(currentUser.getUsername());

            sosPermissionCommands.setDailyPlan(o.createSOSPermissionCommandsDailyPlan());
            sosPermissionCommands.getDailyPlan().setView(o.createSOSPermissionCommandsDailyPlanView());
            sosPermissionCommands.setJobschedulerMaster(o.createSOSPermissionCommandsJobschedulerMaster());
            sosPermissionCommands.setJobschedulerMasterCluster(o.createSOSPermissionCommandsJobschedulerMasterCluster());
            sosPermissionCommands.setHistory(o.createSOSPermissionCommandsHistory());
            sosPermissionCommands.setOrder(o.createSOSPermissionCommandsOrder());
            sosPermissionCommands.setJobChain(o.createSOSPermissionCommandsJobChain());
            sosPermissionCommands.setJob(o.createSOSPermissionCommandsJob());
            sosPermissionCommands.setProcessClass(o.createSOSPermissionCommandsProcessClass());
            sosPermissionCommands.setSchedule(o.createSOSPermissionCommandsSchedule());
            sosPermissionCommands.setLock(o.createSOSPermissionCommandsLock());

            sosPermissionCommands.getJobschedulerMaster().setView(o.createSOSPermissionCommandsJobschedulerMasterView());
            sosPermissionCommands.getJobschedulerMaster().setExecute(o.createSOSPermissionCommandsJobschedulerMasterExecute());
            sosPermissionCommands.getJobschedulerMaster().setAdministration(o.createSOSPermissionCommandsJobschedulerMasterAdministration());
            sosPermissionCommands.getJobschedulerMaster().getExecute().setRestart(o.createSOSPermissionCommandsJobschedulerMasterExecuteRestart());
            sosPermissionCommands.getJobschedulerMasterCluster().setExecute(o.createSOSPermissionCommandsJobschedulerMasterClusterExecute());

            sosPermissionCommands.getOrder().setView(o.createSOSPermissionCommandsOrderView());
            sosPermissionCommands.getOrder().setChange(o.createSOSPermissionCommandsOrderChange());
            sosPermissionCommands.getOrder().setExecute(o.createSOSPermissionCommandsOrderExecute());

            sosPermissionCommands.getJobChain().setView(o.createSOSPermissionCommandsJobChainView());
            sosPermissionCommands.getJobChain().setExecute(o.createSOSPermissionCommandsJobChainExecute());
            sosPermissionCommands.getJobChain().setChange(o.createSOSPermissionCommandsJobChainChange());

            sosPermissionCommands.getJob().setView(o.createSOSPermissionCommandsJobView());
            sosPermissionCommands.getJob().setExecute(o.createSOSPermissionCommandsJobExecute());
            sosPermissionCommands.getJob().setChange(o.createSOSPermissionCommandsJobChange());

            sosPermissionCommands.getProcessClass().setView(o.createSOSPermissionCommandsProcessClassView());
            sosPermissionCommands.getProcessClass().setChange(o.createSOSPermissionCommandsProcessClassChange());

            sosPermissionCommands.getSchedule().setView(o.createSOSPermissionCommandsScheduleView());
            sosPermissionCommands.getSchedule().setChange(o.createSOSPermissionCommandsScheduleChange());

            sosPermissionCommands.getLock().setView(o.createSOSPermissionCommandsLockView());
            sosPermissionCommands.getLock().setChange(o.createSOSPermissionCommandsLockChange());

            sosPermissionCommands.getJobschedulerMaster().getView().setStatus(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:view:status"));
            sosPermissionCommands.getJobschedulerMaster().getView().setParameter(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:view:parameter"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().getRestart().setAbort(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:restart:terminate"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().getRestart().setTerminate(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:restart:abort"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().setPause(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:pause"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().setContinue(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:continue"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().setTerminate(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:terminate"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().setAbort(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:abort"));
            sosPermissionCommands.getJobschedulerMaster().getExecute().setStop(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:execute:stop"));
            sosPermissionCommands.getJobschedulerMaster().getAdministration().setManageCategories(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master:manage_categories"));

            sosPermissionCommands.getJobschedulerMasterCluster().getExecute().setTerminateFailSafe(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master_cluster:execute:terminate_fail_safe"));
            sosPermissionCommands.getJobschedulerMasterCluster().getExecute().setRestart(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master_cluster:execute:restart"));
            sosPermissionCommands.getJobschedulerMasterCluster().getExecute().setTerminate(haveRight(masterId,
                    "sos:products:commands:jobscheduler_master_cluster:execute:terminate"));

            sosPermissionCommands.getDailyPlan().getView().setStatus(haveRight(masterId, "sos:products:commands:jobscheduler_master:view:calendar"));

            sosPermissionCommands.getHistory().setView(haveRight(masterId, "sos:products:commands:history:view"));

            sosPermissionCommands.getOrder().getView().setStatus(haveRight(masterId, "sos:products:commands:order:view:status"));
            sosPermissionCommands.getOrder().getChange().setStartAndEndNode(haveRight(masterId,
                    "sos:products:commands:order:change:start_and_end_node"));
            sosPermissionCommands.getOrder().getChange().setTimeForAdhocOrder(haveRight(masterId,
                    "sos:products:commands:order:change:time_for_adhoc_orders"));
            sosPermissionCommands.getOrder().getChange().setParameter(haveRight(masterId, "sos:products:commands:order:change:parameter"));
            sosPermissionCommands.getOrder().getChange().setOther(haveRight(masterId, "sos:products:commands:order:change:other"));
            sosPermissionCommands.getOrder().getChange().setRunTime(haveRight(masterId, "sos:products:commands:order:change:run_time"));
            sosPermissionCommands.getOrder().getChange().setState(haveRight(masterId, "sos:products:commands:order:change:state"));
            sosPermissionCommands.getOrder().getChange().setHotFolder(haveRight(masterId, "sos:products:commands:order:change:hot_folder"));
            sosPermissionCommands.getOrder().getExecute().setStart(haveRight(masterId, "sos:products:commands:order:execute:start"));
            sosPermissionCommands.getOrder().getExecute().setUpdate(haveRight(masterId, "sos:products:commands:order:execute:update"));
            sosPermissionCommands.getOrder().getExecute().setSuspend(haveRight(masterId, "sos:products:commands:order:execute:suspend"));
            sosPermissionCommands.getOrder().getExecute().setResume(haveRight(masterId, "sos:products:commands:order:execute:resume"));
            sosPermissionCommands.getOrder().getExecute().setReset(haveRight(masterId, "sos:products:commands:order:execute:reset"));
            sosPermissionCommands.getOrder().getExecute().setRemoveSetback(haveRight(masterId, "sos:products:commands:order:execute:remove_setback"));
            sosPermissionCommands.getOrder().setDelete(haveRight(masterId, "sos:products:commands:order:delete"));

            sosPermissionCommands.getJobChain().getView().setStatus(haveRight(masterId, "sos:products:commands:job_chain:view:status"));
            sosPermissionCommands.getJobChain().getExecute().setStop(haveRight(masterId, "sos:products:commands:job_chain:execute:stop"));
            sosPermissionCommands.getJobChain().getExecute().setUnstop(haveRight(masterId, "sos:products:commands:job_chain:execute:unstop"));
            sosPermissionCommands.getJobChain().getExecute().setAddOrder(haveRight(masterId, "sos:products:commands:job_chain:execute:add_order"));
            sosPermissionCommands.getJobChain().getExecute().setSkipJobChainNode(haveRight(masterId,
                    "sos:products:commands:job_chain:execute:skip_jobchain_node"));
            sosPermissionCommands.getJobChain().getExecute().setProcessJobChainNode(haveRight(masterId,
                    "sos:products:commands:job_chain:execute:process_jobchain_node"));
            sosPermissionCommands.getJobChain().getExecute().setStopJobChainNode(haveRight(masterId,
                    "sos:products:commands:job_chain:execute:stop_jobchain_node"));
            sosPermissionCommands.getJobChain().getExecute().setRemove(haveRight(masterId, "sos:products:commands:job_chain:remove"));
            sosPermissionCommands.getJobChain().getChange().setHotFolder(haveRight(masterId, "sos:products:commands:job_chain:change:hot_folder"));

            sosPermissionCommands.getJob().getView().setStatus(haveRight(masterId, "sos:products:commands:job:view:status"));
            sosPermissionCommands.getJob().getChange().setRunTime(haveRight(masterId, "sos:products:commands:job:change:run_time"));
            sosPermissionCommands.getJob().getChange().setHotFolder(haveRight(masterId, "sos:products:commands:job:change:hot_folder"));
            sosPermissionCommands.getJob().getExecute().setStart(haveRight(masterId, "sos:products:commands:job:execute:start"));
            sosPermissionCommands.getJob().getExecute().setStop(haveRight(masterId, "sos:products:commands:job:execute:stop"));
            sosPermissionCommands.getJob().getExecute().setUnstop(haveRight(masterId, "sos:products:commands:job:execute:unstop"));
            sosPermissionCommands.getJob().getExecute().setTerminate(haveRight(masterId, "sos:products:commands:job:execute:terminate"));
            sosPermissionCommands.getJob().getExecute().setKill(haveRight(masterId, "sos:products:commands:job:execute:kill"));
            sosPermissionCommands.getJob().getExecute().setEndAllTasks(haveRight(masterId, "sos:products:commands:job:execute:end_all_tasks"));
            sosPermissionCommands.getJob().getExecute().setSuspendAllTasks(haveRight(masterId,
                    "sos:products:commands:job:execute:suspend_all_tasks"));
            sosPermissionCommands.getJob().getExecute().setContinueAllTasks(haveRight(masterId,
                    "sos:products:commands:job:execute:continue_all_tasks"));

            sosPermissionCommands.getProcessClass().getView().setStatus(haveRight(masterId, "sos:products:commands:process_class:view:status"));
            sosPermissionCommands.getProcessClass().setRemove(haveRight(masterId, "sos:products:commands:process_class:remove"));
            sosPermissionCommands.getProcessClass().getChange().setEditContent(haveRight(masterId,
                    "sos:products:commands:process_class:change:edit_content"));
            sosPermissionCommands.getProcessClass().getChange().setHotFolder(haveRight(masterId,
                    "sos:products:commands:process_class:change:hot_folder"));

            sosPermissionCommands.getSchedule().getView().setStatus(haveRight(masterId, "sos:products:commands:schedule:view:status"));
            sosPermissionCommands.getSchedule().getChange().setAddSubstitute(haveRight(masterId,
                    "sos:products:commands:schedule:change:add_substitute"));
            sosPermissionCommands.getSchedule().getChange().setHotFolder(haveRight(masterId, "sos:products:commands:schedule:change:hot_folder"));

            sosPermissionCommands.getLock().getView().setStatus(haveRight(masterId, "sos:products:commands:lock:view:status"));
            sosPermissionCommands.getLock().setRemove(haveRight(masterId, "sos:products:commands:lock:remove"));
            sosPermissionCommands.getLock().getChange().setHotFolder(haveRight(masterId, "sos:products:commands:lock:change:hot_folder"));
        }
        return sosPermissionCommands;
    }

    private boolean isPermitted(String masterId, String permission) {
        return (currentUser != null && currentUser.isPermitted(masterId, permission) && currentUser.isAuthenticated());
    }

    private boolean haveRight(String masterId, String permission) {
        return isPermitted(masterId, permission);
    }

    private void addRole(List<String> sosRoles, String role, boolean forUser) {
        if (currentUser != null && (!forUser || currentUser.hasRole(role)) && currentUser.isAuthenticated()) {
            if (!sosRoles.contains(role)) {
                sosRoles.add(role);
            }
        }
    }

    public SOSPermissionRoles getRoles(boolean forUser) {

        if (roles == null || !forUser) {
            ObjectFactory o = new ObjectFactory();
            roles = o.createSOSPermissionRoles();

            ini = getIni();
            Section s = ini.getSection("roles");

            if (s != null) {
                for (String role : s.keySet()) {
                    addRole(roles.getSOSPermissionRole(), role, forUser);
                }
            }

            s = ini.getSection("folders");
            if (s != null) {
                for (String role : s.keySet()) {
                    String[] key = role.split("\\|");
                    if (key.length == 1) {
                        addRole(roles.getSOSPermissionRole(), role, forUser);
                    }
                    if (key.length == 2) {
                        addRole(roles.getSOSPermissionRole(), key[1], forUser);
                    }
                }
            }
        }
        return roles;
    }

    public Ini getIni() {

        if (ini == null) {
            return Globals.getIniFromSecurityManagerFactory();
        }
        return ini;
    }

}