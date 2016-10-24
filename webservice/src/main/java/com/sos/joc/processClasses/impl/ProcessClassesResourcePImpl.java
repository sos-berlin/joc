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

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImpl.class);
    private String regex;
    private List<Folder> folders;
    private List<ProcessClassPath> processClasses;

    @Override
    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        LOGGER.debug("init processClasses");
        try {
            JOCDefaultResponse jocDefaultResponse = init(processClassFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            processClasses = processClassFilter.getProcessClasses();
            folders = processClassFilter.getFolders();
            regex = processClassFilter.getRegex();
            ProcessClassesP entity = new ProcessClassesP();
            InventoryInstancesDBLayer instanceDBLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceDBLayer.getInventoryInstanceBySchedulerId(processClassFilter.getJobschedulerId());
            InventoryProcessClassesDBLayer dbLayer = new InventoryProcessClassesDBLayer(Globals.sosHibernateConnection);
            List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
            if (processClasses != null && !processClasses.isEmpty()) {
                List<ProcessClassP> processClassesToAdd = new ArrayList<ProcessClassP>();
                for(ProcessClassPath processClassPath : processClasses) {
                    DBItemInventoryProcessClass processClassFromDb = dbLayer.getProcessClass(processClassPath.getProcessClass(), instance.getId());
                    ProcessClassP processClass = getProcessClassP(dbLayer, processClassFromDb);
                    if (processClass != null) {
                        processClassesToAdd.add(processClass);
                    }
                }
                if (processClassesToAdd != null && !processClassesToAdd.isEmpty()) {
                    listOfProcessClasses.addAll(processClassesToAdd);
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClassesByFolders(folder.getFolder(), instance.getId(), folder.getRecursive().booleanValue());
                    List<ProcessClassP> processClassesToAdd = getProcessClassesToAdd(dbLayer, processClassesFromDb);
                    if(processClassesToAdd != null && !processClassesToAdd.isEmpty()){
                        listOfProcessClasses.addAll(processClassesToAdd);
                    }
                }
            } else {
                List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClasses(instance.getId());
                List<ProcessClassP> processClassesToAdd = getProcessClassesToAdd(dbLayer, processClassesFromDb);
                if(processClassesToAdd != null && !processClassesToAdd.isEmpty()){
                    listOfProcessClasses.addAll(processClassesToAdd);
                }
            }
            entity.setProcessClasses(listOfProcessClasses);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

    private List<ProcessClassP> getProcessClassesToAdd(InventoryProcessClassesDBLayer dbLayer, List<DBItemInventoryProcessClass> processClassesFromDb)
            throws Exception {
        List<ProcessClassP> processClassesToAdd = new ArrayList<ProcessClassP>();
        if (processClassesFromDb != null) {
            for (DBItemInventoryProcessClass processClassFromDb : processClassesFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(processClassFromDb.getName());
                    if (regExMatcher.find()) {
                        processClassesToAdd.add(getProcessClassP(dbLayer, processClassFromDb));
                    }
                } else {
                    processClassesToAdd.add(getProcessClassP(dbLayer, processClassFromDb));
                }
            }
        }
        return processClassesToAdd;
    }

    private ProcessClassP getProcessClassP(InventoryProcessClassesDBLayer dbLayer, DBItemInventoryProcessClass processClassFromDb)
            throws Exception {
        if (processClassFromDb != null) {
            ProcessClassP processClassP = new ProcessClassP();
            processClassP.setMaxProcesses(processClassFromDb.getMaxProcesses());
            processClassP.setName(processClassFromDb.getBasename());
            processClassP.setPath(processClassFromDb.getName());
            processClassP.setSurveyDate(processClassFromDb.getModified());
            processClassP.setConfigurationDate(dbLayer.getProcessClassConfigurationDate(processClassFromDb.getId()));
            return processClassP;
        } else {
            return null;
        }
    }
    
}