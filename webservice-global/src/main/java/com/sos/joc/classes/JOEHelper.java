package com.sos.joc.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonString;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.helper.EConfigFileExtensions;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.jobscheduler.model.event.CustomEventVariables;
import com.sos.joc.Globals;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.lock.Lock;
import com.sos.joc.model.joe.lock.LockInfo;
import com.sos.joc.model.joe.nodeparams.Config;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.other.Other;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.schedule.HolidaysFile;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.joe.schedule.Schedule;
import com.sos.joe.common.XmlDeserializer;

public class JOEHelper {

    public static final Map<String, Class<?>> CLASS_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<?>>() {

        private static final long serialVersionUID = 1L;

        {
            put("JOB", Job.class);
            put("ORDER", Order.class);
            put("JOBCHAIN", JobChain.class);
            put("PROCESSCLASS", ProcessClass.class);
            put("AGENTCLUSTER", ProcessClass.class);
            put("LOCK", Lock.class);
            put("SCHEDULE", Schedule.class);
            put("MONITOR", Monitor.class);
            put("NODEPARAMS", Config.class);
            put("HOLIDAYS", HolidaysFile.class);
            put("RUNTIME", RunTime.class);
            put("OTHER", Other.class);
        }
    });

    public static boolean hasPermission(JobSchedulerObjectType objectType, SOSPermissionJocCockpit sosPermission) {
        if (objectType == null) {
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob() && sosPermission
                    .getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain() && sosPermission.getJobschedulerMaster()
                            .getAdministration().getConfigurations().getDeploy().isMonitor() && sosPermission.getJobschedulerMaster()
                                    .getAdministration().getConfigurations().getDeploy().isProcessClass() && sosPermission.getJobschedulerMaster()
                                            .getAdministration().getConfigurations().getDeploy().isOrder() && sosPermission.getJobschedulerMaster()
                                                    .getAdministration().getConfigurations().getDeploy().isSchedule() && sosPermission
                                                            .getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock();
        }

        switch (objectType) {
        case MONITOR:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isMonitor();
        case JOB:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob();
        case NODEPARAMS:
        case JOBCHAIN:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain();
        case AGENTCLUSTER:
        case PROCESSCLASS:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isProcessClass();
        case ORDER:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isOrder();
        case HOLIDAYS:
        case SCHEDULE:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isSchedule();
        case LOCK:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock();
        case OTHER:
        case FOLDER:
            return true;
        default:
            return false;
        }
    }

    public static String getFileExtension(JobSchedulerObjectType jobSchedulerObjectType) {
        String fileExtension = "";
        switch (jobSchedulerObjectType) {
        case JOB:
            fileExtension = EConfigFileExtensions.JOB.extension();
            break;
        case ORDER:
            fileExtension = EConfigFileExtensions.ORDER.extension();
            break;
        case JOBCHAIN:
            fileExtension = EConfigFileExtensions.JOB_CHAIN.extension();
            break;
        case LOCK:
            fileExtension = EConfigFileExtensions.LOCK.extension();
            break;
        case PROCESSCLASS:
        case AGENTCLUSTER:
            fileExtension = EConfigFileExtensions.PROCESS_CLASS.extension();
            break;
        case SCHEDULE:
            fileExtension = EConfigFileExtensions.SCHEDULE.extension();
            break;
        case MONITOR:
            fileExtension = EConfigFileExtensions.MONITOR.extension();
            break;
        case NODEPARAMS:
            fileExtension = ".config.xml";
            break;
        case FOLDER:
            fileExtension = "/";
            break;
        default:
            break;
        }
        return fileExtension;
    }

    public static String getPathWithoutExtension(String path, JobSchedulerObjectType jobSchedulerObjectType) {
        if (path.length() - getFileExtension(jobSchedulerObjectType).length() < 0) {
            return path;
        } else {
            return path.substring(0, path.length() - getFileExtension(jobSchedulerObjectType).length());
        }
    }

    public static boolean pathIsObjectOf(String path, JobSchedulerObjectType jobSchedulerObjectType) {
        return path.endsWith(JOEHelper.getFileExtension(jobSchedulerObjectType));
    }

    public static byte[] concatByteArray(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(a);
        outputStream.write(b);

        return outputStream.toByteArray();
    }

    public static CustomEvent getJoeUpdatedEvent(String path) {
        CustomEvent evt = new CustomEvent();
        evt.setKey("JoeUpdated");
        CustomEventVariables evtVars = new CustomEventVariables();
        evtVars.setAdditionalProperty("path", path);
        evtVars.setAdditionalProperty("objectType", JobSchedulerObjectType.FOLDER.value());
        evt.setVariables(evtVars);
        return evt;
    }

    public static JOCDefaultResponse get434Response(JoeFolderAlreadyLockedException e) {
        LockInfo entity = new LockInfo();
        entity.setIsLocked(true);
        entity.setLockedSince(e.getLockedSince());
        entity.setLockedBy(e.getLockedBy());
        entity.setDeliveryDate(Date.from(Instant.now()));
        return JOCDefaultResponse.responseStatus434(entity);
    }

    public static void deleteOrdersAndNodeParams(DBLayerJoeObjects dbJoeLayer, DocumentationDBLayer dbDocLayer, DBItemJoeObject jobChain,
            String account, JOCHotFolder jocHotFolder) throws JsonParseException, JsonMappingException, JsonProcessingException, JocException,
            IOException, DocumentException, SAXException {
        renameOrdersAndNodeParams(dbJoeLayer, dbDocLayer, jobChain, account, false, jocHotFolder, null);
    }

    public static void renameOrdersAndNodeParams(DBLayerJoeObjects dbJoeLayer, DocumentationDBLayer dbDocLayer, DBItemJoeObject jobChain,
            String account, boolean jobChainIsOnlyDraft, JOCHotFolder jocHotFolder, String newJobChainPath) throws JsonParseException,
            JsonMappingException, JsonProcessingException, JocException, IOException, DocumentException, SAXException {
        // rename/delete orders, nodeparams
        Date now = Date.from(Instant.now());
        List<DBItemJoeObject> ordersAndNodeParamsInDb = dbJoeLayer.getOrdersAndNodeParams(jobChain);
        for (DBItemJoeObject orderOrNodeParamInDb : ordersAndNodeParamsInDb) {
            if (jobChainIsOnlyDraft) {
                // job chain is delete so that all orders and nodeparams will be deleted too
                dbJoeLayer.delete(orderOrNodeParamInDb);
            } else {
                orderOrNodeParamInDb.setOperation("delete");
                orderOrNodeParamInDb.setAccount(account);
                orderOrNodeParamInDb.setModified(now);
                dbJoeLayer.update(orderOrNodeParamInDb);
                if (newJobChainPath != null) {
                    String newPath = newJobChainPath + orderOrNodeParamInDb.getPath().substring(jobChain.getPath().length());
                    setNewDBItemfromOld(dbJoeLayer, orderOrNodeParamInDb, newPath);
                }
            }
        }
        if (jobChainIsOnlyDraft) {
            if (newJobChainPath != null) {
                DBItemJoeObject newJobChain = new DBItemJoeObject();
                newJobChain.setSchedulerId(jobChain.getSchedulerId());
                newJobChain.setPath(newJobChainPath);
                newJobChain.setObjectType(jobChain.getObjectType());
                restoreOrdersAndNodeParams(dbJoeLayer, dbJoeLayer.getOrdersAndNodeParams(newJobChain), account, now);
            }
        } else {
            jocHotFolder.setAutoCloseHttpClient(false);
            for (JsonString item : jocHotFolder.getFolder(jobChain.getFolder() + "/").getValuesAs(JsonString.class)) {
                String pathWithExtension = jobChain.getFolder() + "/" + item.getString();
                String newPath = null;
                if (pathWithExtension.startsWith(jobChain.getPath() + ",") || pathWithExtension.equals(jobChain.getPath() + JOEHelper
                        .getFileExtension(JobSchedulerObjectType.NODEPARAMS))) {
                    DBItemJoeObject dbItem = new DBItemJoeObject();
                    dbItem.setSchedulerId(jobChain.getSchedulerId());
                    if (JOEHelper.pathIsObjectOf(pathWithExtension, JobSchedulerObjectType.ORDER)) {
                        dbItem.setPath(JOEHelper.getPathWithoutExtension(pathWithExtension, JobSchedulerObjectType.ORDER));
                        dbItem.setObjectType(JobSchedulerObjectType.ORDER.value());
                        if (newJobChainPath != null) {
                            newPath = newJobChainPath + JOEHelper.getPathWithoutExtension(pathWithExtension, JobSchedulerObjectType.ORDER).substring(
                                    jobChain.getPath().length());
                        }
                    } else if (JOEHelper.pathIsObjectOf(pathWithExtension, JobSchedulerObjectType.NODEPARAMS)) {
                        dbItem.setPath(JOEHelper.getPathWithoutExtension(pathWithExtension, JobSchedulerObjectType.NODEPARAMS));
                        dbItem.setObjectType(JobSchedulerObjectType.NODEPARAMS.value());
                        if (newJobChainPath != null) {
                            newPath = newJobChainPath + JOEHelper.getPathWithoutExtension(pathWithExtension, JobSchedulerObjectType.NODEPARAMS)
                                    .substring(jobChain.getPath().length());
                        }
                    }
                    if (!ordersAndNodeParamsInDb.contains(dbItem)) {
                        dbItem.setAccount(account);
                        dbItem.setAuditLogId(null);
                        dbItem.setFolder(jobChain.getFolder());
                        dbItem.setId(null);
                        dbItem.setDocPath(getDocPath(dbDocLayer, dbItem));
                        dbItem.setCreated(now);
                        dbItem.setOperation("delete");
                        dbItem.setConfiguration(null);
                        dbJoeLayer.save(dbItem);
                        if (newPath != null) {
                            setNewDBItemfromOld(dbJoeLayer, dbItem, newPath, Globals.objectMapper.writeValueAsString(XmlDeserializer.deserialize(
                                    jocHotFolder.getFile(pathWithExtension), JOEHelper.CLASS_MAPPING.get(dbItem.getObjectType()))));
                        }
                    }
                }
            }
            jocHotFolder.closeHttpClient();
        }
    }
    
    public static void deleteNodeParams(DBLayerJoeObjects dbJoeLayer, DocumentationDBLayer dbDocLayer, DBItemJoeObject order,
            String account, JOCHotFolder jocHotFolder) throws JsonParseException, JsonMappingException, JsonProcessingException, JocException,
            IOException, DocumentException, SAXException {
        renameNodeParams(dbJoeLayer, dbDocLayer, order, account, false, jocHotFolder, null);
    }
    
    public static void renameNodeParams(DBLayerJoeObjects dbJoeLayer, DocumentationDBLayer dbDocLayer, DBItemJoeObject order, String account,
            boolean orderIsOnlyDraft, JOCHotFolder jocHotFolder, String newOrderPath) throws JsonParseException, JsonMappingException,
            JsonProcessingException, JocException, IOException, DocumentException, SAXException {
        // rename nodeparams
        Date now = Date.from(Instant.now());
        List<DBItemJoeObject> nodeParamsInDb = dbJoeLayer.getNodeParams(order);
        for (DBItemJoeObject nodeParamInDb : nodeParamsInDb) {
            if (orderIsOnlyDraft) {
                // order is delete so that all nodeparams will be deleted too
                dbJoeLayer.delete(nodeParamInDb);
            } else {
                nodeParamInDb.setOperation("delete");
                nodeParamInDb.setAccount(account);
                nodeParamInDb.setModified(now);
                dbJoeLayer.update(nodeParamInDb);
                if (newOrderPath != null) {
                    setNewDBItemfromOld(dbJoeLayer, nodeParamInDb, newOrderPath);
                }
            }
        }
        if (orderIsOnlyDraft) {
            if (newOrderPath != null) {
                DBItemJoeObject newOrder = new DBItemJoeObject();
                newOrder.setSchedulerId(order.getSchedulerId());
                newOrder.setPath(newOrderPath);
                newOrder.setObjectType(order.getObjectType());
                restoreOrdersAndNodeParams(dbJoeLayer, dbJoeLayer.getNodeParams(newOrder), account, now);
            }
        } else {
            jocHotFolder.setAutoCloseHttpClient(false);
            for (JsonString item : jocHotFolder.getFolder(order.getFolder() + "/").getValuesAs(JsonString.class)) {
                String pathWithExtension = order.getFolder() + "/" + item.getString();
                if (pathWithExtension.equals(order.getPath() + JOEHelper.getFileExtension(JobSchedulerObjectType.NODEPARAMS))) {
                    DBItemJoeObject dbItem = new DBItemJoeObject();
                    dbItem.setSchedulerId(order.getSchedulerId());
                    dbItem.setPath(JOEHelper.getPathWithoutExtension(pathWithExtension, JobSchedulerObjectType.NODEPARAMS));
                    dbItem.setObjectType(JobSchedulerObjectType.NODEPARAMS.value());
                    if (!nodeParamsInDb.contains(dbItem)) {
                        dbItem.setAccount(account);
                        dbItem.setAuditLogId(null);
                        dbItem.setFolder(order.getFolder());
                        dbItem.setId(null);
                        dbItem.setDocPath(getDocPath(dbDocLayer, dbItem));
                        dbItem.setCreated(now);
                        dbItem.setOperation("delete");
                        dbItem.setConfiguration(null);
                        dbJoeLayer.save(dbItem);
                        if (newOrderPath != null) {
                            setNewDBItemfromOld(dbJoeLayer, dbItem, newOrderPath, Globals.objectMapper.writeValueAsString(XmlDeserializer.deserialize(
                                    jocHotFolder.getFile(pathWithExtension), JOEHelper.CLASS_MAPPING.get(dbItem.getObjectType()))));
                        }
                    }
                }
            }
            jocHotFolder.closeHttpClient();
        }
    }

    public static void setNewDBItemfromOld(DBLayerJoeObjects dbJoeLayer, DBItemJoeObject oldItem, String newPath) throws DBConnectionRefusedException,
            DBInvalidDataException {
        setNewDBItemfromOld(dbJoeLayer, oldItem, newPath, null);
    }

    public static void setNewDBItemfromOld(DBLayerJoeObjects dbJoeLayer, DBItemJoeObject oldItem, String newPath, String configuration)
            throws DBConnectionRefusedException, DBInvalidDataException {
        DBItemJoeObject newItem = dbJoeLayer.getJoeObject(oldItem.getSchedulerId(), newPath, JobSchedulerObjectType.fromValue(oldItem
                .getObjectType()));
        if (newItem == null) {
            newItem = new DBItemJoeObject();
            newItem.setId(null);
            newItem.setObjectType(oldItem.getObjectType());
            newItem.setPath(newPath);
            newItem.setSchedulerId(oldItem.getSchedulerId());
            newItem.setCreated(oldItem.getCreated());
            if ("/".equals(newPath)) {
                newItem.setFolder(".");
            } else {
                newItem.setFolder(Globals.getParent(newPath));
            }
        }
        newItem.setDocPath(oldItem.getDocPath());
        newItem.setAccount(oldItem.getAccount());
        newItem.setModified(Date.from(Instant.now()));
        if (configuration == null) {
            newItem.setConfiguration(oldItem.getConfiguration());
        } else {
            newItem.setConfiguration(configuration);
        }
        newItem.setOperation("store");
        newItem.setAuditLogId(oldItem.getAuditLogId());
        if (newItem.getId() == null) {
            dbJoeLayer.save(newItem);
        } else {
            dbJoeLayer.update(newItem);
        }
    }

    public static String getDocPath(DocumentationDBLayer dbLayer, DBItemJoeObject dbItem) {
        try {
            JobSchedulerObjectType type = JobSchedulerObjectType.fromValue(dbItem.getObjectType());
            if (type == JobSchedulerObjectType.FOLDER || type == JobSchedulerObjectType.NODEPARAMS) {
                return null;
            }
            return dbLayer.getDocumentationPath(dbItem.getSchedulerId(), type, dbItem.getPath());
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void restoreOrdersAndNodeParams(DBLayerJoeObjects dbLayer, List<DBItemJoeObject> ordersAndNodeParamsInDb, String account, Date now)
            throws DBConnectionRefusedException, DBInvalidDataException {
        for (DBItemJoeObject orderOrNodeParamInDb : ordersAndNodeParamsInDb) {
            if (orderOrNodeParamInDb.getConfiguration() == null) {
                dbLayer.delete(orderOrNodeParamInDb);
            } else {
                orderOrNodeParamInDb.setOperation("store");
                orderOrNodeParamInDb.setAccount(account);
                orderOrNodeParamInDb.setModified(now);
                dbLayer.update(orderOrNodeParamInDb);
            }
        }
    }

}
