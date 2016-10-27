package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassPermanent;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassP;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;
import com.sos.joc.processClasses.resource.IProcessClassesResourceP;

@Path("process_classes")
public class ProcessClassesResourcePImpl extends JOCResourceImpl implements IProcessClassesResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImpl.class);
    private static final String API_CALL = "./process_classes/p";
    private String regex;
    private List<Folder> folders;
    private List<ProcessClassPath> processClasses;

    @Override
    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = 
                    init(processClassFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            processClasses = processClassFilter.getProcessClasses();
            folders = processClassFilter.getFolders();
            regex = processClassFilter.getRegex();
            ProcessClassesP entity = new ProcessClassesP();
            InventoryProcessClassesDBLayer dbLayer = new InventoryProcessClassesDBLayer(Globals.sosHibernateConnection);
            List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
            if (processClasses != null && !processClasses.isEmpty()) {
                List<ProcessClassP> processClassesToAdd = new ArrayList<ProcessClassP>();
                for(ProcessClassPath processClassPath : processClasses) {
                    DBItemInventoryProcessClass processClassFromDb = 
                            dbLayer.getProcessClass(processClassPath.getProcessClass(), dbItemInventoryInstance.getId());
                    ProcessClassP processClass = ProcessClassPermanent.getProcessClassP(dbLayer, processClassFromDb);
                    if (processClass != null) {
                        processClassesToAdd.add(processClass);
                    }
                }
                if (processClassesToAdd != null && !processClassesToAdd.isEmpty()) {
                    listOfProcessClasses.addAll(processClassesToAdd);
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClassesByFolders(folder.getFolder(),
                            dbItemInventoryInstance.getId(), folder.getRecursive().booleanValue());
                    List<ProcessClassP> processClassesToAdd = ProcessClassPermanent.getProcessClassesToAdd(dbLayer, processClassesFromDb, regex);
                    if(processClassesToAdd != null && !processClassesToAdd.isEmpty()){
                        listOfProcessClasses.addAll(processClassesToAdd);
                    }
                }
            } else {
                List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClasses(dbItemInventoryInstance.getId());
                List<ProcessClassP> processClassesToAdd = ProcessClassPermanent.getProcessClassesToAdd(dbLayer, processClassesFromDb, regex);
                if(processClassesToAdd != null && !processClassesToAdd.isEmpty()){
                    listOfProcessClasses.addAll(processClassesToAdd);
                }
            }
            entity.setProcessClasses(listOfProcessClasses);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, processClassFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, processClassFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}