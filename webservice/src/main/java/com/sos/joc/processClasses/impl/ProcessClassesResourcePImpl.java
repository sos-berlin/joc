package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassPermanent;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassP;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;
import com.sos.joc.processClasses.resource.IProcessClassesResourceP;

@Path("process_classes")
public class ProcessClassesResourcePImpl extends JOCResourceImpl implements IProcessClassesResourceP {

    private static final String API_CALL = "./process_classes/p";

    @Override
    public JOCDefaultResponse postProcessClassesP(String xAccessToken, String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        return postProcessClassesP(getAccessToken(xAccessToken, accessToken), processClassFilter);
    }

    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, processClassFilter, accessToken, processClassFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(connection);
            ProcessClassesP entity = new ProcessClassesP();
            InventoryProcessClassesDBLayer dbLayer = new InventoryProcessClassesDBLayer(connection);
            List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
            List<ProcessClassPath> processClassPaths = processClassFilter.getProcessClasses();
            boolean withFolderFilter = processClassFilter.getFolders() != null && !processClassFilter.getFolders().isEmpty();
            List<Folder> folders = addPermittedFolder(processClassFilter.getFolders());

            if (processClassPaths != null && !processClassPaths.isEmpty()) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                for (ProcessClassPath processClassPath : processClassPaths) {
                    checkRequiredParameter("processClass", processClassPath.getProcessClass());
                    if (processClassPath != null && canAdd(processClassPath.getProcessClass(), permittedFolders)) {
                        DBItemInventoryProcessClass processClassFromDb = dbLayer.getProcessClass(normalizePath(processClassPath.getProcessClass()),
                                dbItemInventoryInstance.getId());
                        if (processClassFromDb == null) {
                            continue;
                        }
                        listOfProcessClasses.add(ProcessClassPermanent.getProcessClassP(dbLayer, processClassFromDb));
                    }
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                // no permission
            } else if (folders != null && !folders.isEmpty()) {
                Map<String, ProcessClassP> mapOfProcessClasses = new HashMap<String, ProcessClassP>();
                for (Folder folder : folders) {
                    List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClassesByFolders(folder.getFolder(),
                            dbItemInventoryInstance.getId(), folder.getRecursive().booleanValue());
                    Map<String, ProcessClassP> processClassesToAdd = ProcessClassPermanent.getProcessClassesMap(dbLayer, processClassesFromDb,
                            processClassFilter.getRegex());
                    mapOfProcessClasses.putAll(processClassesToAdd);
                }
                listOfProcessClasses = new ArrayList<ProcessClassP>(mapOfProcessClasses.values());
            } else {
                List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClasses(dbItemInventoryInstance.getId());
                listOfProcessClasses = ProcessClassPermanent.getProcessClassesList(dbLayer, processClassesFromDb, processClassFilter.getRegex());
            }
            entity.setProcessClasses(listOfProcessClasses);
            entity.setDeliveryDate(Date.from(Instant.now()));
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