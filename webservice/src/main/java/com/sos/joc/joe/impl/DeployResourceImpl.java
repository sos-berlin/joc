package com.sos.joc.joe.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.ClusterMemberHandler;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.audit.DeployJoeAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.documentation.Documentation;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerModifyObjectException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeConfigurationException;
import com.sos.joc.joe.common.ConfigurationMonitor;
import com.sos.joe.common.XmlSerializer;
import com.sos.joc.joe.resource.IDeployResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.DeployActionType;
import com.sos.joc.model.joe.common.DeployAnswer;
import com.sos.joc.model.joe.common.DeployFailReason;
import com.sos.joc.model.joe.common.DeployFailReasonType;
import com.sos.joc.model.joe.common.DeployMessage;
import com.sos.joc.model.joe.common.FilterDeploy;
import com.sos.schema.JsonValidator;

import sos.xml.SOSXMLXPath;

@Path("joe")
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    private static final String ROOT_FOLDER = "/";
    private static final String API_CALL = "./joe/deploy";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployResourceImpl.class);

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, FilterDeploy.class);
            FilterDeploy body = Globals.objectMapper.readValue(filterBytes, FilterDeploy.class);
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isMonitor() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isOrder() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isProcessClass() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isSchedule();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("folder", body.getFolder());
            if (body.getObjectType() != null && !body.getObjectType().value().isEmpty() && (!JOEHelper.CLASS_MAPPING.containsKey(body.getObjectType()
                    .value()) || JobSchedulerObjectType.FOLDER == body.getObjectType())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }

            String folder = normalizeFolder(body.getFolder());
            if (!ROOT_FOLDER.equals(folder)  && !folderPermissions.isPermittedForFolder(folder)) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            
            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            DBLayerJoeLocks dbLayerJoeLocks = new DBLayerJoeLocks(sosHibernateSession);
            DocumentationDBLayer dbLayerDocs = new DocumentationDBLayer(sosHibernateSession);
//            InventoryFilesDBLayer dbLayerInventoryFiles = new InventoryFilesDBLayer(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            boolean folderDeploy = body.getObjectType() == null || body.getObjectType() == JobSchedulerObjectType.FOLDER;

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            //filterJoeObjects.setAccount(body.getAccount());  //for specific account we have check in the loops -> deploy report
            if (!folderDeploy) {
                filterJoeObjects.setFolder(folder); 
                if (body.getObjectName() != null && !body.getObjectName().isEmpty()) {
                    filterJoeObjects.setPath((folder + ROOT_FOLDER).replaceAll("//+", ROOT_FOLDER) + body.getObjectName());
                }
                if (JobSchedulerObjectType.JOBCHAIN == body.getObjectType() && (body.getObjectName() == null || body.getObjectName().isEmpty())) {
                    //if clicked deploy in job chain action menu
                    filterJoeObjects.setObjectTypes(body.getObjectType().value(), JobSchedulerObjectType.ORDER.value(),
                            JobSchedulerObjectType.NODEPARAMS.value());
                } else if (JobSchedulerObjectType.JOBCHAIN == body.getObjectType()) {
                    filterJoeObjects.setObjectTypes(body.getObjectType().value(), JobSchedulerObjectType.ORDER.value(),
                            JobSchedulerObjectType.NODEPARAMS.value());
                    filterJoeObjects.setJobChainWithOrders(filterJoeObjects.getPath());
                } else if (JobSchedulerObjectType.ORDER == body.getObjectType()) {
                    filterJoeObjects.setObjectTypes(body.getObjectType().value(), JobSchedulerObjectType.NODEPARAMS.value());
                } else {
                    filterJoeObjects.setObjectType(body.getObjectType());
                }
            } else {
                filterJoeObjects.setPath(folder);
            }
            
            String[] nonFolderObjects = { "NODEPARAMS", "PROCESSCLASS", "AGENTCLUSTER", "LOCK", "MONITOR", "SCHEDULE", "JOB", "JOBCHAIN", "ORDER" };

            if (folderDeploy) {
                // It's not allowed to delete the root folder
                if (ROOT_FOLDER.equals(body.getFolder())) {
                    filterJoeObjects.setObjectType(JobSchedulerObjectType.FOLDER);
                    DBItemJoeObject folderDbItem = dbLayerJoeObjects.getJoeObject(filterJoeObjects);
                    if (folderDbItem != null && folderDbItem.operationIsDelete()) {
                        throw new JobSchedulerModifyObjectException("Deleting the root directory is not allowed.");
                    }
                    filterJoeObjects.setObjectType(null);
                }
                if (body.getRecursive() == null || !body.getRecursive()) {
                    filterJoeObjects.setPathWithChildren(folder);
                    filterJoeObjects.setObjectTypes(nonFolderObjects);
                } else {
                    filterJoeObjects.setRecursive();
                }
            }

            filterJoeObjects.setOrderCriteria("path");
            filterJoeObjects.setSortMode("desc");

            JOCHotFolder jocHotFolder = new JOCHotFolder(this);
            jocHotFolder.setAutoCloseHttpClient(false);
            ConfigurationMonitor configurationMonitor = new ConfigurationMonitor(sosHibernateSession, jocHotFolder, dbItemInventoryInstance.getId(),
                    body.getJobschedulerId(), body.getAccount(), getAccount());

            DeployAnswer deployAnswer = new DeployAnswer();
            deployAnswer.setReport(new ArrayList<DeployMessage>());
            deployAnswer.setFolder(folder);
            deployAnswer.setJobschedulerId(body.getJobschedulerId());
            deployAnswer.setObjectName(body.getObjectName());
            deployAnswer.setObjectType(body.getObjectType());
            deployAnswer.setRecursive(filterJoeObjects.isRecursive());
            
            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance);

            Set<String> touchedFolders = new HashSet<String>();
            Set<String> deletedFolders = new HashSet<String>();

            try {
                // first delete operation
                filterJoeObjects.setOperation("delete");
                List<DBItemJoeObject> listOfJoeObjectsForDelete = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);

                if (listOfJoeObjectsForDelete != null && !listOfJoeObjectsForDelete.isEmpty()) {

                    Map<String, Set<DBItemJoeObject>> groupedJoeObjects = listOfJoeObjectsForDelete.stream().collect(Collectors.groupingBy(
                            DBItemJoeObject::getObjectType, Collectors.toSet()));

                    String[] objTypesToDelete = { "ORDER", "JOBCHAIN", "JOB", "SCHEDULE", "MONITOR", "LOCK", "AGENTCLUSTER", "PROCESSCLASS",
                            "NODEPARAMS" };
                    
//                if (groupedJoeObjects.containsKey("JOBCHAIN")) { //delete orders and nodeparams too
//                    List<DBItemJoeObject> allOrdersAndNodeParams = new ArrayList<DBItemJoeObject>();
//                    for (DBItemJoeObject joeObject : groupedJoeObjects.get("JOBCHAIN")) {
//                        List<DBItemJoeObject> ordersAndNodeParams = dbLayerJoeObjects.getOrdersAndNodeParams(joeObject);
//                        if (ordersAndNodeParams != null) {
//                            allOrdersAndNodeParams.addAll(ordersAndNodeParams);
//                        }
//                        //TODO add orders and nodeparams from Inventory to allOrdersAndNodeParams
//                    }
//                    Map<String, Set<DBItemJoeObject>> ordersAndNodeParamsMap = allOrdersAndNodeParams.stream().map(i -> {
//                        i.setOperation("delete");
//                        return i;
//                    }).collect(Collectors.groupingBy(DBItemJoeObject::getObjectType, Collectors.toSet()));
//                    if (ordersAndNodeParamsMap.containsKey("ORDER")) {
//                        groupedJoeObjects.putIfAbsent("ORDER", new HashSet<DBItemJoeObject>());
//                        groupedJoeObjects.get("ORDER").addAll(ordersAndNodeParamsMap.get("ORDER"));
//                    }
//                    if (ordersAndNodeParamsMap.containsKey("NODEPARAMS")) {
//                        groupedJoeObjects.putIfAbsent("NODEPARAMS", new HashSet<DBItemJoeObject>());
//                        groupedJoeObjects.get("NODEPARAMS").addAll(ordersAndNodeParamsMap.get("NODEPARAMS"));
//                    }
//                }
                    

                    for (String objType : objTypesToDelete) {
                        if (groupedJoeObjects.containsKey(objType)) {
                            for (DBItemJoeObject joeObject : groupedJoeObjects.get(objType)) {

                                DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                                logAuditMessage(deployJoeAudit);
                                
                                JobSchedulerObjectType jsObjType = JobSchedulerObjectType.valueOf(joeObject.getObjectType());
                                boolean objectPermission = JOEHelper.hasPermission(jsObjType, sosPermissionJocCockpit);
                                
                                if (body.getAccount() != null && !body.getAccount().isEmpty() && !body.getAccount().equals(joeObject.getAccount())) {
                                    deployAnswer.getReport().add(getOwnerDeniedMessage(joeObject.getPath(), jsObjType, joeObject.getAccount()));
                                    continue;
                                }
                                
                                if (!objectPermission) {
                                    deployAnswer.getReport().add(getObjectDeniedMessage(joeObject.getPath(), jsObjType));
                                    continue;
                                }

                                if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                                    deployAnswer.getReport().add(getFolderDeniedMessage(joeObject.getPath(), jsObjType));
                                    continue;
                                }
                                
                                if (!folderDeploy && "NODEPARAMS".equals(objType) && filterJoeObjects.getObjectTypes() != null) {
                                    boolean isOrderNodeParams = joeObject.getPath().contains(",");
                                    if (!filterJoeObjects.getObjectTypes().contains(JobSchedulerObjectType.ORDER.value()) && isOrderNodeParams) {
                                        continue;
                                    }
                                    if (!filterJoeObjects.getObjectTypes().contains(JobSchedulerObjectType.JOBCHAIN.value()) && !isOrderNodeParams) {
                                        continue;
                                    }
                                }

                                String extension = JOEHelper.getFileExtension(jsObjType);

                                boolean objExists = true;
                                try {
                                    jocHotFolder.deleteFile(joeObject.getPath() + extension);
                                } catch (JobSchedulerObjectNotExistException e) {
                                    objExists = false;
                                }
                                if (objExists) {
                                    if ("NODEPARAMS".equals(objType)) {
                                        // TODO delete configuration monitor in corresponding jobs
                                    }
                                    clusterMemberHandler.deleteAtOtherClusterMembers(joeObject.getPath() + extension, false);
                                    deleteCalendarUsedBy(sosHibernateSession, body.getJobschedulerId(), objType, joeObject.getPath());
                                }
                                storeAuditLogEntry(deployJoeAudit);
                                touchedFolders.add(joeObject.getFolder());
                                dbLayerJoeObjects.delete(joeObject);
                                try {
                                    if (joeObject.getDocPath() != null) {
                                        Documentation.unassignDocu(dbLayerDocs, body.getJobschedulerId(), joeObject.getPath(), JobSchedulerObjectType.fromValue(objType));
                                    }
                                } catch (Exception e) {
                                    LOGGER.warn(String.format("Assigned documentation for %s %s couldn't deleted", objType, joeObject.getPath()), e);
                                }
                                deployAnswer.getReport().add(getDeployMessage(joeObject.getPath(), jsObjType, true));
                            }
                        }
                    }
                    if (groupedJoeObjects.containsKey("FOLDER")) {
                        for (DBItemJoeObject joeObject : groupedJoeObjects.get("FOLDER")) {

                            DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                            logAuditMessage(deployJoeAudit);

                            if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                                deployAnswer.getReport().add(getFolderDeniedMessage(joeObject.getPath(), JobSchedulerObjectType.FOLDER));
                                continue;
                            }

                            boolean objExists = true;
                            try {
                                jocHotFolder.deleteFolder(joeObject.getPath());
                            } catch (JobSchedulerObjectNotExistException e) {
                                objExists = false;
                            }
                            if (objExists) {
                                clusterMemberHandler.deleteAtOtherClusterMembers(joeObject.getPath(), true);
                                deleteCalendarUsedBy(sosHibernateSession, body.getJobschedulerId(), "FOLDER", joeObject.getPath());
                            }

                            touchedFolders.add(joeObject.getPath());
                            deletedFolders.add(joeObject.getPath());
                            dbLayerJoeObjects.delete(joeObject);
                            // delete all children of deleted folder in the database
                            deleteWithAllChildren(dbLayerJoeObjects, joeObject);
                            dbLayerJoeLocks.delete(body.getJobschedulerId(), joeObject.getPath());
                            deployAnswer.getReport().add(getDeployMessage(joeObject.getPath(), JobSchedulerObjectType.FOLDER, true));
                        }
                    }
                }

                //second step "store"
                filterJoeObjects.setOperation("store");
                List<DBItemJoeObject> listOfJoeObjectsForStore = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);
                if (listOfJoeObjectsForStore != null && !listOfJoeObjectsForStore.isEmpty()) {
                    Map<String, Set<DBItemJoeObject>> groupedJoeObjects = listOfJoeObjectsForStore.stream().collect(Collectors.groupingBy(
                            DBItemJoeObject::getObjectType, Collectors.toSet()));

                    String[] objTypesToStore = nonFolderObjects;
                    for (String objType : objTypesToStore) {
                        if (groupedJoeObjects.containsKey(objType)) {
                            for (DBItemJoeObject joeObject : groupedJoeObjects.get(objType)) {
                                
                                String jsonContent = joeObject.getConfiguration();
                                
                                if (jsonContent == null) {
                                    dbLayerJoeObjects.delete(joeObject);
                                    continue;
                                }

                                DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                                logAuditMessage(deployJoeAudit);

                                JobSchedulerObjectType jsObjType = JobSchedulerObjectType.valueOf(joeObject.getObjectType());
                                boolean objectPermission = JOEHelper.hasPermission(jsObjType, sosPermissionJocCockpit);
                                
                                if (body.getAccount() != null && !body.getAccount().isEmpty() && !body.getAccount().equals(joeObject.getAccount())) {
                                    deployAnswer.getReport().add(getOwnerDeniedMessage(joeObject.getPath(), jsObjType, joeObject.getAccount()));
                                    continue;
                                }
                                
                                if (!objectPermission) {
                                    deployAnswer.getReport().add(getObjectDeniedMessage(joeObject.getPath(), jsObjType));
                                    continue;
                                }
                                
                                if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                                    deployAnswer.getReport().add(getFolderDeniedMessage(joeObject.getPath(), jsObjType));
                                    continue;
                                }
                                
                                if (!folderDeploy && "NODEPARAMS".equals(objType) && filterJoeObjects.getObjectTypes() != null) {
                                    boolean isOrderNodeParams = joeObject.getPath().contains(",");
                                    if (!filterJoeObjects.getObjectTypes().contains(JobSchedulerObjectType.ORDER.value()) && isOrderNodeParams) {
                                        continue;
                                    }
                                    if (!filterJoeObjects.getObjectTypes().contains(JobSchedulerObjectType.JOBCHAIN.value()) && !isOrderNodeParams) {
                                        continue;
                                    }
                                }

                                boolean jsonIsEmpty = jsonContent.trim().equals("{}");
                                
                                if (jsonIsEmpty) {
                                    if ("ORDER".equals(objType)) {
                                        jsonContent = "{\"runTime\":{}}";
                                    } else if (!"NODEPARAMS".equals(objType) && !"LOCK".equals(objType)) {
                                        deployAnswer.getReport().add(getIncompleteMessage(joeObject.getPath(), jsObjType, "Empty configuration"));
                                        continue;
                                    }
                                }
                                
                                String xmlContent = null;
                                try {
                                    xmlContent = XmlSerializer.serializeToStringWithHeader(jsonContent, objType, true);
                                } catch (JoeConfigurationException e1) {
                                    deployAnswer.getReport().add(getIncompleteMessage(joeObject.getPath(), jsObjType, e1.getMessage()));
                                    continue;
                                }
                                
                                String extension = JOEHelper.getFileExtension(jsObjType);
                                
                                if ("NODEPARAMS".equals(objType) && jsonIsEmpty) {
                                    // TODO delete configuration monitor in corresponding jobs
                                    try {
                                        jocHotFolder.deleteFile(joeObject.getPath() + extension);
                                        clusterMemberHandler.deleteAtOtherClusterMembers(joeObject.getPath() + extension, false);
                                    } catch (JobSchedulerObjectNotExistException e) {
                                    }
                                } else {
                                    if ("NODEPARAMS".equals(objType)) {
                                        configurationMonitor.addNodeParams(joeObject);
                                    }
                                    jocHotFolder.putFile(joeObject.getPath() + extension, xmlContent);
                                    clusterMemberHandler.updateAtOtherClusterMembers(joeObject.getPath() + extension, false, xmlContent);
                                    updateCalendarUsedBy(xmlContent, sosHibernateSession, body.getJobschedulerId(), objType, joeObject.getPath());
                                }
                                storeAuditLogEntry(deployJoeAudit);

                                touchedFolders.add(joeObject.getFolder());
                                dbLayerJoeObjects.delete(joeObject);
                                try {
                                    if (joeObject.getDocPath() != null) {
                                        Documentation.assignDocu(dbLayerDocs, body.getJobschedulerId(), joeObject.getPath(), joeObject.getDocPath(),
                                                JobSchedulerObjectType.fromValue(objType));
                                    }
                                } catch (Exception e) {
                                    LOGGER.warn(String.format("Documentation for %s %s couldn't assign", objType, joeObject.getPath()), e);
                                }
                                deployAnswer.getReport().add(getDeployMessage(joeObject.getPath(), jsObjType, false));
                            }
                        }
                        if ("NODEPARAMS".equals(objType)) {
                            groupedJoeObjects.put("JOB", configurationMonitor.addConfigurationMonitor(groupedJoeObjects.get("JOB")));
                        }
                    }
                    if (groupedJoeObjects.containsKey("FOLDER")) {
                        for (DBItemJoeObject joeObject : groupedJoeObjects.get("FOLDER")) {

                            DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                            logAuditMessage(deployJoeAudit);

                            if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                                deployAnswer.getReport().add(getFolderDeniedMessage(joeObject.getPath(), JobSchedulerObjectType.FOLDER));
                                continue;
                            }

                            jocHotFolder.putFolder(normalizeFolder(joeObject.getPath()));

                            storeAuditLogEntry(deployJoeAudit);

                            JsonArray folderContent = jocHotFolder.getFolder(normalizeFolder(joeObject.getPath()));
                            boolean emptyFolder = folderContent == null || folderContent.size() == 0;

                            touchedFolders.add(joeObject.getPath());
                            if (!emptyFolder) {
                                dbLayerJoeObjects.delete(joeObject);
                            }
                            deployAnswer.getReport().add(getDeployMessage(joeObject.getPath(), JobSchedulerObjectType.FOLDER, false));
                        }
                    }
                }
                
                
            } finally {
                jocHotFolder.closeHttpClient();
                
                for (String touchedFolder : touchedFolders) {
                    try {
                        CustomEvent evt = JOEHelper.getJoeUpdatedEvent(touchedFolder);
                        SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
                    } catch (Exception e) {
                        //
                    }
                }

                // release folder if no further drafts inside of current body.getAccount()
                touchedFolders.removeAll(deletedFolders);
                FilterJoeObjects filterObj = new FilterJoeObjects();
                filterObj.setSchedulerId(body.getJobschedulerId());
                filterObj.setAccount(body.getAccount());
                filterObj.setObjectTypes(nonFolderObjects);
                for (String touchedFolder : touchedFolders) {
                    if (body.getAccount() != null && !body.getAccount().isEmpty()) {
                        filterObj.setFolder(touchedFolder);
                        List<DBItemJoeObject> dbItems = dbLayerJoeObjects.getJoeObjectList(filterObj, 0);
                        if (dbItems != null && dbItems.size() == 0) {
                            LockResourceImpl.release(dbLayerJoeLocks, body.getJobschedulerId(), touchedFolder, body.getAccount());
                        }
                    } else {
                        LockResourceImpl.release(dbLayerJoeLocks, body.getJobschedulerId(), touchedFolder, getAccount()); 
                    }
                }
                
                clusterMemberHandler.executeHandlerCalls(sosHibernateSession);
            }

            return JOCDefaultResponse.responseStatus200(deployAnswer);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void deleteWithAllChildren(DBLayerJoeObjects dbLayerJoeObjects, DBItemJoeObject joeObject) throws DBConnectionRefusedException,
            DBInvalidDataException {
        FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
        filterJoeObjects.setSchedulerId(joeObject.getSchedulerId());
        filterJoeObjects.setPath(joeObject.getPath());
        List<DBItemJoeObject> dbItems = dbLayerJoeObjects.getRecursiveJoeObjectList(filterJoeObjects);
        if (dbItems != null) {
            for (DBItemJoeObject dbItem : dbItems) {
                dbLayerJoeObjects.delete(dbItem);
            }
        }
    }

    private void deleteCalendarUsedBy(SOSHibernateSession sosHibernateSession, String schedulerId, String objType, String path)
            throws JsonProcessingException, JocException {
        if ("JOB".equals(objType) || "ORDER".equals(objType) || "SCHEDULE".equals(objType)) {
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(sosHibernateSession);
            List<DBItemInventoryClusterCalendarUsage> dbCalendarUsage = calendarUsageDBLayer.getCalendarUsagesOfAnObject(schedulerId, objType, path);
            if (dbCalendarUsage != null) {
                for (DBItemInventoryClusterCalendarUsage dbItem : dbCalendarUsage) {
                    calendarUsageDBLayer.deleteCalendarUsage(dbItem);
                }

                if (dbCalendarUsage.size() > 0) {
                    CalendarEvent calEvt = new CalendarEvent();
                    calEvt.setKey("CalendarUsageUpdated");
                    CalendarVariables calEvtVars = new CalendarVariables();
                    calEvtVars.setPath(path);
                    calEvtVars.setObjectType(CalendarObjectType.fromValue(objType));
                    calEvt.setVariables(calEvtVars);
                    SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
                }
            }
        }
    }

    public void updateCalendarUsedBy(String xmlContent, SOSHibernateSession sosHibernateSession, String schedulerId, String objType, String path)
            throws Exception {
        if ("JOB".equals(objType) || "ORDER".equals(objType) || "SCHEDULE".equals(objType)) {
            SOSXMLXPath sosxml = new SOSXMLXPath(new StringBuffer(xmlContent));
            Set<String> calendarPaths = new HashSet<String>();
            NodeList calendarNodes = sosxml.selectNodeList("//date/@calendar|//holiday/@calendar");
            Node calendarsNode = sosxml.selectSingleNode("//calendars");
            Map<String, Calendar> calendars = new HashMap<String, Calendar>();
            if (calendarsNode != null) {
                String calendarData = null;
                if (calendarsNode.hasChildNodes()) {
                    calendarData = calendarsNode.getFirstChild().getNodeValue();
                } else {
                    calendarData = calendarsNode.getTextContent();
                }
                if (calendarData != null && !calendarData.isEmpty()) {
                    Calendars cals = Globals.objectMapper.readValue(calendarData, Calendars.class);
                    if (cals != null) {
                        calendars = cals.getCalendars().stream().filter(cal -> cal.getBasedOn() != null).collect(Collectors.toMap(Calendar::getBasedOn,
                                Function.identity()));
                    }
                }
            }

            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(sosHibernateSession);
            CalendarsDBLayer calendarsDBLayer = new CalendarsDBLayer(sosHibernateSession);
            List<DBItemInventoryClusterCalendarUsage> dbCalendarUsage = calendarUsageDBLayer.getCalendarUsagesOfAnObject(schedulerId, objType, path);
            if (dbCalendarUsage == null) {
                dbCalendarUsage = new ArrayList<DBItemInventoryClusterCalendarUsage>();
            }
            boolean calendarEventIsNecessary = dbCalendarUsage.size() + calendars.size() > 0;
            DBItemInventoryClusterCalendarUsage calendarUsageDbItem = new DBItemInventoryClusterCalendarUsage();
            calendarUsageDbItem.setSchedulerId(schedulerId);
            calendarUsageDbItem.setObjectType(objType);
            calendarUsageDbItem.setEdited(false);
            calendarUsageDbItem.setPath(path);

            for (int i = 0; i < calendarNodes.getLength(); i++) {
                String calendarPath = calendarNodes.item(i).getNodeValue();
                if (calendarPath != null && !calendarPaths.contains(calendarPath)) {
                    calendarPaths.add(calendarPath);
                    DBItemInventoryClusterCalendar calendarDbItem = calendarsDBLayer.getCalendar(schedulerId, calendarPath);
                    if (calendarDbItem != null) {
                        calendarUsageDbItem.setCalendarId(calendarDbItem.getId());
                        Calendar calendar = calendars.get(calendarPath);
                        if (calendar != null) {
                            calendar.setBasedOn(null);
                            calendar.setType(null);
                            // check if periods exist for working day calendar
                            calendarUsageDbItem.setConfiguration(Globals.objectMapper.writeValueAsString(calendar));
                        }
                        int index = dbCalendarUsage.indexOf(calendarUsageDbItem);
                        if (index == -1) {
                            calendarUsageDBLayer.saveCalendarUsage(calendarUsageDbItem);
                        } else {
                            DBItemInventoryClusterCalendarUsage dbItem = dbCalendarUsage.remove(index);
                            dbItem.setEdited(false);
                            dbItem.setConfiguration(calendarUsageDbItem.getConfiguration());
                            calendarUsageDBLayer.updateCalendarUsage(dbItem);
                        }
                    }
                }
            }
            for (DBItemInventoryClusterCalendarUsage dbItem : dbCalendarUsage) {
                calendarUsageDBLayer.deleteCalendarUsage(dbItem);
            }
            if (calendarEventIsNecessary) {
                CalendarEvent calEvt = new CalendarEvent();
                calEvt.setKey("CalendarUsageUpdated");
                CalendarVariables calEvtVars = new CalendarVariables();
                calEvtVars.setPath(path);
                calEvtVars.setObjectType(CalendarObjectType.fromValue(objType));
                calEvt.setVariables(calEvtVars);
                SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
            }
        }
    }

    private DeployMessage getFolderDeniedMessage(String path, JobSchedulerObjectType objectType) {
        String msg = null; //"Access denied for folder " + path; 
        return getDeniedMessage(path, objectType, DeployFailReasonType.MISSING_FOLDER_PERMISSIONS, msg, null);
    }
    
    private DeployMessage getObjectDeniedMessage(String path, JobSchedulerObjectType objectType) {
        String msg = null; //"Access denied for " + objectType.value().toLowerCase() + " " + path; 
        return getDeniedMessage(path, objectType, DeployFailReasonType.MISSING_OBJECT_PERMISSIONS, msg, null);
    }
    
    private DeployMessage getOwnerDeniedMessage(String path, JobSchedulerObjectType objectType, String owner) {
        String msg = null; //"Access denied. The owner is " + owner; 
        return getDeniedMessage(path, objectType, DeployFailReasonType.WRONG_OWNERSHIP, msg, owner);
    }
    
    private DeployMessage getIncompleteMessage(String path, JobSchedulerObjectType objectType, String errMsg) {
        return getDeniedMessage(path, objectType, DeployFailReasonType.INCOMPLETE_CONFIGURATION, errMsg, null);
    }
    
    private DeployMessage getDeployMessage(String path, JobSchedulerObjectType objectType, boolean deleted) {
        DeployMessage deployMessage = new DeployMessage();
        if (deleted) {
            deployMessage.setAction(DeployActionType.DELETED);
        } else {
            deployMessage.setAction(DeployActionType.DEPLOYED);
        }
        deployMessage.setObjectType(objectType);
        deployMessage.setPath(path);
        deployMessage.setFailReason(null);
        return deployMessage;
    }
    
    private DeployMessage getDeniedMessage(String path, JobSchedulerObjectType objectType, DeployFailReasonType failReason, String msg, String owner) {
        DeployMessage deployMessage = new DeployMessage();
        deployMessage.setAction(DeployActionType.SKIPPED);
        deployMessage.setObjectType(objectType);
        deployMessage.setPath(path);
        DeployFailReason reason = new DeployFailReason();
        reason.set_key(failReason);
        reason.setMessage(msg);
        reason.setOwner(owner);
        deployMessage.setFailReason(reason);
        return deployMessage;
    }

}
