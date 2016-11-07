package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

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
    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        try {
            initLogging(API_CALL, processClassFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, processClassFilter.getJobschedulerId(), getPermissons(accessToken).getLock()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Globals.beginTransaction();
            ProcessClassesP entity = new ProcessClassesP();
            InventoryProcessClassesDBLayer dbLayer = new InventoryProcessClassesDBLayer(Globals.sosHibernateConnection);
            List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
            List<ProcessClassPath> processClassPaths = processClassFilter.getProcessClasses();
            List<Folder> folders = processClassFilter.getFolders();
            
            if (processClassPaths != null && !processClassPaths.isEmpty()) {
                for (ProcessClassPath processClassPath : processClassPaths) {
                    checkRequiredParameter("processClass", processClassPath.getProcessClass());
                    DBItemInventoryProcessClass processClassFromDb = dbLayer.getProcessClass(normalizePathForDB(processClassPath.getProcessClass()),
                            dbItemInventoryInstance.getId());
                    if (processClassFromDb == null) {
                        //LOGGER.warn(String.format("process class '%1$s' doesn't exist in table %2$s", processClassPath.getProcessClass(), DBLayer.DBITEM_INVENTORY_PROCESS_CLASSES));
                        continue;
                    }
                    listOfProcessClasses.add(ProcessClassPermanent.getProcessClassP(dbLayer, processClassFromDb));
                }
            } else if (folders != null && !folders.isEmpty()) {
                Map<String, ProcessClassP> mapOfProcessClasses = new HashMap<String, ProcessClassP>();
                for (Folder folder : folders) {
                    List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClassesByFolders(normalizePathForDB(folder.getFolder()),
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
            Globals.rollback();
        }
    }

}