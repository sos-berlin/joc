package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IFolderResource;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.other.Folder;

@Path("joe")
public class FolderResourceImpl extends JOCResourceImpl implements IFolderResource {

    private static final String API_CALL = "./joe/read/folder";

    @Override
    public JOCDefaultResponse readFolder(final String accessToken, final JSObjectEdit body) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            String path = normalizeFolder(body.getPath() + "/");

            if (!isPermittedForFolder(path)) {
                return accessDeniedResponse();
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);

            final int parentDepth = Paths.get(path).getNameCount();
            //Map: grouped by DBItemJoeObject::isDeleted -> DBItemJoeObject::objectType -> DBItemJoeObject::path collection
            Map<Boolean, Map<String, Set<String>>> folderContent = dbLayer.getFolderContentRecursive(body.getJobschedulerId(), path).stream().filter(
                    item -> {
                        return Paths.get(item.getPath()).getParent().getNameCount() == parentDepth; //not recursive
                    }).collect(Collectors.groupingBy(DBItemJoeObject::isDeleted, Collectors.groupingBy(DBItemJoeObject::getObjectType, Collectors
                            .mapping(DBItemJoeObject::getPath, Collectors.toSet()))));
            folderContent.putIfAbsent(Boolean.FALSE, new HashMap<String, Set<String>>());
            folderContent.putIfAbsent(Boolean.TRUE, new HashMap<String, Set<String>>());

            List<String> objectTypes = Arrays.asList("FOLDER", "JOB", "JOBCHAIN", "ORDER", "PROCESSCLASS", "SCHEDULE", "LOCK", "MONITOR",
                    "NODEPARAMS", "OTHER");

            Map<String, Set<String>> folderContentToAdd = folderContent.get(Boolean.FALSE);
            for (String objectType : objectTypes) {
                folderContentToAdd.putIfAbsent(objectType, new HashSet<String>());
            }
            
            Map<String, Set<String>> folderContentToDelete = folderContent.get(Boolean.TRUE);
            for (String objectType : objectTypes) {
                folderContentToDelete.putIfAbsent(objectType, new HashSet<String>());
            }
            
            for (String folder: folderContentToAdd.get("FOLDER")) {
                if (!isPermittedForFolder(folder)) {
                    folderContentToDelete.get("FOLDER").add(folder);
                }
            }

            JOCHotFolder httpClient = new JOCHotFolder(this);
            JsonArray folder = httpClient.getFolder(path);

            for (JsonString jsonStr : folder.getValuesAs(JsonString.class)) {
                String s = jsonStr.getString();
                if (s.startsWith(".")) {
                    continue;
                }
                if (s.endsWith("/")) {
                    if (isPermittedForFolder(path + s)) {
                        folderContentToAdd.get("FOLDER").add(s.replaceFirst("/$", ""));
                    }
                } else if (s.endsWith(".job.xml")) {
                    folderContentToAdd.get("JOB").add(s);
                } else if (s.endsWith(".job_chain.xml")) {
                    folderContentToAdd.get("JOBCHAIN").add(s);
                } else if (s.endsWith(".order.xml")) {
                    folderContentToAdd.get("ORDER").add(s);
                } else if (s.endsWith(".process_class.xml")) {
                    folderContentToAdd.get("PROCESSCLASS").add(s);
                } else if (s.endsWith(".schedule.xml")) {
                    folderContentToAdd.get("SCHEDULE").add(s);
                } else if (s.endsWith(".lock.xml")) {
                    folderContentToAdd.get("LOCK").add(s);
                } else if (s.endsWith(".monitor.xml")) {
                    folderContentToAdd.get("MONITOR").add(s);
                } else if (s.endsWith(".config.xml")) { // maybe test if job chain exist
                    folderContentToAdd.get("NODEPARAMS").add(s);
                } else {
                    folderContentToAdd.get("OTHER").add(s);
                }
            }
            
            for (String objectType : objectTypes) {
                folderContentToAdd.get(objectType).removeAll(folderContentToDelete.get(objectType));
            }
            
            Folder entity = new Folder();
            if (!folderContentToAdd.get("FOLDER").isEmpty()) {
                entity.setFolders(folderContentToAdd.get("FOLDER"));
            }
            if (!folderContentToAdd.get("JOB").isEmpty()) {
                entity.setJobs(folderContentToAdd.get("JOB"));
            }
            if (!folderContentToAdd.get("JOBCHAIN").isEmpty()) {
                entity.setJobChains(folderContentToAdd.get("JOBCHAIN"));
            }
            if (!folderContentToAdd.get("ORDER").isEmpty()) {
                entity.setOrders(folderContentToAdd.get("ORDER"));
            }
            if (!folderContentToAdd.get("PROCESSCLASS").isEmpty()) {
                entity.setProcessClasses(folderContentToAdd.get("PROCESSCLASS"));
            }
            if (!folderContentToAdd.get("SCHEDULE").isEmpty()) {
                entity.setSchedules(folderContentToAdd.get("SCHEDULE"));
            }
            if (!folderContentToAdd.get("LOCK").isEmpty()) {
                entity.setLocks(folderContentToAdd.get("LOCK"));
            }
            if (!folderContentToAdd.get("MONITOR").isEmpty()) {
                entity.setMonitors(folderContentToAdd.get("MONITOR"));
            }
            if (!folderContentToAdd.get("NODEPARAMS").isEmpty()) {
                entity.setNodeParams(folderContentToAdd.get("NODEPARAMS"));
            }
            if (!folderContentToAdd.get("OTHER").isEmpty()) {
                entity.setOthers(folderContentToAdd.get("OTHER"));
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            entity.setPath(path);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
